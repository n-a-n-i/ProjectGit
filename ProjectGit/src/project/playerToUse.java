package project;

public class playerToUse {

	protected String name;
	private Mark mark;

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires name != null; requires mark == Mark.XX || mark== Mark.OO;
	 * ensures this.getName() == name; ensures this.getMark() == mark;
	 */
	/**
	 * Creates a new Player object.
	 * 
	 */
	public playerToUse (String name, Mark mark) {
		this.name = name;
		this.mark = mark;
	}

	// -- Queries ----------------------------------------------------

	/**
	 * Returns the name of the player.
	 */
	/* @ pure */ public String getName() {
		return name;
	}

	/**
	 * Returns the mark of the player.
	 */
	// @ensures mark == Mark.OOO || mark == Mark.XXX || mark == Mark.EMP;
	/* @ pure */ public Mark getMark() {
		return mark;
	}


}
