package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import server.Client;
import protocol.ProtocolConstants;
import protocol.ProtocolControl;

public class ClientTUIG extends Observable implements Runnable, Observer, ProtocolConstants, ProtocolControl {

	// --------Variables------

	private static Scanner s = new Scanner(System.in);
	private static boolean done = false;
	private static int port = 0;
	private static InetAddress host = null;
	private static String username;

	private BufferedReader console;
	private Client client;

	private static boolean humanPlayer;
	private static int thinkInterval;

	// --------Main method to start the program-----
	public static void main(String[] args) {
		try {
			startClientApp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// --------Constructor
	public ClientTUIG(Client clientArg) {
		console = new BufferedReader(new InputStreamReader(System.in));
		this.client = clientArg;
	}

	// --------Thread
	/*
	 * Run method that starts the consoleListner-Thread
	 */
	@Override
	public void run() {
		consoleListner.start();
//		if (!client.getPlayerType().equals("Computer")) {
//
//		}
	}

	/*
	 * Creates a new Thread that listens for user's input on the console.
	 */
	Thread consoleListner = new Thread(new Runnable() {
		public void run() {
			String input = null;
			while (true) {
				try {
					input = console.readLine();
					if (input != null) {
						client.readFromConsole(input);
					}
				} catch (IOException e){
					System.out.println("Could not read from console, shutting down TUI.");
					client.shutdown();
				}
			}
		}
	});
	
	//------commands
	
	
	/*
	 * 
	 */
	public void printInput(String msg) {
		System.out.println(msg);
	}
	
	/*
	 * 
	 */
	private static void startClientApp() throws IOException {
		System.out.println("Starting a client...");
		enterPort();
		enterHostAddress();
		Client client = new Client(host, port);
		client.run();
		
		//TODO: this should be done after connection.
//		enterName();
//		enterType();
//		String type = (humanPlayer ? "human" : "computer");

	}
	
	/*
	 * 
	 */
	public static void enterPort() {
		System.out.println("Enter port number: ");
		while (!done) {
			try {
				port = Integer.parseInt(s.nextLine()); 
				if (port > 1023 && port < 65535) {
					done = true;
				} else {
					done = false;
					System.out.println("Choose a port number between 1023 and 65,535");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a number.");
			} catch (IllegalArgumentException e) {
				System.out.println("Enter a 4 or 5 digit number.");
			}
		}
	}
	
	/*
	 * 
	 */
	private static void enterHostAddress() {
		done = false;
		System.out.println("Enter a host address: ");
		while (!done) {
			try {
				host = InetAddress.getByName(s.nextLine());
				done = true;
			} catch (UnknownHostException e) {
				System.out.println("The entered host address is not valid.");
				System.out.println("Please enter a new address.");
			}
		}
	}
	
	/*
	 * 
	 */
	private static void enterName() {
		done = false;
		System.out.println("Enter username: ");
		while (!done) {
			String possibleUsername = s.nextLine();
			if (possibleUsername.matches(charRegex)) {
				username = possibleUsername;
				done = true;
			} else {
				System.out.println("Username is not correct. (a-z, A-Z, 0-9)");
				System.out.println("Enter another username:");
			}
		}
	}
	
	/*
	 * 
	 */
	private static void enterType() {
		done = false;
		System.out.println("Choose your player type:");
		System.out.println("Human or Computer");
		while (!done) {
			String possibleOpponent = s.nextLine();
			if (possibleOpponent.equals("Human")) {
				humanPlayer = true;
				done = true;
			} else if (possibleOpponent.equals("Computer")) {
				humanPlayer = false;
				enterThinkInterval();
				done = true;
			} else {
				System.out.println("Choose 'Human' or 'Computer'.");
			}
		}
	}
	
	/*
	 * 
	 */
	private static void enterThinkInterval() {
		done = false;
		System.out.println("Enter how long this player should think before it does a move in milliseconds.");
		while (!done) {
			try {
				String line = s.nextLine();
				
				int interval = Integer.parseInt(line);
				if (interval > 0) {
					thinkInterval = interval;
					done = true;
				} else {
					System.out.println("Enter a valid interval.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Enter a integer.");
			}
		}
	}
	

	
	
	/*
	 * 
	 */
	public void update(Observable o, Object arg){
		if (((String) arg).contains("moveIsMade")) {
			client.sendMessage(getBoard);
			client.sendMessage(playerTurn);
			if (client.getIsCurrentPlayer()) {
				System.out.println("Enter a column number: ");
			}
		}
	}
	
	
}
