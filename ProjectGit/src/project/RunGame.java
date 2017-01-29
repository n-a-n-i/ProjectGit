package project;

import project.Game.UncloseableInputStream;

public class RunGame {
	
	public static final int NUMBER_PLAYERS = 2;
	private Player[] players;
	private int current;
	
	Board board;
	
	public int dim;
	
	public String ID1;
	public String ID2;
	public String name1;
	public String name2;
	
	public RunGame(String pID1, String p1, String pID2, String p2, int dimension){
		board = new Board(dimension);
		
		players = new Player[NUMBER_PLAYERS];
		
		players[0] = pID1;
		players[1] = pID2;
		current = 0;
				
	}
	
	
	
	public void start(){
		boolean doorgaan = true;
		while (doorgaan) {
			reset();
			play();
			doorgaan = readBoolean("\n> Play another time? (y/n)?", "y", "n");
		}
	}
	
	
	/**
	 * Plays the Tic Tac Toe game. <br>
	 * First the (still empty) board is shown. Then the game is played until it
	 * is over. Players can make a move one after the other. After each move,
	 * the changed game situation is printed.
	 */
	private void play() {
		while (!board.hasWinner() && !board.isFull()) {
			
		}
			players[current].makeMove(board);
			current = (current + 1) % 2;
		}
		
		if(board.hasWinner()){
			server.StartServer.connectFourServer.clients.get(players).sendMessage("1");
			server.StartServer.connectFourServer.clients.get(players).sendMessage("1");
		}
	
		
		


	
	/**
	 * Resets the game. <br>
	 * The board is emptied and player[0] becomes the current player.
	 */
	void reset() {
		current = 0;
		board.reset();
	}
	
	
}
