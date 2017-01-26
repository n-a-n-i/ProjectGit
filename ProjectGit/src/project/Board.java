package project;

import java.util.LinkedList;
import java.util.List;

public class Board {

	private Mark[][][] board;
	public int dim;
	public static final int WIN = 4;
	public int lastMoveX;
	public int lastMoveY;
	public int lastMoveZ;
	public Mark lastM = Mark.EMP;
	int[][] winningMoves = new int[4][3];

	/*
	 * @requires dimension > 0; ensures \forall int x,y,z; board[x][y][z].Mark
	 * == Mark.EMP;
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

	public void showBoard() {
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

	/**
	 * Creates a deep copy of this field.
	 */
	/*
	 * @ ensures \result != this; ensures (\forall int i; 0 <= i & i < DIM *
	 * DIM; \result.getField(i) == this.getField(i));
	 * 
	 * @
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
		copy.showBoard();
		return copy;
	}

	// @assert isField(row, col, height)
	public boolean isEmptyField(int row, int col, int height) {
		if (this.getField(row, col, height) == Mark.EMP) {
			return true;
		}
		return false;
	}

	/* @ requires 0 <= row < DIM && 0 <= col < DIM && 0 <= height < DIM */
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
	public Mark getField(int row, int col, int height) {
		assert isField(row, col, height);
		return board[row][col][height];
	}

	/*
	 * @ requires 0 <= row < DIM && 0 <= col < DIM; requires isEmptyField(row,
	 * col, z) != null; ensures 0 <= \result > DIM; requires m == Mark.XXX || m
	 * == Mark.OOO;
	 */
	public void setField(int row, int col, Mark m) {
		int z = firstEmptyField(row, col);
		board[row][col][z] = m;

		lastMoveX = row;
		lastMoveY = col;
		lastMoveZ = z;
		lastM = m;
	}

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
	/* @ pure */ public boolean isWinner() {

		return hasRow() || hasColumn() || hasStack() || has2DDiagonal() || has3DDiagonal();
	}

	// @ensures \result == isWinner();
	/* @pure */ public boolean hasWinner() {
		return isWinner();
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result == true || false;
	public boolean hasRow() {
		boolean hasWin = false;
		boolean hasRow = false;
		int winning = 1;

		if (lastM != Mark.EMP) {
			for (int y = lastMoveY + 1; y < dim; y++) {
				if (getField(lastMoveX, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						for (int i = 0; i < 4; i++) {
						}
						hasRow = true;
						return hasRow;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}
		}
		return hasWin;
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;
	public boolean hasColumn() {
		boolean hasColumn = false;
		int winning = 1;

		if (lastM != Mark.EMP) {
			for (int x = lastMoveX + 1; x < dim; x++) {
				if (getField(x, lastMoveY, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						hasColumn = true;
						return hasColumn;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}
		}
		return hasColumn;
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;
	public boolean hasStack() {
		boolean hasStack = false;
		int winning = 1;

		if (lastM != Mark.EMP) {
			for (int z = lastMoveZ - 1; z >= 0; z--) {
				if (getField(lastMoveX, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						hasStack = true;
						return hasStack;
					}
				} else {
					winning = 1;
					break;
				}
			}
		}
		return hasStack;
	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;
	public boolean has2DDiagonal() {
		int winning = 1;
		if (lastM != Mark.EMP) {
			// -------------- check for diagonal left to right in z ----------
			for (int x = lastMoveX + 1, y = lastMoveY + 1; x < dim && y < dim; x++, y++) {
				if (getField(x, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}
			// -------------- check for diagonal right to left in z ----------
			for (int x = lastMoveX + 1, y = lastMoveY - 1; x < dim && y >= 0; x++, y--) {
				if (getField(x, y, lastMoveZ) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}
			// -------------- check for diagonal left to right in x ----------
			for (int y = lastMoveY + 1, z = lastMoveZ + 1; y < dim && z < dim; y++, z++) {
				if (getField(lastMoveX, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}

			// -------------- check for diagonal left to right in y ----------
			for (int x = lastMoveX + 1, z = lastMoveZ + 1; x < dim && z < dim; x++, z++) {
				if (getField(x, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}

			// -------------- check for diagonal right to left in x ----------
			for (int y = lastMoveY + 1, z = lastMoveZ - 1; y < dim && z >= 0; y++, z--) {
				if (getField(lastMoveX, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}

			// -------------- check for diagonal right to left in y ----------
			for (int x = lastMoveX + 1, z = lastMoveZ - 1; x < dim && z >= 0; x++, z--) {
				if (getField(x, lastMoveY, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}
		}
		return false;

	}

	// @requires \exists board.Mark != EMP;
	// @ensures \result = true || false;
	public boolean has3DDiagonal() {
		int winning = 1;

		if (lastM != Mark.EMP) {
			for (int x = lastMoveX + 1, y = lastMoveY + 1, z = lastMoveZ + 1; x < dim && y < dim
					&& z < dim; x++, y++, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}

			for (int x = lastMoveX + 1, y = lastMoveY - 1, z = lastMoveZ + 1; x < dim && y >= 0
					&& z < dim; x++, y--, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}

			for (int x = lastMoveX - 1, y = lastMoveY + 1, z = lastMoveZ + 1; x >= 0 && y < dim
					&& z < dim; x--, y++, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}

			for (int x = lastMoveX - 1, y = lastMoveY - 1, z = lastMoveZ + 1; x >= 0 && y >= 0
					&& z < dim; x--, y--, z++) {
				if (getField(x, y, z) == lastM) {
					winning++;
					if (winning == WIN) {
						return true;
					}
				} else {
					winning = 1;
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
					winning = 1;
					break;
				}
			}
		}
		return false;
	}

}
