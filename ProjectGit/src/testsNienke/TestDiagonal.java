package testsNienke;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import project.*;
import org.junit.Before;
import org.junit.Test;


public class TestDiagonal {
	
	
	private Board testBoard;
	private int DIM;

	@Before
	public void setUp(){
		DIM = 4;
		testBoard = new Board(DIM);
	}

	//------- test has winner -----
	@Test
	public void testDiagonalLtR(){
		testBoard.reset();
		
		assertFalse(testBoard.has2DDiagonal());
		testBoard.lastM = testBoard.lastM.other();
		assertFalse(testBoard.has2DDiagonal());
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 1, Mark.OOO);
		testBoard.setField(2, 2, Mark.XXX);
		testBoard.setField(3, 3, Mark.OOO);
		
		assertFalse(testBoard.has2DDiagonal());
		testBoard.lastM = testBoard.lastM.other();
		assertFalse(testBoard.has2DDiagonal());
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 1, Mark.OOO);
		testBoard.setField(3, 3, Mark.OOO);
		testBoard.setField(2, 2, Mark.OOO);
		
		assertTrue(testBoard.has2DDiagonal());		
		testBoard.lastM = testBoard.lastM.other();
		assertFalse(testBoard.has2DDiagonal());
		//testBoard.showBoard();
	}
	
	@Test
	public void test2DDiagonalTRtL(){
		testBoard.reset();
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(1, 2, Mark.XXX);
		testBoard.setField(2, 1, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);
		
		assertFalse(testBoard.has2DDiagonal());
		testBoard.lastM = testBoard.lastM.other();
		assertFalse(testBoard.has2DDiagonal());
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(1, 2, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);
		testBoard.setField(2, 1, Mark.OOO);
		
		assertTrue(testBoard.has2DDiagonal());
		testBoard.lastM = testBoard.lastM.other();
		assertFalse(testBoard.has2DDiagonal());
		//testBoard.showBoard();
	}

	
	@Test
	public void testDiagonalThroughZIncreasing(){
		testBoard.reset();
		
		testBoard.setField(0, 0, Mark.XXX);
		
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 1, Mark.XXX);
		
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 2, Mark.XXX);
		testBoard.setField(0, 2, Mark.XXX);
		
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(0, 3, Mark.XXX);
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(0, 3, Mark.XXX);
		
		//testBoard.showBoard();
		
		assertTrue(testBoard.has2DDiagonal());
		testBoard.lastM = testBoard.lastM.other();
		assertFalse(testBoard.has2DDiagonal());
		
	}
	@Test
	public void testDiagonalThroughZDecreasing(){
		testBoard.reset();
		
		testBoard.setField(0, 3, Mark.XXX);
		
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 2, Mark.XXX);
		
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 1, Mark.XXX);
		testBoard.setField(0, 1, Mark.XXX);
		
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.XXX);
		
		testBoard.showBoard();
		
		assertTrue(testBoard.has2DDiagonal());
		testBoard.lastM = testBoard.lastM.other();
		assertFalse(testBoard.has2DDiagonal());
		
	}
	
}
