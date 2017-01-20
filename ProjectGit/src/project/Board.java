package project;

public class Board {
	
	private Mark[][][] board;
	public int DIM;
	public int WIN = 4;
	
	public Board(int dimension) {
		board = new Mark[dimension][dimension][dimension];
		DIM = dimension;
		
		for (int x = 0; x < dimension; x++){
			for (int y = 0; y < dimension; y++){
				for (int z = 0; z < dimension; z++){
					board[x][y][z] = Mark.EMP;
				}
			}
		}
	}
	
	public void showBoard(){
		StringBuilder numbers = new StringBuilder();
		for (int z = 0; z < DIM; z++){
			numbers.append("  |");
			for(int i = 0; i < DIM; i++){
				if (i == DIM -1){
					numbers.append("  " + i + " ");
				} else{
					numbers.append("  " + i + "  ");
				}
			}
		}
		String number = numbers.toString();
		System.out.println(number);
		
		
		for (int x = 0; x < DIM; x++){
			System.out.printf("%d | ", x);
			for (int z = 0; z < DIM; z++){
				for (int y = 0; y < DIM; y++){
					System.out.printf("%s  ", board[x][y][z]);
				}
				System.out.printf("| ");
			}
			System.out.printf("\n");
		}
		
		
		StringBuilder layers = new StringBuilder();
		StringBuilder spaces = new StringBuilder();
		
		layers.append("  |");
		for (int i = 0; i < ((DIM*5)-10)/2; i++){
			spaces.append(" ");
		}
		for (int z = 0; z < DIM; z++){
			if((DIM%2)==0){
				layers.append(spaces + "  layer " + z + spaces + "  |");
			} else{
				layers.append(spaces + "  layer " + z + spaces + "   |");
			}
		}
		String layer = layers.toString();
		System.out.println(layer);

	}
	
	
	public int index(int row, int col, int height) {
	   	assert (row >= 0 && row < DIM);
	   	assert (col >= 0 && col < DIM);
	   	assert (height >= 0 && height < DIM);

	   	int index = row * DIM + col + (height * DIM * DIM); 
	   	return index;
	}
	
	   
	   
	public boolean isEmptyField(int row, int col, int height) {
	    if (isField(row, col, height)){
	    	return (this.getField(row, col, height) == Mark.EMP);
	    }
	   	return false;
	}
	    
	    public boolean isEmptyField(int row, int col) {
	    	if (isField(row, col)){
	    		return (this.getField(row, col, firstEmptyField(row, col)) == Mark.EMP);
	    	}
	    	return false;
	    }
	    
	    
	    public boolean isField(int row, int col, int height) {
	    	if( row >= 0 && row < DIM && col >= 0 && col < DIM && height >= 0 && height < DIM){ 
	    		return true;
	    	}
	    	return false;
	    }
	   
	    public boolean isField(int row, int col){
	    	if(isField(row, col, firstEmptyField(row, col))){
	    		return true;
	    	} else {
	    	return false;
	    	}
	    }
	    
