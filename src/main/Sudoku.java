package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
		
		
		{1, 0, 0, 0, 0, 0, 0, 2, 0},
					{0, 0, 3, 0, 7, 0, 0, 0, 0},
					{4, 0, 0, 0, 0, 0, 0, 0, 8},
					{0, 0, 0, 0, 8, 0, 0, 3, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 2, 0, 0, 0},
					{6, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 4, 7, 0, 0, 0, 0, 9, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0}	
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
	
	private int[][] board;
	
	public Sudoku() {
		this("Normal");
	}
	
	public Sudoku(String diff) {
		try {
			difficulty = Difficulty.valueOf(diff.toUpperCase());
		} catch (IllegalArgumentException e) {
			difficulty = Difficulty.NORMAL;
		}
		
		generate();
		
	}

	public int[][] getBoard() {
		return board;
	}

	public void generate() {
		int clues = difficulty.clues;		

		board = new int[9][9];
		
		int[][] randomCoords = getRandomCoords(clues);
	        
		fillClues(randomCoords);
		System.out.println(validateEntire());
		
	}

	private void fillClues(int[][] randomCoords) {
		for (int iterator = 0; iterator < randomCoords.length; iterator++) {
			int i = randomCoords[iterator][0];
			int j = randomCoords[iterator][1];
			
			fillNumber(i, j, iterator);
		}
		
	}

	private void fillNumber(int i, int j, int iterator) {
		
//		List<Integer> choices = new ArrayList<>(getCellChoices(i, j));
//		
//		Random rand = new Random();
//		int randomIndex = 0;
//		if(!choices.isEmpty()) {
//			do {
//				randomIndex = rand.nextInt(choices.size());
//				board[i][j] = choices.get(randomIndex);
//				choices.remove(randomIndex);
//			} while (!validateCell(i, j));
//		} else {
//			System.out.println("failed");
//		}
		board[i][j] = iterator+1;
	}

	private List<Integer> getCellChoices(int row, int col) {
		
		List<Integer> rowChoices = getRowChoices(row);
		List<Integer> colChoices = getColChoices(col);
		List<Integer> boxChoices = getBoxChoices(row, col);
//		System.out.println("Row: " + row + " Col: " + col);
//		System.out.println("Row Choices: " + rowChoices.toString());
//		System.out.println("Col Choices: " + colChoices.toString());
//		System.out.println("Box Choices: " + boxChoices.toString());
//		System.out.println(getUniqueChoice(rowChoices, colChoices, boxChoices).toString());
		return getUniqueChoice(rowChoices, colChoices, boxChoices);
	}

	private List<Integer> getBoxChoices(int row, int col) {
		int checkRow = (int) Math.ceil((row+1) / 3.0);
		int checkCol = (int) Math.ceil((col+1) / 3.0);
				
		List<Integer> seen = new ArrayList<>();
				
		for (int i = (checkRow*3)-3; i < checkRow*3; i++) {
			for (int j = (checkCol*3)-3; j < checkCol*3; j++) {
				if (board[i][j] > 0) {
					seen.add(board[i][j]);
				}
			}
		}
		for(int i = 1; i <= 9; i++) {
			if(seen.contains(i)) {
				Integer n = i;
				seen.remove(n);
			} else seen.add(i);
		}
		return seen;
	}

	private List<Integer> getColChoices(int col) {
		List<Integer> curCol = new ArrayList<>();
		for(int[] roww : board) {
			if (roww[col] > 0) {
				curCol.add(roww[col]);
			}
		}
		for(int i = 1; i <= 9; i++) {
			if(curCol.contains(i)) {
				Integer n = i;
				curCol.remove(n);
			} else curCol.add(i);
		}
		
		return curCol;
	}

	private List<Integer> getRowChoices(int row) {
		List<Integer> curRow = new ArrayList<>();
		for (int num : board[row]) {
			if (num > 0) {
				curRow.add(num);
			}
		}
		
		for(int i = 1; i <= 9; i++) {
			if(curRow.contains(i)) {
				Integer n = i;
				curRow.remove(n);
			} else {
				curRow.add(i);
			}
		}
		
		return curRow;
	}

	private List<Integer> getUniqueChoice(List<Integer> rowChoices, List<Integer> colChoices,
			List<Integer> boxChoices) {
		List<Integer> firstJoin = rowChoices.stream().distinct().filter(colChoices::contains).toList();
		return firstJoin.stream().distinct().filter(boxChoices::contains).toList();
	}

	//Generates 2D Array of Unique coordinates for clues
	private static int[][] getRandomCoords(int clues) {
		//Random Object for random numbers
		Random rand = new Random();
		
		//2D Array to store coordinates
		int[][] coordinates = new int[clues][2];

		//1D Array to cache existing coordinates
		boolean[] seen = new boolean[81];
		
		for (int counter = 0; counter < clues; counter++) {
			
			//Random number column and row between (0-8)
			int i = rand.nextInt(9);
			int j = rand.nextInt(9);
			
			//Convert 2D array index to 1D index
			int index = (i * 9) + j;
			
			//Check if coordinate already exists
			while (seen[index]) {
				i = rand.nextInt(9);
				j = rand.nextInt(9);
				index = (i * 9) + j;
			}
			int[] coord = {i, j};
			//Cache coordinate
			seen[index] = true;
			
			//Update 2D array with unique coordinates
			coordinates[counter] = coord;
			
		}
		sort2DArray(coordinates);
		
		//Final check if coordinates are unique, if not call method recursively 
		return checkRandomCoords(coordinates) ? coordinates : getRandomCoords(clues);
	}

	private static void sort2DArray(int[][] coordinates) {
		int n = coordinates.length;
		int i, j, arrJ, arrJ1;
		boolean swapped;
		for (i = 0; i < n - 1; i++) {
			swapped = false;
			for (j = 0; j < n - i - 1; j++) {
				arrJ = (coordinates[j][0]) * 9 + coordinates[j][1];
				arrJ1 = (coordinates[j + 1][0]) * 9 + coordinates[j + 1][1];
				if (arrJ > arrJ1) {

					int[] temp = coordinates[j];
					coordinates[j] = coordinates[j + 1];
					coordinates[j + 1] = temp;
					swapped = true;
				}
			}
			if (swapped == false)
				break;
		}
		
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
		if (cellValue < 1) return true;
		//Get the row and column that cell belongs to
		List<Integer> curRow = Arrays.stream(board[row]).boxed().toList();
		List<Integer> curCol = new ArrayList<>();
		for(int[] roww : board) {
			curCol.add(roww[col]);
		}
		
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
