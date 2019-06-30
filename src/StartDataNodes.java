import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;


public class StartDataNodes {

	public static void main(String[] args) throws IOException {
		//start three data nodes: 65530, 65531, 65532
		int ports[] = {65530, 65531, 65532};
		for(int i = 0; i < ports.length; i++) {
			int p = ports[i];
			startNode(p, "DNode_"+p+".log"); //put port in log name
		}
		
		System.out.println("All data nodes are running...");
	}
	
	public static void startNode(int port, String logFile) throws IOException {
		//start each data node in its own process
		ProcessBuilder pb = new ProcessBuilder("java", "-classpath", System.getProperty("java.class.path"),
				"DataNode", String.valueOf(port));
		File log = new File(logFile);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		pb.start();
	}

}
