import java.net.*;
import java.util.Scanner;

public class Client_Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Scanner inp = new Scanner(System.in);
			String cmd = "";
			do {
				cmd = inp.nextLine();
				Socket serverCon = new Socket("localhost", 3001);
				java.io.PrintWriter writer = new java.io.PrintWriter(serverCon.getOutputStream(), true);
				writer.write("CLIENT: " + cmd);
				System.out.println(cmd);
				writer.flush();
				serverCon.close();
				
			} while (!cmd.equals("exit"));
			
		} catch (Exception e) {}
	}

}
