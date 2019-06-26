
public class Central extends Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c = new Client();
		c.startConnection("127.0.0.1", 6667);
		String resp = c.sendMessage("Hello World");
		
	}

}
