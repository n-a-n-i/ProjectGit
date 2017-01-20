package project;

import java.util.Arrays;

public class OldBoard {
	
	
    public int DIM = 5;
    
    private static final String[] NUMBERING = {" /--+---+---",
        " 3 | 4 | 5 ", "---+---+---", " 6 | 7 | 8 "};
    private static final String LINE = NUMBERING[1];
    private static final String DELIM = "     ";

    
   // String[] line = new String[DIM];
    
    public void setupTUI(){
    	StringBuilder lines = new StringBuilder();
    	    	
    	lines.append("|---|");
    	for (int i = 1; i < (DIM); i++){
    		lines.append("---|");
    	}
    	String line = lines.toString();
    	System.out.println(line);
    	
    	
    	StringBuilder layerLines = new StringBuilder();
    	layerLines.append("|---|");
    	
    	for (int i = 1; i < (DIM); i++){
    		int middle = DIM / 2; 
    		if(i == middle){
    			layerLines.append("new layer-|");
    		} else if (i == middle +1){
    			
    		} else if (i == middle -1) {
    			layerLines.append("-");
    		} else{
    			layerLines.append("---|");
    		}
    	}
    	String layerLine = layerLines.toString();
    	System.out.println(layerLine);
    }
    
    
    
    
    /**
     * The DIM by DIM fields of the Tic Tac Toe student. See NUMBERING for the
     * coding of the fields.
     */
  
    //@ private invariant fields.length == DIM*DIM;
    /*@ invariant (\forall int i; 0 <= i & i < DIM*DIM;
        getField(i) == Mark.EMPTY || getField(i) == Mark.XX || getField(i) == Mark.OO); */
    private Mark[] fields;

    private String[] numbering;
    
    
    
    // -- Constructors -----------------------------------------------

    /**
     * Creates an empty student.
     */
    //@ ensures (\forall int i; 0 <= i & i < DIM * DIM; this.getField(i) == Mark.EMPTY);
    public OldBoard() {
    			
    	fields = new Mark[DIM * DIM * DIM];
    	
    	for (int i = 0; i < DIM*DIM*DIM; i++){
    		this.setField(i, Mark.EMPTY);
    	}
    	
    	
    	
    	numbering = new String[DIM-1];
    	
//    	for (int x = 0; x < DIM*DIM)
//    		for (int y = 0; y < DIM*DIM)
//    	numbering[0] = ""
//    	
//    	private String[] numbers = new String[DIM - 1];
//    	
//    	
    }
    
    
    /**
     * Creates a deep copy of this field.
     */
    /*@ ensures \result != this;
        ensures (\forall int i; 0 <= i & i < DIM * DIM;
                                \result.getField(i) == this.getField(i));
      @*/
    public OldBoard deepCopy() {
    	OldBoard copy = new OldBoard();
    	for (int i = 0; i < DIM * DIM * DIM; i++) {
    		copy.setField(i, this.getField(i));
    	}
        return copy;
    }

    
    /**
     * Calculates the index in the linear array of fields from a (row, col)
     * pair.
     * @return the index belonging to the (row,col)-field
     */
    //@ requires 0 <= row & row < DIM;
    //@ requires 0 <= col & col < DIM;
    /*@pure*/
    public int index(int row, int col, int hight) {    	
    	assert (row >= 0 && row < DIM);
    	assert (col >= 0 && col < DIM);
    	assert (hight >= 0 && hight < DIM);

        int index = row * DIM + col + (hight * DIM * DIM); 
        return index;
    }

    public boolean isValidIndex(int index) {
    	if (0 <= index && index < DIM * DIM * DIM){
    		return true;
    	}
        return false;
    }
    
    
    public int firstEmptyIndex(int row, int col){
    	assert (row >= 0 && row < DIM);
    	assert (col >= 0 && col < DIM);
    	
    	int index = row * DIM + col;
    	
    	for (int x = 0; x < DIM * DIM * DIM;){
    		if (isEmptyField(index)){
    			break;
    		} else {
    			x = x + (DIM * DIM);
    		}
    	}
    	
    	return index;
    }
    
    
    
    /**
     * Returns true if ix is a valid index of a field on the student.
     * @return true if 0 <= index < DIM*DIM
     */
    //@ ensures \result == (0 <= index && index < DIM * DIM);
    /*@pure*/
    public boolean isField(int index) {
    	if(isValidIndex(index)){
    		return true;
    	}
    	return false;
    }

