import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class DataNodeHandler extends Thread {
	private Socket clientSocket; //connection with name node
	private DataNode myNode; //reference to data node that spawned handler
	private BufferedReader br; //input stream
	PrintWriter pw; //output stream

	//constructor
	public DataNodeHandler(Socket clientSocket, DataNode node) {
		this.clientSocket  = clientSocket;
		this.myNode = node; //data node this thread acts on
	}

	//get message from name mode
	private String readCommand() {
		String command = null;
		try {
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			command = br.readLine().trim(); 
		} catch (Exception e) {
			System.err.println("Handler error while trying to read a command!");
			e.printStackTrace();
		}
		return command;
	}

	//return message to name node
	private void output(String out) {
		try {
			pw = new PrintWriter(clientSocket.getOutputStream());
			pw.print(out); 
			pw.flush();
		} catch (Exception e) {
			System.err.println("Handler error while trying to return message to name node!");
			e.printStackTrace();
		}
	}

	//ensure connections are closed
	private void close() {
		try {
			this.clientSocket.close();
			br.close(); //input stream
			pw.close(); //output stream
		} catch (Exception e) {
			System.err.println("Handler error while trying to close thread!");
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		//this is where the parsing, data node commanding, and message return happens
		String command = readCommand();
		System.out.println("Port[" + this.clientSocket.getPort() + "] received message: " + command);

		String returnMsg = "DEFAULT";

		// parse the command here and do what it asks you to do
		String cmdParts[] = command.split(" ", 2);
		String cmdKey = cmdParts[0];
		
		switch(cmdKey.toUpperCase()) {
		case "ALLOC":
			//call alloc
			int alloc_blk = myNode.alloc(); 
			returnMsg = String.valueOf(alloc_blk);
			break;
		case "READ":
			//call read
			int read_blk = Integer.valueOf(cmdParts[1]);
			String contents = myNode.read(read_blk);
			returnMsg = contents;
			break;
		case "WRITE":
			//call write
			String writeParts[] = cmdParts[1].split(" ", 2);
			int write_blk = Integer.valueOf(writeParts[0]);
			String writeData = writeParts[1];
			myNode.write(write_blk, writeData);
			returnMsg = "COMPLETE";
			break;
		default:
			//Error, invalid command
			//do nothing
			break;
		}

		//send return message
		output(returnMsg);

		//close thread
		close();
	}
}