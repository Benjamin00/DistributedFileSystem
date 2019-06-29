
public class Pair {
	private String DataNum;
	//private int DataNode_Port;
	private int BlockNum;
	
	public Pair(String DNum, int BNum)
	{
		DataNum = DNum;
		BlockNum = BNum;
	}
	
	public String getdataNode(){return DataNum;}
	public void setdataNode(String DNum) {DataNum = DNum;}
	
	public int getblockNode(){return BlockNum;}
	public void setblockNode(int BNum) {BlockNum = BNum;}
}
