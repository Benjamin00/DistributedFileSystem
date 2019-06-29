
public class Central extends Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c = new Client();
		c.startConnection("127.0.0.1", 6667);
		String resp = c.sendMessage("Hello World");
		

		// For greater than 4 MB

		Client d = new Client();
		d.startConnection("127.0.0.1", 6667);
		String messageStr = d.sendMessage("Hello");

		// For the less than 4 MB 

		Client e = new Client(); 
		e.startConnection("127.0.0.1", 6667);
		String messageString = e.sendMessage("Hi");
	}

}
