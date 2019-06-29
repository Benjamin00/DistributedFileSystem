
public class Central extends Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c = new Client();
		c.startConnection("127.0.0.1", 6667);
		String resp = c.sendMessage("Hello World");
		

		// Each of these client objects send a request


		// For greater than 4 MB

		Client d = new Client();
		d.startConnection("127.0.0.1", 6667);
		String messageStr = d.sendMessage("READ greaterThanFourMB.txt");
		String str = d.sendMessage("APPEND greaterThanFourMB.txt My name is");

		// For the less than 4 MB 

		Client e = new Client(); 
		e.startConnection("127.0.0.1", 6667);
		String messageString = e.sendMessage("READ D19.txt");
		String msgString = e.sendMessage("APPEND D19.txt Hi");
	}

}
