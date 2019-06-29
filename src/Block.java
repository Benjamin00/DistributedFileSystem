import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//contains a block's information (memory location, and necessary locks)
public class Block {
	private String filename;
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock rLock = rwLock.readLock(); //reader side of readWriteLock
	private final Lock wLock = rwLock.writeLock(); //writer side of readWriteLock
	
	Block(String fname){
		this.filename = fname;
	}
	
	String getFilename() {
		return this.filename;
	}

	Lock getRLock() {
		return this.rLock;
	}

	Lock getWLock() {
		return this.wLock;
	}

}
