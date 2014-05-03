import java.net.*;
import java.util.Scanner;
import java.io.*;

import HTTP.HeaderDictionary;
import HTTP.HttpReader;


public class Server {

	private static ServerSocket server;
	private static CommandThread cmdGet;
	
	private static class CommandThread extends Thread {
		private String cmd = "none";
		private Scanner scan = new Scanner(System.in);
		public void run() {
			while (true) {
				cmd = scan.next();
				if (cmd.equals("exit")) {
					try {
						server.close();
						System.exit(0);
					} catch (Exception e) {}
				}
			}
		}
		
		public String getCommand() {
			return cmd;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int clientId = 0;
			cmdGet = new CommandThread();
			cmdGet.start();
			server = new ServerSocket(3001);
			System.out.println("Server started at" + server.getLocalSocketAddress() + ":" + server.getLocalPort());
			System.out.println("To disconnect the server and regain console control, type 'exit'\n");
			while (!cmdGet.getCommand().equals("exit")) {
				Socket client = server.accept();
				java.io.PrintWriter writer = new java.io.PrintWriter(client.getOutputStream(), true);
				System.out.println("Client Connected! ID=" + clientId++);
				java.io.BufferedReader reader = new java.io.BufferedReader(new InputStreamReader(client.getInputStream()) );
				String request = "";
				//System.out.println("ready to read: " + reader.ready());
				String bob = "";
				while ( (bob = reader.readLine()) != null) {
					System.out.println("client MsG: " + bob);
					request += bob + "\n";
				}
				//HeaderDictionary fieldPairs = HttpReader.parseRequest(request);
				//fieldPairs.print();
				//writer.write("HTTP/1.1 200 Document follows\r\nServer: Test-Server\r\nContent-Type: text/html\r\n\r\n");
				//writer.append("Hello, world!<br /><a href='http://www.google.com'>Google<a>");
				//writer.flush();
				client.close();
				System.out.println("Client Disconnected.\n");
			}
			
			server.close();
				
		} catch (Exception e) {		}
	}

}
