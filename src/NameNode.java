import java.net.*;
import java.io.*;

public class NameNode {
	//Server Side
	private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    //Client
	static String [][] arr = new String[40][10];	//hash table with 40*10 bc maximum 39 blocks, and maximum 10 for each
	static int fileNum = 0;//counter for file number
	static boolean fileFound = false;
	static String temp[] = new String[] {"D10.txt","D19.txt","D39.txt"}; //test case
	
	// TODO: We can have a thread class.
	// TODO: In each thread, we can have a lock.
	// TODO: The process of accepting the requests can in itself be a thread.
	// TODO: Each time we get a request, we can spawn a new thread immediately

	/*(new Thread(new exampleThread())).start();*/

	public static void main(String[] args) throws IOException {
		 NameNode server = new NameNode();
		 server.start(5558);
	 }
	
    public void start(int port) {
        try {
			serverSocket = new ServerSocket(port);
			while (true)
			    new NameNodeHandler(serverSocket.accept()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void stop() {
        try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	 private static class NameNodeHandler extends Thread {
	        private Socket clientSocket;
	        private PrintWriter out;
	        private BufferedReader in;
	 
	        public NameNodeHandler(Socket socket) {
	            this.clientSocket = socket;
	        }
	 
	        public void run() {
	            try {
					out = new PrintWriter(clientSocket.getOutputStream(), true);
					in = new BufferedReader(
					  new InputStreamReader(clientSocket.getInputStream()));
					 
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
					    if (".".equals(inputLine)) {
					        out.println("bye");
					        break;
					    }
					    out.println(inputLine);
					}
 
					in.close();
					out.close();
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	 }

	//CLIENT SIDE
	public static void Append(String filename, String content) //filename ends in txt, centent is one big string
	{	
		arr[0][0] = filename;//store filename in the fist dimension
		
		int BlockNum = temp.length; // number of blocks assigned
		for(int i = 1; i <= BlockNum; i++ )
		{
			arr[fileNum][i] = temp[i-1];

		}
		
		//pass content to DataNode()
		//DataNode() gives back the free block(s), format as an array [D10,D19,D39]
		fileNum++;
		/* test cases (hard code)
		System.out.println(arr[0][1]);
		System.out.println(arr[0][2]);
		System.out.println(arr[0][3]);
		*/
	}
	
	public static void Read(String filename) throws IOException
	{
		FileInputStream instream = null;
		FileOutputStream outstream = null;
		int i=0;
		int index = 0;
		//if the file exist, set the flag to true
		for(i = 0; i < arr.length; i++){  
	        		if(arr[i][0]==filename) {
	        			fileFound = true;
	        			index = i;
	        		}
	    }		
		for(int j = 1; j < arr[index].length; j++)
		{
			if(arr[index][j]!=null)
			{
				File infile =new File(arr[index][j]);
		    	    File outfile =new File(filename);
		    	    instream = new FileInputStream(infile);
		    	    outstream = new FileOutputStream(outfile,true);
		    	    byte[] buffer = new byte[1024];
		    	    int length;
		    	    while ((length = instream.read(buffer)) > 0){
		    	    	outstream.write(buffer, 0, length);
		    	    }
		    	    //Closing the input/output file streams
		    	    instream.close();
		    	    outstream.close();
			}
		}
		
	}
	
	public static void printArr(String[][] example){ //print the 2d array 
		    for(int i = 0; i < example.length; i++){  
		        for(int j = 0; j < example[i].length; j++){ 
		        		if(example[i][j] != null)
		        			System.out.print(example[i][j] + "\t");
		        }
		        System.out.print("\n");
		    }
		}

}

/*
class exampleThread extends Thread implements Runnable 
{
	// Lock

	// Recieves Request
		// Spawns a new thread while the previous request is still being catered to.
		// We could have something like a while loop here 
		// In this loop, we could also implement a lock
		// This is just an idea



	// Unlock
}
*/
