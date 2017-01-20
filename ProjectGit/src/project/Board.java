package project;

public class Board {

	private Mark[][][] board;
	public int DIM;
	public int WIN = 4;
	public int lastMoveX;
	public int lastMoveY;
	public int lastMoveZ;
	public Mark lastM = Mark.EMP;

	public Board(int dimension) {
		board = new Mark[dimension][dimension][dimension];
		DIM = dimension;

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
		for (int z = 0; z < DIM; z++) {
			numbers.append("  |");
			for (int i = 0; i < DIM; i++) {
				if (i == DIM - 1) {
					numbers.append("  " + i + " ");
				} else {
					numbers.append("  " + i + "  ");
				}
			}
		}
		String number = numbers.toString();
		System.out.println(number);

		for (int x = 0; x < DIM; x++) {
			System.out.printf("%d | ", x);
			for (int z = 0; z < DIM; z++) {
				for (int y = 0; y < DIM; y++) {
					System.out.printf("%s  ", board[x][y][z]);
				}
				System.out.printf("| ");
			}
			System.out.printf("\n");
		}

		StringBuilder layers = new StringBuilder();
		StringBuilder spaces = new StringBuilder();

		layers.append("  |");
		for (int i = 0; i < ((DIM * 5) - 10) / 2; i++) {
			spaces.append(" ");
		}
		for (int z = 0; z < DIM; z++) {
			if ((DIM % 2) == 0) {
				layers.append(spaces + "  layer " + z + spaces + "  |");
			} else {
				layers.append(spaces + "  layer " + z + spaces + "   |");
			}
		}
		String layer = layers.toString();
		System.out.println(layer);

	}

	// public int index(int row, int col, int height) {
	// assert (row >= 0 && row < DIM);
	// assert (col >= 0 && col < DIM);
	// assert (height >= 0 && height < DIM);
	//
	// int index = row * DIM + col + (height * DIM * DIM);
	// return index;
	// }

	// @assert isField(row, col, height)
	public boolean isEmptyField(int row, int col, int height) {
		if (this.getField(row, col, height) == Mark.EMP) {
			return true;
		}
		return false;
	}

	//// @assert isField(row, col)
	// public boolean isEmptyField(int row, int col) {
	// if (this.getField(row, col, firstEmptyField(row, col)) == Mark.EMP){
	// return true;
	// }
	// return false;
	// }

	public boolean isField(int row, int col, int height) {
		if (row >= 0 && row < DIM && col >= 0 && col < DIM && height >= 0 && height < DIM) {
			return true;
		}
		return false;
	}

	// public boolean isField(int row, int col){
	// if(isField(row, col, firstEmptyField(row, col))){
	// return true;
	// } else {
	// return false;
	// }
	// }

