import java.net.*;
import java.util.Scanner;

public class Client_Test {
	public static void main(String[] args) {
		String host = "localhost";
		if (args.length == 1)
			host = args[0];
		System.out.println("Welcome to the test client. Type in your message to send it to the server, then press enter. To leave, type 'exit'");
		System.out.println("To read response data, type 'read'\n");
		try {
			// Open scanner to grab messages from standard input
			Scanner inp = new Scanner(System.in);
			
			// Open socket to connect to the server; first argument is the host, second argument is the port
			Socket serverCon = new Socket(host, 3001);
			
			// Store commend
			String cmd = "";
			do {
				// Grabe the message
				cmd = inp.nextLine();
				
				if (cmd.equals("exit"))
					break;
				
				if (cmd.equals("read")) {
					java.io.BufferedReader rdr = new java.io.BufferedReader( new java.io.InputStreamReader( serverCon.getInputStream() ) );
					String line;
					while ( (line = rdr.readLine()) != null) {
						System.out.println(line);
					}
					continue;
				}
				
				// Open a PrintWriter to be able to write (string) messages to the server
				java.io.PrintWriter writer = new java.io.PrintWriter(serverCon.getOutputStream(), true);
				
				// Write the command to the server's input pipe (server uses readLine, so use writer.println())
				writer.println(cmd);
				
				// Send the data to the server
				writer.flush();
				
			} while (!cmd.equals("exit"));
			
			// Close the connection to the server
			serverCon.close();
			
		} catch (Exception e) {}
	}

}
