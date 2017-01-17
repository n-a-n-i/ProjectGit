package project;

import java.util.Scanner;

/**
 * Class for maintaining a human player in Tic Tac Toe. Module 2 lab assignment
 * 
 * @author Theo Ruys
 * @version $Revision: 1.4 $
 */
public class HumanPlayer extends Player {

    // -- Constructors -----------------------------------------------

    /*@
       requires name != null;
       requires mark == Mark.XX || mark == Mark.OO;
       ensures this.getName() == name;
       ensures this.getMark() == mark;
    */
    /**
     * Creates a new human player object.
     * 
     */
    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
    }

    // -- Commands ---------------------------------------------------

    /*@
       requires board != null;
       ensures board.isField(\result) && board.isEmptyField(\result);

     */
    /**
     * Asks the user to input the field where to place the next mark. This is
     * done using the standard input/output. \
     * 
     * @param board
     *            the game board
     * @return the player's chosen field
     */
    public int[] determineMove(Board board) {
    	boolean valid = false;
    	int[] choices = new int[1];
    	int choiceX = 0;
    	int choiceY = 0;
    	while (!valid) {
    		String promptX = "> " + getName() + " (" + getMark().toString() + ")"
                    + ", which row do you want to chooose? ";
            choiceX = readInt(promptX);
            
            String promptY = "> " + getName() + " (" + getMark().toString() + ")"
                    + ", which column do you want to chooose? ";
            choiceY = readInt(promptY);
            
            valid = board.isField(choiceX, choiceY) && board.isEmptyField(choiceX, choiceY);
            
            System.out.println("ERROR: field " + choiceX + ", " 
                        + " is no valid choice.");
            }
    	choices[0] = choiceX;
    	choices[1] = choiceY;
    	return choices;
    }
    	

    /**
     * Writes a prompt to standard out and tries to read an int value from
     * standard in. This is repeated until an int value is entered.
     * 
     * @param prompt
     *            the question to prompt the user
     * @return the first int value which is entered by the user
     */
    private int readInt(String prompt) {
        int value = 0;
        boolean intRead = false;
        @SuppressWarnings("resource")
        Scanner line = new Scanner(System.in);
        do {
            System.out.print(prompt);
            try (Scanner scannerLine = new Scanner(line.nextLine());) {
                if (scannerLine.hasNextInt()) {
                    intRead = true;
                    value = scannerLine.nextInt();
                }
            }
        } while (!intRead);
        return value;
    }

}
