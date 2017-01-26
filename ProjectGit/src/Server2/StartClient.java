package server2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class StartClient {

	private static String name;
	private static BufferedReader in;
	private static InetAddress host;
	private static int port;
	
	public static void main(String[] args) {
		in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("Enter username: ");
			name = in.readLine();
			
			System.out.println("Enter IP adress of the server you want to join: ");
			host = InetAddress.getByName(in.readLine());
			
			System.out.println("Enter port number: ");
			port = Integer.parseInt(in.readLine());
			
			new Client(name, host, port);
			
		} catch (IOException e) {
			System.out.println("fout2");
		}
		
	}
}
