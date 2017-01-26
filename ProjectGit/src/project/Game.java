package project;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Game {
	// -- Instance variables -----------------------------------------

	public static final int NUMBER_PLAYERS = 2;

	/*
	 * @ invariant board != null;
	 */
	/**
	 * The board.
	 */
	Board board;

	/*
	 * @ private invariant players.length == NUMBER_PLAYERS; private invariant
	 * (\forall int i; 0 <= i && i < NUMBER_PLAYERS; players[i] != null);
	 */
	/**
	 * The 2 players of the game.
	 */
	private Player[] players;

	/*
	 * @ private invariant 0 <= current && current < NUMBER_PLAYERS;
	 */
	/**
	 * Index of the current player.
	 */
	private int current;

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires s0 != null; requires s1 != null;
	 */
	/**
	 * Creates a new Game object.
	 * 
	 * @param s0
	 *            the first player
	 * @param s1
	 *            the second player
	 */
	public Game(Player s0, Player s1) {
		board = new Board(ConnectFour.dim);
		players = new Player[NUMBER_PLAYERS];
		players[0] = s0;
		players[1] = s1;
		current = 0;
		System.setIn(new UncloseableInputStream());
	}

	// -- Commands ---------------------------------------------------

	/**
	 * Starts the Tic Tac Toe game. <br>
	 * Asks after each ended game if the user want to continue. Continues until
	 * the user does not want to play anymore.
	 */
	public void start() {
		boolean doorgaan = true;
		while (doorgaan) {
			reset();
			play();
			doorgaan = readBoolean("\n> Play another time? (y/n)?", "y", "n");
		}
	}

	/**
	 * Prints a question which can be answered by yes (true) or no (false).
	 * After prompting the question on standard out, this method reads a String
	 * from standard in and compares it to the parameters for yes and no. If the
	 * user inputs a different value, the prompt is repeated and te method reads
	 * input again.
	 * 
	 * @parom prompt the question to print
	 * @param yes
	 *            the String corresponding to a yes answer
	 * @param no
	 *            the String corresponding to a no answer
	 * @return true is the yes answer is typed, false if the no answer is typed
	 */

	/*
	 * @ requires prompt, no, yes != null; ensures \result answer.equals(yes);
	 */
	private boolean readBoolean(String prompt, String yes, String no) {
		String answer;
		do {
			System.out.print(prompt);
			try (Scanner in = new Scanner(System.in)) {
				answer = in.hasNextLine() ? in.nextLine() : null;
			}
		} while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
		return answer.equals(yes);
	}

	/**
	 * Resets the game. <br>
	 * The board is emptied and player[0] becomes the current player.
	 */
	void reset() {
		current = 0;
		board.reset();
	}

	/**
	 * Plays the Tic Tac Toe game. <br>
	 * First the (still empty) board is shown. Then the game is played until it
	 * is over. Players can make a move one after the other. After each move,
	 * the changed game situation is printed.
	 */
	private void play() {
		update();

		while (!board.hasWinner() && !board.isFull()) {
			players[current].makeMove(board);
			current = (current + 1) % 2;
			update();
		}
		System.out.println("The winning move was " + board.lastMoveX + ", " + board.lastMoveY + ", " + board.lastMoveZ);
		System.out.println("1: " + board.hasRow() + " 2: " + board.hasColumn() + " 3: " + board.hasStack() + " 4: "
				+ board.has2DDiagonal() + " 5: " + board.has3DDiagonal());
		printResult();
	}

	/**
	 * Prints the game situation.
	 */
	private void update() {
		System.out.println("\ncurrent game situation: \n\n");
		board.showBoard();
	}

	/**
	 * Prints the result of the last game. <br>
	 */
	/*
	 * @ requires this.board.isFull() || this.board.hasWinner();
	 */
	private void printResult() {
		if (board.isFull()) {
			System.out.println("Draw, there is no winner.");
		} else {
			System.out.println("Player with mark " + board.lastM + " has won!");
		}
	}

	/**
	 * Wraps an input stream to prevent it from being closed.
	 */
	private static class UncloseableInputStream extends FilterInputStream {

		/**
		 * Creates a wrapper around {@link System.in}.
		 */
		UncloseableInputStream() {
			this(System.in);
		}

		/**
		 * Creates a wrapper around an arbitrary {@link InputStream}.
		 * 
		 * @param stream
		 *            The stream to wrap.
		 */
		UncloseableInputStream(InputStream stream) {
			super(stream);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() throws IOException {
			// Don't do anything
		}
	}
}