	    public int firstEmptyField(int row, int col){
	    	assert (row >= 0 && row < DIM);
	    	assert (col >= 0 && col < DIM);
	    	int height = -1;
	    	
	    	try{
	    		for (int z = 0; z < DIM; z++){
		    		if (isEmptyField(row, col, z)){
		    			height = z;
		    			break;
		    		}
		    	}
	    	} catch (Exception e){
	    		//throw error
	    	}
	    	return height;
	    }
	
	
	    public Mark getField(int row, int col, int height) {
	    	assert (isField(row, col, height));
	    	return board[row][col][height];
	    }
	
	    
	    public void setField(int row, int col, Mark m) {
	    	board[row][col][firstEmptyField(row, col)] = m;
	    }
	    
	   
	    //@ ensures \result == (\forall int i; i <= 0 & i < DIM * DIM; this.getField(i) != Mark.EMPTY);
	    /*@pure*/
	    public boolean isFull() {
	    	boolean full = false;
	    	for (int i = 0; i < DIM; i++) {
	    		for (int j = 0; j < DIM; j++){
	    			for (int z = 0; z < DIM; z++){
	    				if(!isEmptyField(i, j, z)){
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
	    	for (int x = 0; x < DIM; x++){
				for (int y = 0; y < DIM; y++){
					for (int z = 0; z < DIM; z++){
						board[x][y][z] = Mark.EMP;
					}
				}
			}
	    }
	    
	    
	    
	    
	    
	    public boolean hasRow(Mark m) {
	    	boolean hasRow = false;
	    	int hasWin = 0;
	    	
	    	for (int z = 0; z < DIM; z++){
	    		for (int x = 0; x < DIM; x++){
	    			for(int y = 0; y < (DIM-3); y++) {
	    	    		if(getField(x, y, z) == m){
	    	    			for(int w = (y+1); w < (y+4); ){
	    	    				if(hasWin == WIN-1){
    	    						hasRow=true;
    	    						return hasRow;
    	    					}
	    	    				if(getField(x, w, z) == m){
	    	    					hasWin++;
	    	    					w++;
	    	    				} else {
	    	    					hasRow = false;
	    	    					hasWin = 0;
	    	    					break;
	    	    				}
	    	    			}
	    	    		}
	    			}
	    		}
	    	}
	    	return hasRow;
	    }
	    				
	    public boolean hasColumn(Mark m) {
	    	boolean hasColumn = false;
	    	int hasWin = 0;
	    	
	    	for (int y = 0; y < DIM; y++){
	    		for (int z = 0; z < DIM; z++){
	    			for(int x = 0; x < (DIM); x++) {
	    	    		if(getField(x, y, z) == m){
	    	    			for(int w = (x+1); w < (x+4); ){
	    	    				if(hasWin == WIN-1){
    	    						hasColumn=true;
    	    						return hasColumn;
    	    					}
	    	    				if(getField(w, y, z) == m){
	    	    					hasWin++;
	    	    					w++;
	    	    				} else {
	    	    					hasColumn = false;
	    	    					hasWin = 0;
	    	    					break;
	    	    				}
	    	    			}
	    	    		}
	    			}
	    		}
	    	}
	    	return hasColumn;
	    }
	    	
	    public boolean hasStack(Mark m) {
	    	boolean hasStack = false;
	    	int hasWin = 0;
	    	
	    	for (int y = 0; y < DIM; y++){
	    		for (int x = 0; x < DIM; x++){
	    			for(int z = 0; z < (DIM); z++) {
	    	    		if(getField(x, y, z) == m){
	    	    			for(int w = (z+1); w < (z+4); ){
	    	    				if(hasWin == WIN-1){
    	    						hasStack=true;
    	    						return hasStack;
    	    					}
	    	    				if(getField(x, y, w) == m){
	    	    					hasWin++;
	    	    					w++;
	    	    				} else {
	    	    					hasStack = false;
	    	    					hasWin = 0;
	    	    					break;
	    	    				}
	    	    			}
	    	    		}
	    			}
	    		}
	    	}
	    	return hasStack;
	    }
	    	
//	    	for (int i = 0; i < DIM; i++){
//	    		
//	    		for (int j = 0; j < DIM; j++) {
//	    			int stack = 0;
//	    			
//	    			for (int z = 0; z < DIM; z++) {
//	        			if (getField(i, j, z) == m) stack++;
//	        		}
//	        		if (stack == DIM) {
//	        			hasStack = true;
//	        			break;
//	        		}
//	    		}
//	    	}
//	    	return hasStack;
//	    }
//	    
	    
	    
	    
	    
	    public boolean hasDiagonal(Mark m) {
	    	boolean hasDiagonalLeftZ = false;
	    	boolean hasDiagonalRightZ = false;
	    	
//	    	for (int z = 0; z < DIM; z++){
	    		
	    	
	    		for (int x = 0; x < DIM; x++){
	    			int z= 0;
	    			if(getField(x, x, z) == m){
	    				hasDiagonalLeftZ = true;
	    			} else {
	    				hasDiagonalLeftZ = false;
	    				break;
	    			}
	    		}
	    		
	    		for (int y = DIM-1; y >= 0; y--){
	    			for (int x = 0; x < DIM; x++){
	    				int z = 0;
	    				if(getField(x, y, z) == m){
	    					hasDiagonalRightZ = true;
	    				} else {
	    					hasDiagonalRightZ = false;
	    					break;
	    				}
	    				
	    			}
	    		}
	    	
	    	boolean hasDiagonalLeftI = false;
	    	boolean hasDiagonalRightI = false;
	    	
	    	for (int i = 0; i < DIM; i++){
	    		
	    		for (int x = 0; x < DIM; x++){
	    			if(getField(i, x, x) == m){
	    				hasDiagonalLeftI = true;
	    			} else {
	    				hasDiagonalLeftI = false;
	    				break;
	    			}
	    		}
	    		
	    		for (int x = 0; x < DIM; x++){
	    			for (int y = DIM-1; y >= 0; y--){
	    				if(getField(i, x, y) == m){
	    					hasDiagonalRightI = true;
	    				} else {
	    					hasDiagonalRightI = false;
	    					break;
	    				}
	    				
	    			}
	    		}
	    	}
	    	
	    	boolean hasDiagonalLeftJ = false;
	    	boolean hasDiagonalRightJ = false;
	    	
	    	for (int j = 0; j < DIM; j++){
	    		
	    		for (int x = 0; x < DIM; x++){
	    			if(getField(x, j, x) == m){
	    				hasDiagonalLeftJ = true;
	    			} else {
	    				hasDiagonalLeftJ = false;
	    				break;
	    			}
	    		}
	    		
	    		for (int x = 0; x < DIM; x++){
	    			for (int y = DIM-1; y >= 0; y--){
	    				if(getField(x, j, y) == m){
	    					hasDiagonalRightJ = true;
	    				} else {
	    					hasDiagonalRightJ = false;
	    					break;
	    				}
	    				
	    			}
	    		}
	    	}
	    	
	    	
	    	
	    	return hasDiagonalLeftZ || hasDiagonalRightZ || hasDiagonalLeftI || hasDiagonalRightI || hasDiagonalLeftJ || hasDiagonalRightJ;
	    }
	    
	    
	    //@requires m == Mark.XX | m == Mark.OO;
	    //@ ensures \result == this.hasRow(m) || this.hasColumn(m) | this.hasDiagonal(m);
	    /*@ pure */
	    public boolean isWinner(Mark m) {
	    	assert ( m == Mark.XXX || m == Mark.OOO);
	    	
	    	return hasRow(m) || hasColumn(m) || hasDiagonal(m) || hasStack(m);
	    }
	    
	    
	    //@ ensures \result == isWinner(Mark.XX) | \result == isWinner(Mark.OO);
	    /*@pure*/
	    public boolean hasWinner() {
	    	return isWinner(Mark.XXX) || isWinner(Mark.OOO);
	    }	    
	    
	    
	    
}


