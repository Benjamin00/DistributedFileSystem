import java.util.ArrayList;

//TODO why doesn't the project work in eclipse?
//TODO data node needs to listen and handle requests
//TODO each request is handled in a separate thread
/*Once manipulating data structures from separate threads, be careful not to end up with
race condition. Also, you want to handle locks or other synchronization primitives in
such a way that your system is deadlock free.*/
//TODO reader-writers problem

public class DataNode {
	private int MAX_BLOCKS = 10;
	private int blk; //TODO use bitmap instead
	int id;
	private ArrayList<String> data; //TODO write out file instead? (requires block -> file mapping)
	
	public static void main(String[] args){
		
	}	
	
	//constructor
	DataNode(int id){
		this.blk = -1;
		this.id = id;
		data = new ArrayList<String>(MAX_BLOCKS);
	}
	
	int id() {
		return this.id;
	}
	
	//three functions to support:
	//1. alloc returns the block number allocated, -1 if none available
	int alloc() { //TODO multiple threads could request at same time; make thread safe
		if(this.blk < MAX_BLOCKS-1) {
			this.blk++;
			return this.blk;
		}
		else {
			return -1;
		}
	}
	
	//2. given block id, read data and return contents
	String read(int blk_id) { //TODO could write and read at same time, make it safe
		//TODO use a file instead of process memory
		return data.get(blk_id);
	}
	
	//3. write contents to block id
	boolean write(int blk_id, String contents) { //TODO make safe for multiple writer/reader
		//TODO use a file instead of memory
		if(blk_id >= data.size()) {
			data.add(contents);
		}
		else {
			data.set(blk_id, contents);
		}
		return true;
	}
	
	//some convenient functions
	boolean isFull() {
		return this.blk == MAX_BLOCKS-1;
	}
	
	int numEmptyBlks() {
		return (MAX_BLOCKS-1)-blk;
	}
	
	void print(){
		//TODO read files instead?
		System.out.println("Data Node " + id + " contents:");
		for(int i = 0; i < data.size(); i++) {
			System.out.println("Block " + i + ": " + data.get(i));
		}
	}
	
	void mockRun() {
	//declare a couple data notes
			DataNode one = new DataNode(1); //numbers
			DataNode two = new DataNode(2); //animals
			DataNode three = new DataNode(3); //fruits
			
			//add some data
			for(int i = 0; i < 11; i++) {
				int block = one.alloc();
				if(block < 0) {
					System.out.println("Couldn't allocate data for block " + i);
					if(one.isFull()) {
						System.out.println("yes, we're full...");
					}
				}
				else {
					one.write(i, String.valueOf(i));
				}
				System.out.println(one.numEmptyBlks() + " blocks available for node one");
			}
			
			//node two
			int block = two.alloc();
		  two.write(block, "reindeer");
			block = two.alloc();
			two.write(block, "elephant");
			block = two.alloc();
			two.write(block, "sheep");
			
			//node three
			block = three.alloc();
			three.write(block, "apple");
			three.write(block, "orange");
			three.write(block, "mango");
			System.out.println("Do we have mango? " + three.read(block));
			
		//print out all three node contents
			one.print();
			two.print();
			three.print();

	}
}
