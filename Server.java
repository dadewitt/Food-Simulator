import java.net.*;
import java.io.*;

import java.util.*;
import java.sql.*;

import HTTP.HeaderDictionary;
import HTTP.HttpReader;


public class Server {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int clientId = 0;
			ServerSocket server = new ServerSocket(3001);
			System.out.println("Server started at " + server.getLocalSocketAddress() + ":" + server.getLocalPort() + "\n");
			while (true) {
			
				/*
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
				*/
				Socket incoming = server.accept();
				System.out.println("ADDR: " + incoming.getRemoteSocketAddress());
				System.out.println("Spawning " + clientId);
				Runnable r = new ThreadHandler(incoming);
				Thread t = new Thread(r);
				t.start();
				clientId++;
			}				
		} catch (Exception e) {	e.printStackTrace();	}
	}

}

class ThreadHandler implements Runnable {
	final static String ServerUser = "user";
	final static String ServerPassword = "password";
	private Socket incoming;
	
	public ThreadHandler(Socket i) {
		incoming = i;
	}
	
	
   public static Connection getConnection() throws SQLException, IOException
   {
      Properties props = new Properties();
      FileInputStream in = new FileInputStream("database.properties");
      props.load(in);
      in.close();
      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null)
        System.setProperty("jdbc.drivers", drivers);
      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");

      System.out.println("url="+url+" user="+username+" password="+password);
      return DriverManager.getConnection( url, username, password);
   }


   void handleRequest( InputStream inStream, OutputStream outStream) {
        Scanner in = new Scanner(inStream);         
        PrintWriter out = new PrintWriter(outStream, 
                                      true /* autoFlush */);

	// Get parameters of the call
	String request = in.nextLine();

	System.out.println("Request="+request);

	String requestSyntax = "Syntax: SAVE;user;password;Score;MAC;Lat;Lon";

	try {
		// Get arguments.
		// The format is COMMAND|USER|PASSWORD|OTHER|ARGS...
		String [] args = request.split(";");

		// Print arguments
		for (int i = 0; i < args.length; i++) {
			System.out.println("Arg "+i+": "+args[i]);
		}

		// Get command and password
		String command = args[0];
		String user = args[1];
		String password = args[2];

		// Check user and password. Now it is sent in plain text.
		// You should use Secure Sockets (SSL) for a production environment.
		if ( !user.equals(ServerUser) || !password.equals(ServerPassword)) {
			System.out.println("Bad user or password");
			out.println("Bad user or password");
			return;
		}

		// Do the operation
		if (command.equals("TEST")) {
			System.out.println("Do the test");
			testDB(args, out);
		} else if (command.equals("SAVE")) {
			saveDB(args, out);
		}
		/*
		if (command.equals("GET-ALL-PETS")) {
			//getAllPets(args, out);
		}
		else if (command.equals("GET-PET-INFO")) {
			//getPetInfo(args, out);
		}
		*/
	}
	catch (Exception e) {		
		System.out.println(requestSyntax);
		out.println(requestSyntax);

		System.out.println(e.toString());
		out.println(e.toString());
	}
   }
   
    void saveDB( String [] args, PrintWriter out) {

      Connection conn=null;
      try
      {
	conn = getConnection();
        Statement stat = conn.createStatement();
	
	stat.executeUpdate( "INSERT INTO user (mac, latitude, longitude, score) values ('" + args[5] + "'," + args[3] + "," + args[4] + "," + args[6] + ") " + 
											"on duplicate key update latitude=values(latitude), longitude=values(longitude), score=values(score);");

	/*while(result.next()) {
       		out.print(result.getString(1)+"|");
       		out.print(result.getString(2)+"|");
       		out.print(result.getString(3)+"|");
       		out.print(result.getString(4)+"|");
       		out.print(result.getString(5));
		out.println("");
	}*/

	//result.close();
      }
      catch (Exception e) {
	System.out.println(e.toString());
	out.println(e.toString());
      }
      finally
      {
	try {
         if (conn!=null) conn.close();
	}
	catch (Exception e) {
	}
      }
   }
   
    void testDB( String [] args, PrintWriter out) {

      Connection conn=null;
      try
      {
	conn = getConnection();
        Statement stat = conn.createStatement();
	
	ResultSet result = stat.executeQuery( "SELECT * FROM user");

	while(result.next()) {
			System.out.print(result.getString(1)+"|");
			System.out.print(result.getString(2)+"|");
			System.out.print(result.getString(3)+"|");
			System.out.print(result.getString(4)+"|");
			System.out.println(result.getString(5)+"|");
       		out.print(result.getString(1)+"|");
       		out.print(result.getString(2)+"|");
       		out.print(result.getString(3)+"|");
       		out.print(result.getString(4)+"|");
       		out.print(result.getString(5));
		out.println("");
	}

	result.close();
      }
      catch (Exception e) {
	System.out.println(e.toString());
	out.println(e.toString());
      }
      finally
      {
	try {
         if (conn!=null) conn.close();
	}
	catch (Exception e) {
	}
      }
   }

   public void run() {  
      try
      {  
         try
         {
            InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream();
	    handleRequest(inStream, outStream);

         }
      	 catch (IOException e)
         {  
            e.printStackTrace();
         }
         finally
         {
            incoming.close();
         }
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
   }
}
