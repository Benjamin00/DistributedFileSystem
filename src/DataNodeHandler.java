import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

class DataNodeHandler extends Thread {
    private Socket clientSocket;
   
    public DataNodeHandler(Socket clientSocket) {
        this.clientSocket  = clientSocket;
    }

    private String readCommand() {
        String command = null;
        try {
            Reader reader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            command = br.readLine().trim();
        } catch (Exception e) {

        }
        return command;
    }

    private void output(String out) {
        try {
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            pw.print(out);
            pw.flush();
        } catch (Exception e) {

        }
    }

    private void close() {
        try {
            this.clientSocket.close();
        } catch (Exception e) {

        }
    }

    @Override
    public void run(){
        String command = readCommand();
        System.out.println("Port[" + this.clientSocket.getPort() + "] received message: " + command);
                
        // parse the command here and do what it asks you to do
        String cmdParts[] = command.split(" ", 2);
        String cmdKey = cmdParts[0];
        switch(cmdKey) {
        case "ALLOC":
        	//call alloc
        	int block = alloc(); //FIXME I need access to my data node...
        case "READ":
        	//call read
        case "WRITE":
        	//call write
        default:
        	//Error, invalid command
        	//do nothing
        }

        String out = "your output goes here";

        // overwrite out as you wish

        output(out);

        close();
    }
}