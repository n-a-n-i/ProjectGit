package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import server.Client;

/**
 * Class for maintaining a human player in Tic Tac Toe. Module 2 lab assignment
 * 
 * @author Theo Ruys, with adaptions from Nienke Huitink & Lex Favrin
 * @version $Revision: 1.4 $
 */
public class HumanPlayer extends Player {
	
	private Client client;

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires name != null; requires mark == Mark.XXX || mark == Mark.OOO;
	 * ensures this.getName() == name; ensures this.getMark() == mark;
	 */
	/**
	 * Creates a new human player object.
	 * 
	 */
	public HumanPlayer(String name, Mark mark, Client client) {
		super(name, mark);
		this.client = client;
	}
	// -- Commands ---------------------------------------------------

	/*
	 * @ requires board != null; ensures board.isField(\result) &&
	 * board.isEmptyField(\result); ensures choices.size() == 2;
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
		int[] choices = new int[2];
		int choiceX = 0;
		int choiceY = 0;
		
//		BufferedReader inMove = new BufferedReader(new InputStreamReader(System.in));
		
		while (!valid) {	
			String promptX = "> " + getName() + " (" + getMark().toString() + ")"
					+ ", which row do you want to choose? ";
			client.sendMessage(promptX);
			
			
			String promptY = "> " + this.getName() + " (" + getMark().toString() + ")"
					+ ", which column do you want to choose? ";
			client.sendMessage(promptY);
			
			valid = board.isEmptyField(choiceX, choiceY, board.firstEmptyField(choiceX, choiceY));
			
			if(!valid){
				System.out.println("Error: field " + choiceX + " " + choiceY + " is not a valid choice");
			} else {
				choices[0] = choiceX;
				choices[1] = choiceY;
				break;
			}
			//choiceY = readInt(promptY);
			
		/*	if (!(0 <= choiceX && choiceX < board.dim) || !(0 <= choiceY && choiceY < board.dim) ||
					board.firstEmptyField(choiceX, choiceY) == -1) {
				System.out.println("ERROR: field " + choiceX + ", " + choiceY + " is no valid choice.");
			} else {
				choices[0] = choiceX;
				choices[1] = choiceY;
				valid = true;
			}*/
			
//			try {
//				String promptX = "> " + getName() + " (" + getMark().toString() + ")"
//						+ ", which row do you want to choose? ";
//				System.out.println(promptX);
//				choiceX = inMove.read();
//
//				String promptY = "> " + this.getName() + " (" + getMark().toString() + ")"
//						+ ", which column do you want to choose? ";
//				System.out.println(promptY);
//				choiceY = inMove.read();
////				choiceY = in.nextInt();
////				choiceY = readInt(promptY);
//
//				valid = board.isEmptyField(choiceX, choiceY, board.firstEmptyField(choiceX, choiceY));
//
//				if (valid) {
//					choices[0] = choiceX;
//					choices[1] = choiceY;
//					break;
//				}
//			} catch (IOException e) {
//				System.out.println("Could not read move.");
//			}
		}
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
	/*
	 * @ requires prompt != null; ensures \result >= 0; ensures \result <
	 * board.dim;
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

	@Override
	public int getMoveX(Game game, Mark m) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMoveY(Game game, Mark m) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Mark getMark() {
		return mark;
	}

}
