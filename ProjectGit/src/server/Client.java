package server;

import java.util.Scanner;

import View.ClientTUIG;

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
import protocol.ProtocolConstants;
import protocol.ProtocolControl;

/**
 * P2 prac wk4. <br>
 * Client.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Client implements ProtocolConstants, ProtocolControl, Runnable {

	private ClientTUIG tui;

	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

	private Board board;
	private Mark myMark;

	private boolean isCurrentPlayer;
	private boolean gameIsStarted;
	private boolean gameIsEnded;

	private String playerType;
	private String clientName;
	public String name;
	public String pID;
	public String colour;

	private int hint;
	private int myLastMove;
	private int thinkInterval;

	private ClientGame game;
	private HumanPlayer humanPlayer;
	private DummyPlayer opponant;
	
	private static String LINE = "--------------------------------------------------------";

	/**
	 * Constructs a Client-object and tries to make a socket connection.
	 */
	public Client(InetAddress host, int port) throws IOException {
		tui = new ClientTUIG(this);
		tui.run();

		try {
			sock = new Socket(host, port);
			// -------Set up BufferedReader and Writer------
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			tui.printInput("Could not connect to server " + host);
		}

		gameIsStarted = false;
		gameIsEnded = false;

	}

	public void run() {
		socketReader.start();

		// // -------Create scanner to handle input typed in the console.
		// typedInput = new Scanner(System.in);
		//
		// try {
		// // ----Start infinite loop to handle input------
		// String input = "";
		// while ((input = in.readLine()) != null) {
		// handleInput(input);
		// }
		// } catch (IOException e) {
		// System.out.println("Connection errored with server.");
		// e.printStackTrace();
		// } finally {
		// shutdown();
		// }
	}

	Thread socketReader = new Thread(new Runnable() {
		public void run() {
			String input = null;
			while (true) {
				try {
					input = in.readLine();
					if (input != null) {
						processProtocol(input);
					}
				} catch (IOException e) {
					tui.printInput("Couldn't read from socket, shutting down connection");
					shutdown();
				} catch (NullPointerException ee) {
					tui.printInput("Please enter a new port number.");
						ee.printStackTrace();
						shutdown();
				}
			}
		}
	});

	//TODO: hier moet hij lezen van de console
	public void readFromConsole(String input) {
		if (!input.equals("")) {
			String msg = "";
			/*
			 * TODO: als hij van de server capabilities ontvangt moet hij zijn eigen capabilities gaan typen
			 * Probleem: hier lijst hij wat je typt in de console (input), dus er moet net als gameIsStarted een soort
			 * boolean komen waaruit deze loop op kan maken dat de capabilities zijn ontvangen en dat die hier getypt moeten worden
			 */
			if (input.contains("sendCapabilities")) {
				sendMessage(input);
				
			} else if (gameIsStarted) {
				try {
					myLastMove = Integer.parseInt(input);
					if (myLastMove /* a valid move */) {
						msg = "makeMove" + myLastMove;
						sendMessage(msg);
					} else {
						tui.printInput("Not a valid move.");
					}
				} catch (NumberFormatException e) {
					msg = inputFromConsole;
					if (msg.equals("hint") && isCurrentPlayer) {
						hint();
						tui.printInput("You should play " + Integer.toString(hint));
					} else if (!isCurrentPlayer) {
						tui.printInput("Wait for your opponent to make a move.");
					} else {
						tui.printInput("Enter a number or 'hint'.");
					}
				}
			} else if (!gameIsStarted) {
				tui.printInput("Waiting for an opponant.");
			} else {
				tui.printInput("This peace of code is impossible to reach.");
			}
		} else {
			tui.printInput("Please enter something.");
		}
	}

	/*
	 * @requires getBw() != null && msg != null;
	 */
	/**
	 * send a message to a ClientHandler.
	 */
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			tui.printInput("The socket connection broke down.");
			tui.printInput("The client will shut down.");
			shutdown();
		} catch (NullPointerException ee) {
			tui.printInput("Server is not online.");
			shutdown();
		}
	}

	/** close the socket connection. */
	/*
	 * @ requires getBw() != null && getBr() != null && getSocket() != null;
	 */
	public void shutdown() {
		try {
			System.exit(0);
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			tui.printInput("Unable to close all the connections.");
			tui.printInput("Please shut down the application manually.");
		}
	}

	/**
	 * A method that processes the messages from the socket connection, using
	 * the protocol.
	 * 
	 * @param msg
	 *            the message to be processed
	 */
	private void processProtocol(String msg) {
		String[] message = msg.split(msgSeperator);
		switch (message[0]) {
		case serverCapabilities:
			serverCapabilities(message);
			break;
		case acceptRequest:
			acceptRequest(message[1]);
			break;
		case startGame:
			startGame(message[1]);
			break;
		case moveResult:
			moveResult(msg);
			break;
		case sendBoard:
//			sendBoard(message);
			break;
		case turn:
			turn(message[1]);
			break;
		case endGame:
			endGame(msg);
			break;
		case rematchConfirm:
			rematchConfirm();
			break;
		case invalidCommand:
//			error(message[1]);
			break;
		default:
			tui.printInput("'" + msg + "' is not processed via the protocol.");
			break;
		}
	}

	/**
	 * 
	 */
	private void serverCapabilities(String[] capabilities) {
		String amountOfPlayers = capabilities[1];
		String roomSupport = capabilities[2];
		String maxRoomDimensionX = capabilities[3];
		System.out.println("Enter your preferences.");
	}

	/**
	 * When the clientName is accepted by the server, an acceptRequest is sent.
	 * It assigns the player to a mark. First player receives Mark.YELLOW and
	 * the second receives Mark.RED as mark.
	 * 
	 * @param markArg
	 *            the mark to be assigned
	 */
	// @ requires markArg != null;
	// @ requires markArg == yellow || markArg == red;
	// @ ensures getIsCurrentPlayer() == true || getIsCurrentPlayer() == false;
	// @ ensures getClientName() == username;
	private void acceptRequest(String markArg) {
		if (markArg.equals(yellow)) {
			isCurrentPlayer = true;
			myMark = Mark.XXX;
//			clientName = username;
			tui.printInput("Your mark is: XXX.");
			tui.printInput("Now, we just wait for your opponent to show up...");
		} else {
//			clientName = username;
			myMark = Mark.OOO;
			tui.printInput("Request accepted!");
			tui.printInput("Your mark is: OOO.");
		}
	}

	/**
	 * When the game starts, the board is initialized and a player may begin its
	 * turn.
	 * 
	 * @param startingPlayer
	 *            the player that starts the game
	 */
	// @ requires startingPlayer != null;
	private void startGame(String startingPlayer) {
		gameIsStarted = true;
		board = new Board(4);
		board.addObserver(tui);
		tui.printInput("Game has started!");
		tui.printInput(LINE);
		if (isCurrentPlayer) {
			tui.printInput("You can make the first move.");
			if (playerType.equals("computer")) {
//				doComputerMove();
			}
		} else {
			tui.printInput("The opponent '" + startingPlayer + "' starts");
		}
		tui.printInput(board.toString());
		if (isCurrentPlayer) {
			System.out.print("Enter a column number: ");
		} else {
			tui.printInput("Waiting for move from opponent...");
		}
	}

	/**
	 * Changes the current player.
	 * 
	 * @param input
	 *            consists information of the next player
	 */
	// @ requires input != null;
	private void moveResult(String input) {
		String[] message = input.split(msgSeperator);
		String playerWhoDidMove = message[2];
		if (messageIsTrue(message[3])) {
			// gives turn to the other player
			isCurrentPlayer = !isCurrentPlayer;
		} else {
			tui.printInput(playerWhoDidMove + " has made an invalid move.");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				tui.printInput("The thread was unable to fall asleep.");
			}
		}
	}

