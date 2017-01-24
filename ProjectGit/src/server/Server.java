package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Server.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Server {
	private static final String USAGE = "usage: " + Server.class.getName() + " <name> <port>";
	private static Socket clientSocket;
	private static ServerSocket serversock;
	static List<Socket> clientList;
	static List<String> playerList;
	static Map<Socket, String> clientMap;

	/** Starts a Server-application. */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(USAGE);
			System.exit(0);
		}

		String name = args[0];
		InetAddress addr = null;
		String hostaddress = null;
		int port = 0;
		clientList = new LinkedList<>();
		playerList = new LinkedList<>();
		clientMap = new HashMap<>();

		// Socket sock = null;
		try {
			hostaddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			System.out.println("Invalid local address: " + hostaddress);
			e1.printStackTrace();
		}

		// parse args[1] - the port
		try {
			port = Integer.parseInt(args[1]);

			System.out.println("Server has started on " + hostaddress + " and port " + port);
		} catch (NumberFormatException e) {
			System.out.println(USAGE);
			System.out.println("ERROR: port " + args[1] + " is not an integer");
			System.exit(0);
		}

		// try to open a Socket to the server
		try {
			serversock = new ServerSocket(port);

			while (true) {
				clientSocket = serversock.accept();
				System.out.println(serversock);
				// create Peer object and start the two-way communication
				try {
					Peer server = new Peer(name, clientSocket);
					clientList.add(server.sock);
					playerList.add(server.name);
					clientMap.put(server.sock, name);
					System.out.println(clientList.size());
					Thread streamInputHandler = new Thread(server);
					streamInputHandler.start();
					server.SendServerCapabilities();
					//System.out.println(server.RecievedCapabilities());
//					if (server.RecievedCapabilities()) {
//						server.MessageForWaiting();
//					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			System.out.println("ERROR: could not create a socket on " + addr + " and port " + port + ": Port busy");
		}
	}
}
