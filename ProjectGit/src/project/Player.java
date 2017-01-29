package project;

public abstract class Player {

	// -- Instance variables -----------------------------------------

	protected String name;
	protected Mark mark;

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires name != null; requires mark == Mark.XX || mark== Mark.OO;
	 * ensures this.getName() == name; ensures this.getMark() == mark;
	 */
	/**
	 * Creates a new Player object.
	 * 
	 */
	public Player(String name, Mark mark) {
		this.name = name;
		this.mark = mark;
	}

	// -- Queries ----------------------------------------------------

	/**
	 * Returns the name of the player.
	 */
	/* @ pure */ public String getName() {
		return this.name;
	}

//	/**
//	 * Returns the mark of the player.
//	 */
//	// @ensures mark == Mark.OOO || mark == Mark.XXX || mark == Mark.EMP;
//	/* @ pure */ public Mark getMark() {
//		return mark;
//	}

	/*
	 * @ requires board != null & !board.isFull(); ensures
	 * board.isField(\result) & board.isEmptyField(\result);
	 * 
	 */
	/**
	 * Determines the field for the next move.
	 * 
	 * @param board
	 *            the current game board
	 * @return the player's choice
	 */
	public abstract int getMoveX(Game game, Mark m);

	public abstract int getMoveY(Game game, Mark m);

	public abstract Mark getMark();

}
