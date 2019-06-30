import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.ServerSocket;

//NOTES:
/*Once manipulating data structures from separate threads, be careful not to end up with
race condition. Also, you want to handle locks or other synchronization primitives in
such a way that your system is deadlock free.*/

/*the data nodes have to serialize WRITE requests that happen to be on the same block.*/

/*lsof -i -n -P | grep TCP to get list of ports in use*/

public class DataNode {
	ServerSocket dataServer = null;

	private int port; //also serves as id number
	private int MAX_BLOCKS = 100;
	private Queue<Integer> availQ; //queue of available blocks
	private HashMap<Integer, Block> used; //map of used blocks (filename as value)
	private Path dataDir;

	//keep shared resources safe
	private final Object qLock = new Object(); //for locking available block queue
	private final Object uLock = new Object(); //for locking the used block hashmap

	public static void main(String[] args) throws InterruptedException{
		//get port number from command line; expected to be first argument
		int port = parseCmdLine(args);

		//Set up server to listen indefinitely
		DataNode d = new DataNode(port);
		d.start();
		//d.mockRun(); //uncomment to test data node as stand-alone

		//TODO how do we signal to data node it's time to stop?
		d.stop();
	}

	//constructor
	DataNode(int port){
		this.port = port;

		//add available blocks to queue
		availQ = new LinkedList<Integer>();
		for(int i = 0; i < MAX_BLOCKS; i++) {
			availQ.add(i);
		}

		//declare files
		used = new HashMap<Integer, Block>(MAX_BLOCKS);

		//create directory for storing files
		dataDir = Paths.get("./data_" + String.valueOf(port));
		System.out.println("Storing all files in: " + dataDir);
		if(Files.notExists(dataDir)) {
			try {
				Files.createDirectory(dataDir);
			} catch (IOException e) {
				System.err.println("Unable to create directory: " + dataDir.toString());
				e.printStackTrace();
				System.exit(5);
			}
		}
	}

	int port() {
		return this.port;
	}

	void start() {
		//allocate port
		try {

			System.out.println("Running on port:" + port);
			dataServer = new ServerSocket(port);

			while (true) {

				//final Socket dataClient = dataServer.accept();
				System.out.println("Accepted Client");

				//use data handler to handle requests
				new DataNodeHandler(dataServer.accept(), this).start();
			}

		} catch (IOException e) {
			e.printStackTrace();

			System.err.println("Unable to allocate port: " + port);
			System.exit(2);
		}
	}

	public void stop() {
		try {
			dataServer.close();
		} catch (IOException e) {
			System.err.println("Issue stopping DataNode Server...");
			System.exit(3);
		}
	}

	//three functions to support:
	//1. alloc returns the block number allocated, -1 if none available
	int alloc() {

		//get a block
		int block = -1; //return -1 if no blocks available
		synchronized(qLock) {
			if( availQ.isEmpty()) {
				return block;
			}
			block = availQ.poll();
		}

		//make a file(name)
		if(block != -1) {
			String filename = dataDir.toString() + "/blk_" + block + ".bin";
			System.out.println("Allocating: " + filename);
			//make block as used
			synchronized(uLock) {
				Block blkObj = new Block(filename);
				used.put(block, blkObj);
			}
			try {
				Path p = Paths.get(filename);
				Files.deleteIfExists(p); //this is just so that we don't have to clear directories between runs
				Files.createFile(p);
			} catch (IOException e) {
				System.err.println("Unable to open file: " + filename + " for block: " + block);
				e.printStackTrace();
			}
		}
		return block;
	}

	//2. given block id, read data and return contents
	//returns null if invalid blk_id
	String read(int blk_id) {

		//safety
		if(blk_id >= MAX_BLOCKS || !used.containsKey(blk_id)) {
			System.err.println("Requested block not mine or not in use: " + blk_id);
			return null;
		}

		Block blk = used.get(blk_id);
		String filename = blk.getFilename();
		Path p = Paths.get(filename);
		if(!Files.exists(p)) {
			//files does not exist?!
			return null;
		}
		byte[] bData = null;

		//get a reader lock (specific to this block)
		used.get(blk_id).getRLock().lock();
		try {
			bData = Files.readAllBytes(p);
		} catch (IOException e) {
			System.err.println("Unable to read file: " + p.toString());
			e.printStackTrace();
		}finally {
			//make sure we always unlock the readWriteLock
			used.get(blk_id).getRLock().unlock();
		}

		return new String(bData); //convert bytes to string
	}

	//3. write contents to block id
	boolean write(int blk_id, String contents) {
		//safety
		if(blk_id >= MAX_BLOCKS || !used.containsKey(blk_id)) {
			System.err.println("Requested block not mine: " + blk_id);
			return false;
		}

		//write out contents in a byte array 
		Block blk = used.get(blk_id);
		String filename = blk.getFilename();
		Path p = Paths.get(filename);
		byte[] bData = contents.getBytes();

		//get a writer lock (unique to this block)
		used.get(blk_id).getWLock().lock();
		try {
			Files.write(p, bData);
		} catch (IOException e) {
			System.err.println("Unable to write contents to block: " + blk_id + "(file: " + filename + ")");
			e.printStackTrace();
		}finally {
			//ensure we release the readWriteLock
			used.get(blk_id).getWLock().unlock();
		}
		return true;
	}

	//some convenient functions ... for testing
	boolean isFull() {
		return availQ.isEmpty();
	}

	int numEmptyBlks() {
		return availQ.size();
	}

	void print(){
		System.out.println("Data Node " + port + " contents:");
		
		//don't allow any modifications while we're printing contents
		synchronized(uLock){
			for(Entry<Integer, Block> entry: used.entrySet()) {
				System.out.println("Block " + entry.getKey() + ": " + read(entry.getKey()));
			}
		}
	}

	void mockRun() { //test function

		//add some data
		for(int i = 0; i < 11; i++) {
			int block = alloc();
			if(block < 0) {
				System.out.println("Couldn't allocate data for block " + i);
				if(isFull()) {
					System.out.println("yes, we're full...");
				}
			}
			else {
				write(i, String.valueOf(i));
			}
			System.out.println(numEmptyBlks() + " blocks available for node one");
		}

		print();

	}

	//given the command line arguments, find and return port number
	private static int parseCmdLine(String[] args) {
		int port = 0;

		if(args.length == 0) {
			System.out.println("Please specify port number as first command line argument.");
			System.exit(0);
		}
		else {
			try {
				port = Integer.parseInt(args[0]);
			}catch(NumberFormatException e){
				System.err.println("Expected first argument to be an port number (integer).");
				System.exit(1);
			}
		}
		return port;
	}
}
