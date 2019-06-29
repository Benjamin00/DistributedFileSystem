import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;


public class StartDataNodes {

	public static void main(String[] args) throws IOException {
		//start three data nodes: 65530, 65531, 65532
		startNode(65530, "D0.log");
		startNode(65531, "D1.log");
		startNode(65532, "D2.log");
	}
	
	public static void startNode(int port, String logFile) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("java", "-classpath", System.getProperty("java.class.path"),
				"DataNode", String.valueOf(port));
		File log = new File(logFile);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		pb.start();
	}

}
