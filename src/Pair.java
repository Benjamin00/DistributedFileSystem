
public class Pair {
	private String DataNum;
	//private int DataNode_Port;
	private String BlockNum;
	
	public Pair(String DNum, String BNum)
	{
		DataNum = DNum;
		BlockNum = BNum;
	}
	
	public String getdataNode(){return DataNum;}
	public void setdataNode(String DNum) {DataNum = DNum;}
	
	public String getblockNode(){return BlockNum;}
	public void setblockNode(String BNum) {BlockNum = BNum;}
}