//	/**
//	 * Adjusts the board to the current state after a move is made.
//	 * 
//	 * @param fields
//	 *            A string array of the board
//	 */
//	// @ requires fields != null;
//	// @ requires fields.length == Board.X * Board.Y;
//	private void sendBoard(String[] fields) {
//		tui.printInput(LINE);
//		// @ loop_invariant i >= 1 && i < Board.X * Board.Y;
//		for (int i = 1; i < Board.X * Board.Y; i++) {
//			switch (fields[i]) {
//			case red:
//				board.setIndexField(i, Mark.RED);
//				break;
//			case yellow:
//				board.setIndexField(i, Mark.YELLOW);
//				break;
//			case empty:
//				board.setIndexField(i, Mark.EMPTY);
//			}
//		}
//		tui.printInput(board.toString());
//	}

	/**
	 * When the game ended a proper action must be made.
	 * 
	 * @param message
	 *            the message that contains the cause of the ended game
	 */
	// @ requires message != null;
	private void endGame(String message) {
		String[] reasons = message.split(msgSeperator);
		if (reasons[2].equals(winner)) {
			sendMessage(getBoard);
			if (clientName.equals(reasons[1])) {
				tui.printInput("Congratulations! You won!");
				gameIsStarted = false;
				gameIsEnded = true;
			} else {
				tui.printInput("You just lost the game...");
				gameIsStarted = false;
				gameIsEnded = true;
			}
		} else if (reasons[2].equals(draw)) {
			tui.printInput("Good game! It's a draw though.");
			gameIsStarted = false;
			gameIsEnded = true;
		} else if (reasons[2].equals(connectionlost)) {
			tui.printInput("\nThe opponent lost connection with the server.");
			tui.printInput("You win!");
			shutdown();
		} else if (reasons[2].equals(unknownerror)) {
			tui.printInput("An unknown error caused the game to end.");
			tui.printInput("There is no winner to be declared.");
			shutdown();
		}
	}

	/**
	 * When the server sends a rematchConfirm to the Clients, the game is
	 * resetted. A line on the TUI is printed, a gameIsEnded and gameIsStarted
	 * and respectively set on false and true, so integer input is accepted
	 * again by readFromConsole().
	 */
	private void rematchConfirm() {
		tui.printInput(LINE);
		gameIsEnded = false;
		gameIsStarted = true;
	}

