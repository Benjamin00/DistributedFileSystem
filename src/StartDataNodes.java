import java.io.IOException;

public class StartDataNodes {

	public static void main(String[] args) throws IOException {
		//start three data nodes: 65530, 65531, 65532
		ProcessBuilder pb0 = new ProcessBuilder("java", "-classpath", System.getProperty("java.class.path"),
				"DataNode", "65530");
		pb0.start();
		
		ProcessBuilder pb1 = new ProcessBuilder("java", "-classpath", System.getProperty("java.class.path"),
				"DataNode", "65531");
		pb1.start();
		
		ProcessBuilder pb2 = new ProcessBuilder("java", "-classpath", System.getProperty("java.class.path"),
				"DataNode", "65532");
		pb2.start();
		
	}

}
