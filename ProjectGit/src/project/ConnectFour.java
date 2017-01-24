package project;

public class ConnectFour {
	static int dim;

	/*
	 * @ requires p1,p2 != null requires 0 < dimension
	 * 	 ensures dim == dimension;
	 *   ensures player1 != null && player2 != null;
	 */
	public static void main(String p1, String p2, int dimension) {
		Player player1;
		Player player2;

		dim = dimension;

		player1 = new HumanPlayer(p1, Mark.OOO);
		player2 = new HumanPlayer(p2, Mark.XXX);

		Game game = new Game(player1, player2);
		game.start();
	}
}
