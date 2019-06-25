import java.util.ArrayList;

public class DataNode {
	private int MAX_BLOCKS = 10;
	private int blk;
	private int id;
	private ArrayList<String> data;
	
	public static void main(String[] args){
		//declare a couple data notes
		DataNode one = new DataNode(1); //numbers
		DataNode two = new DataNode(2); //animals
		DataNode three = new DataNode(3); //fruits
		
		//add some data
		for(int i = 0; i < 11; i++) {
			int block = one.alloc();
			if(block < 0) {
				System.out.println("Couldn't allocate data for block " + i);
				if(one.isFull()) {
					System.out.println("yes, we're full...");
				}
			}
			else {
				one.write(i, String.valueOf(i));
			}
			System.out.println(one.numEmptyBlks() + " blocks available for node one");
		}
		
		//node two
		int block = two.alloc();
	    two.write(block, "reindeer");
		block = two.alloc();
		two.write(block, "elephant");
		block = two.alloc();
		two.write(block, "sheep");
		
		//node three
		block = three.alloc();
		three.write(block, "apple");
		three.write(block, "orange");
		three.write(block, "mango");
		System.out.println("Do we have mango? " + three.read(block));
		
	//print out all three node contents
		one.print();
		two.print();
		three.print();

	}	
	
	//constructor
	DataNode(int id){
		this.blk = -1;
		this.id = id;
		data = new ArrayList<String>(MAX_BLOCKS);
	}
	
	int id() {
		return this.id;
	}
	
	//three functions to support:
	//1. alloc returns the block number allocated, -1 if none available
	int alloc() {
		if(this.blk < MAX_BLOCKS-1) {
			this.blk++;
			return this.blk;
		}
		else {
			return -1;
		}
	}
	
	//2. given block id, read data and return contents
	String read(int blk_id) {
		return data.get(blk_id);
	}
	
	//3. write contents to block id
	boolean write(int blk_id, String contents) {
		if(blk_id >= data.size()) {
			data.add(contents);
		}
		else {
			data.set(blk_id, contents);
		}
		return true;
	}
	
	//some convenient functions
	boolean isFull() {
		return this.blk == MAX_BLOCKS-1;
	}
	
	int numEmptyBlks() {
		return (MAX_BLOCKS-1)-blk;
	}
	
	void print(){
		System.out.println("Data Node " + id + " contents:");
		for(int i = 0; i < data.size(); i++) {
			System.out.println("Block " + i + ": " + data.get(i));
		}
	}
}
