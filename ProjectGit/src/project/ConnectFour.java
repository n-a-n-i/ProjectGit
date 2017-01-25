package project;

public class ConnectFour {
	static int dim;

	/*
	 * @ requires p1,p2 != null requires 0 < dimension
	 * 	 ensures dim == dimension;
	 *   ensures player1 != null && player2 != null;
	 */
	public static void main(String[] args) {//String p1, String p2, int dimension
		Player player1;
		Player player2;

		dim = Integer.parseInt(args[2]);//dimension

		player1 = new ComputerPlayer(args[0], Mark.OOO);//p1
		player2 = new ComputerPlayer(args[1], Mark.XXX);//p2

		Game game = new Game(player1, player2);
		game.start();
	}
}
