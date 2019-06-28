import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NNHM {
	final static int MB = 4194304;//String cut in 4MB
	//hashmap that stores the filename and DNum+BNum
	static Map<String, List<Pair>> map=new HashMap<String, List<Pair>>(); 
	//ArrayList that contain all the substring of contents
	static List<String> subString = new ArrayList<String>(); //break down strings
	static List<String> conCat = new ArrayList<String>(); //concatenate strings
	//mutex
	//private final Object mutex = new Object();
	
	
	public static void main(String[] args) {
		//Append("First.txt","Hello!"); //Append only when it does not exist, otherwise deny it
		
	}
	
	public static void Append(String filename, String content) //filename ends in txt, centent is one big string
	{
	   int blockNum = 0;
	   if(content.length()*2>MB)
		   blockNum = MB/content.length()+1; 
	   for(int i = 0; i < blockNum; i++)//cut string by by
	   {
		   subString.set(i,content.substring(i*MB,(i+1)*MB));
	   }
	   
	   //ask if D1 has free blocks
	   //D2...
	   //D3...
	   //send blockNum & arrayList subString
	   //get back block1 from D1, NameNode pair them up and store in the hash table
	   //if gives 3 blocks back
	   List<Pair> list = new ArrayList<Pair>();
	   Pair pair1 = new Pair("D1","B2");
	   list.add(pair1);
	   
	   Pair pair2 = new Pair("D2","B3");
	   list.add(pair2);
	   
	   Pair pair3 = new Pair("D3","B4");
	   list.add(pair3);

	   map.put(filename, list);//saves in the hash table
	}
	
	public static void Read(String filename)
	{
		List<Pair> temp = new ArrayList<Pair>();
		if(map.containsKey(filename))//if the file is found
		{
			temp = map.get(filename);	
			for(int j = 0; j < temp.size(); j++)//add mutex to this???
			{
					//File infile =new File(arr[index][j]);
					temp.get(j).getdataNode();
					temp.get(j).getblockNode();
					//send a message to the specific data node
					//give back the centent of block note
					
					//conCat.set(j, //contnet);
			}
			String joined = String.join(" ", conCat);
			//joined is send back to client
		}else{System.out.println("The file does not exist.");
		}
	}
	
	
}
	
	


