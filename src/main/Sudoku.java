package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sudoku {
	/* 	
 	Invalid 3x3 test case
    {1,2,3,4,5,6,7,8,9},
	{9,1,2,3,4,5,6,7,8},
	{8,9,1,2,3,4,5,6,7},
	{7,8,9,1,2,3,4,5,6},
	{6,7,8,9,1,2,3,4,5},
	{5,6,7,8,9,1,2,3,4},
	{4,5,6,7,8,9,1,2,3},
	{3,4,5,6,7,8,9,1,2},
	{2,3,4,5,6,7,8,9,1}
	
	Valid test case
	{5, 3, 4, 6, 7, 8, 9, 1, 2},
	{6, 7, 2, 1, 9, 5, 3, 4, 8},
	{1, 9, 8, 3, 4, 2, 5, 6, 7},
	{8, 5, 9, 7, 6, 1, 4, 2, 3},
	{4, 2, 6, 8, 5, 3, 7, 9, 1},
	{7, 1, 3, 9, 2, 4, 8, 5, 6},
	{9, 6, 1, 5, 3, 7, 2, 8, 4},
	{2, 8, 7, 4, 1, 9, 6, 3, 5},
	{3, 4, 5, 2, 8, 6, 1, 7, 9}
	
	 */
	
	//private static int[][] board = new int[9][9];
	public static void main(String[] args) {
		System.out.println(validate());
	}
	
	private static int[][] mainBoard = {
			{1,2,3,4,5,6,7,8,9},
			{9,1,2,3,4,5,6,7,8},
			{8,9,1,2,3,4,5,6,7},
			{7,8,9,1,2,3,4,5,6},
			{6,7,8,9,1,2,3,4,5},
			{5,6,7,8,9,1,2,3,4},
			{4,5,6,7,8,9,1,2,3},
			{3,4,5,6,7,8,9,1,2},
			{2,3,4,5,6,7,8,9,1}
        };
	
	private static boolean validate() {
		
		boolean valid = true;
		List<Integer> curRow, curCol;
		int overall=0;
		
		do {
			
			//Loop for each cell - 81
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9 && valid; j++) {
					
					int cellValue = mainBoard[i][j];
					
					//Get the row and column that the cell belongs to
					curRow = Arrays.stream(mainBoard[i]).boxed().toList();
					curCol = Arrays.stream(mainBoard[j]).boxed().toList();
					
					//Count number of occurrences cell value has
					int occurrence = Collections.frequency(curRow, cellValue) + Collections.frequency(curCol, cellValue);
					
					//Call function to check if value is already in 3x3
					if(occurrence > 2 || inBox(cellValue, i, j)) {valid = false;}
					overall++;
				}
			}
		} while (valid && overall < 81);
		
		return valid;
	}
	
	
	
	
	//Checks if cellValue is Unique in 3x3 grid
	private static boolean inBox(int cellValue, int row, int col) {
		//Finds grid that cellValue belongs to
		int checkRow = (int) Math.ceil((row+1) / 3.0);
		int checkCol = (int) Math.ceil((col+1) / 3.0);
		
		//List of seen values in 3x3 grid
		List<Integer> seen = new ArrayList<>();
		//Checks value against each cell in 3x3 grid
		
		for (int i = (checkRow*3)-3; i < checkRow*3; i++) {
			for (int j = (checkCol*3)-3; j < checkCol*3; j++) {

				if (Collections.frequency(seen, cellValue) > 1) return true;
				else seen.add(mainBoard[i][j]);
				
			}
		}
		return false;
	}

	
	
	//Given a int[][] board = new int[9][9] this method fills the board to 0's
	public static void fillBoardEmpty(int[][] inputBoard) {
		for (int[] row : inputBoard) {Arrays.fill(row, 0);}
			
	}
	

}
