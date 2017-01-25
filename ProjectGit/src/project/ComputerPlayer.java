package project;

import java.util.Random;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Mark mark) {
		super(name, mark);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] determineMove(Board board) {
		return generateMove(board, this.getMark());
	}

	public int[] generateMove(Board board, Mark m) {
		int[] randomMove = new int[3]
				;
		Random r1 = new Random();
		Random r2 = new Random();
		Random r3 = new Random();
		
		int low = 0;
		int high = board.dim;
		int generateMoveX = r1.nextInt(high - low) + low;
		int generateMoveY = r2.nextInt(high - low) + low;
		int generateMoveZ = r3.nextInt(high - low) + low;
		
		randomMove[0] = generateMoveX;
		randomMove[1] = generateMoveY;
		randomMove[2] = generateMoveZ;
		System.out.println(randomMove[0]);
		return randomMove;
	}
}
