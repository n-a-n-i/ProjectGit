package testsNienke;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import project.*;
import org.junit.Before;
import org.junit.Test;



public class TestWin {
	
	private Board testBoard;
	private int DIM;

	@Before
	public void setUp(){
		DIM = 4;
		testBoard = new Board(DIM);
	}

	//------- test has winner -----
	@Test
	public void testHasWinner(){
		assertFalse(testBoard.hasWinner());
		assertFalse(testBoard.isWinner(Mark.OOO));
		assertFalse(testBoard.isWinner(Mark.XXX));
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 3, Mark.OOO);
		
		
		assertFalse(testBoard.isWinner(Mark.XXX));
		assertTrue(testBoard.isWinner(Mark.OOO));
		assertTrue(testBoard.hasWinner());
	}
	
	
	

	//-------- test rows -----------
	@Test
	public void testRowOnFirstY(){
		testBoard.reset();
		
		assertFalse(testBoard.hasRow(Mark.OOO));
		assertFalse(testBoard.hasRow(Mark.XXX));
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 3, Mark.OOO);

		assertTrue(testBoard.hasRow(Mark.OOO));
		
		assertFalse(testBoard.hasRow(Mark.XXX));
		
	}
	

	
	@Test
	public void testRowInLayer(){
		testBoard.reset();
		
		assertTrue(!testBoard.hasRow(Mark.OOO));
		assertTrue(!testBoard.hasRow(Mark.XXX));
		
		//----fill bottom layer---
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 2, Mark.XXX);
		testBoard.setField(0, 3, Mark.OOO);
		
		//----fill second layer with win---
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 3, Mark.OOO);
		
//		assertTrue(testBoard.hasRow(Mark.OOO));
//		assertFalse(testBoard.hasRow(Mark.XXX));
		
		assertTrue(testBoard.hasWinTest());
	}
	

	//-------- test columns ------------
	@Test
	public void testColumnOnFirstX(){
		testBoard.reset();
		
		assertTrue(!testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(2, 0, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);
		
		assertTrue(testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
	}
	
	@Test
	public void testColumnInLayer(){
		testBoard.reset();
		
		assertTrue(!testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.XXX);
		testBoard.setField(2, 0, Mark.OOO);
		testBoard.setField(3, 0, Mark.XXX);
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(2, 0, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);
		
		
		assertTrue(testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
	}

	
	//------- test stacks ---------------
	@Test
	public void testFirstStack(){
		assertTrue(!testBoard.hasStack(Mark.OOO));
		assertTrue(!testBoard.hasStack(Mark.XXX));
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.OOO);

		assertTrue(testBoard.hasStack(Mark.OOO));
		assertTrue(!testBoard.hasStack(Mark.XXX));
	}
	
	@Test
	public void testStackOnLargerBoard(){
		testBoard = new Board(5);

		assertTrue(!testBoard.hasStack(Mark.OOO));
		assertTrue(!testBoard.hasStack(Mark.XXX));
		
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.OOO);

		assertTrue(testBoard.hasStack(Mark.OOO));
		assertFalse(testBoard.hasStack(Mark.XXX));
		
	}
		
	@Test
	public void testHasNoStack(){
		if(DIM > 4){
			testBoard.reset();
			assertFalse(testBoard.hasStack(Mark.OOO));
			assertFalse(testBoard.hasStack(Mark.XXX));
			
			
			testBoard.setField(0, 0, Mark.OOO);
			testBoard.setField(0, 0, Mark.XXX);
			testBoard.setField(0, 0, Mark.OOO);
			testBoard.setField(0, 0, Mark.OOO);
			testBoard.setField(0, 0, Mark.OOO);

			assertFalse(testBoard.hasStack(Mark.OOO));
			assertFalse(testBoard.hasStack(Mark.XXX));
		}	
	}
		
	
	//------ test bigger boards ----
	
	public void testHasNoColumn(){
		testBoard = new Board(5);
		
		assertTrue(!testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(2, 0, Mark.XXX);
		testBoard.setField(3, 0, Mark.OOO);
		testBoard.setField(4, 0, Mark.OOO);
		
		assertTrue(testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
	}

	public void testHasNoRow(){
		testBoard = new Board(5);
		
		assertTrue(!testBoard.hasRow(Mark.OOO));
		assertTrue(!testBoard.hasRow(Mark.XXX));
		
		//----fill bottom layer---
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 2, Mark.XXX);
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(0, 4, Mark.OOO);
		
		assertFalse(testBoard.hasRow(Mark.OOO));
		assertFalse(testBoard.hasRow(Mark.XXX));
	}
	
	@Test
	public void testRowOnLastY(){
		testBoard = new Board(5);
		
		assertTrue(!testBoard.hasRow(Mark.OOO));
		assertTrue(!testBoard.hasRow(Mark.XXX));
		
		testBoard.setField(0, (DIM), Mark.OOO);
		testBoard.setField(0, (DIM-1), Mark.OOO);
		testBoard.setField(0, (DIM-2), Mark.OOO);
		testBoard.setField(0, (DIM-3), Mark.OOO);
		
		assertTrue(testBoard.hasRow(Mark.OOO));
		assertFalse(testBoard.hasRow(Mark.XXX));
	}
	
	@Test
	public void testColumnOnLastX(){
		testBoard = new Board(5);
		
		assertTrue(!testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
		
		testBoard.setField((DIM), 0, Mark.OOO);
		testBoard.setField((DIM-1), 0, Mark.OOO);
		testBoard.setField((DIM-2), 0, Mark.OOO);
		testBoard.setField((DIM-3), 0, Mark.OOO);
		
		assertTrue(testBoard.hasColumn(Mark.OOO));
		assertTrue(!testBoard.hasColumn(Mark.XXX));
	}
}
