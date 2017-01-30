package server;

import project.Board;
import project.Game;
import project.Mark;
import project.Player;

public class NetworkPlayer extends Player {

	private ClientHandler clientHandler;

	private int i;
	private Mark m;

	public NetworkPlayer(String name, ClientHandler clientHandler, Mark mark) {
		super(name, mark);
		this.clientHandler = clientHandler;
		m = mark;
	}

	/**
	 * Returns the ClientHandler which NetworkPlayer is communicating with
	 */
	public ClientHandler getClientHandler() {
		return this.clientHandler;
	}

	@Override
	public synchronized int getMoveX(Game game, Mark m) {
		int moveX = -1;

		while (moveX < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		return moveX;
	}

	@Override
	public synchronized int getMoveY(Game game, Mark m) {
		int moveY = -1;

		while (moveY < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		return moveY;
	}
	
	public synchronized void makeMove(int x, int y){
		
	}
	
	@Override
	public Mark getMark(){
		return this.m;
	}

}
