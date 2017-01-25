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
	public void testDiagonalZ(){
		testBoard.reset();
		
		assertFalse(testBoard.hasDiagonal(Mark.XXX));
		assertFalse(testBoard.hasDiagonal(Mark.OOO));
		
		System.out.println(testBoard.lastMoveX);
		System.out.println(testBoard.lastMoveY);
		System.out.println(testBoard.lastMoveZ);
		System.out.println(testBoard.lastM);
		
		assertFalse(testBoard.hasDiagonalT(Mark.XXX));
		assertFalse(testBoard.hasDiagonalT(Mark.OOO));
		
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 1, Mark.OOO);
		testBoard.setField(2, 2, Mark.XXX);
		testBoard.setField(3, 3, Mark.OOO);
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 1, Mark.OOO);
		testBoard.setField(3, 3, Mark.OOO);
		testBoard.setField(2, 2, Mark.OOO);
		
		assertFalse(testBoard.hasDiagonal(Mark.XXX));
		assertTrue(testBoard.hasDiagonal(Mark.OOO));
		
	}
	
	@Test
	public void testDiagonalT(){
		testBoard.reset();
		
//		assertFalse(testBoard.hasDiagonalT(Mark.XXX));
//		assertFalse(testBoard.hasDiagonalT(Mark.OOO));
		
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(1, 2, Mark.XXX);
		testBoard.setField(2, 1, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(1, 2, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);
		testBoard.setField(2, 1, Mark.OOO);
		
		assertFalse(testBoard.hasDiagonalT(Mark.XXX));
		assertTrue(testBoard.hasDiagonalT(Mark.OOO));
		
	}

	
	@Test
	public void testDiagonalZ(){
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
		
		testBoard.showBoard();
		
		assertTrue(testBoard.hasDiagonalZ(testBoard.lastM));
		
	}
	
}
