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
	
	
	public static void main(String[] args) throws IOException {
		//Set up server to listen
		NameNode n = new NameNode();
		n.start(6667);
		
		//Append("First.txt","Hello!");
		//printArr(arr);	
		//Read("First.txt");
	 }
	
	
	//SERVER SIDE
	 public void start(int port) {
	        try {
				serverSocket = new ServerSocket(port);
				clientSocket = serverSocket.accept();
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String greeting = in.readLine();
				    if ("hello server".equals(greeting)) {
				        out.println("hello client");
				    }
				    else {
				        out.println("unrecognised greeting");
				    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	 
	    public void stop() {
	        try {
				in.close();
				out.close();
				clientSocket.close();
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
