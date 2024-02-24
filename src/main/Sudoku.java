package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sudoku {
	
	 /*
	 * Constraints: Each number (1-9)
	 * - Appear once per Row
	 * - Appear Once per Col 
	 * - Appear Once per Box
	 * 
	 */
	//private static int[][] board = new int[9][9];
	public static void main(String[] args) {
		System.out.println(validate());
	}
	
	private static int[][] mainBoard = {
			{5, 3, 4, 6, 7, 8, 9, 1, 2},
		    {6, 7, 2, 1, 9, 5, 3, 4, 8},
		    {1, 9, 8, 3, 4, 2, 5, 6, 7},
		    {8, 5, 9, 7, 6, 1, 4, 2, 3},
		    {4, 2, 6, 8, 5, 3, 7, 9, 1},
		    {7, 1, 3, 9, 2, 4, 8, 5, 6},
		    {9, 6, 1, 5, 3, 7, 2, 8, 4},
		    {2, 8, 7, 4, 1, 9, 6, 3, 5},
		    {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
	/* 	3x3 Test Case
	    {1,2,3,4,5,6,7,8,9},
		{9,1,2,3,4,5,6,7,8},
		{8,9,1,2,3,4,5,6,7},
		{7,8,9,1,2,3,4,5,6},
		{6,7,8,9,1,2,3,4,5},
		{5,6,7,8,9,1,2,3,4},
		{4,5,6,7,8,9,1,2,3},
		{3,4,5,6,7,8,9,1,2},
		{2,3,4,5,6,7,8,9,1}
	 */
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
	
	
	
	
	
	private static boolean inBox(int cellValue, int row, int col) {
		
		int checkRow = (int) Math.ceil((row+1) / 3.0);
		int checkCol = (int) Math.ceil((col+1) / 3.0);
		
		String output = "--------Information----------\n";
		output += "Value: " + cellValue + " Index: " + (row+1)*(col+1) + " Grid: " + (checkRow*checkCol) + "\n-----------------------------\n";
				
		String coord = "";
		List<Integer> seen = new ArrayList<>();
		for (int i = (checkRow*3)-3; i < checkRow*3; i++) {
			
			String coordsChecked = "";
			String numbChecked = "";
			
			for (int j = (checkCol*3)-3; j < checkCol*3; j++) {

				coordsChecked += "(" + j + ", " + i + ")";
				numbChecked += mainBoard[i][j] + " ";
				
				if(Collections.frequency(seen, cellValue) > 1) {
					System.out.println("Error");
					return true;
				} else { seen.add(mainBoard[i][j]);}
				
			}
			
			coord += coordsChecked + "   " + numbChecked + "\n";

		}
		output += coord;
		//System.out.println(output);
		return false;
	}

	private static int[][] generate() {
		//Initialise 9x9 Board
		
		//Initialise row & column for checking
		List<Integer> curRow, curCol;
		
		//Instantiate new random object for randNum
		Random random = new Random();
		
		//Fill each cell with 0 initially

		
		int overall = 0;
		boolean valid = true;
		while (valid && overall<81) {
			for (int i = 0; i < 9; i++) {
				
				for (int j = 0; j < 9; j++) {
					int unique = 0;

					curRow = Arrays.stream(mainBoard[i]).boxed().toList();
					curCol = Arrays.stream(mainBoard[j]).boxed().toList();
					System.out.println("Unique = " + Integer.toString(unique));
					int rand = random.nextInt(9) + 1;
					while (curRow.contains(rand) || curCol.contains(rand)) {
						if(unique >= 9) {
							valid = false;
							break;
						}
						
						rand = random.nextInt(9) + 1;
						unique++;

					}
					
					if (valid) {
						mainBoard[i][j] = rand;
					}
					overall++;
					System.out.println(overall);

				}
			}
		}
		
		System.out.println(valid);
		return mainBoard;
	}
	
	//Given a int[][] board = new int[9][9] this method fills the board to 0's
	public static void fillBoardEmpty(int[][] inputBoard) {
		for (int[] row : inputBoard) {Arrays.fill(row, 0);}
			
	}
	

}