	public int firstEmptyField(int row, int col) {
		assert (row >= 0 && row < DIM);
		assert (col >= 0 && col < DIM);
		int height = -1;

		try {
			for (int z = 0; z < DIM; z++) {
				if (isEmptyField(row, col, z)) {
					height = z;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("There is no empty field in this stack.");
		}
		return height;
	}

	public Mark getField(int row, int col, int height) {
		assert (isField(row, col, height));
		return board[row][col][height];
	}

	public void setField(int row, int col, Mark m) {
		int z = firstEmptyField(row, col);
		board[row][col][z] = m;

		lastMoveX = row;
		lastMoveY = col;
		lastMoveZ = z;
		lastM = m;
	}

	// @ ensures \result == (\forall int i; i <= 0 & i < DIM * DIM;
	// this.getField(i) != Mark.EMPTY);
	/* @pure */
	public boolean isFull() {
		boolean full = false;
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				for (int z = 0; z < DIM; z++) {
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

	public void reset() {
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				for (int z = 0; z < DIM; z++) {
					board[x][y][z] = Mark.EMP;
				}
			}
		}
	}
	
	// @requires m == Mark.XX | m == Mark.OO;
	// @ ensures \result == this.hasRow(m) || this.hasColumn(m) |
	// this.hasDiagonal(m);
	/* @ pure */
	public boolean isWinner() {
		return hasRow() || hasColumn() || hasStack() || has2DDiagonal() || has3DDiagonal();
	}

	// @ ensures \result == isWinner(Mark.XX) | \result == isWinner(Mark.OO);
	/* @pure */
	public boolean hasWinner() {
		return isWinner() || isWinner();
	}

	
	
	
	
	public boolean hasRow(){
		boolean hasWin = false;
		boolean hasRow = false;
		int winning = 1;
		if(lastM != Mark.EMP){
			for (int y = (lastMoveY + 1); y < DIM; y++) {
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
			for (int y = (lastMoveY - 1); y >= 0; y--) {
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
	
	public boolean hasColumn(){
		boolean hasColumn = false;
		int winning = 1;

		if(lastM != Mark.EMP){
			for (int x = (lastMoveX + 1); x < DIM; x++) {
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
			for (int x = (lastMoveX - 1); x >= 0; x--) {
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
		
	public boolean hasStack(){
		boolean hasStack = false;
		int winning = 1;
		
		if(lastM != Mark.EMP){
			for(int z = (lastMoveZ - 1); z >= 0 ; z--) {
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
	
		
	public boolean has2DDiagonal(){
		boolean has2DDiagonal = false;
		int winning = 1;
		
		if(lastM != Mark.EMP){
			//-------------- check for diagonal left to right in z ----------
			for (int x = (lastMoveX + 1), y = (lastMoveY + 1); x < DIM && y < DIM; x++, y++){
				if(getField(x, y, lastMoveZ) == lastM){
					winning++;
					if(winning == WIN){
						return has2DDiagonal = true;
					}
				} else {
					break;
				}
			}
			for(int x = (lastMoveX - 1), y = (lastMoveY - 1); x >= 0 && y >= 0; x--, y--){
				if(getField(x, y, lastMoveZ) == lastM){
					winning++;
					if(winning == WIN){
						return has2DDiagonal = true;
					} 
				} else {
					winning = 1;
					break;
				}
			}
			

			//-------------- check for diagonal right to left in z ----------
			for (int x = (lastMoveX + 1), y = (lastMoveY - 1); x < DIM && y >= 0; x++, y--){
				System.out.println("test");
				if(getField(x, y, lastMoveZ) == lastM){
					winning++;
					if(winning == WIN){
						has2DDiagonal = true;
						return has2DDiagonal;
					}
				} else {
					break;
				}
			}
			for(int x = (lastMoveX - 1), y = (lastMoveY + 1); x >= 0 && y < DIM; x--, y++){
				if(getField(x, y, lastMoveZ) == lastM){
					winning++;
					if(winning == WIN){
						return has2DDiagonal = true;
					} 
				} else {
					winning = 1;
					break;
				}
			}
			

			//-------------- check for diagonal left to right in x ----------
			for (int y = (lastMoveY + 1), z = (lastMoveZ + 1); y < DIM && z < DIM; y++, z++){
				if(getField(lastMoveX, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has2DDiagonal = true;
					}
				} else {
					break;
				}
			}
			for(int y = (lastMoveY - 1), z = (lastMoveZ - 1); y >= 0 && z >= 0; y--, z--){
				if(getField(lastMoveX, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has2DDiagonal = true;
					} 
				} else {
					winning = 1;
					break;
				}
			}
			
			//-------------- check for diagonal left to right in y ----------
			for (int x = (lastMoveX + 1), z = (lastMoveZ + 1); x < DIM && z < DIM; x++, z++){
				if(getField(x, lastMoveY, z) == lastM){
					winning++;
					if(winning == WIN){
						return has2DDiagonal = true;
					}
				} else {
					break;
				}
			}
			for(int x = (lastMoveX - 1), z = (lastMoveZ - 1); x >= 0 && z >= 0; x--, z--){
				if(getField(x, lastMoveY, z) == lastM){
					winning++;
					if(winning == WIN){
						return has2DDiagonal = true;
					} 
				} else {
					winning = 1;
					break;
				}
			}
			
			//-------------- check for diagonal right to left in x ----------
					for (int y = (lastMoveY + 1), z = (lastMoveZ - 1); y < DIM && z >= 0; y++, z--){
						if(getField(lastMoveX, y, z) == lastM){
							winning++;
							if(winning == WIN){
								return has2DDiagonal = true;
							}
						} else {
							break;
						}
					}
					for(int y = (lastMoveY - 1), z = (lastMoveZ + 1); y >= 0 && z < DIM; y--, z++){
						if(getField(lastMoveX, y, z) == lastM){
							winning++;
							if(winning == WIN){
								return has2DDiagonal = true;
							} 
						} else {
							winning = 1;
							break;
						}
					}

					
					
					
					//-------------- check for diagonal right to left in y ----------
					for (int x = (lastMoveX + 1), z = (lastMoveZ - 1); x < DIM && z >= 0; x++, z--){
						if(getField(x, lastMoveY, z) == lastM){
							winning++;
							if(winning == WIN){
								return has2DDiagonal = true;
							}
						} else {
							break;
						}
					}
					for(int x = (lastMoveX - 1), z = (lastMoveZ + 1); x >= 0 && z < DIM; x--, z++){
						if(getField(x, lastMoveY, z) == lastM){
							winning++;
							if(winning == WIN){
								return has2DDiagonal = true;
							} 
						} else {
							winning = 1;
							break;
						}
					}
		}
	return has2DDiagonal;
	}


	public boolean has3DDiagonal(){
		boolean has3DDiagonal = false;
		int winning = 1;
		
		if(lastM != Mark.EMP){
			for (int x = (lastMoveX + 1), y = (lastMoveY + 1), z = (lastMoveZ + 1); x < DIM && y < DIM && z < DIM; x++, y++, z++){
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					break;
				}
				}
			for (int x = (lastMoveX - 1), y = (lastMoveY - 1), z = (lastMoveZ - 1); x >= 0 && y >= 0 && z >= 0; x--, y--, z--){
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					winning = 1;
					break;
				}
			}
			
			
			for (int x = (lastMoveX + 1), y = (lastMoveY - 1), z = (lastMoveZ + 1); x < DIM && y >= 0 && z < DIM; x++, y--, z++){
				System.out.println("test");
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					winning = 1;
					break;
				}
			}	
			for (int x = (lastMoveX - 1), y = (lastMoveY + 1), z = (lastMoveZ - 1); x >= 0 && y < DIM && z >= 0; x--, y++, z--){
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					winning = 1;
					break;
				}
			}
			
			
			for (int x = (lastMoveX - 1), y = (lastMoveY + 1), z = (lastMoveZ + 1); x >= 0 && y < DIM && z < DIM; x--, y++, z++){
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					break;
				}
				}
			for (int x = (lastMoveX + 1), y = (lastMoveY - 1), z = (lastMoveZ - 1); x < DIM && y >= 0 && z >= 0; x++, y--, z--){
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					winning = 1;
					break;
				}
			}
			
			
			for (int x = (lastMoveX - 1), y = (lastMoveY - 1), z = (lastMoveZ + 1); x >= 0 && y >= 0 && z < DIM; x--, y--, z++){
				System.out.println("test");
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					winning = 1;
					break;
				}
			}	
			for (int x = (lastMoveX + 1), y = (lastMoveY + 1), z = (lastMoveZ - 1); x < DIM && y < DIM && z >= 0; x++, y++, z--){
				if(getField(x, y, z) == lastM){
					winning++;
					if(winning == WIN){
						return has3DDiagonal = true;
					}
				} else {
					winning = 1;
					break;
				}
			}
		}
		return has3DDiagonal;
	}

}
