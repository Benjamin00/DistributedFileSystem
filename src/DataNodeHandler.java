import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

class DataNodeHandler extends Thread {
	private Socket clientSocket;
	private DataNode myNode;
	private BufferedReader br;
	PrintWriter pw;

	public DataNodeHandler(Socket clientSocket, DataNode node) {
		this.clientSocket  = clientSocket;
		this.myNode = node;
	}

	private String readCommand() {
		String command = null;
		System.out.println("I want to read a command, pls.");
		try {
			//Reader reader = new InputStreamReader(clientSocket.getInputStream());
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			command = br.readLine().trim();
			System.out.println("I've got a command!" + command);
		} catch (Exception e) {
			System.out.println("Found error while trying to read a command ");
			e.printStackTrace();
		}
		return command;
	}

	private void output(String out) {
		try {
			System.out.println("Inside output: " + out);
			pw = new PrintWriter(clientSocket.getOutputStream());
			pw.print(out);
			pw.flush();
		} catch (Exception e) {
			System.out.println("output error! ");
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			this.clientSocket.close();
			br.close();
			pw.close();
		} catch (Exception e) {
			System.out.println("close error ");
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
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
			System.out.println("Returning: "+ returnMsg);
			break;
		case "READ":
			//call read
			int read_blk = Integer.valueOf(cmdParts[1]);
			String contents = myNode.read(read_blk);
			returnMsg = contents;
			System.out.println("Returning: "+ returnMsg);

			break;
		case "WRITE":
			//call write
			String writeParts[] = cmdParts[1].split(" ", 2);
			int write_blk = Integer.valueOf(writeParts[0]);
			String writeData = writeParts[1];
			myNode.write(write_blk, writeData);
			returnMsg = "COMPLETE";
			System.out.println("Returning: "+ returnMsg);
			break;
		default:
			//Error, invalid command
			//do nothing
			break;
		}

		output(returnMsg);

		close();
	}
}