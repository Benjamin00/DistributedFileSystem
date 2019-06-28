import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
	//request to the name nodes
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
	public static void main(String[] args) {
		//Read a user string
		//Check that string
		//Send that string to the NameNode
		
		Client c1 = new Client();
		c1.startConnection("127.0.0.1", 5558);
		String msg1 = c1.sendMessage("Read");
	    String msg2 = c1.sendMessage("Append");
	    String terminate = c1.sendMessage(".");
	    
	    Client c2 = new Client();
	    c2.startConnection("127.0.0.1", 5558);
		String msg11 = c2.sendMessage("Read");
	    String msg21 = c2.sendMessage("Write");
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
