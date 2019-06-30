import java.net.*;
import java.util.*;
import java.io.*;
import java.util.Scanner;

public class Client {
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
	public static void main(String[] args) {
		String input = new String();
		String filename = new String(); 
		String appendContents = new String();
		System.out.print("::");
		Scanner sc = new Scanner(System.in);
		while((input = sc.nextLine()) != null) {
			
			String[] tokens = input.split(" ");
			if(tokens[0].toLowerCase().equals("read") && tokens.length > 1) {
				Client c = new Client();
				c.startConnection("127.0.0.1", 5558);
				String ret = c.sendMessage(tokens[0] + " " + tokens[1]);	//send the read command assembly the file name.
				System.out.println(ret);									//Give the returned string to the user
				String terminate = c.sendMessage("."); 						
				c.stopConnection();											//close the connection
			}
			
			else if(tokens[0].toLowerCase().equals("append") && tokens[1]!=null && tokens[2]!=null) {
				String[] contents = input.split(" ", 3);
				Client c = new Client();
				c.startConnection("127.0.0.1", 5558);
				String ret = c.sendMessage(tokens[0] + " " + tokens[1] + " " + contents[2]);	//send the append command assembly the file name.
				String terminate = c.sendMessage("."); 											//tell the NameNode to close the connection
			    c.stopConnection();   										 //close the connection on client side
			}
			
			else {
				System.out.println("Invalid Input");
			}
			
			System.out.print("::");
		
		}
	}
	//function to begin connection to a given ip and port
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
	//send a message through the open connection
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
	 //close the connection
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
