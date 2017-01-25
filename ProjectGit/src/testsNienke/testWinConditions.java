package testsNienke;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import project.*;
import org.junit.Before;
import org.junit.Test;



public class testWinConditions {

	private Board testBoard;
	private int DIM;

	@Before
	public void setUp(){
		DIM = 5;
		testBoard = new Board(DIM);
	}

	
	@Test
	public void testRow(){
		testBoard.reset();
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 2, Mark.XXX);
		testBoard.setField(0, 3, Mark.OOO);

		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 3, Mark.OOO);

		assertTrue(testBoard.hasWinTest());
	}
	
	
	@Test
	public void testColumn(){
		testBoard.reset();
		
		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(2, 0, Mark.OOO);
		testBoard.setField(3, 0, Mark.XXX);
		testBoard.setField(4, 0, Mark.OOO);

		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(2, 0, Mark.OOO);
		testBoard.setField(4, 0, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);
		
		assertTrue(testBoard.hasWinTest());
	}
	
	@Test
	public void testStack(){
		testBoard.reset();
		
		testBoard.setField(1, 0, Mark.XXX);
		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);

		assertTrue(testBoard.hasWinTest());
		
	}
	
	@Test
	public void testFlatDiagonal(){
		testBoard.reset();
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(1, 1, Mark.OOO);
		testBoard.setField(2, 2, Mark.OOO);
		testBoard.setField(3, 3, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);

		assertTrue(testBoard.hasWinTest());
	}
	
	@Test
	public void testFlatDiagonalYtoZ(){
		testBoard.reset();
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(1, 2, Mark.OOO);
		testBoard.setField(2, 1, Mark.XXX);
		testBoard.setField(3, 0, Mark.OOO);
		testBoard.setField(1, 0, Mark.OOO);
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(1, 2, Mark.OOO);
		testBoard.setField(2, 1, Mark.OOO);
		testBoard.setField(3, 0, Mark.OOO);

		testBoard.showBoard();
		
		assertTrue(testBoard.hasWinTest());
		
	}
	
	@Test
	public void testDiagonalInZ(){
		testBoard.reset();
		
		testBoard.setField(0, 0, Mark.OOO);
		
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 1, Mark.OOO);
		
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 2, Mark.XXX);
		testBoard.setField(0, 2, Mark.OOO);
		
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(0, 3, Mark.XXX);
		testBoard.setField(0, 3, Mark.OOO);
		testBoard.setField(0, 3, Mark.OOO);
		
		assertTrue(testBoard.hasDiagonal2());
		
		
		testBoard.reset();
		
		testBoard.setField(0, 3, Mark.OOO);
		
		testBoard.setField(0, 2, Mark.OOO);
		testBoard.setField(0, 2, Mark.OOO);
		
		testBoard.setField(0, 1, Mark.OOO);
		testBoard.setField(0, 1, Mark.XXX);
		testBoard.setField(0, 1, Mark.OOO);
		
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.setField(0, 0, Mark.OOO);
		testBoard.setField(0, 0, Mark.OOO);
		
		testBoard.showBoard();
		
		assertTrue(testBoard.hasDiagonal2());
		
	}
	
	
	
	
	@Test
	public void test3D(){
		testBoard.reset();
		
		testBoard.setField(0, 0, Mark.OOO);
		
		testBoard.setField(1, 1, Mark.XXX);
		testBoard.setField(1, 1, Mark.OOO);
		
		testBoard.setField(2, 2, Mark.OOO);
		testBoard.setField(2, 2, Mark.XXX);
		testBoard.setField(2, 2, Mark.OOO);
		
		testBoard.setField(3, 3, Mark.OOO);
		testBoard.setField(3, 3, Mark.OOO);
		testBoard.setField(3, 3, Mark.XXX);
		testBoard.setField(3, 3, Mark.OOO);
		
		testBoard.showBoard();
		assertTrue(testBoard.has3DDiagonal());
		
		
		testBoard.reset();
		
		testBoard.setField(0, 4, Mark.OOO);
		
		testBoard.setField(1, 3, Mark.XXX);
		testBoard.setField(1, 3, Mark.OOO);
		
		testBoard.setField(2, 2, Mark.XXX);
		testBoard.setField(2, 2, Mark.OOO);
		testBoard.setField(2, 2, Mark.OOO);
		
		testBoard.setField(3, 1, Mark.OOO);
		testBoard.setField(3, 1, Mark.OOO);
		testBoard.setField(3, 1, Mark.OOO);
		testBoard.setField(3, 1, Mark.OOO);
		
		testBoard.showBoard();
		assertTrue(testBoard.has3DDiagonal());
		
		
		testBoard.reset();
		
		testBoard.setField(4, 0, Mark.OOO);
		
		testBoard.setField(3, 1, Mark.XXX);
		testBoard.setField(3, 1, Mark.OOO);
		
		testBoard.setField(2, 2, Mark.XXX);
		testBoard.setField(2, 2, Mark.OOO);
		testBoard.setField(2, 2, Mark.OOO);
		
		testBoard.setField(1, 3, Mark.OOO);
		testBoard.setField(1, 3, Mark.OOO);
		testBoard.setField(1, 3, Mark.OOO);
		testBoard.setField(1, 3, Mark.OOO);
		
		testBoard.showBoard();
		
		assertTrue(testBoard.has3DDiagonal());
		
		
		
		testBoard.reset();
		
		testBoard.setField(4, 0, Mark.OOO);
		
		testBoard.setField(3, 1, Mark.XXX);
		testBoard.setField(3, 1, Mark.OOO);
		
		testBoard.setField(2, 2, Mark.XXX);
		testBoard.setField(2, 2, Mark.OOO);
		testBoard.setField(2, 2, Mark.OOO);
		
		testBoard.setField(1, 3, Mark.OOO);
		testBoard.setField(1, 3, Mark.OOO);
		testBoard.setField(1, 3, Mark.OOO);
		testBoard.setField(1, 3, Mark.OOO);
		
		System.out.println("printen");
		testBoard.showBoard();
		
		assertTrue(testBoard.has3DDiagonal());
		
	}

}
