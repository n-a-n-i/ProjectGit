package server;

import project.Player;

public class ServerGame extends project.Game implements Runnable {
	
	private Thread t;
	private boolean finished;

	public ServerGame(Player s0, Player s1, int dim) {
		super(s0, s1, dim);
		this.finished = false;
		this.t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		startGame();
		stopGame();
		
	}
	
	public void stopGame() {
		if (getBoard().hasWinner()) {
			
		}
		setFinished();	
	}
	
	public void setFinished() {
		this.finished = true;
	}
	
	
	public void move() {
		int moveX = getPlayer().getMoveX(this, getPlayer().getMark());
		int moveY = getPlayer().getMoveY(this, getPlayer().getMark());
		
		if (getBoard().isEmptyField(moveX, moveY, getBoard().firstEmptyField(moveX, moveY))){
			getBoard().setField(moveX, moveY, getPlayer().getMark());
		} else {
			// TODO: wrong move exception?
		}
	}
	
}
