package server2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * ClientHandler.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class ClientHandler extends Thread {

	private Socket sock;
	private DataInputStream in;
	private DataOutputStream out;
	private String clientName;

	private String clientID;

	public Socket clientSock;

	public boolean hasMatch;

	/**
	 * Constructs a ClientHandler object Initialises both Data streams. @
	 * requires server != null && sock != null;
	 */
	public ClientHandler(String clientID, Socket sockArg) throws IOException {
		System.out.println("Setting up a client handler.");
		this.clientID = clientID;
		clientSock = sockArg;

		out = new DataOutputStream(clientSock.getOutputStream());
		in = new DataInputStream(clientSock.getInputStream());

		runSetup();
	}

	/**
	 * This method takes care of sending messages from the Client. Every message
	 * that is received, is preprended with the name of the Client, and the new
	 * message is offered to the Server for broadcasting. If an IOException is
	 * thrown while reading the message, the method concludes that the socket
	 * connection is broken and shutdown() will be called.
	 */
	public synchronized void runSetup() {
		try {

			out.writeUTF(StartServer.connectFourServer.serverCapabilities());
			System.out.println("Waiting for client preferences...");

			String clientPrefs = in.readUTF();
			System.out.println(clientID + " has preferences: " + clientPrefs);

			StartServer.connectFourServer.mapPreferences(clientID, clientPrefs);

			String[] parts = clientPrefs.split(" ");

			String gamePrefs = parts[1] + " " + parts[3] + " " + parts[4] + " " + parts[5] + " " + parts[6] + " "
					+ parts[7] + " " + parts[8];


			StartServer.connectFourServer.addHandler(this);
			StartServer.connectFourServer.checkMatch(clientID, gamePrefs);

//			String game = in.readUTF();
//			
//			if (game.contains("startGame")) {
//				System.out.println("Hier3?");
//				out.writeBoolean(hasMatch);
//				runGame();
//			}

		} catch (Exception e) {
			// TODO: throw exception
			System.out.println("FOUT");
			e.printStackTrace();
		}

	}

	public void runGame() {
		System.out.println("Hier?");
		String line = null;

		try {
			while ((line = in.readUTF()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.out.println("Something went wrong while reading from socket. " + "Turning off connection.");
			e.printStackTrace();
			this.shutdown();
		}
	}

	/**
	 * This method can be used to send a message over the socket connection to
	 * the Client. If the writing of a message fails, the method concludes that
	 * the socket connection has been lost and shutdown() is called.
	 */
	public void sendMessage(String msg) {
		try {
			out.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not write to output stream. Turning off connection");
			shutdown();
		}

		// TODO Add implementation
	}

	/**
	 * This ClientHandler signs off from the Server and subsequently sends a
	 * last broadcast to the Server to inform that the Client is no longer
	 * participating in the chat.
	 */
	private void shutdown() {
		StartServer.connectFourServer.removeHandler(this);
		StartServer.connectFourServer.broadcast("[" + clientName + " has left]");
	}

}
