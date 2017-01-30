package project;

public class playerMove {

	public int[] determineMove(Board board) {
		boolean valid = false;
		int[] choices = new int[2];
		int choiceX = 0;
		int choiceY = 0;
		while (!valid) {
			String promptX = "> " + getName() + " (" + getMark().toString() + ")"
					+ ", which row do you want to choose? ";
			choiceX = readInt(promptX);

			String promptY = "> " + this.getName() + " (" + getMark().toString() + ")"
					+ ", which column do you want to choose? ";
			choiceY = readInt(promptY);

			valid = board.isEmptyField(choiceX, choiceY, board.firstEmptyField(choiceX, choiceY));

			if (valid) {
				choices[0] = choiceX;
				choices[1] = choiceY;
				break;
			} else {
				System.out.println("ERROR: field " + choiceX + ", " + choiceY + " is no valid choice.");
			}
		}
		return choices;
	}

}
