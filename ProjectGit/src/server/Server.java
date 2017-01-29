package server;

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
import java.util.Random;
import java.util.Vector;

import project.Mark;

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

	private String hostAddress;
	private ServerSocket serverSock;
	public Socket clientSock;
	private int clientNO = 100;

	public String clientID;

	public Map<String, String> clientPreferences;
	public Map<String, String> gameNames;

	public Map<String, ClientHandler> clients;

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
				new Thread(newClient).start();
				newClient.sendMessage(serverCapabilities());
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
		String clientID = Integer.toString(clientNO);
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

	public void putPreferences(String clientID, String name, String preferences) {
		System.out.println("putting a client with preferences and name");
		clientPreferences.put(clientID, preferences);
		gameNames.put(clientID, name);

	}

	public void checkMatch(String clientID, String prefs) {

		for (Map.Entry<String, String> entry : clientPreferences.entrySet()) {
			if (entry.getValue().equals(prefs) && !entry.getKey().equals(clientID)) {
				System.out.println("Setting up a game for " + entry.getKey() + " and " + clientID
						+ " with game preferences " + prefs);

				String[] parts = prefs.split(" ");
				int dim = Integer.parseInt(parts[2]);

				String roomDimensionX = parts[2];
				String roomDimensionY = parts[3];
				String roomDimensionZ = parts[4];
				String lenghtToWin = parts[5];

				String playerID1 = clientID;
				String playerName1 = gameNames.get(clientID);
				String playerColour1 = "0000ff";
				ClientHandler ch1 = clients.get(playerID1);

				String playerID2 = entry.getKey();
				String playerName2 = gameNames.get(entry.getKey());
				String playerColour2 = "ff1a00";
				ClientHandler ch2 = clients.get(playerID2);

				String output = "startGame " + roomDimensionX + "|" + roomDimensionY + "|" + roomDimensionZ + "|"
						+ lenghtToWin + "| " + playerID1 + "|" + playerName1 + "|" + playerColour1 + " " + playerID2
						+ "|" + playerName2 + "|" + playerColour2;

				ch1.sendMessage(output);
				ch2.sendMessage(output);

				NetworkPlayer player1 = new NetworkPlayer(playerName1, ch1, Mark.OOO);
				NetworkPlayer player2 = new NetworkPlayer(playerName2, ch2, Mark.XXX);
				ch1.setMark(Mark.OOO);
				ch2.setMark(Mark.XXX);

				ch1.setOpponent(ch2);
				ch2.setOpponent(ch1);

				Random r = new Random();
				if ((r.nextInt() % 2) == 0) {
					ServerGame game = new ServerGame(player1, player2, Integer.parseInt(roomDimensionX));
					ch1.setGame(game);
					ch2.setGame(game);
					ch1.sendMessage("playerTurn " + playerID1);
					ch2.sendMessage("playerTurn " + playerID1);

				} else {
					ServerGame game = new ServerGame(player2, player1, Integer.parseInt(roomDimensionX));
					ch1.setGame(game);
					ch2.setGame(game);
					ch1.sendMessage("playerTurn " + playerID2);
					ch2.sendMessage("playerTurn " + playerID2);

				}

				clientPreferences.remove(playerID1);
				clientPreferences.remove(playerID2);
			}
		}
	}

}
