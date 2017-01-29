package server;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import project.Board;
import project.DummyPlayer;
import project.HumanPlayer;
import project.Mark;

/**
 * P2 prac wk4. <br>
 * Client.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Client extends Thread {
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

	// static DataInputStream in;
	// static DataOutputStream out;
	public InetAddress address;

	public String name;
	public String pID;
	public String colour;

	private ClientGame game;
	private HumanPlayer humanPlayer;
	private DummyPlayer opponant;

	private Board board;
	
	private Scanner typedInput;

	/**
	 * Constructs a Client-object and tries to make a socket connection.
	 */
	public Client(InetAddress host, int port) throws IOException {
		sock = new Socket(host, port);
		// in = new DataInputStream(sock.getInputStream());
		// out = new DataOutputStream(sock.getOutputStream());

		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	public void run() {
		typedInput = new Scanner(System.in);
		
		try {
			String input = "";
			while ((input = in.readLine()) != null) {
				handleInput(input);
			}
		} catch (IOException e) {
			System.out.println("Connection errored with server.");
			e.printStackTrace();
		} finally {
			shutdown();
		}
	}

	public synchronized void handleInput(String msg) {
		System.out.println(msg);
		
		if (msg.contains("serverCapabilities")) {
			
			sendMessage(sendPreferences(msg));
			
		} else if (msg.contains("startGame")) {
			String[] parts = msg.split(" ");
			String gameSettings = parts[1];
			String player1 = parts[2];
			String player2 = parts[3];

			if (player1.contains(name)) {
				String[] parts1 = player1.split("\\|");
				pID = parts1[0];
				colour = parts1[2];
				
			} else if (player2.contains(name)) {
				String[] parts2 = player2.split("\\|");
				pID = parts2[0];
				colour = parts2[2];
			}
			
			System.out.println("My ID is " + pID);
			humanPlayer = new HumanPlayer(name, Mark.XXX, this);
			opponant = new DummyPlayer("opponant", Mark.OOO);
			
			String[] partsG = gameSettings.split("\\|");
			game = new ClientGame(humanPlayer, opponant, Integer.parseInt(partsG[0]));


		} else if (msg.contains("playerTurn " + pID)) {
			if (msg.contains(pID)) {
				game.getBoard().showBoard();
				sendMessage(determineMove());
			} else {
				System.out.println("waiting");
			}
			
		} else if (msg.contains("notifyMove")) {
			String[] parts = msg.split(" ");

			String id = parts[1];
			int moveX = Integer.parseInt(parts[2]);
			int moveY = Integer.parseInt(parts[3]);

			if (id.equals(pID)) {
				game.makeMove(moveX, moveY);
			} else {
				game.moveOponant(moveX, moveY);
			}

			System.out.println("Current board:");
			game.getBoard().showBoard();

			// TODO: set field player(id)

		} else if (msg.contains("notifyEnd")) {
			String[] parts = msg.split(" ");

			int reason = Integer.parseInt(parts[1]);
			String player = parts[2];

			if (reason == 1) {
				System.out.println("Player " + player + "has won!");
			} else if (reason == 2) {
				System.out.println("All spaces on the board have been filled, ther is no winner...");
			} else if (reason == 3) {
				System.out.println("Player " + player + " has disconnected");
				// TODO: quit game.
			}

		} else if (msg.contains("error")) {
			String[] parts = msg.split(" ");

			int error = Integer.parseInt(parts[1]);

			if (error == 1) {
				System.out.println("Client has not yet sent capabilities message; server cannot proceed.");
			} else if (error == 2) {
				System.out.println("Room sent in message joinRoom dos not exist.");
			} else if (error == 3) {
				System.out.println("The chosen room is no longer available.");
			} // TODO: write other errors

		}
	}

	/**
	 * Reads the messages in the socket connection. Each message will be
	 * forwarded to the MessageUI
	 */

	public String sendPreferences(String serverCapabilities) {

		// TODO: compare with serverCapabilities and give errors if values are
		// out of bounds.
		System.out.println("Enter your game preferences. Make sure your preferences "
				+ "fit within the server capabilities!");
		System.out.println("With how many players do you want to play?");
		String players = typedInput.nextLine();
		System.out.println("What is your game name? (Don't use spaces!)");
		name = typedInput.nextLine();
		System.out.println("Do you support rooms? Yes = 1, No = 0");
		String roomSupport = typedInput.nextLine();
		System.out.println("With which dimentions do you want to play a game?");
		System.out.println("Enter lenght dimension x: ");
		String maxRoomDimensionX = typedInput.nextLine();
		System.out.println("Enter lenght dimension y: ");
		String maxRoomDimensionY = typedInput.nextLine();
		System.out.println("Enter lenght dimension z: ");
		String maxRoomDimensionZ = typedInput.nextLine();
		System.out.println("How long does a winning connection have to be?"
				+ "(This length cannot be bigger than the smallest dimention)");
		String maxLengthToWin = typedInput.nextLine();
		System.out.println("Do you support chats? Yes = 1, No = 0");
		String chatSupport = typedInput.nextLine();

		String preferences = "sendCapabilities " + players + " " + name + " " + roomSupport + " " 
				+ maxRoomDimensionX + " " + maxRoomDimensionY + " " + maxRoomDimensionZ + " " 
				+ maxLengthToWin + " " + chatSupport;

		System.out.println("You are sending these preferences to the server: " + preferences);

		return preferences;
	}

	
	public String determineMove(){
		System.out.println(getName() + " (" + humanPlayer.getMark() + ") ,wich row dow you want to choose?");
		String moveX = typedInput.nextLine();
		
		System.out.println(getName() + " (" + humanPlayer.getMark() + ") ,wich column dow you want to choose?");
		String moveY = typedInput.nextLine();
		
		String movePlayer = "makeMove " + pID + " " + moveX + " " + moveY;
		System.out.println("You are sending this move to the server: " + movePlayer);
		return movePlayer;
	}
	
	/** send a message to a ClientHandler. */
	public  void sendMessage(String msg) {
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
	
	public void waiting() {
		System.out.println("Waiting");
		try {
			out.newLine();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("could not wait");
			e.printStackTrace();
		}
	}

	/** close the socket connection. */
	public void shutdown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			System.out.println("Something went wrong while trying to shutdown client.");
		}
		// TODO Add implementation
	}

	/** returns the client name. */
	public String getClientName() {
		return clientName;
	}

//	public int[] determineMove(Board board) {
//		boolean valid = false;
//		int[] choices = new int[2];
//		int choiceX = 0;
//		int choiceY = 0;
//		while (!valid) {
//			Scanner move = new Scanner(System.in);
//			System.out.println("which row do you want to choose? ");
//			choiceX = Integer.parseInt(move.nextLine());
//			System.out.println("which column do you want to choose? ");
//			choiceY = Integer.parseInt(move.nextLine());
//
//			valid = board.isEmptyField(choiceX, choiceY, board.firstEmptyField(choiceX, choiceY));
//
//			if (valid) {
//				choices[0] = choiceX;
//				choices[1] = choiceY;
//				break;
//			} else {
//				System.out.println("ERROR: field " + choiceX + ", " + choiceY 
//					+ " is no valid choice.");
//			}
//		}
//		return choices;
//	}

}
