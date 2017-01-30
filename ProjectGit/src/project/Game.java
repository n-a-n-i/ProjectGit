package project;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Scanner;


public class Game extends Observable {

/**
 * ConnectFour over a Server project Game.
 * 
 * @author Nienke Huitink & Lex Favrin
 * @version 2017.01.26
 */

	// -- Instance variables -----------------------------------------

	public static final int NUMBER_PLAYERS = 2;

	/*
	 * @ invariant board != null;
	 */
	/**
	 * The connect four board.
	 * @invariant board != null
	 */
	private Board board;

	/*
	 * @ private invariant players.length == NUMBER_PLAYERS; private invariant
	 * (\forall int i; 0 <= i && i < NUMBER_PLAYERS; players[i] != null);
	 */
	/**
	 * The 2 players of the game.
	 * @invariant	players.length == 2
	 * 				players[0] != null
	 * 				players[1] != null
	 */
	protected Player[] players;

	/*
	 * @ private invariant 0 <= current && current < NUMBER_PLAYERS;
	 */
	/**
	 * Index of the current player.
	 * @invariant 0 <= current < NUMBER_PLAYERS
	 */
	private int current;

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires s0 != null; requires s1 != null;
	 */
	/**
	 * Creates a new Game object.
	 * @param s0 the first player
	 * @param s1 the second player
	 * @require s0 != null && s1 != null
	 */
	public Game(Player s0, Player s1, int dim) {
		this.board = new Board(dim);
		this.players = new Player[NUMBER_PLAYERS];
		this.players[0] = s0;
		this.players[1] = s1;
		this.current = 0;
	}

	// -- Commands ---------------------------------------------------
	/**
	 * Changes the turn of the Players. 
	 */
	public void changePlayer() {
		current = (current + 1) % 2;
	}

	/**
	 * Returns the board which is played on.
	 */
	public Board getBoard() {
		System.out.println("getting board");
		return this.board;
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
	 * Returns the current Player
	 */

	public Player getCurrent() {
		System.out.println("getting players");
		return this.players[current];
	}
	private void play() {
	//	update();

		while (!board.hasWinner() && !board.isFull()) {
	//		players[current].makeMove(board);
			current = (current + 1) % 2;
	//		update();
		}
		printResult();

	}
	
	/**
	 * 
	 * @return
	 */
	public void changeCurrent(){
		current = (current + 1) % NUMBER_PLAYERS;
	}
	
	/*
	 * 
	 * @ requires this.board.isFull() || this.board.hasWinner();
	 */
	private void printResult() {
		if (board.isFull()) {
			System.out.println("Draw, there is no winner.");
		} else {
			System.out.println(players[(current + 1) % NUMBER_PLAYERS].getName() + 
					" with mark " + board.lastM + " has won!");
			System.out.println("The winning move was " + "(" + board.lastMoveX + ", " 
					+ board.lastMoveY + ", " + board.lastMoveZ + ")");
		}
	}

	/**
	 * Wraps an input stream to prevent it from being closed.
	 */
	public Player[] getPlayers() {
		return players;
	}
	
//	public void switchPlayer() {
//		current = (current + 1) % 2;
//	}
//	
//	/**
//	 * 
//	 */
//	public void move() {
//		int moveX = getPlayer().getMoveX(this, getPlayer().getMark());
//		int moveY = getPlayer().getMoveY(this, getPlayer().getMark());
//		
//		if (getBoard().isEmptyField(moveX, moveY, getBoard().firstEmptyField(moveX, moveY))){
//			getBoard().setField(moveX, moveY, getPlayer().getMark());
//		} else {
//			// TODO: wrong move exception?
//		}
//	}
//	
//
//	
//
//	/**
//	 * Prints the game situation.
//	 */
//	private void update() {
//		System.out.println("\ncurrent game situation: \n\n");
//		board.showBoard();
//	}
//
//	/**
//	 * Prints the result of the last game. <br>
//	 */
//	/*
//	 * @ requires this.board.isFull() || this.board.hasWinner();
//	 */
//	private void printResult() {
//		if (board.isFull()) {
//			System.out.println("Draw, there is no winner.");
//		} else {
//			System.out.println("Player with mark " + board.lastM + " has won!");
//		}
//	}
//
//	
//	
//	
//	/**
//	 * Starts a game of connect four.
//	 * Gives the current player a turn to place a mark and switches the player turns.
//	 */
//	public void startGame() {
////		System.out.println("A game of Connect Four has started");
//		move();
//		while (!board.isFull() && !!board.hasWinner()) {
//			changePlayer();
//			move();
//		}
//	}
//	
}
