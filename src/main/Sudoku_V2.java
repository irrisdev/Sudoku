package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sudoku_V2 {
	private static final int SIZE = 9;
	private int[][] board;
	private Random rand;
	private int clues;
	private boolean[] locked;

	public Sudoku_V2() {
		this.rand = new Random();
	}

	public void generate() {

		generate(30);

	}
    
	// Method to generate a Sudoku board with a specified number of clues
	public void generate(int n) {
		this.board = new int[SIZE][SIZE];
		this.clues = n;
		this.locked = fillClues();

	}

    // Method to fill the initial clues in the Sudoku puzzle
	private boolean[] fillClues() {
		boolean[] local_locked = new boolean[81];
		int row, col, nChoices, rolled, location, index = -1;
		ArrayList<Integer>[] choices = (ArrayList<Integer>[]) new ArrayList[81];

		// Generates random coordinates
		for (int i = 0; i < clues; i++) {

			location = rand.nextInt(81);

			while (local_locked[location]) {
				location = rand.nextInt(81);
			}

			local_locked[location] = true;
		}
		while (index < 80) {
			index++;

			if (index < SIZE) {
				row = 0;
				col = index;
			} else {
				row = ((int) Math.floor(index / SIZE));
				col = (index % SIZE);
			}

			if (board[row][col] == 0) {
				choices[index] = (new ArrayList<>(getCellChoices(row, col)));
			}

			nChoices = choices[index].size();

			while (nChoices == 0) {
				index--;
	
				nChoices = choices[index].size();
				if (index < SIZE) {
					row = 0;
					col = index;
				} else {
					row = ((int) Math.floor(index / SIZE));
					col = (index % SIZE);
				}
				board[row][col] = 0;
			}

			rolled = choices[index].get(rand.nextInt(nChoices));
			choices[index].remove((Integer) rolled);
			board[row][col] = rolled;

		}
		
		int oneD;
		for(int z = 0; z < SIZE; z++) {
			for(int x = 0; x < SIZE; x++) {
				oneD = z*SIZE + x;
				
				if(!local_locked[oneD]) {
					board[z][x] = 0;
				}
			
				
			}
			
		}

		return local_locked;
	}
    
	// Method to solve the Sudoku puzzle
	public void solve() {
		int row, col, nChoices, rolled, index = -1;
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] choices = (ArrayList<Integer>[]) new ArrayList[81];
		while (index < 80) {
			index++;
			if (locked[index])
				continue;

			if (index < SIZE) {
				row = 0;
				col = index;
			} else {
				row = ((int) Math.floor(index / SIZE));
				col = (index % SIZE);
			}

			if (board[row][col] == 0) {
				choices[index] = (new ArrayList<>(getCellChoices(row, col)));
			}

			nChoices = choices[index].size();

			while (nChoices == 0) {
				index--;
				
				while (locked[index]) {
					
					index--;
					
				}
				nChoices = choices[index].size();
				if (index < SIZE) {
					row = 0;
					col = index;
				} else {
					row = ((int) Math.floor(index / SIZE));
					col = (index % SIZE);
				}
				board[row][col] = 0;
			}

			rolled = choices[index].get(rand.nextInt(nChoices));
			choices[index].remove((Integer) rolled);
			board[row][col] = rolled;

		}

	}
	
	// Method to get available choices for a given cell (row, col)
	private List<Integer> getCellChoices(int row, int col) {
		List<Integer> rowChoices = getRowChoices(row);
		List<Integer> colChoices = getColChoices(col);
		List<Integer> boxChoices = getBoxChoices(row, col);
		return getUniqueChoice(rowChoices, colChoices, boxChoices);
	}

    // Method to get available choices for a 3x3 box containing the cell (row, col)
	private List<Integer> getBoxChoices(int row, int col) {
		int checkRow = (int) Math.ceil((row + 1) / 3.0);
		int checkCol = (int) Math.ceil((col + 1) / 3.0);

		List<Integer> seen = new ArrayList<>();

		for (int i = (checkRow * 3) - 3; i < checkRow * 3; i++) {
			for (int j = (checkCol * 3) - 3; j < checkCol * 3; j++) {
				if (board[i][j] > 0) {
					seen.add(board[i][j]);
				}
			}
		}
		for (int i = 1; i <= SIZE; i++) {
			if (seen.contains(i)) {
				Integer n = i;
				seen.remove(n);
			} else
				seen.add(i);
		}
		return seen;
	}

	 // Method to get available choices for a given column
	private List<Integer> getColChoices(int col) {
		List<Integer> curCol = new ArrayList<>();
		for (int[] roww : board) {
			if (roww[col] > 0) {
				curCol.add(roww[col]);
			}
		}
		for (int i = 1; i <= SIZE; i++) {
			if (curCol.contains(i)) {
				Integer n = i;
				curCol.remove(n);
			} else
				curCol.add(i);
		}

		return curCol;
	}

	// Method to get available choices for a given row
	private List<Integer> getRowChoices(int row) {
		List<Integer> curRow = new ArrayList<>();
		for (int num : board[row]) {
			if (num > 0) {
				curRow.add(num);
			}
		}

		for (int i = 1; i <= SIZE; i++) {
			if (curRow.contains(i)) {
				Integer n = i;
				curRow.remove(n);
			} else {
				curRow.add(i);
			}
		}

		return curRow;
	}

    // Method to get unique choices common to row, column, and box choices
	private List<Integer> getUniqueChoice(List<Integer> rowChoices, List<Integer> colChoices,
			List<Integer> boxChoices) {
		List<Integer> firstJoin = rowChoices.stream().distinct().filter(colChoices::contains).toList();
		return firstJoin.stream().distinct().filter(boxChoices::contains).toList();
	}

    // Method to validate if a cell (row, col) is compliant with Sudoku rules
	private boolean validateCell(int row, int col) {
		boolean valid = true;
		// Get current cell value
		int cellValue = board[row][col];
		// Get the row and column that cell belongs to
		List<Integer> curRow = Arrays.stream(board[row]).boxed().toList();
		List<Integer> curCol = new ArrayList<>();
		for (int[] roww : board) {
			curCol.add(roww[col]);
		}

		// Count number of occurrences cell value has in its row and column
		int occurrence = Collections.frequency(curRow, cellValue) + Collections.frequency(curCol, cellValue);

		// Call function to check if value is already in 3x3
		if (occurrence > 2 || inBox(cellValue, row, col)) {
			valid = false;
		}

		return valid;
	}

    // Method to check if a cellValue is unique within its 3x3 box
	private boolean inBox(int cellValue, int row, int col) {
		// Finds grid that cellValue belongs to
		int checkRow = (int) Math.ceil((row + 1) / 3.0);
		int checkCol = (int) Math.ceil((col + 1) / 3.0);

		// List of seen values in 3x3 grid
		List<Integer> seen = new ArrayList<>();

		// Checks value against each cell in 3x3 grid
		for (int i = (checkRow * 3) - 3; i < checkRow * 3; i++) {
			for (int j = (checkCol * 3) - 3; j < checkCol * 3; j++) {

				if (Collections.frequency(seen, cellValue) > 1)
					return true;
				else
					seen.add(board[i][j]);

			}
		}
		return false;
	}
	
    // Method to display the Sudoku board
	public void displayBoard() {
		for (int[] row : this.board) {
			System.out.println(Arrays.toString(row));
		}
	}
	
	// Method to validate the entire Sudoku board
	public boolean validateEntire() {

		boolean valid = true;
		// Loop for each cell - 81
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE && valid; j++) {

				valid = validateCell(i, j);

			}
		}

		return valid;
	}

}
