package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
	private Difficulty difficulty;

	private enum Difficulty {
		  NORMAL(30),
		  EASY(45),
		  HARD(25),
		  EXTREME(17);
		
		  public final int clues;

	        private Difficulty(int clues) {
	            this.clues = clues;
	        }
		}
	
	private int[][] board = new int[9][9];;
	
	public Sudoku() {
		this("Normal");
	}
	
	public Sudoku(String diff) {
		try {
			difficulty = Difficulty.valueOf(diff.toUpperCase());
		} catch (IllegalArgumentException e) {
			difficulty = Difficulty.NORMAL;
		}
		System.out.println(difficulty);
		generate();
		
	}

	public int[][] getBoard() {
		return board;
	}

	public void generate() {
		int clues = difficulty.clues;		
		//Fills the board with clues
		fillClues(getRandomCoords(clues));
		System.out.println(validateEntire());
		
	}

	private void fillClues(int[][] randomCoords) {
		for (int i = 0; i < randomCoords.length; i++) {
			
			int row = randomCoords[i][1];
			int col = randomCoords[i][0];
			
			fillNumber(row, col);
		}
		
	}

	private void fillNumber(int row, int col) {

		Random rand = new Random();
		
		board[row][col] = rand.nextInt(9) + 1;
		
	}

	//Generates 2D Array of Unique coordinates for clues
	private static int[][] getRandomCoords(int clues) {
		//Random Object for random numbers
		Random rand = new Random();
		
		//2D Array to store coordinates
		int[][] coordinates = new int[clues][2];
		
		//1D Array to cache existing coordinates
		boolean[] seen = new boolean[81];
		
		for (int i = 0; i < clues; i++) {
			
			//Random number column and row between (0-8)
			int randomRow = rand.nextInt(9);
			int randomCol = rand.nextInt(9);
			
			//Convert 2D array index to 1D index
			int index = (randomRow * 9) + randomCol;
			
			//Check if coordinate already exists
			while (seen[index]) {
				randomRow = rand.nextInt(9);
				randomCol = rand.nextInt(9);
				index = (randomRow * 9) + randomCol;
			}
			
			//Cache coordinate
			seen[index] = true;
			
			//Update 2D array with unique coordinates
			coordinates[i][0] = randomRow;
			coordinates[i][1] = randomCol;

		}
		
		//Final check if coordinates are unique, if not call method recursively 
		return checkRandomCoords(coordinates) ? coordinates : getRandomCoords(clues);
	}

	//Used for verifying random coordinates are unique
	private static boolean checkRandomCoords(int[][] coordinates) {
		
		Set<String> seenCoords = new HashSet<>();
		
		for (int[] coor : coordinates) {
			String xy = "( " + coor[0] + ", " + coor[1] + " )";
			if (seenCoords.contains(xy)) {				
				return false;
			} else seenCoords.add(xy);
		}
	
		return true;
	}

	//Returns if cell is constraint compliant
	private boolean validateCell(int row, int col) {
		boolean valid = true;
		//Get current cell value
		int cellValue = board[row][col];
		
		//Get the row and column that cell belongs to
		List<Integer> curRow = Arrays.stream(board[row]).boxed().toList();
		List<Integer> curCol = Arrays.stream(board[col]).boxed().toList();
		
		//Count number of occurrences cell value has in its row and column
		int occurrence = Collections.frequency(curRow, cellValue) + Collections.frequency(curCol, cellValue);
		
		//Call function to check if value is already in 3x3
		if(occurrence > 2 || inBox(cellValue, row, col)) {valid = false;}

		return valid;
	}

	//Checks if cellValue is Unique in 3x3 grid
	private boolean inBox(int cellValue, int row, int col) {
		//Finds grid that cellValue belongs to
		int checkRow = (int) Math.ceil((row+1) / 3.0);
		int checkCol = (int) Math.ceil((col+1) / 3.0);
		
		//List of seen values in 3x3 grid
		List<Integer> seen = new ArrayList<>();
		
		//Checks value against each cell in 3x3 grid
		for (int i = (checkRow*3)-3; i < checkRow*3; i++) {
			for (int j = (checkCol*3)-3; j < checkCol*3; j++) {

				if (Collections.frequency(seen, cellValue) > 1) return true;
				else seen.add(board[i][j]);
				
			}
		}
		return false;
	}
	
	//Returns if board is constraint compliant
	private boolean validateEntire() {
		
		boolean valid = true;		
			//Loop for each cell - 81
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9 && valid; j++) {
					
					valid = validateCell(i, j);
					
				}
			}
		
		return valid;
	}

	//Displays entire Sudoku Board
	public void displayBoard() {
		for (int[] row : board) {System.out.println(Arrays.toString(row));}
	}
	
	//Generates a board full of random numbers
	private void generateTest() {
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Random rand = new Random();
				board[i][j] = rand.nextInt(9) + 1;
			}
		}
		
	}
	
	//Given a int[][] board, method fills the board to 0's
	private void fillBoardWithNumber() {
		for (int[] row : board) {Arrays.fill(row, 1);}
				
	}
}
