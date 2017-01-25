package testsNienke;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import project.*;
import org.junit.Before;
import org.junit.Test;

public class TestFields {
	
	private Board testBoard;
	private int DIM;

	@Before
	public void setUp(){
		DIM = 4;
		testBoard = new Board(DIM);
	}
	
	@Test
	public void TestShowBoard(){
		testBoard.showBoard();
		
		Board testBoard2 = new Board(5);
		testBoard2.showBoard();
	}
	
	
	@Test
	public void testFields(){
		//--- row out of bounds---
		assertFalse(testBoard.isField(5, 0, 0));
		//--- column out of bounds---
		assertFalse(testBoard.isField(0, 5, 0));
		//---- stack out of bounds ----
		assertFalse(testBoard.isField(0, 0, 5));
	}
	
	@Test
	public void testEmptyFields(){
		for (int x = 0; x < DIM; x++){
			for (int y = 0; y < DIM; y++){
				for (int z = 0; z < DIM; z++){
					assertTrue(testBoard.isEmptyField(x, y, z));
				}
			}
		}
		
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.showBoard();
		assertFalse(testBoard.isEmptyField(0, 0, 0));
		
		
		
		
	}

	@Test
	public void testFirstEmptyField(){
		assertEquals(0, testBoard.firstEmptyField(0, 0));
		
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.setField(0, 0, Mark.XXX);
		testBoard.setField(0, 0, Mark.XXX);
		
		System.out.println(testBoard.firstEmptyField(0, 0));
	}

	@Test
	public void testFull(){
		testBoard.reset();
		assertFalse(testBoard.isFull());
		
		for (int x = 0; x < DIM; x++){
			for (int y = 0; y < DIM; y++){
				for (int z = 0; z < DIM; ){
					testBoard.setField(x, y, Mark.XXX);
					z++;
				}
			}
		}
		testBoard.showBoard();
		assertTrue(testBoard.isFull());
	}
	
}
