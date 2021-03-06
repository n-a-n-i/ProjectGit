package project;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Board extends Observable{

/**
 * ConnectFour over a Server project Board
 * 
 * @author Nienke Huitink & Lex Favrin
 * @version 2017.01.26
 */
	private Mark[][][] board;
	public int dim;
	public static final int WIN = 4;
	public int lastMoveX;
	public int lastMoveY;
	public int lastMoveZ;
	public int winning;
	public Mark lastM = Mark.EMP;
	public int[] neighbour = new int[3];
	public List<Integer> neighbourDirections;
	/*
	 * @requires dimension > 0; ensures \forall int x,y,z; board[x][y][z].Mark
	 * == Mark.EMP;
	 */

	/*
	 * @param dimension Creates a board with the dimension as a parameter. The
	 * board will always be a cube. It will be initialized with empty fields
	 */
	public Board(int dimension) {
		board = new Mark[dimension][dimension][dimension];
		this.dim = dimension;

		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				for (int z = 0; z < dimension; z++) {
					board[x][y][z] = Mark.EMP;
				}
			}
		}
	}

	public String toString(){
		//TODO: zorgen dat hij alles print
		StringBuilder boardbuilder = new StringBuilder();
		StringBuilder numbers = new StringBuilder();
		for (int z = 0; z < dim; z++) {
			numbers.append("  |");
			for (int i = 0; i < dim; i++) {
				if (i == dim - 1) {
					numbers.append("  " + i + " ");
				} else {
					numbers.append("  " + i + "  ");
				}
			}
		}
		String number = numbers.toString();
		for (int x = 0; x < dim; x++) {
			System.out.printf("%d | ", x);
			for (int z = 0; z < dim; z++) {
				for (int y = 0; y < dim; y++) {
					boardbuilder.append("%s  ");
					boardbuilder.append(board[x][y][z]).toString();
				}
				boardbuilder.append("| ");
			}
			boardbuilder.append("\n");
			//String board;
//			board.format(%s%n, args)
		}

		StringBuilder layers = new StringBuilder();
		StringBuilder spaces = new StringBuilder();

		layers.append("  |");
		for (int i = 0; i < ((dim * 5) - 10) / 2; i++) {
			spaces.append(" ");
		}
		for (int z = 0; z < dim; z++) {
			if ((dim % 2) == 0) {
				layers.append(spaces + "  layer " + z + spaces + "  |");
			} else {
				layers.append(spaces + "  layer " + z + spaces + "   |");
			}
		}
		String layer = layers.toString();
		
		String string = String.format("%s%n%s%n", number, layer);
		boardbuilder.append(string);
		String boardString = String.format("%s%n%s%n", boardbuilder);
		return boardString;
	}
	
	

	/*
	 * Prints the current status of the board. The layout is initialized as
	 * well.
	 */
	public void showBoard() {
		System.out.println("showing board via Board");
		StringBuilder numbers = new StringBuilder();
		for (int z = 0; z < dim; z++) {
			numbers.append("  |");
			for (int i = 0; i < dim; i++) {
				if (i == dim - 1) {
					numbers.append("  " + i + " ");
				} else {
					numbers.append("  " + i + "  ");
				}
			}
		}
		String number = numbers.toString();
		System.out.println(number);

		for (int x = 0; x < dim; x++) {
			System.out.printf("%d | ", x);
			for (int z = 0; z < dim; z++) {
				for (int y = 0; y < dim; y++) {
					System.out.printf("%s  ", board[x][y][z]);
				}
				System.out.printf("| ");
			}
			System.out.printf("\n");
		}

		StringBuilder layers = new StringBuilder();
		StringBuilder spaces = new StringBuilder();

		layers.append("  |");
		for (int i = 0; i < ((dim * 5) - 10) / 2; i++) {
			spaces.append(" ");
		}
		for (int z = 0; z < dim; z++) {
			if ((dim % 2) == 0) {
				layers.append(spaces + "  layer " + z + spaces + "  |");
			} else {
				layers.append(spaces + "  layer " + z + spaces + "   |");
			}
		}
		String layer = layers.toString();
		System.out.println(layer);

	}

	/*
	 * @ ensures \result != this; ensures (\forall int i; 0 <= i & i < DIM *
	 * DIM; \result.getField(i) == this.getField(i));
	 * 
	 * @
	 */
	/**
	 * Creates a deep copy of this field. This can be used to provide the
	 * computer player with a board to test moves on.
	 */
	public Board deepCopy() {
		Board copy = new Board(dim);
		for (int z = 0; z < dim; z++) {
			for (int x = 0; x < dim; x++) {
				for (int y = 0; y < dim; y++) {
					Mark m = getField(x, y, z);
					copy.setField(x, y, m);
				}
			}
		}
		return copy;
	}

	// @assert isField(row, col, height)
	/*
	 * Checks whether a field is empty or not
	 */
	public boolean isEmptyField(int row, int col, int height) {
		if (this.getField(row, col, height) == Mark.EMP) {
			return true;
		}
		return false;
	}

	/* @ requires 0 <= row < DIM && 0 <= col < DIM && 0 <= height < DIM */

	/*
	 * Checks whether a the coordinates given correspond to a valid field on the
	 * board.
	 */
	/* @ pure */public boolean isField(int row, int col, int height) {
		if (row >= 0 && row < dim && col >= 0 && col < dim && height >= 0 && height < dim) {
			return true;
		}
		return false;
	}

	/*
	 * @ requires 0 <= row < DIM && 0 <= col < DIM; requires isEmptyField(row,
	 * col, z) != null; ensures 0 <= \result > DIM;
	 */

	/*
	 * Checks the first z-coordinate where a piece can be dropped. Returns the
	 * first empty field on (row, col) and the corresponding z.
	 */
	/* @pure */public int firstEmptyField(int row, int col) {
		assert row >= 0 && row < dim;
		assert col >= 0 && col < dim;
		int height = -1;

		for (int z = 0; z < dim; z++) {
			if (isEmptyField(row, col, z)) {
				height = z;
				break;
			} else {
				// System.out.println("There is no empty field in this stack.");
			}
		}
		return height;
	}

	/*
	 * @ requires 0 <= row < DIM && 0 <= col < DIM && 0 <= height < DIM;
	 * requires isField(row, col, height) != null; ensures 0 <= \result > DIM;
	 */

	/*
	 * Returns the current mark on the given field
	 */
	public Mark getField(int row, int col, int height) {
		assert isField(row, col, height);
		return board[row][col][height];
	}

	/*
	 * @ requires 0 <= row < DIM && 0 <= col < DIM; requires isEmptyField(row,
	 * col, z) != null; ensures 0 <= \result < DIM;
	 */

	/*
	 * Changes the mark on the empty field to the mark of the current player, on
	 * the position it gets from that player.
	 */
	public void setField(int row, int col, Mark m) {
		System.out.println("We plaatsen een mark");
		int z = firstEmptyField(row, col);
		board[row][col][z] = m;

		lastMoveX = row;
		lastMoveY = col;
		lastMoveZ = z;
		lastM = m;
	}

	/*
	 * Returns whether the board is full or not.
	 */
	/* @pure */ public boolean isFull() {
		boolean full = false;
		for (int z = 0; z < dim; z++) {
			for (int i = 0; i < dim; i++) {
				for (int j = 0; j < dim; j++) {
					if (!isEmptyField(i, j, z)) {
						full = true;
					} else {
						full = false;
						break;
					}
				}
			}
		}
		return full;
	}

	// @ ensures (\forall int x,y,z; board[x][y][z].Mark == Mark.EMP);

	/*
	 * Resets the board to an empty one.
	 */

	public void reset() {
		for (int x = 0; x < dim; x++) {
			for (int y = 0; y < dim; y++) {
				for (int z = 0; z < dim; z++) {
					board[x][y][z] = Mark.EMP;
				}
			}
		}
	}

	/*
	 * @ensures \result == this.hasRow() || this.hasColumn() || this.hasStack()
	 * || this.has2DDiagonal() || this.has3DDiagonal;
	 */

	/*
	 * Returns true if one of the win conditions have been met
	 */
	/* @ pure */ public boolean isWinner() {

		return hasRow() || hasColumn() || hasStack() || has2DDiagonal() || has3DDiagonal();
	}

	// @ensures \result == isWinner();

	/*
	 * Checks whether the game has a winner. Returns true if there is a winner.
	 */
	/* @pure */ public boolean hasWinner() {
		return isWinner();
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result == true || false;

	/*
	 * Checks whether a player has won by filling a row. Returns true if 4 marks
	 * of a player have been found next to each other in a row.
	 */
	public boolean hasRow() {
		boolean hasWin = false;
		boolean hasRow = false;
		winning = 1;
		if (lastM != Mark.EMP) {
			for (int y = lastMoveY + 1; y < dim; y++) {
				if (getField(lastMoveX, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						hasRow = true;
						return hasRow;
					}
				} else {
					break;
				}
			}
			for (int y = lastMoveY - 1; y >= 0; y--) {
				if (getField(lastMoveX, y, lastMoveZ) == lastM) {
					winning++;

					if (winning == WIN) {
						hasRow = true;
						return hasRow;
					}
				} else {
					break;
				}
			}
		}
		return hasWin;
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;

	/*
	 * Checks whether a player has won by filling a column. Returns true if 4
	 * marks of a player have been found next to each other in a column.
	 */
	public boolean hasColumn() {
		boolean hasColumn = false;
		winning = 1;

		if (lastM != Mark.EMP) {
			for (int x = lastMoveX + 1; x < dim; x++) {
				if (getField(x, lastMoveY, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						hasColumn = true;
						return hasColumn;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX - 1; x >= 0; x--) {
				if (getField(x, lastMoveY, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						hasColumn = true;
						return hasColumn;
					}
				} else {
					break;
				}
			}
		}
		return hasColumn;
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;

	/*
	 * Checks whether a player has won by filling a stack. Returns true if 4
	 * marks of a player have been found next to each other in a stack.
	 */
	public boolean hasStack() {
		boolean hasStack = false;
		winning = 1;

		if (lastM != Mark.EMP) {
			for (int z = lastMoveZ - 1; z >= 0; z--) {
				if (getField(lastMoveX, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						hasStack = true;
						return hasStack;
					}
				} else {
					break;
				}
			}
		}
		return hasStack;
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;

	/*
	 * Checks whether a player has won by filling a diagonal in the 2D plane.
	 * Returns true if 4 marks of a player have been found next to each other in
	 * a 2D diagonal.
	 */
	public boolean has2DDiagonal() {
		winning = 1;
		if (lastM != Mark.EMP) {
			// -------------- check for diagonal left to right in z ----------
			for (int x = lastMoveX + 1, y = lastMoveY + 1; x < dim && y < dim; x++, y++) {
				if (getField(x, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX - 1, y = lastMoveY - 1; x >= 0 && y >= 0; x--, y--) {
				if (getField(x, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			// -------------- check for diagonal right to left in z ----------
			for (int x = lastMoveX + 1, y = lastMoveY - 1; x < dim && y >= 0; x++, y--) {
				if (getField(x, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX - 1, y = lastMoveY + 1; x >= 0 && y < dim; x--, y++) {
				if (getField(x, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			// -------------- check for diagonal right to left in x ----------
			for (int y = lastMoveY + 1, z = lastMoveZ - 1; y < dim && z >= 0; y++, z--) {
				if (getField(lastMoveX, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int y = lastMoveY - 1, z = lastMoveZ + 1; y >= 0 && z < dim; y--, z++) {
				if (getField(lastMoveX, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			// -------------- check for diagonal left to right in x ----------
			for (int y = lastMoveY + 1, z = lastMoveZ + 1; y < dim && z < dim; y++, z++) {
				if (getField(lastMoveX, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int y = lastMoveY - 1, z = lastMoveZ - 1; y >= 0 && z >= 0; y--, z--) {
				if (getField(lastMoveX, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			// -------------- check for diagonal left to right in y ----------
			for (int x = lastMoveX + 1, z = lastMoveZ + 1; x < dim && z < dim; x++, z++) {
				if (getField(x, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX - 1, z = lastMoveZ - 1; x >= 0 && z >= 0; x--, z--) {
				if (getField(x, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			// -------------- check for diagonal right to left in y ----------
			for (int x = lastMoveX + 1, z = lastMoveZ - 1; x < dim && z >= 0; x++, z--) {
				if (getField(x, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX - 1, z = lastMoveZ + 1; x >= 0 && z < dim; x--, z++) {
				if (getField(x, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
		}
		winning = 1;
		return false;

	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;

	/*
	 * Checks whether a player has won by filling a diagonal in the 3
	 * dimensional plane. Returns true if 4 marks of a player have been found
	 * next to each other in a 3D diagonal.
	 */
	public boolean has3DDiagonal() {
		winning = 1;

		if (lastM != Mark.EMP) {
			for (int x = lastMoveX + 1, y = lastMoveY + 1, z = lastMoveZ + 1; x < dim && y < dim
					&& z < dim; x++, y++, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX - 1, y = lastMoveY - 1, z = lastMoveZ - 1; x >= 0 && y >= 0
					&& z >= 0; x--, y--, z--) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			for (int x = lastMoveX + 1, y = lastMoveY - 1, z = lastMoveZ + 1; x < dim && y >= 0
					&& z < dim; x++, y--, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX - 1, y = lastMoveY + 1, z = lastMoveZ - 1; x >= 0 && y < dim
					&& z >= 0; x--, y++, z--) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			for (int x = lastMoveX - 1, y = lastMoveY + 1, z = lastMoveZ + 1; x >= 0 && y < dim
					&& z < dim; x--, y++, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX + 1, y = lastMoveY - 1, z = lastMoveZ - 1; x < dim && y >= 0
					&& z >= 0; x++, y--, z--) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			winning = 1;
			for (int x = lastMoveX - 1, y = lastMoveY - 1, z = lastMoveZ + 1; x >= 0 && y >= 0
					&& z < dim; x--, y--, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
			for (int x = lastMoveX + 1, y = lastMoveY + 1, z = lastMoveZ - 1; x < dim && y < dim
					&& z >= 0; x++, y++, z--) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					break;
				}
			}
		}
		return false;
	}

	public boolean hasNeighbour(int x, int y, int z, Mark m) {
		neighbourDirections = new LinkedList<Integer>();
		if (isField(x + 1, y, z) && getField(x + 1, y, z) == m) {
			neighbourDirections.add(1);
		} if (isField(x, y + 1, z) && getField(x, y + 1, z) == m) {
			neighbourDirections.add(2);
		} if (isField(x, y, z + 1) && getField(x, y, z + 1) == m) {
			neighbourDirections.add(3);
		} if (isField(x + 1, y + 1, z) && getField(x + 1, y + 1, z) == m) {
			neighbourDirections.add(4);
		} if (isField(x, y + 1, z + 1) && getField(x, y + 1, z + 1) == m) {
			neighbourDirections.add(5);
		} if (isField(x + 1, y, z + 1) && getField(x + 1, y, z + 1) == m) {
			neighbourDirections.add(6);
		} if (isField(x + 1, y + 1, z + 1) && getField(x + 1, y + 1, z + 1) == m) {
			neighbourDirections.add(7);
		} if (isField(x - 1, y, z) && getField(x - 1, y, z) == m) {
			neighbourDirections.add(8);
		} if (isField(x, y - 1, z) && getField(x, y - 1, z) == m) {
			neighbourDirections.add(9);
		} if (isField(x, y, z - 1) && getField(x, y, z - 1) == m) {
			neighbourDirections.add(10);
		} if (isField(x - 1, y - 1, z) && getField(x - 1, y - 1, z) == m) {
			neighbourDirections.add(11);
		} if (isField(x, y - 1, z - 1) && getField(x, y - 1, z - 1) == m) {
			neighbourDirections.add(12);
		} if (isField(x - 1, y, z - 1) && getField(x - 1, y, z - 1) == m) {
			neighbourDirections.add(13);
		} if (isField(x - 1, y - 1, z - 1) && getField(x - 1, y - 1, z - 1) == m) {
			neighbourDirections.add(14);
		} if (isField(x - 1, y + 1, z + 1) && getField(x - 1, y + 1, z + 1) == m) {
			neighbourDirections.add(15);
		} if (isField(x + 1, y - 1, z + 1) && getField(x + 1, y - 1, z + 1) == m) {
			neighbourDirections.add(16);
		} if (isField(x + 1, y + 1, z - 1) && getField(x + 1, y + 1, z - 1) == m) {
			neighbourDirections.add(17);
		} if (isField(x - 1, y - 1, z + 1) && getField(x - 1, y - 1, z + 1) == m) {
			neighbourDirections.add(18);
		} if (isField(x - 1, y + 1, z - 1) && getField(x - 1, y + 1, z - 1) == m) {
			neighbourDirections.add(19);
		} if (isField(x + 1, y - 1, z - 1) && getField(x + 1, y - 1, z - 1) == m) {
			neighbourDirections.add(20);
		}
		
		if(neighbourDirections.size() > 0){
			System.out.println("Lijst size is: " + neighbourDirections.size());

			return true;
		}
		return false;
	}
}
