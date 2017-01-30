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
import java.util.Scanner;
import java.util.Vector;

import project.Mark;
import protocol.ProtocolConstants;
import protocol.ProtocolControl;

/**
 * P2 prac wk5. <br>
 * Server. A Thread class that listens to a socket connection on a specified
 * port. For every socket connection with a Client, a new ClientHandler thread
 * is started.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Server extends Thread implements ProtocolConstants, ProtocolControl {

	private ServerSocket socket;

	// -----Maps to handle clients and their preferences
	public Map<Integer, String> clientPreferences;
	public Map<Integer, String> gameNames;
	public Map<Integer, ClientHandler> clients;

	private static Scanner scanner;
	private static int port;

	private String name;
	private int serverPort;

	private String hostAddress;
	private ServerSocket serverSock;
	public Socket clientSock;
	private int clientNO = 100;

	public String clientID;

	// ---- server capabilities----
	int amountOfPlayers = 2;
	int roomSupport = 0;
	int maxRoomDimensionX = 10;
	int maxRoomDimensionY = 10;
	int maxRoomDimensionZ = 10;
	int maxLengthToWin = 4;
	int chatSupport = 0;

	// ------Constructor
	public Server(int portArgs) throws IOException {
		clients = new HashMap<Integer, ClientHandler>();
		clientPreferences = new HashMap<Integer, String>();
		gameNames = new HashMap<Integer, String>();

		socket = new ServerSocket(port);
		System.out.println("Server is listening on port number: " + port + " and address: " + hostAddress);
	}

	// ------Run method

	/*
	 * 
	 */
	public void run() {
		Socket clientSocket = null;
		boolean running = true;
		while (running) {
			while (true) {
				try {
					int id = 100;

					clientSocket = socket.accept();
					ClientHandler handler = new ClientHandler(this, clientSocket);
					handler.start();
					handler.sendMessage(serverCapabilities());
					addHandler(id, handler);
					System.out.println("Client with id: " + id + " has entered.");
					id++;
				} catch (IOException e) {
					System.out.println("Something went wrong.");
				} catch (NullPointerException e) {
					running = false;
				}
			}
		}
	}

	// --- Commands
	// ---------------------------------------------------------------------

	/**
	 * it sends messages to a list of clienthandlers.
	 * 
	 * @param message
	 *            the message that is sent
	 * @param listToBroadcast
	 *            a list of clienthandlers
	 */
	// @ requires message != null && listToBroadcast != null;
	public void broadcast(String message, List<ClientHandler> listToBroadcast) {
		for (ClientHandler c : listToBroadcast) {
			c.sendMessage(message);
		}
	}

	/**
	 * A clientHandler which is added to the allConnections list.
	 * 
	 * @param handler
	 *            The ClientHandler to be added
	 */
	// @ requires handler != null;
	public void addHandler(int id, ClientHandler handler) {
		clients.put(id, handler);
	}

	/**
	 * A ClientHandler which is removes from the allConnections list.
	 * 
	 * @param handler
	 *            the ClientHandler to be removed
	 */
	// @ requires handler != null;
	public void removeHandler(ClientHandler handler) {
		clients.remove(clients.get(handler));
	}

	/**
	 * Resets the waitingThreads list for other players to play connect4.
	 */
	public void resetWaitingList() {
		clientPreferences.clear();
	}

	/**
	 * An important method that adds clientHandler to the waitingThreads list.
	 * When two ClientHandlers are in this list, a game is started.
	 * 
	 * @param handler
	 *            the ClientHandler which want to play a game
	 */
	public void addToWaitingThreads(ClientHandler handler) {
		// if (clientPreferences.size() == 1) {
		//
		// }
		//
		// if (waitingThreads.size() == 1) {
		// // organize clienthandlers
		// ClientHandler h0 = waitingThreads.get(0);
		// ClientHandler h1 = handler;
		// // get player names
		// String n0 = h0.getClientName();
		// String n1 = h1.getClientName();
		// // get players
		// Player p0 = new Player(n0);
		// Player p1 = new Player(n1);
		// // set marks
		// p0.setMark(Mark.YELLOW);
		// p1.setMark(Mark.RED);
		// // set players in clienthandler
		// h0.setPlayer(p0);
		// h1.setPlayer(p1);
		// // set clienthandler in player
		// p0.setClientHandler(h0);
		// p1.setClientHandler(h1);
		// // set opponent players for both handlers
		// h0.setOpponentPlayerName(n1);
		// h1.setOpponentPlayerName(n0);
		// // start new game
		// Game game = new Game(new Player[] { p0, p1 });
		// System.out.println(n0 + " and " + n1 + " start a game.");
		// h0.setGame(game);
		// h1.setGame(game);
		// String startGameMessage = startGame + msgSeperator + n0 +
		// msgSeperator + n1;
		// h0.sendMessage(startGameMessage);
		// h1.sendMessage(startGameMessage);
		// resetWaitingList();
		// // initialize the list in ClientHandler that contains both player's
		// // handlers
		// h0.setHandlersOfGame();
		// h1.setHandlersOfGame();
		// } else {
		// waitingThreads.add(handler);
		// }
	}

	/**
	 * A static method that asks the user to enter a valid port number on which
	 * the server can listen to. This requires a port number between 1023 and
	 * 65535.
	 */
	private static void enterPort() {
		boolean done = false;
		int possiblePort = 0;
		System.out.println("Enter port number:");
		scanner = new Scanner(System.in);
		while (!done) {
			try {
				possiblePort = Integer.parseInt(scanner.nextLine());
				// port numbers between 1024 and 65535 are valid
				if (possiblePort < 65535 && possiblePort > 1023) {
					port = possiblePort;
					done = true;
				} else {
					System.out.println("Enter a port number between 1023 and 65535.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter an integer.");
			}
		}
	}

	// --- Queries
	// -----------------------------------------------------------------------

	/**
	 * checks if the name is valid by checking if the name is already in use by
	 * other clients checking if the name only consists of the charRegex
	 * determined during a tutorial group meeting.
	 * 
	 * @param name
	 *            of the client
	 * @param handler
	 *            On which clienthandler it must checked
	 * @return true when the name is valid
	 */
	/*
	 * @ requires name != null && handler != null;
	 */
	public boolean isValidName(String name, ClientHandler handler) {
		for (String value : gameNames.values()) {
			if (value.equals(name)) {
				handler.sendMessage(invalidCommand + msgSeperator + usernameInUse);
				return false;
			}
		}
		if (!name.matches(charRegex) && name.equals("")) {
			handler.sendMessage(invalidCommand + msgSeperator + invalidUsername);
			return false;
		} else {
			// TODO: make super isValidName sends an ID to replace 100:
			gameNames.put(100, name);
			return true;
		}
	}

	/**
	 * This method will return the clientHandler of your player.
	 * 
	 * @param yourHandler
	 *            The Clienthandler
	 * @return the opponent's ClientHandler
	 */
	// @pure;
	// public ClientHandler getTheOppenentHandler(ClientHandler yourHandler) {
	// TODO: geen idee?
	// return opponentOfPlayer.get(yourHandler);
	// }

	/**
	 * A getter for the waitingList.
	 * 
	 * @return waitingThread list
	 */
	/* @ pure */
	public Map<Integer, String> getWaitingList() {
		return clientPreferences;
	}

	/**
	 * A getter for the port.
	 * 
	 * @return port on which the server waits
	 */
	// @ pure;
	public int getPort() {
		return port;
	}

	// --- Main Method
	// --------------------------------------------------------------------------
	public static void main(String[] args) {
		boolean running = true;
		while (running) {
			enterPort();
			try {
				Thread server = new Server(port);
				server.start();
				running = false;
			} catch (IOException e) {
				System.out.println("Port number already in use.");
			}
		}

	}

	// /** Constructs a new Server object. */
	// public Server(String name, int portArg) {
	// this.serverPort = portArg;
	// this.name = name;
	//
	// clients = new HashMap<String, ClientHandler>();
	// clientPreferences = new HashMap<String, String>();
	// gameNames = new HashMap<String, String>();
	//
	// try {
	// hostAddress = InetAddress.getLocalHost().getHostAddress();
	// System.out.println("Sever has started on IP-address: " + hostAddress + "
	// and local port: " + portArg);
	// } catch (UnknownHostException e) {
	// // TODO throw unknow hostexception
	// System.out.println("Invalid local address: " + hostAddress);
	// }
	// }
	//
	// /**
	// * Listens to a port of this Server if there are any Clients that would
	// like
	// * to connect. For every new socket connection a new ClientHandler thread
	// is
	// * started that takes care of the further communication with the Client.
	// */
	// public synchronized void run() {
	//
	// try {
	// serverSock = new ServerSocket(serverPort);
	//
	// while (true) {
	// System.out.println("Waiting for clients to connect...");
	//
	// Socket clientSocket = serverSock.accept();
	// System.out.println("Connected: " + clientSocket);
	//
	// this.clientID = getID();
	//
	// ClientHandler newClient = new ClientHandler(this.clientID, clientSocket);
	// new Thread(newClient).start();
	// newClient.sendMessage(serverCapabilities());
	// }
	//
	// } catch (Exception e) {
	// System.out.println("fout1");
	// e.printStackTrace();
	// // TODO: exception
	// }
	// }
	//
	public synchronized String serverCapabilities() {
		String capabilities = "serverCapabilities " + amountOfPlayers + " " + roomSupport + " " + maxRoomDimensionX
				+ " " + maxRoomDimensionY + " " + maxRoomDimensionZ + " " + maxLengthToWin + " " + chatSupport;
		return capabilities;
	}
	//
	// /**
	// * Sends a message using the collection of connected ClientHandlers to all
	// * connected Clients.
	// *
	// * @param msg
	// * message that is send
	// */
	// public synchronized void broadcast(String msg) {
	// for (Map.Entry<String, ClientHandler> clients : clients.entrySet()) {
	// ClientHandler send = clients.getValue();
	// send.sendMessage(msg);
	//
	// }
	// }
	//
	// public String getID() {
	// clientNO++;
	// String clientID = Integer.toString(clientNO);
	// return clientID;
	// }
	//
	// /**
	// * Add a ClientHandler to the collection of ClientHandlers.
	// *
	// * @param handler
	// * ClientHandler that will be added
	// */
	// public synchronized void addHandler(ClientHandler handler) {
	// if (!clients.containsValue(handler)) {
	// clients.put(this.clientID, handler);
	// } else {
	// // TODO: throw exception
	// System.out.println("This ClientHandler allready exists.");
	// }
	// }
	//
	// /**
	// * Remove a ClientHandler from the collection of ClientHanlders.
	// *
	// * @param handler
	// * ClientHandler that will be removed
	// */
	// public synchronized void removeHandler(ClientHandler handler) {
	//
	// if (clients.containsValue(handler)) {
	// clients.remove(handler);
	// } else {
	// System.out.println("Cannot remove " + handler);
	// // TODO: throw exception clienthandler not in the list
	// }
	// }
	//
	// public void putPreferences(String clientID, String name, String
	// preferences) {
	// System.out.println("putting a client with preferences and name");
	// clientPreferences.put(clientID, preferences);
	// gameNames.put(clientID, name);
	//
	// }
	//
	// public void checkMatch(String clientID, String prefs) {
	//
	// for (Map.Entry<String, String> entry : clientPreferences.entrySet()) {
	// if (entry.getValue().equals(prefs) && !entry.getKey().equals(clientID)) {
	// System.out.println("Setting up a game for " + entry.getKey() + " and " +
	// clientID
	// + " with game preferences " + prefs);
	//
	// String[] parts = prefs.split(" ");
	// int dim = Integer.parseInt(parts[2]);
	//
	// String roomDimensionX = parts[2];
	// String roomDimensionY = parts[3];
	// String roomDimensionZ = parts[4];
	// String lenghtToWin = parts[5];
	//
	// String playerID1 = clientID;
	// String playerName1 = gameNames.get(clientID);
	// String playerColour1 = "0000ff";
	// ClientHandler ch1 = clients.get(playerID1);
	//
	// String playerID2 = entry.getKey();
	// String playerName2 = gameNames.get(entry.getKey());
	// String playerColour2 = "ff1a00";
	// ClientHandler ch2 = clients.get(playerID2);
	//
	// String output = "startGame " + roomDimensionX + "|" + roomDimensionY +
	// "|" + roomDimensionZ + "|"
	// + lenghtToWin + "| " + playerID1 + "|" + playerName1 + "|" +
	// playerColour1 + " " + playerID2
	// + "|" + playerName2 + "|" + playerColour2;
	//
	// ch1.sendMessage(output);
	// ch2.sendMessage(output);
	//
	// NetworkPlayer player1 = new NetworkPlayer(playerName1, ch1, Mark.OOO);
	// NetworkPlayer player2 = new NetworkPlayer(playerName2, ch2, Mark.XXX);
	// ch1.setMark(Mark.OOO);
	// ch2.setMark(Mark.XXX);
	//
	// ch1.setOpponent(ch2);
	// ch2.setOpponent(ch1);
	//
	// Random r = new Random();
	// if ((r.nextInt() % 2) == 0) {
	// ServerGame game = new ServerGame(player1, player2,
	// Integer.parseInt(roomDimensionX));
	// ch1.setGame(game);
	// ch2.setGame(game);
	// ch1.sendMessage("playerTurn " + playerID1);
	// ch2.sendMessage("playerTurn " + playerID1);
	//
	// } else {
	// ServerGame game = new ServerGame(player2, player1,
	// Integer.parseInt(roomDimensionX));
	// ch1.setGame(game);
	// ch2.setGame(game);
	// ch1.sendMessage("playerTurn " + playerID2);
	// ch2.sendMessage("playerTurn " + playerID2);
	//
	// }
	//
	// clientPreferences.remove(playerID1);
	// clientPreferences.remove(playerID2);
	// }
	// }
	// }

}
