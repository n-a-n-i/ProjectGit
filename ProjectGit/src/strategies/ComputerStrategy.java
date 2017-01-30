package strategies;

import project.Board;
import project.Mark;

public interface ComputerStrategy {

	public String getName();

	public int[] determineMove(Board b, Mark m);
}
