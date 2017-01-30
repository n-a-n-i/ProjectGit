package project;

import server.ClientHandler;

/**
 * ConnectFour over a Server project
 * Board 
 * @author  Nienke Huitink & Lex Favrin, based on original code by Theo Ruys
 * @version 2017.01.26
 */
public class Player {

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
	public Player(String name /*, Mark mark */) {
		this.name = name;
//		this.mark = mark;
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


}
