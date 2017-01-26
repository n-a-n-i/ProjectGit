package server2;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * P2 prac wk5. <br>
 * Server. A Thread class that listens to a socket connection on a specified
 * port. For every socket connection with a Client, a new ClientHandler thread
 * is started.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Server extends Thread {
	private String name;
	private int serverPort;
	private MessageUI mui;
	private Map<String, ClientHandler> clients;
	private String hostAddress;
	private ServerSocket serverSock;
	public Socket clientSock;
	private int clientNO = 100;

	public String clientID;
	private Map<String, String> clientPreferences;
	private Map<String, String> gameNames;

	// ---- server capabilities----
	int amountOfPlayers = 2;
	int roomSupport = 0;
	int maxRoomDimensionX = 10;
	int maxRoomDimensionY = 10;
	int maxRoomDimensionZ = 10;
	int maxLengthToWin = 4;
	int chatSupport = 0;

	/** Constructs a new Server object. */
	public Server(String name, int portArg) {
		this.serverPort = portArg;
		this.name = name;

		clients = new HashMap<String, ClientHandler>();
		clientPreferences = new HashMap<String, String>();
		gameNames = new HashMap<String, String>();

		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Sever has started on IP-address: " + hostAddress + " and local port: " + portArg);
		} catch (UnknownHostException e) {
			// TODO throw unknow hostexception
			System.out.println("Invalid local address: " + hostAddress);
		}
	}

	/**
	 * Listens to a port of this Server if there are any Clients that would like
	 * to connect. For every new socket connection a new ClientHandler thread is
	 * started that takes care of the further communication with the Client.
	 */
	public synchronized void run() {

		try {
			serverSock = new ServerSocket(serverPort);

			while (true) {
				System.out.println("Waiting for clients to connect...");

				Socket clientSocket = serverSock.accept();
				System.out.println("Connected: " + clientSocket);

				this.clientID = getID();

				ClientHandler newClient = new ClientHandler(this.clientID, clientSocket);

				newClient.start();

			}
		} catch (Exception e) {
			System.out.println("fout1");
			e.printStackTrace();
			// TODO: exception
		}
	}

	public synchronized String serverCapabilities() {
		String capabilities = "serverCapabilities " + amountOfPlayers + " " + roomSupport + " " + maxRoomDimensionX
				+ " " + maxRoomDimensionY + " " + maxRoomDimensionZ + " " + maxLengthToWin + " " + chatSupport;
		return capabilities;
	}

	/**
	 * Sends a message using the collection of connected ClientHandlers to all
	 * connected Clients.
	 * 
	 * @param msg
	 *            message that is send
	 */
	public synchronized void broadcast(String msg) {
		for (Map.Entry<String, ClientHandler> clients : clients.entrySet()) {
			ClientHandler send = clients.getValue();
			send.sendMessage(msg);
		}
	}

	public String getID() {
		clientNO++;
		String clientID = "C-" + Integer.toString(clientNO);
		return clientID;
	}

	/**
	 * Add a ClientHandler to the collection of ClientHandlers.
	 * 
	 * @param handler
	 *            ClientHandler that will be added
	 */
	public synchronized void addHandler(ClientHandler handler) {
		if (!clients.containsValue(handler)) {
			clients.put(this.clientID, handler);
		} else {
			// TODO: throw exception
			System.out.println("This ClientHandler allready exists.");
		}
	}

	/**
	 * Remove a ClientHandler from the collection of ClientHanlders.
	 * 
	 * @param handler
	 *            ClientHandler that will be removed
	 */
	public synchronized void removeHandler(ClientHandler handler) {

		if (clients.containsValue(handler)) {
			clients.remove(handler);
		} else {
			System.out.println("Cannot remove " + handler);
			// TODO: throw exception clienthandler not in the list
		}
	}

	public void mapPreferences(String clientID, String clientPref) {
		String[] parts = clientPref.split(" ");

		String clientNames = parts[2];
		String gamePrefs = parts[1] + " " + parts[3] + " " + parts[4] + " " + parts[5] + " " + parts[6] + " " + parts[7]
				+ " " + parts[8];

		if (!clientPreferences.containsKey(clientID)) {
			clientPreferences.put(clientID, gamePrefs);
			gameNames.put(clientID, clientNames);
		} else {
			System.out.println("Something went wrong trying to add preferences to the map.");
		}
	}

	public boolean checkMatch(String client, String prefs) {
		boolean match = false;

		for (Map.Entry<String, String> entry : clientPreferences.entrySet()) {
			if (entry.getValue().equals(prefs) && !entry.getKey().equals(client)) {
				String player1 = client;
				String player2 = entry.getKey();

				// System.out.println("Found a match!");
				// System.out.println(player1);
				// System.out.println(player2);
				// System.out.println(clients);

				clients.get(player1).hasMatch = true;
				clients.get(player2).hasMatch = true;

				clientPreferences.remove(player1);
				clientPreferences.remove(player2);
				startGame(player1, player2, prefs);
			}
		}
		return match;
	}

	public synchronized void startGame(String p1, String p2, String gamePrefs) {
		System.out.println("Setting up a game for " + p1 + " and " + p2 + " with game preferences " + gamePrefs);

		clients.get(p1).sendMessage("Setting up a game of connect four against " + p2);
		clients.get(p2).sendMessage("Setting up a game of connect four against " + p1);

		String[] parts = gamePrefs.split(" ");
		int dim = Integer.parseInt(parts[1]);

		String roomDimensionX = parts[1];
		String roomDimensionY = parts[2];
		String roomDimensionZ = parts[3];
		
		String playerID1 = p1;
		String playerName1 = gameNames.get(p1);
		String playerColour1 = "0000ff";
		
		String playerID2 = p2;
		String playerName2 = gameNames.get(p2);
		String playerColour2 = "ff1a00";
		
		
		String output = "startGame " + roomDimensionX + "|" + roomDimensionY + "|" + 
				roomDimensionZ + "|" + playerID1 + "|" + playerName1 + "|" + playerColour1 +
				" " + playerID2 + "|" + playerName2 + "|" + playerColour2;   

		clients.get(p1).sendMessage(output);
		clients.get(p2).sendMessage(output);
		
		projectServer.ConnectFour.main(playerName1, playerName2, dim);

	}


	
	

}
