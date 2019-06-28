import java.net.*;
import java.util.*;
import java.io.*;
import java.util.Scanner;

public class Client {
	//request to the name nodes
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
	public static void main(String[] args) {
		//Read a user string
		//Check that string
		//Send that string to the NameNode

		String input = new String();
		String filename = new String(); 
		String appendContents = new String();

		Scanner fileNameString = new Scanner(System.in);

		input = fileNameString.next();

		// String parser 
		
		if(input.substring(0, 4).equals("READ"));
		{

			filename = input.substring(5, input.length());

			// Call READ command for the appropiate file name 

		}

		if(input.substring(0, 6).equals("APPEND"))
		{
			filename = input.substring(7, input.indexOf(" ", 7));
			
			appendContents = input.substring((input.indexOf(" ", 7) + 1), input.length());

			// Call APPEND command for the appropiate file name 

		}

		// Still need to send to the name node 

		Client c1 = new Client();
		c1.startConnection("127.0.0.1", 5558);
		String msg1 = c1.sendMessage("hello");
	    String msg2 = c1.sendMessage("world");
	    String terminate = c1.sendMessage(".");
	    
	    Client c2 = new Client();
	    c2.startConnection("127.0.0.1", 5558);
		String msg11 = c2.sendMessage("hello");
	    String msg21 = c2.sendMessage("world");
	    String terminate1 = c2.sendMessage(".");
		
	    terminate = c2.sendMessage(".");
	    
	    System.out.println("First Client: " + msg1 + " " + msg2);
	    System.out.println("Second Client: " + msg11 + " " + msg21);
	    
	    c1.stopConnection();
		c2.stopConnection();
		
	
	    
	}
	
	public void startConnection(String ip, int port) {
	       try {
	    	clientSocket = new Socket(ip, port);
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	       }catch(Exception e) {
	    	   System.out.println("Error Starting Connection");
	    	   e.printStackTrace();
	       }
	    }

	    public String sendMessage(String msg) {
	        try {
				out.println(msg);
				String resp = in.readLine();
				return resp;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return "ERROR";
	    }
	 
	    public void stopConnection() {
	        try {
				in.close();
				out.close();
		        clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

}