    /**
     * Returns true of the (row,col) pair refers to a valid field on the student.
     *
     * @return true if 0 <= row < DIM && 0 <= col < DIM
     */
    //@ ensures \result == (0 <= row && row < DIM && 0 <= col && col < DIM);
    /*@pure*/
    public boolean isField(int row, int col, int hight) {
    	if (isValidIndex(index(row, col, hight))){
    		return true;
    	}
        return false;
    }
    
    /**
     * Returns the content of the field i.
     *
     * @param i
     *            the number of the field (see NUMBERING)
     * @return the mark on the field
     */
    //@ requires this.isField(i);
    //@ ensures \result == Mark.EMPTY || \result == Mark.XX || \result == Mark.OO;
    /*@pure*/
    public Mark getField(int i) {
    	
    	assert (isField(i));
    	
    	return fields[i];
    }

    /**
     * Returns the content of the field referred to by the (row,col) pair.
     *
     * @param row
     *            the row of the field
     * @param col
     *            the column of the field
     * @return the mark on the field
     */
    //@ requires this.isField(row,col);
    //@ ensures \result == Mark.EMPTY || \result == Mark.XX || \result == Mark.OO;
    /*@pure*/
    public Mark getField(int row, int col, int hight) {
    	assert (isField(index(row, col, hight)));
    	
    	return fields[index(row,col,hight)];
    }

    /**
     * Returns true if the field i is empty.
     *
     * @param i
     *            the index of the field (see NUMBERING)
     * @return true if the field is empty
     */
    //@ requires this.isField(i);
    //@ ensures \result == (this.getField(i) == Mark.EMPTY);
    /*@pure*/
    public boolean isEmptyField(int i) {
    	if (isField(i)){
    		return (this.getField(i) == Mark.EMPTY);
    	}
        return false;
    }

    /**
     * Returns true if the field referred to by the (row,col) pair it empty.
     *
     * @param row
     *            the row of the field
     * @param col
     *            the column of the field
     * @return true if the field is empty
     */
    //@ requires this.isField(row,col);
    //@ ensures \result == (this.getField(row,col) == Mark.EMPTY);
    /*@pure*/
    public boolean isEmptyField(int row, int col, int hight) {
    	if (isField(row, col, hight)){
    		return (this.getField(index(row, col, hight)) == Mark.EMPTY);
    	}
    	return false;
    }

    /**
     * Tests if the whole student is full.
     *
     * @return true if all fields are occupied
     */
    //@ ensures \result == (\forall int i; i <= 0 & i < DIM * DIM; this.getField(i) != Mark.EMPTY);
    /*@pure*/
    public boolean isFull() {
    	boolean full = true;
    	for (int i = 0; i < DIM * DIM * DIM; i++) {
    		if (isEmptyField(i)) {
    			full = false;
    			break;
    		}
    	}
        return full;
    }

    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or the whole student is full.
     *
     * @return true if the game is over
     */
    //@ ensures \result == this.isFull() || this.hasWinner();
    /*@pure*/
    public boolean gameOver() {
    	return isFull() || hasWinner();
    }

    /**
     * Checks whether there is a row which is full and only contains the mark
     * m.
     *
     * @param m
     *            the mark of interest
     * @return true if there is a row controlled by m
     */
    /*@ pure */
    public boolean hasRow(Mark m) {
    	boolean hasRow = false;
   
    	for (int z = 0; z < DIM; z++){
    		
    		for (int i = 0; i < DIM; i++) {
        		
    			int column = 0;
    			for (int j = 0; j < DIM; j++) {
        			if (getField(i, j, z) == m) column++;
        		}
        		if (column == DIM) {
        			hasRow = true;
        			break;
        		}
    		}
    	}
        return hasRow;
    }

    
    /**
     * Checks whether there is a column which is full and only contains the mark
     * m.
     *
     * @param m
     *            the mark of interest
     * @return true if there is a column controlled by m
     */
    /*@ pure */
    public boolean hasColumn(Mark m) {
    	boolean hasCol = false;
    	for (int z = 0; z < DIM; z++){
    		for (int i = 0; i < DIM; i++) {
        		int c = 0;
        		for (int j = 0; j < DIM; j++) {
        			if (getField(j, i, z) == m) c++;
        		}
        		if (c == DIM) {
        			hasCol = true;
        			break;
        		}
    		}
    	}
        return hasCol;
    }
    
