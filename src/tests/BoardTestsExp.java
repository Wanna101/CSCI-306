/*
 * Authors: David Young, Isabelle Neckel
 */

package tests;

import java.util.*;
import experiment.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTestsExp {
	TestBoard board;
	
	@BeforeEach
	void setUp() throws Exception {
		board = new TestBoard();
	}

	@Test
	void testAdjacency() {
		// tests if adjacency list is correct size and has exactly the right elements 
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		Assert.assertEquals(2, testList.size());
	}
	
	
	@Test
	void testAdjCorners() {
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		// tests top left corner
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		// tests bottom right corner
		cell = board.getCell(3, 3);
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	void testAdjEdges() {
		TestBoardCell cell = board.getCell(1, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		// tests right edge
		Assert.assertTrue(testList.contains(board.getCell(0, 3)));
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		// tests left edge
		cell = board.getCell(3, 1);
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(3, 0)));
		Assert.assertTrue(testList.contains(board.getCell(2, 1)));
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
	}
	
	@Test
	void testAdjMid() {
		TestBoardCell cell = board.getCell(2, 2);
		Set<TestBoardCell> testList = cell.getAdjList();
		// tests middle of the board
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 1)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
	}
	
	@Test
	void testTargetsNormal() {
		// tests targets with several rolls and start locations
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	@Test
	void testTargetsMixed() {
		// tests occupied cells
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 2).setIsRoom(true);
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
	}

}
