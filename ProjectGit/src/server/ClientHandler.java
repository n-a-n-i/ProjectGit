package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import project.Board;
import project.Game;
import project.Mark;
import project.Player;
import project.playerToUse;
import protocol.ProtocolConstants;
import protocol.ProtocolControl;

/**
 * ClientHandler.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class ClientHandler extends Thread implements ProtocolConstants, ProtocolControl {

	private Server server;
	public Socket clientSock;
	private BufferedReader in;
	private BufferedWriter out;

	private ArrayList<ClientHandler> handlersOfThisGame;

	private boolean running;

	private Player player;
	private Game game;

	private String clientName, opponentName, preferences;
	private int pID;

	// -- Constructor
	// ---------------------------------------------------------------

	public ClientHandler(Server serverArg, Socket sockArg, int playerID) throws IOException {
		this.clientSock = sockArg;
		this.server = serverArg;
		this.pID = playerID;
		in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));
		handlersOfThisGame = new ArrayList<ClientHandler>();
		running = true;
		System.out.println("A new player is connected to the server.");
	}

	// -- run method
	// -----------------------------------------------------------------------------

	/**
	 * The run method that constantly listens on the socket connection.
	 */
	public void run() {
		String input = "";
		while (running) {
			try {
				if ((input = in.readLine()) != null) {
					processProtocol(input);
				}
			} catch (IOException e) {
				System.out.println("One of the players has left the game.");
				server.broadcast(endGame + msgSeperator + clientName + msgSeperator + connectionlost,
						handlersOfThisGame);
				server.removeHandler(this);
				server.resetWaitingList();
				running = false;
			}
		}
		disconnect();
	}

	// -- Commands
	// ----------------------------------------------------------------------

	/**
	 * When something went wrong, the in and output stream and socket will be
	 * closed.
	 */
	private void disconnect() {
		running = false;
		server.removeHandler(this);
		try {
			out.close();
			in.close();
			clientSock.close();
		} catch (IOException e) {
			System.out.println("Unable to close the socket connection.");
		}
	}

	/**
	 * The method that sends messages to the Client.
	 * 
	 * @param msg
	 *            the message to be sent
	 */
	// @ requires msg != null;
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("The socket connection is broken.");
			server.removeHandler(this);
			disconnect();
		}
	}

	/**
	 * The messages that are sent by the Client are to be processed according to
	 * the protocol that is determined. It could lead to several scenarios, with
	 * a default of invalidCommand, that is sent back to the Client.
	 * 
	 * @param msg
	 *            the message that is to be processed via the protocol
	 */
	// @ requires msg != null;
	private void processProtocol(String msg) {
		String[] assignment = msg.split(msgSeperator);
		switch (assignment[0]) {
		case clientPreferences:
			addPreferences(msg);
			server.checkMatch(pID, preferences);
		case getBoard:
			// getBoard();
			break;
		case joinRequest:
			joinRequest(assignment[1]);
			break;
		case doMove:
			// doMove(assignment[1]);
			break;
		case playerTurn:
			sendMessage(turn + msgSeperator + getGame().getCurrent().getName());
			break;
		default:
			sendMessage(invalidCommand + msgSeperator + invalidCommand);
			break;
		}
	}

	// /**
	// * It creates a String array of the Board according to the protocol. Then
	// it
	// * is converted to a normal String, so it could be sent to the Client.
	// *
	// * @return a String of a String representation of the Board
	// */
	// private void getBoard() {
	// String[] sendBoardArray = new String[Board.X * Board.Y + 1];
	// sendBoardArray[0] = sendBoard;
	// // @ loop_invariant i >= 1;
	// // @ loop_invariant i <= Board.X * Board.Y;
	// for (int i = 1; i < Board.X * Board.Y; i++) {
	// Mark mark = getGame().getBoard().getField(i);
	// switch (mark) {
	// case XXX:
	// sendBoardArray[i] = xxx;
	// break;
	// case OOO:
	// sendBoardArray[i] = ooo;
	// break;
	// case EMPTY:
	// sendBoardArray[i] = empty;
	// break;
	// }
	// }
	// sendMessage(arrayToString(sendBoardArray));
	// }

	/**
	 * 
	 */
	public void addPreferences(String prefs) {
		System.out.println("Komen we hier langs?");
		String[] parts = prefs.split(" ");

		String clientNames = parts[2];
		preferences = parts[1] + " " + parts[3] + " " + parts[4] + " " + parts[5] + " " + parts[6] + " " + parts[7]
				+ " " + parts[8];

		if (!server.clientPreferences.containsKey(pID)) {
			server.clientPreferences.put(pID, preferences);
			server.gameNames.put(pID, clientNames);
			System.out.println(server.clientPreferences);
		} else {
			// TODO: cannot add clientpreferences
		}

	}

	/**
	 * The name is checked by the Server whether the name is already in use and
	 * is according the protocol.
	 * 
	 * @param playername
	 *            the name of the player
	 */
	// @ requires playername != null;
	private void joinRequest(String playername) {
		if (server.isValidName(playername, this)) {
			clientName = playername;
			if (server.getWaitingList().isEmpty()) {
				sendMessage(acceptRequest + msgSeperator + yellow);
			} else {
				sendMessage(acceptRequest + msgSeperator + red);
			}
			server.addToWaitingThreads(this);
		}
	}

	// /**
	// * The method that processes the move of a Client.
	// *
	// * @param move
	// * a String representation of the column number
	// */
	// // @ requires move != null;
	// private void doMove(String move) {
	// try {
	// int col = 0;
	// // parsing the move parameter
	// col = Integer.parseInt(move);
	// // declaring the board and mark
	// Board b = getGame().getBoard();
	// Mark mark = player.getMark();
	// // create a prefix
	// String prefix = moveResult + msgSeperator + col + msgSeperator +
	// clientName + msgSeperator;
	// // check if the client is the current player and if the move is
	// // valid
	// if (isMyPlayerTheCurrentPlayer() && b.isValidMove(col)) {
	// // the move is set and the board is send to both clientHandlers
	// b.setField(col, mark);
	// // Check for a win or draw
	// if (b.isWinner(mark)) {
	// server.broadcast(endGame + msgSeperator + clientName + msgSeperator +
	// winner, handlersOfThisGame);
	// } else if (b.isFull()) {
	// server.broadcast(endGame + msgSeperator + clientName + msgSeperator +
	// draw, handlersOfThisGame);
	// } else {
	// server.broadcast(prefix + "true" + msgSeperator + opponentName,
	// handlersOfThisGame);
	// getGame().changeCurrent();
	// }
	// } else if (!(b.isValidMove(col) && isMyPlayerTheCurrentPlayer())) {
	// server.broadcast(prefix + "false" + msgSeperator +
	// getGame().getCurrent(), handlersOfThisGame);
	// } else if
	// (!this.getPlayer().equals(this.getPlayer().getGame().getCurrent())) {
	// server.broadcast(invalidCommand + msgSeperator + invalidUserTurn +
	// msgSeperator + clientName,
	// handlersOfThisGame);
	// }
	// } catch (NumberFormatException e) {
	// sendMessage(invalidCommand + msgSeperator + invalidMove);
	// } catch (ArrayIndexOutOfBoundsException e) {
	// sendMessage(invalidCommand + msgSeperator + invalidMove);
	// }
	// }

	// --- setters -------------------------

	/**
	 * Assigns a player to the ClientHandler.
	 * 
	 * @param playerArg
	 *            a player
	 */
	// @ requires playerArg != null;
	// @ ensures getPlayer() == playerArg;
	public void setPlayer(Player playerArg) {
		player = playerArg;
	}

	/**
	 * Assigns a game to the ClientHandler.
	 * 
	 * @param gameArg
	 *            a game
	 */
	// @ requires gameArg != null;
	// @ ensures getGame() == gameArg;
	public void setGame(Game gameArg) {
		game = gameArg;
	}

	/**
	 * Saves the name of the opponent.
	 * 
	 * @param opponentNameArg
	 *            the name of the opponent
	 */
	// @ requires opponentNameArg != null;
	// @ ensures getOpponentName() == opponentNameArg;
	public void setOpponentPlayerName(String opponentNameArg) {
		opponentName = opponentNameArg;
	}

	/**
	 * The ClientHandler knows the opponent's ClientHandler
	 */
	public void setHandlersOfGame() {
		handlersOfThisGame.add(game.getPlayers()[0].getClientHandler());
		handlersOfThisGame.add(game.getPlayers()[1].getClientHandler());
	}
	// -- Queries
	// --------------------------------------------------------------------

	/**
	 * It converts an array to a String object.
	 * 
	 * @param array
	 *            the array to be converted
	 * @return a String representation of the array
	 */
	private String arrayToString(String[] array) {
		String result = "";
		// @ loop_invariant i >= 0;
		// @ loop_invariant i <= array.length;
		for (int i = 0; i < array.length; i++) {
			if (i == 0) {
				result = array[i];
			} else {
				result = result + msgSeperator + array[i];
			}
		}
		return result;
	}

	/**
	 * Checks whether your player is the current player.
	 * 
	 * @return true if you are the current player
	 */
	// @ pure;
	private boolean isMyPlayerTheCurrentPlayer() {
		return getGame().getCurrent().equals(player);
	}

	// --- getters --------------------------

	/**
	 * A getter for the player
	 * 
	 * @return player
	 */
	// @ pure;
	public Player getPlayer() {
		return player;
	}

	/**
	 * A getter for the opponents name.
	 * 
	 * @return the name of the opponent
	 */
	// @ pure;
	public String getOpponentName() {
		return opponentName;
	}

	/**
	 * Retreives the list of ClientHandlers in a game.
	 * 
	 * @return list of ClientHandlers
	 */
	// @ pure;
	public ArrayList<ClientHandler> getHandlersOfThisGame() {
		return handlersOfThisGame;
	}

	/**
	 * A getter for the game
	 * 
	 * @return game
	 */
	// @ pure;
	public Game getGame() {
		return game;
	}

	/**
	 * A getter for the Server.
	 * 
	 * @return server
	 */
	// @ pure;
	public Server getServer() {
		return server;
	}

	/**
	 * A getter for the clientname.
	 * 
	 * @return clientName
	 */
	// @ pure;
	public String getClientName() {
		return clientName;
	}

	//
	// /**
	// * Constructs a ClientHandler object Initialises both Data streams. @
	// * requires server != null && sock != null;
	// */
	// public ClientHandler(String clientID, Socket sockArg) throws IOException
	// {
	// System.out.println("Setting up a client handler.");
	// this.clientID = clientID;
	// clientSock = sockArg;
	//
	// StartServer.connectFourServer.addHandler(this);
	//
	// // in = new DataInputStream(clientSock.getInputStream());
	// // out = new DataOutputStream(clientSock.getOutputStream());
	//
	// in = new BufferedReader(new
	// InputStreamReader(clientSock.getInputStream()));
	// out = new BufferedWriter(new
	// OutputStreamWriter(clientSock.getOutputStream()));
	// }
	//
	// /**
	// * This method takes care of sending messages from the Client. Every
	// message
	// * that is received, is preprended with the name of the Client, and the
	// new
	// * message is offered to the Server for broadcasting. If an IOException is
	// * thrown while reading the message, the method concludes that the socket
	// * connection is broken and shutdown() will be called.
	// */
	// public synchronized void run() {
	// try {
	// String input = "";
	//
	// while ((input = in.readLine()) != null) {
	// handleInput(input);
	// }
	// } catch (IOException e) {
	// System.out.println("Connection errord with " + clientID);
	// } finally {
	// shutdown();
	// }
	// }
	//
	// public void handleInput(String input) {
	// if (input.contains("sendCapabilities")) {
	//
	// String[] parts = input.split(" ");
	//
	// String clientNames = parts[2];
	// String gamePrefs = parts[1] + " " + parts[3] + " " + parts[4] + " " +
	// parts[5] + " "
	// + parts[6] + " " + parts[7] + " " + parts[8];
	//
	// if
	// (!StartServer.connectFourServer.clientPreferences.containsKey(clientID))
	// {
	//
	// StartServer.connectFourServer.putPreferences(clientID, clientNames,
	// gamePrefs);
	//
	// StartServer.connectFourServer.checkMatch(clientID, gamePrefs);
	//
	// } else {
	// System.out.println("Something went wrong trying to add preferences to the
	// map.");
	// }
	// } else if (input.contains("waiting")) {
	//
	// } else if (input.contains("makeMove")) {
	// System.out.println(input);
	// game.getBoard().showBoard();
	// String[] parts = input.split(" ");
	// int moveX = Integer.parseInt(parts[2]);
	// int moveY = Integer.parseInt(parts[3]);
	// try {
	// System.out.println("Gaan we een move plaatsen?");
	// System.out.println(moveX);
	// System.out.println(moveY);
	// System.out.println(mark);
	//
	// game.getBoard().setField(moveX, moveY, mark);
	// game.getBoard().showBoard();
	//
	// String notifyMove = "notifyMove " + clientID + " " + moveX + " " + moveY;
	// broadcastGamers(notifyMove);
	// } catch (NumberFormatException e) {
	// sendMessage("Error 5");
	// }
	// }
	// }
	//
	// /**
	// * This method can be used to send a message over the socket connection to
	// * the Client. If the writing of a message fails, the method concludes
	// that
	// * the socket connection has been lost and shutdown() is called.
	// */
	// public void sendMessage(String msg) {
	// try {
	// out.write(msg);
	// out.newLine();
	// out.flush();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// // e.printStackTrace();
	// System.out.println("Could not write to output stream. Turning off
	// connection");
	// shutdown();
	// }
	// }
	//
	// public void broadcastGamers(String msg) {
	// this.sendMessage(msg);
	// opponent.sendMessage(msg);
	// }
	//
	//
	//
	// /**
	// * This ClientHandler signs off from the Server and subsequently sends a
	// * last broadcast to the Server to inform that the Client is no longer
	// * participating in the chat.
	// */
	// private void shutdown() {
	// try {
	// in.close();
	// out.close();
	// clientSock.close();
	// } catch (IOException e) {
	// System.out.println("Something went wrong when shutting down " + this);
	// }
	// StartServer.connectFourServer.removeHandler(this);
	// }
	//
	// // ---------methods to setup and play a game -------
	// public void setPlayer() {
	// }
	//
	// public NetworkPlayer getPlayer() {
	// return player;
	// }
	//
	// public void setMark(Mark m){
	// this.mark = m;
	//// System.out.println(m);
	// }
	//
	// public Mark getMark() {
	// return player.getMark();
	// }
	//
	// public void setGame(ServerGame game) {
	// this.game = game;
	// }
	//
	// public void setOpponent(ClientHandler handler) {
	// opponent = handler;
	// }

}
