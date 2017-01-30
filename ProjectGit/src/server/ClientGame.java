package server;

import project.Game;
import project.Mark;
import project.Player;

public class ClientGame extends project.Game {

	/**
	 * Constructs a ClientGame
	 * @param s0 the first player
	 * @param s1
	 */
	public ClientGame(Player s0, Player s1, int dim) {
		super(s0, s1, dim);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public void moveOponant(int x, int y) {
		getBoard().setField(x, y, players[1].getMark());
		refresh();
	}
	
	/**
	 * 
	 */
	public void makeMove(int x, int y) {
		getBoard().setField(x, y, players[0].getMark());
	}
	
	
	public void refresh() {
		setChanged();
		notifyObservers();
	}
	
	
}
