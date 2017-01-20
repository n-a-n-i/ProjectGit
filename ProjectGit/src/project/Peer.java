package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Peer for a simple client-server application
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Peer implements Runnable {
	public static final String EXIT = "exit";

	protected String name;
	protected Socket sock;
	protected BufferedReader in;
	protected BufferedWriter out;
	protected PrintWriter serverOut;
	String serverCapabilities;
	int amountOfPlayers = 2;
	int roomSupport = 0;
	int dimension = 10;
	int maxLengthToWin = 4;
	int chatSupport = 0;
	/*
	 * @ requires (nameArg != null) && (sockArg != null);
	 */
	/**
	 * Constructor. creates a peer object based in the given parameters.
	 * 
	 * @param nameArg
	 *            name of the Peer-proces
	 * @param sockArg
	 *            Socket of the Peer-proces
	 */

	public Peer(String nameArg, Socket sockArg) throws IOException {
		this.name = nameArg;
		this.sock = sockArg;

		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	/**
	 * Reads strings of the stream of the socket-connection and writes the
	 * characters to the default output.
	 */
	public void run() {
		String line = null;
		try {
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.out.println("Something went wrong while reading from socket. Turning off connection.");
			shutDown();
		}
	}

	/**
	 * Reads a string from the console and sends this string over the
	 * socket-connection to the Peer process. On Peer.EXIT the method ends
	 */
	public void handleTerminalInput() {
		Scanner scan = new Scanner(System.in);
		String line = scan.nextLine();

		while (line != null) {
			if (line.equals(Peer.EXIT)) {
				scan.close();
				shutDown();
				break;
			} else {
				try {
					out.write(this.name + ": " + line);
					out.newLine();
					out.flush();
				} catch (IOException e) {
					System.out.println("Could not write to output stream.");
					;
				}
			}
			line = scan.nextLine();
		}
	}

	/**
	 * Closes the connection, the sockets will be terminated
	 */
	public void shutDown() {
		try {
			sock.close();
			in.close();
			out.close();

			System.out.println("Exit succesfull");
		} catch (IOException e) {
			System.out.println("Could not shutdown the connection.");
		}
	}

	/** returns name of the peer object */
	public String getName() {
		return name;
	}

	/** read a line from the default input */
	static public String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}

	/*
	 * Sends a message to a client waiting for a game. If there are enough
	 * players for the game, a message will be sent saying that the game will be
	 * starting.
	 */
	public void MessageForWaiting() {
		List<Integer> odd = new ArrayList<>();
		try {
			for (int number = 0; number < 100; number++) {
				if (number % 2 != 0) {
					odd.add(number);
				}
			}
			if (odd.contains(Server.clientList.size())) {
				serverOut = new PrintWriter(sock.getOutputStream(), true);
				serverOut.println("Waiting for other player(s)...");
			} else {
				for (int i = 0; i < Server.clientList.size(); i++) {
					serverOut = new PrintWriter(Server.clientList.get(i).getOutputStream(), true);
					serverOut.println("Two players have connected, starting a game..");
				}
			}
		} catch (IOException e) {
			// TODO Make Exception for being unable to create a serverOut
			e.printStackTrace();
		}
	}

	public void SendServerCapabilities() {
		try {
			serverOut = new PrintWriter(sock.getOutputStream(), true);
			serverCapabilities = "ServerCapabilities " + amountOfPlayers + " " + roomSupport + " " + dimension + " "
					+ dimension + " " + dimension + " " + maxLengthToWin + " " + chatSupport;
			serverOut.println(serverCapabilities + "\n" + "What are your preferences?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not write to socket: " + sock);
			e.printStackTrace();
		}

	}

	public boolean RecievedCapabilities() {
		Scanner scan = new Scanner(System.in);
		String line = scan.nextLine();
		Scanner words = new Scanner(line);
		boolean RecievedCapabilities = false;
		while (line != null) {
			if (words.hasNext()) {
				String word = words.next();

				if (word.equals("sendCapabilities")) {
					RecievedCapabilities = true;
					scan.close();
					words.close();
				}
			}
		}
		return RecievedCapabilities;
	}
}
