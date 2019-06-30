import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//contains a block's information (memory location, and necessary locks)
public class Block {
	private String filename; //file where data is stored in a data node
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock rLock = rwLock.readLock(); //reader side of readWriteLock
	private final Lock wLock = rwLock.writeLock(); //writer side of readWriteLock
	
	Block(String fname){ //create block with filename
		this.filename = fname;
	}
	
	String getFilename() { //get block filename
		return this.filename;
	}

	Lock getRLock() { //get access to read lock
		return this.rLock;
	}

	Lock getWLock() { //get access to write lock
		return this.wLock;
	}

}