//	/**
//	 * Handles several error scenerios. Prints to the TUI.
//	 * 
//	 * @param error
//	 *            the sort of error
//	 */
//	// @ requires error != null;
//	private void error(String error) {
//		if (error.equals(invalidUserTurn)) {
//			tui.printError("It is not your turn.");
//		} else if (error.equals(usernameInUse)) {
//			tui.printError("The username you entered is already in use by another player.");
//			tui.printError("Restart the application and enter another username.");
//			shutdown();
//		} else if (error.equals(invalidMove)) {
//			tui.printError("You made an invalid move.");
//		} else if (error.equals(invalidUsername)) {
//			tui.printError("Invalid username (a-z, A-Z, 0-9)");
//		} else if (error.equals(invalidCommand)) {
//			tui.printError("invalid command");
//		} else {
//			tui.printError("Something went wrong!");
//		}
//	}

//	/**
//	 * The hint generator for both players using the smart strategy. It can only
//	 * be used when it is your turn and when the game has started.
//	 */
//	public void hint() {
//		if (!gameIsStarted) {
//			tui.printInput("The game has not started yet");
//		} else if (isCurrentPlayer && gameIsStarted) {
//			SmartStrategy hintGenerator = new SmartStrategy(myMark);
//			hint = hintGenerator.determineMove(board, myMark);
//		} else {
//			tui.printInput("It is not your turn.");
//		}
//	}

	/**
	 * It prints out whose turn it is. When the computer is on turn, it calls
	 * the command doComputerMove().
	 * 
	 * @param username
	 *            the name of the client which turn it is
	 */
	private void turn(String username) {
		if (username.equals(clientName)) {
			tui.printInput("It's your turn.");
		} else {
			tui.printInput("It's " + username + "'s turn.");
		}
//		if (playerType.equals("computer") && isCurrentPlayer) {
//			doComputerMove();
//		}
	}

