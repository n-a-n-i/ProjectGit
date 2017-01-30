package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class StartClient {

	// private static String name;
	private static BufferedReader in;
	private static InetAddress host;
	private static int port;

	private static Client client;

	public static void main(String[] args) {
		in = new BufferedReader(new InputStreamReader(System.in));

		try {
			//-----Asks for the IP-adress------------
			System.out.println("Enter IP adress of the server you want to join: ");
			host = InetAddress.getByName(in.readLine());

			//-----Asks for the port number----------
			System.out.println("Enter port number: ");
			port = Integer.parseInt(in.readLine());

			//-----Starts a new client thread---------
			client = new Client(host, port);
			(new Thread(client)).start();

		} catch (IOException e) {
			System.out.println("Could not connect to IP-address " + host + "with port " + port + ".");
		}
	}

}
