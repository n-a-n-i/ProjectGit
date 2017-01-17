package project;


public class ConnectFour {
	static int DIM;

	public static void main(String[] args) {
    	Player player1;
    	Player player2;
    	
    	DIM = Integer.parseInt(args[2]);
    	
    		player1 = new HumanPlayer(args[0], Mark.OO);
    		player2 = new HumanPlayer(args[1], Mark.XX);
    	
        Game game = new Game(player1, player2);
        game.start();
     }
}
