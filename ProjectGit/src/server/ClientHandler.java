package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import project.Mark;
import project.playerToUse;

/**
 * ClientHandler.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class ClientHandler extends Thread implements Runnable {

	public Socket clientSock;

	private BufferedReader in;
	private BufferedWriter out;

	private String clientName;

	private NetworkPlayer player;
	private ClientHandler opponent;
	private ServerGame game;

	private String clientID;

	public boolean hasMatch = false;
	
	private Mark mark;
	

	/**
	 * Constructs a ClientHandler object Initialises both Data streams. @
	 * requires server != null && sock != null;
	 */
	public ClientHandler(String clientID, Socket sockArg) throws IOException {
		System.out.println("Setting up a client handler.");
		this.clientID = clientID;
		clientSock = sockArg;

		StartServer.connectFourServer.addHandler(this);

		// in = new DataInputStream(clientSock.getInputStream());
		// out = new DataOutputStream(clientSock.getOutputStream());

		in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));
	}

	/**
	 * This method takes care of sending messages from the Client. Every message
	 * that is received, is preprended with the name of the Client, and the new
	 * message is offered to the Server for broadcasting. If an IOException is
	 * thrown while reading the message, the method concludes that the socket
	 * connection is broken and shutdown() will be called.
	 */
	public synchronized void run() {
		try {
			String input = "";

			while ((input = in.readLine()) != null) {
				handleInput(input);
			}
		} catch (IOException e) {
			System.out.println("Connection errord with " + clientID);
		} finally {
			shutdown();
		}
	}

	public void handleInput(String input) {
		if (input.contains("sendCapabilities")) {

			String[] parts = input.split(" ");

			String clientNames = parts[2];
			String gamePrefs = parts[1] + " " + parts[3] + " " + parts[4] + " " + parts[5] + " " 
					+ parts[6] + " " + parts[7] + " " + parts[8];

			if (!StartServer.connectFourServer.clientPreferences.containsKey(clientID)) {

				StartServer.connectFourServer.putPreferences(clientID, clientNames, gamePrefs);

				StartServer.connectFourServer.checkMatch(clientID, gamePrefs);

			} else {
				System.out.println("Something went wrong trying to add preferences to the map.");
			}
		} else if (input.contains("waiting")) {
			
		} else if (input.contains("makeMove")) {
			System.out.println(input);
			game.getBoard().showBoard();
			String[] parts = input.split(" ");
			int moveX = Integer.parseInt(parts[2]);
			int moveY = Integer.parseInt(parts[3]);
			try {
				System.out.println("Gaan we een move plaatsen?");
				System.out.println(moveX);
				System.out.println(moveY);
				System.out.println(mark);
				
				game.getBoard().setField(moveX, moveY, mark);
				game.getBoard().showBoard();
				
				String notifyMove = "notifyMove " + clientID + " " + moveX + " " + moveY;
				broadcastGamers(notifyMove);
			} catch (NumberFormatException e) {
				sendMessage("Error 5");
			}
		}
	}

	/**
	 * This method can be used to send a message over the socket connection to
	 * the Client. If the writing of a message fails, the method concludes that
	 * the socket connection has been lost and shutdown() is called.
	 */
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Could not write to output stream. Turning off connection");
			shutdown();
		}
	}
	
	public void broadcastGamers(String msg) {
		this.sendMessage(msg);
		opponent.sendMessage(msg);
	}
	
	

	/**
	 * This ClientHandler signs off from the Server and subsequently sends a
	 * last broadcast to the Server to inform that the Client is no longer
	 * participating in the chat.
	 */
	private void shutdown() {
		try {
			in.close();
			out.close();
			clientSock.close();
		} catch (IOException e) {
			System.out.println("Something went wrong when shutting down " + this);
		}
		StartServer.connectFourServer.removeHandler(this);
	}

	// ---------methods to setup and play a game -------
	public void setPlayer() {
	}

	public NetworkPlayer getPlayer() {
		return player;
	}

	public void setMark(Mark m){
		this.mark = m;
//		System.out.println(m);
	}
	
	public Mark getMark() {
		return player.getMark();
	}

	public void setGame(ServerGame game) {
		this.game = game;
	}

	public void setOpponent(ClientHandler handler) {
		opponent = handler;
	}

}
