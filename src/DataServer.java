import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

class DataServer extends Thread {
    private Socket clientSocket;

    public DataServer(Socket clientSocket) {
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
    public void run() {
        String command = readCommand();

        // parse the command here and do what it asks you to do

        String out = "your output goes here";

        // overwrite out as you wish

        output(out);

        close();
    }
}