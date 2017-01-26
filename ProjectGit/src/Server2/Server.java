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
			System.out.println("Sever has started on IP-address: " + hostAddress);
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
	public void run() {

		try {
			System.out.println("Waiting...");
			serverSock = new ServerSocket(serverPort);

			while (true) {
				Socket socket = serverSock.accept();
				System.out.println("Connected: " + socket);

				String ID = getID();

				ClientHandler newClient = new ClientHandler(ID, socket);
				addHandler(newClient);

				newClient.run();

				System.out.println(clientPreferences);

			}
		} catch (Exception e) {
			System.out.println("fout1");
			e.printStackTrace();
			// TODO: exception
		}
	}

	// public void runSetup(ClientHandler newClient){
	// serverCapabilities(newClient);
	// }

	public String serverCapabilities() {
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
	public void broadcast(String msg) {
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
	public void addHandler(ClientHandler handler) {
		if (!clients.containsValue(handler)) {
			clientNO++;
			clientID = "C-" + Integer.toString(clientNO);
			clients.put(clientID, handler);
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
	public void removeHandler(ClientHandler handler) {

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

		for (Map.Entry<String, String> entry : clientPreferences.entrySet()) {
			System.out.println("checking other preferences");

			System.out.println(gamePrefs);
			System.out.println(entry.getValue());

			if (entry.getValue().equals(gamePrefs)) {
				System.out.println("preferences are the same");
				String player1 = clientID;
				String player2 = entry.getKey();

				clientPreferences.remove(player2);
				startGame(player1, player2, gamePrefs);
			}
		}

		if (!clientPreferences.containsKey(clientID)) {
			clientPreferences.put(clientID, gamePrefs);
			gameNames.put(clientID, clientNames);
		} else {
			System.out.println("Something went wrong trying to add preferences to the map.");
		}
	}

	public void startGame(String p1, String p2, String gamePrefs) {
		System.out.println("Setting up a game for " + p1 + " and " + p2 + " with game preferences " + gamePrefs);
		
		clients.get(p1).sendMessage("Setting up a game of connect four against " + p2);
		clients.get(p2).sendMessage("Setting up a game of connect four against " + p1);
		
		String name1 = gameNames.get(p1);
		String name2 = gameNames.get(p2);

		String[] parts = gamePrefs.split(" ");

		int dim = Integer.parseInt(parts[1]);

		project.ConnectFour.main(name1, name2, dim); //werkt nog niet ivm args[]
	}

}
