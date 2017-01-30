package project;

import java.util.Random;

import strategies.ComputerStrategy;

/**
 * ConnectFour over a Server project Board
 * 
 * @author Nienke Huitink & Lex Favrin
 * @version 2017.01.26
 */
public class ComputerPlayer extends Player {

	ComputerStrategy s;
	int[] move = new int[2];
	
	public ComputerPlayer(String name, Mark mark, ComputerStrategy strategy) {
		super(name, mark);
		this.s = strategy;
	}

	public int[] determineMove(Board board) {
		boolean valid = false;
		move = this.s.determineMove(board, this.getMark());
		while (!valid) {
			if (!(0 <= move[0] && move[0] < board.dim) || !(0 <= move[1] && move[1] < board.dim) ||
					board.firstEmptyField(move[0], move[1]) == -1) {
				move = this.s.determineMove(board, this.getMark());
			} else {
				valid = true;
			}
		}
		return move;
	}

	@Override
	public int getMoveX(Game game, Mark m) {
		determineMove(game.getBoard());
		return move[0];
	}

	@Override
	public int getMoveY(Game game, Mark m) {
		determineMove(game.getBoard());
		return move[1];
	}
}
