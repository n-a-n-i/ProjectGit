package project;

import server.Client;
import strategies.*;

/**
 * ConnectFour over a Server project ConnectFour
 * 
 * @author Nienke Huitink & Lex Favrin
 * @version 2017.01.26
 */

public class ConnectFour {
	static int dim;

	/*
	 * @ requires p1,p2 != null requires 0 < dimension ensures dim == dimension;
	 * ensures player1 != null && player2 != null;
	 */

	public static void main(String p1, String p2, int dimension) {

		Player player1;
		Player player2;
		// RandomStrategy random = new RandomStrategy();
		// SmartStrategy smart = new SmartStrategy();
		// dim = Integer.parseInt(args[2]); // dimension

		dim = dimension;

	//	player1 = new HumanPlayer(p1, Mark.OOO, client);
	//	player2 = new HumanPlayer(p2, Mark.XXX, client);

		// player1 = new ComputerPlayer(random.getName(), Mark.OOO, random); //
		// p1
		// player2 = new ComputerPlayer(smart.getName(),Mark.XXX, smart); // p2
		// player1 = new HumanPlayer("Lex", Mark.OOO);

	//	Game game = new Game(player1, player2, dim);
		//game.start();
	}
}