//	/**
//	 * Generates a move for the computerplayer after several milliseconds which
//	 * are defined in the TUI by the user.
//	 */
//	private void doComputerMove() {
//		// TODO: SmartStrategy / computerplayer van jou Lex
//		SmartStrategy strategy = new SmartStrategy(myMark);
//		int move = strategy.determineMove(board, myMark);
//		try {
//			Thread.sleep(thinkInterval);
//			String msg = doMove + msgSeperator + move;
//			sendMessage(msg);
//		} catch (InterruptedException e) {
//			tui.printInput("The Thread is interrupted.");
//		}
//	}

	// --- Queries
	// --------------------------------------------------------------------------------------

	/**
	 * Returns whether the client is the current player.
	 * 
	 * @return true if the client is on turn
	 */
	// @ pure;
	public boolean getIsCurrentPlayer() {
		return isCurrentPlayer;
	}

	/**
	 * Returns the hint when the user is on turn.
	 * 
	 * @return hint
	 */
	/* @ pure */
	public int getHint() {
		return hint;
	}

	/** @return the client name. */
	/* @ pure */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Returns the playertype of the client.
	 * 
	 * @return human or computer
	 */
	/* @ pure */
	public String getPlayerType() {
		return playerType;
	}

	/**
	 * practical getter to solve private visability issues with regards to JML
	 * specification.
	 * 
	 * @return the BufferedReader object - in
	 */
	/* @ pure */
	public BufferedReader getBr() {
		return in;
	}

	/**
	 * practical getter to solve private visability issues with regards to JML
	 * specification.
	 * 
	 * @return the BufferedWriter object - out
	 */
	/* @ pure */
	public BufferedWriter getBw() {
		return out;
	}

	/**
	 * A getter for the socket to which the client is connected.
	 * 
	 * @return socket
	 */
	/* @ pure */
	public Socket getSocket() {
		return sock;
	}

	/**
	 * Checks whether the message is true or false.
	 * 
	 * @param message
	 *            the message
	 * @return true if the message says true
	 */
	// @ requires message != null;
	private boolean messageIsTrue(String message) {
		if (message.equals("true")) {
//			board.setField(myLastMoveX, myLastMoveY myMark);
		}
		return message.equals("true");
	}

	// /*
	// * Handles the input recieved by the client from the server.
	// */
	// public synchronized void handleInput(String msg) {
	//
	// // ----If server sends capabilities, return preferences----
	// if (msg.contains("serverCapabilities")) {
	//
	// sendMessage(sendPreferences(msg));
	//
	// // ----If server starts a game, setup a ClientGame---------
	// } else if (msg.contains("startGame")) {
	// String[] parts = msg.split(" ");
	// String gameSettings = parts[1];
	// String player1 = parts[2];
	// String player2 = parts[3];
	//
	// // -----If string contains name, set clientID (pID)----
	// if (player1.contains(name)) {
	// String[] parts1 = player1.split("\\|");
	// pID = parts1[0];
	// colour = parts1[2];
	//
	// } else if (player2.contains(name)) {
	// String[] parts2 = player2.split("\\|");
	// pID = parts2[0];
	// colour = parts2[2];
	// }
	//
	// System.out.println("My ID is " + pID);
	//
	// // -----Setup a human player-----
	// humanPlayer = new HumanPlayer(name, Mark.XXX, this);
	// opponant = new DummyPlayer("opponant", Mark.OOO);
	//
	// // -----Setup a game to remember moves-----
	// String[] partsG = gameSettings.split("\\|");
	// game = new ClientGame(humanPlayer, opponant,
	// Integer.parseInt(partsG[0]));
	//
	// // ----If server sends playerTurn, determine a move or wait
	// } else if (msg.contains("playerTurn " + pID)) {
	// // -----If it's my turn, determine move----
	// if (msg.contains(pID)) {
	// game.getBoard().showBoard();
	// sendMessage(determineMove());
	// // ----If it's not my turn, wait------
	// } else {
	// System.out.println("waiting");
	// }
	//
	// // -----If server notifys a move, set this move on the board of
	// // game.
	// } else if (msg.contains("notifyMove")) {
	// String[] parts = msg.split(" ");
	//
	// String id = parts[1];
	// int moveX = Integer.parseInt(parts[2]);
	// int moveY = Integer.parseInt(parts[3]);
	//
	// if (id.equals(pID)) {
	// game.makeMove(moveX, moveY);
	// } else {
	// game.moveOponant(moveX, moveY);
	// }
	//
	// System.out.println("Current board:");
	// game.getBoard().showBoard();
	//
	// // TODO: set field player(id)
	//
	// } else if (msg.contains("notifyEnd")) {
	// String[] parts = msg.split(" ");
	//
	// int reason = Integer.parseInt(parts[1]);
	// String player = parts[2];
	//
	// if (reason == 1) {
	// System.out.println("Player " + player + "has won!");
	// } else if (reason == 2) {
	// System.out.println("All spaces on the board have been filled, ther is no
	// winner...");
	// } else if (reason == 3) {
	// System.out.println("Player " + player + " has disconnected");
	// // TODO: quit game.
	// }
	//
	// // -----Handle errors---------
	// } else if (msg.contains("error")) {
	// String[] parts = msg.split(" ");
	//
	// int error = Integer.parseInt(parts[1]);
	//
	// if (error == 1) {
	// System.out.println("Client has not yet sent capabilities message; server
	// cannot proceed.");
	// } else if (error == 2) {
	// System.out.println("Room sent in message joinRoom dos not exist.");
	// } else if (error == 3) {
	// System.out.println("The chosen room is no longer available.");
	// } // TODO: write other errors
	//
	// }
	// }
	//
	// /**
	// * Asks for the preferences of the human player. TODO: add errors if
	// * preferences don't fit within server Capabilities for example: name
	// cannot
	// * contain spaces, dimention cannot be greater than maxserver connection
	// */
	// public String sendPreferences(String serverCapabilities) {
	//
	// // TODO: compare with serverCapabilities and give errors if values are
	// // out of bounds.
	// System.out.println(
	// "Enter your game preferences. Make sure your preferences " + "fit within
	// the server capabilities!");
	// System.out.println("With how many players do you want to play?");
	// String players = typedInput.nextLine();
	// System.out.println("What is your game name? (Don't use spaces!)");
	// name = typedInput.nextLine();
	// System.out.println("Do you support rooms? Yes = 1, No = 0");
	// String roomSupport = typedInput.nextLine();
	// System.out.println("With which dimentions do you want to play a game?");
	// System.out.println("Enter lenght dimension x: ");
	// String maxRoomDimensionX = typedInput.nextLine();
	// System.out.println("Enter lenght dimension y: ");
	// String maxRoomDimensionY = typedInput.nextLine();
	// System.out.println("Enter lenght dimension z: ");
	// String maxRoomDimensionZ = typedInput.nextLine();
	// System.out.println("How long does a winning connection have to be?"
	// + "(This length cannot be bigger than the smallest dimention)");
	// String maxLengthToWin = typedInput.nextLine();
	// System.out.println("Do you support chats? Yes = 1, No = 0");
	// String chatSupport = typedInput.nextLine();
	//
	// String preferences = "sendCapabilities " + players + " " + name + " " +
	// roomSupport + " " + maxRoomDimensionX
	// + " " + maxRoomDimensionY + " " + maxRoomDimensionZ + " " +
	// maxLengthToWin + " " + chatSupport;
	//
	// System.out.println("You are sending these preferences to the server: " +
	// preferences);
	//
	// return preferences;
	// }
	//
	// /*
	// * Asks the human player
	// */
	// public String determineMove() {
	// System.out.println(getName() + " (" + humanPlayer.getMark() + ") ,wich
	// row dow you want to choose?");
	// String moveX = typedInput.nextLine();
	//
	// System.out.println(getName() + " (" + humanPlayer.getMark() + ") ,wich
	// column dow you want to choose?");
	// String moveY = typedInput.nextLine();
	//
	// String movePlayer = "makeMove " + pID + " " + moveX + " " + moveY;
	// System.out.println("You are sending this move to the server: " +
	// movePlayer);
	// return movePlayer;
	// }
	//
	// /** send a message to a ClientHandler. */
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
	// public void waiting() {
	// System.out.println("Waiting");
	// try {
	// out.newLine();
	// out.flush();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// System.out.println("could not wait");
	// e.printStackTrace();
	// }
	// }
	//
	// /** close the socket connection. */
	// public void shutdown() {
	// try {
	// in.close();
	// out.close();
	// sock.close();
	// } catch (IOException e) {
	// System.out.println("Something went wrong while trying to shutdown
	// client.");
	// }
	// // TODO Add implementation
	// }
	//
	// /** returns the client name. */
	// public String getClientName() {
	// return clientName;
	// }

}