    public boolean hasStack(Mark m) {
    	boolean hasStack = false;
    	for (int base = 0; base < DIM*DIM; base++){
    		int stack = 0;
    		for (int z = 0; z <DIM; z++){
    			if (getField(((base + (z*DIM))))== m) stack++;
    		}
    		if(stack == DIM){
    			hasStack = true;
    			break;
    		}
    	}
    	return hasStack;
    }

    /**
     * Checks whether there is a diagonal which is full and only contains the
     * mark m.
     *
     * @param m
     *            the mark of interest
     * @return true if there is a diagonal controlled by m
     */
    /*@ pure */
    public boolean hasDiagonal(Mark m) {
    	boolean hasDiagonalLeft = true;
    	boolean hasDiagonalRight = true;
    	
    	for (int z = 0; z < DIM; z++){
    		for (int i = 0 ; i < DIM ; i ++){
        		if (getField((DIM + 1)*i) != m){
        			hasDiagonalLeft = false; 
        			break;
        		}
        	}
        	
        	for (int i = 0 ; i < DIM ; i ++){
        		if (getField((DIM - 1)*(i+1)) != m){
        			hasDiagonalRight = false;
        			break;
        		}
        	}
    	}
        return hasDiagonalLeft || hasDiagonalRight;
    }

    
    
    
    
    
    /**
     * Checks if the mark m has won. A mark wins if it controls at
     * least one row, column or diagonal.
     *
     * @param m
     *            the mark of interest
     * @return true if the mark has won
     */
    //@requires m == Mark.XX | m == Mark.OO;
    //@ ensures \result == this.hasRow(m) || this.hasColumn(m) | this.hasDiagonal(m);
    /*@ pure */
    public boolean isWinner(Mark m) {
    	assert ( m == Mark.XXX || m == Mark.OOO);
    	
    	return hasRow(m) || hasColumn(m) || hasDiagonal(m) || hasStack(m);
    }

    /**
     * Returns true if the game has a winner. This is the case when one of the
     * marks controls at least one row, column or diagonal.
     *
     * @return true if the student has a winner.
     */
    //@ ensures \result == isWinner(Mark.XX) | \result == isWinner(Mark.OO);
    /*@pure*/
    public boolean hasWinner() {
    	return isWinner(Mark.XXX) || isWinner(Mark.OOO);
    }

//    /**
//     * Returns a String representation of this student. In addition to the current
//     * situation, the String also shows the numbering of the fields.
//     *
//     * @return the game situation as String
//     */
//    public String toString() {
//        String s = "";
//        for (int i = 0; i < DIM; i++) {
//            String row = "";
//            for (int j = 0; j < DIM; j++) {
//                row = row + " " + getField(i, j, z).toString() + " ";
//                if (j < DIM - 1) {
//                    row = row + "|";
//                }
//            }
//            s = s + row + DELIM + NUMBERING[i * 2];
//            if (i < DIM - 1) {
//                s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
//            }
//        }
//        return s;
//    }

    /**
     * Empties all fields of this student (i.e., let them refer to the value
     * Mark.EMPTY).
     */
    /*@ ensures (\forall int i; 0 <= i & i < DIM * DIM;
                                this.getField(i) == Mark.EMPTY); @*/
    public void reset() {
    	for (int i = 0 ; i < DIM * DIM * DIM ; i++){
    		setField(i, Mark.EMPTY);
    	}
    }

    /**
     * Sets the content of field i to the mark m.
     *
     * @param i
     *            the field number (see NUMBERING)
     * @param m
     *            the mark to be placed
     */
    //@ requires this.isField(i);
    //@ ensures this.getField(i) == m;
    public void setField(int i, Mark m) {
    	for (int x = i; x < DIM * DIM * DIM;){
    		if (isEmptyField(x)){
    			fields[x] = m;
    			break;
    		} else {
    			x = x + (DIM*DIM);
    		}
    	}
    }

    /**
     * Sets the content of the field represented by the (row,col) pair to the
     * mark m.
     *
     * @param row
     *            the field's row
     * @param col
     *            the field's column
     * @param m
     *            the mark to be placed
     */
    //@ requires this.isField(row,col);
    //@ ensures this.getField(row,col) == m;
    public void setField(int row, int col, Mark m) {
    	fields[firstEmptyIndex(row, col)] = m;    	
    }
    
}
