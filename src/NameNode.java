import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameNode {
	final static int MB = 4194304;//String cut in 4MB
	//hashmap that stores the filename and DNum+BNum
	static Map<String, List<Pair>> map=new HashMap<String, List<Pair>>(); 
	//ArrayList that contain all the substring of contents
	static List<String> conCat = new ArrayList<String>(); //concatenate strings

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	//mutex
	//private final Object mutex = new Object();


	public static void main(String[] args) {
		//Append("First.txt","Hello!"); //Append only when it does not exist, otherwise deny it
		NameNode server = new NameNode();
		server.start(5558);
	}

	public void start(int port) {
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				new NameNodeHandler(serverSocket.accept()).start();
			}
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
		static NameNodeHandlerClient ctoD1;
		static NameNodeHandlerClient ctoD2;
		static NameNodeHandlerClient ctoD3;
		public class NameNodeHandlerClient {
			private Socket clientSocket;
			private PrintWriter out;
			private BufferedReader in;
			public void startConnection(String ip, int port) {
				try {
					clientSocket = new Socket(ip, port);
					out = new PrintWriter(clientSocket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
				return "";
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
		private static Socket clientSocket;
		private PrintWriter out;
		private BufferedReader in;


		// ---- START section that actually implements the name node handler -----
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
					if (".".equals(inputLine)) {//if a dot is sent, close everything
						break;
					}
					System.out.println("InputLine: " + inputLine);

					//Parsing the string needs to occur here 
					ctoD1 = new NameNodeHandlerClient();
					ctoD2 = new NameNodeHandlerClient();
					ctoD3 = new NameNodeHandlerClient();

					ctoD1.startConnection("127.0.0.1", 65530); //Instantiate DataNode 1
					ctoD2.startConnection("127.0.0.1", 65531); //Instantiate DataNode 2
					ctoD3.startConnection("127.0.0.1", 65532); //Instantiate DataNode 3

					String[] tokens = inputLine.split(" ");
					String file;
					if(tokens[0].toLowerCase().equals("read") && tokens.length == 2) {
						file = tokens[1];
						Read(file);
					}
					else if(tokens[0].toLowerCase().equals("append") && tokens.length >= 3) {
						file = tokens[1];
						System.out.println("file name in NameNode: " + inputLine);
						String[] contents = inputLine.split(" ", 3);
						System.out.println("contents in NameNode: " + contents[2]);
						String cont = contents[2];				//gets the contents of the string including spaces

						Append(file,cont);
					}
					else
					{
						System.out.println("Failed to parse string in NameNode");
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

		//---------   The following are the "NameNode" functions -----------
		public static void Append(String filename, String content) //filename ends in txt, centent is one big string
		{
			int blockNum = 0;
			List<String> subString = new ArrayList<String>(); //break down strings
			System.out.println("Content: " + content);
			if(content.length()*2>MB) {
				blockNum = MB/content.length()+1; 
			}
			else {
				blockNum = 1;
			}
			System.out.println("First BlockNum: " + blockNum);

			for(int i = 0; i < blockNum; i++)//cut string by by
			{
				int idx1 = i*MB;
				int idx2 = (i+1)*MB;
				if(idx2 > content.length()) {
					idx2 = content.length();
				}
				System.out.println("idx1=" + idx1 + " idx2=" + idx2);
				
				subString.add(content.substring(idx1, idx2));
				System.out.println("Substring in Append: " + subString.get(i));
			}


			String returnID = null; //the message giving back
			String success = null;
			List<Pair> list = new ArrayList<Pair>();
			int NumBlocksReceived = 0;
			int DNDirector = 0;
			System.out.println("BlockNum: " + blockNum);
			while(NumBlocksReceived < blockNum)
			{
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//reqest send to DN1, DN2, DN3
				//talk to DNDirect%3(th) DataNode
				//EX: 5 blockNum, request to DN1,DN2,DN3,DN1,DN2
				//if a freeblock is given back, set gotit to true
				System.out.println("DNDirector="+DNDirector);
				if(DNDirector%3 == 0)
				{
					returnID = ctoD1.sendMessage("Alloc");
					System.out.println("Received: " + returnID);
					System.out.println("Sent message Alloc to 1");
					if(returnID.equals("-1")) 
					{DNDirector++;}//-1 ,parse it to int 
					else
					{
						Pair pair = new Pair("D1",Integer.parseInt(returnID));
						list.add(pair);
						String msg = "Write "+returnID+ " "+ subString.get(NumBlocksReceived);
						System.out.println("Msg: " + msg);
						System.out.println("DataNode is : "+ pair.getdataNode());
						System.out.println("BlockID is : "+ pair.getblockNode());

						success = ctoD1.sendMessage("Write "+returnID+ " "+ subString.get(NumBlocksReceived));
						NumBlocksReceived++;
					}
				}
				else if(DNDirector%3 == 1)
				{
					returnID = ctoD2.sendMessage("Alloc");
					System.out.println("Received: " + returnID);

					System.out.println("Sent message Alloc to 2");
					if(returnID.equals("-1")) 
					{DNDirector++;}//-1 ,parse it to int 
					else
					{
						Pair pair = new Pair("D2",Integer.parseInt(returnID));
						list.add(pair);
						success = ctoD2.sendMessage("Write "+returnID+ " "+ subString.get(NumBlocksReceived));
						NumBlocksReceived++;
					}
				}
				else if(DNDirector%3 == 2)
				{
					returnID = ctoD3.sendMessage("Alloc");
					System.out.println("Received: " + returnID);

					System.out.println("Sent message Alloc to 3");
					if(returnID.equals("-1")) 
					{DNDirector++;}//-1 ,parse it to int 
					else
					{
						Pair pair = new Pair("D3",Integer.parseInt(returnID));
						list.add(pair);
						success = ctoD1.sendMessage("Write "+returnID+ " "+ subString.get(NumBlocksReceived));
						NumBlocksReceived++;
					}
				}

			}
			map.put(filename, list);//saves in the hash table
			System.out.println("Called append within the handler");
		}


		public static void Read(String filename)
		{
			String content = null;
			List<Pair> temp = new ArrayList<Pair>();
			if(map.containsKey(filename))//if the file is found
			{
				temp = map.get(filename);	
				for(int j = 0; j < temp.size(); j++)//add mutex to this???
				{
					//File infile =new File(arr[index][j]);
					temp.get(j).getdataNode();
					//send a message to the specific data node
					//give back the centent of block note
					if(temp.get(j).getdataNode().equals("D1"))
					{
						content = ctoD1.sendMessage("Read " + Integer.toString(temp.get(j).getblockNode()));
						conCat.set(j,content);
					}
					if(temp.get(j).getdataNode().equals("D2"))
					{
						content = ctoD2.sendMessage("Read " + Integer.toString(temp.get(j).getblockNode()));
						conCat.set(j,content);
					}
					if(temp.get(j).getdataNode().equals("D3"))
					{
						content = ctoD3.sendMessage("Read " + Integer.toString(temp.get(j).getblockNode()));
						conCat.set(j,content);
					}
				}
				String joined = String.join(" ", conCat); //conCat is a string list
				//joined is send back to client
				output(joined);

			}else{System.out.println("The file does not exist.");
			}
			System.out.println("Called Read within the handler");
		}
		private static void output(String out) {
			try {
				PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
				pw.print(out);
				pw.flush();
			} catch (Exception e) {

			}
		}
	}	//END NAME NODE HANDLER


}//END NAME NODE




