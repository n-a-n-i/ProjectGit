package project;

public abstract class Player {

    // -- Instance variables -----------------------------------------

    protected String name;
    private Mark mark;

    // -- Constructors -----------------------------------------------

    /*@
       requires name != null;
       requires mark == Mark.XX || mark== Mark.OO;
       ensures this.getName() == name;
       ensures this.getMark() == mark;
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
    /*@ pure */ public String getName() {
    	return name;
    }

    /**
     * Returns the mark of the player.
     */
    /*@ pure */ public Mark getMark() {
        return mark;
    }

    /*@
       requires board != null & !board.isFull();
       ensures board.isField(\result) & board.isEmptyField(\result);

     */
    /**
     * Determines the field for the next move.
     * 
     * @param board
     *            the current game board
     * @return the player's choice
     */
    public abstract int[] determineMove(Board board);

    // -- Commands ---------------------------------------------------

    /*@
       requires board != null & !board.isFull();
     */
    /**
     * Makes a move on the board. <br>
     * 
     * @param board
     *            the current board
     */
    public void makeMove(Board board) {
    	int[] move = determineMove(board);
        int choiceX = move[0];
        int choiceY = move[1];
        board.setField(choiceX, choiceY, getMark());
        
    }

}
