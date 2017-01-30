package project;

import server.ClientHandler;

public abstract class Player {

	// -- Instance variables -----------------------------------------

	protected String name;
	protected Mark mark;
	private Game game;
	private ClientHandler clientHandler;

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires name != null; requires mark == Mark.XX || mark== Mark.OO;
	 * ensures this.getName() == name; ensures this.getMark() == mark;
	 */
	/**
	 * Creates a new Player object.
	 * 
	 */
	public Player(String name, Mark mark) {
		this.name = name;
		this.mark = mark;
	}

	// -- Queries ----------------------------------------------------

	/**
	 * Returns the name of the player.
	 */
	/* @ pure */ public String getName() {
		return this.name;
	}

	/**
	 * Returns the mark of the player.
	 */
	// @ensures mark == Mark.OOO || mark == Mark.XXX || mark == Mark.EMP;
	/* @ pure */ public Mark getMark() {
		return mark;
	}

	/**
	 * 
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * 
	 */
	public ClientHandler getClientHandler() {
		return clientHandler;
	}

	// -- Commands ---------------------------------------------------

	/**
	 * Sets the name of the player.
	 * 
	 * @param nameArg
	 *            the name of the player
	 */
	// @ requires nameArg != null;
	// @ ensures getName() == nameArg;
	public void setName(String nameArg) {
		name = nameArg;
	}

	/**
	 * Sets the mark of the player.
	 * 
	 * @param markArg
	 *            the mark of the player
	 */
	// @ requires markArg != null;
	// @ ensures getMark() == markArg;
	public void setMark(Mark markArg) {
		mark = markArg;
	}

	/**
	 * Sets the game of the player.
	 * 
	 * @param gameArg
	 *            the game of the player
	 */
	// @ requires gameArg != null;
	// @ ensures getGame() == gameArg;
	public void setGame(Game gameArg) {
		game = gameArg;
	}

	/**
	 * Sets the clientHandler of the player.
	 * 
	 * @param handler
	 *            the clientHandler of the player
	 */
	// @ requires handler != null;
	// @ ensures getClientHandler() == handler;
	public void setClientHandler(ClientHandler handler) {
		clientHandler = handler;
	}

	// /*
	// * @ requires board != null & !board.isFull(); ensures
	// * board.isField(\result) & board.isEmptyField(\result);
	// *
	// */
	// /**
	// * Determines the field for the next move.
	// *
	// * @param board
	// * the current game board
	// * @return the player's choice
	// */
	// public abstract int getMoveX(Game game, Mark m);
	//
	// public abstract int getMoveY(Game game, Mark m);

}
