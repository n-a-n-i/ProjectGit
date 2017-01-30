package strategies;

import project.Board;
import project.Mark;

public class RandomStrategy implements ComputerStrategy {
	
public String name = "RandomAI";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int[] determineMove(Board b, Mark m) {
		int[] randomMove = new int[2];
		int generateMoveX = (int) (Math.random() * b.dim);
		int generateMoveY = (int) (Math.random() * b.dim);
		
		randomMove[0] = generateMoveX;
		randomMove[1] = generateMoveY;

		return randomMove;
	}

}
