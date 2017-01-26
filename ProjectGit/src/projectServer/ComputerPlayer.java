package projectServer;

import java.util.Random;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Mark mark) {
		super(name, mark);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] determineMove(Board board) {
		int[] nextMove = new int[2];
		nextMove = randomMove(board);
		boolean validMove = false;
		while (!validMove) {
			if (board.firstEmptyField(nextMove[0], nextMove[1]) == -1) {
				nextMove = randomMove(board);
			} else {
				validMove = true;
			}
		}
		return nextMove;
	}

//	public int[] generateMove(Board board, Mark m) {
//
//		System.out.println(board.firstEmptyField(generateMoveX, generateMoveY));
//		if (board.firstEmptyField(generateMoveX, generateMoveY) == -1) {
//
//		} else {
//			System.out.println("ERROR: field " + generateMoveX + ", " + generateMoveY + " is no valid choice.");
//		}
//		return randomMove;
//	}

	public int[] randomMove(Board board) {
		int[] randomMove = new int[2];

		Random r1 = new Random();
		Random r2 = new Random();

		int low = 0;
		int high = board.dim;
		int generateMoveX = r1.nextInt(high - low) + low;
		int generateMoveY = r2.nextInt(high - low) + low;
		randomMove[0] = generateMoveX;
		randomMove[1] = generateMoveY;

		return randomMove;
	}
}
