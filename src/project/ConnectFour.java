package project;


public class ConnectFour {
	static int DIM;

	public static void main(String p1, String p2, int dimension) {
    	Player player1;
    	Player player2;
    	
    	DIM = dimension;
    	
    		player1 = new HumanPlayer(p1, Mark.OOO);
    		player2 = new HumanPlayer(p2, Mark.XXX);
    	
        Game game = new Game(player1, player2);
        game.start();
     }
}
