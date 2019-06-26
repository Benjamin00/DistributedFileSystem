import java.net.*;
import java.io.*;

public class Client {
	//request to the name nodes
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client = new Client();
	    client.startConnection("127.0.0.1", 6667);
	    String response = client.sendMessage("hello world");
	    System.out.println("Response: " + response);
	    client.stopConnection();
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
