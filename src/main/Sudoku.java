package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.Set;

public class Sudoku {

	private Difficulty difficulty;

	private enum Difficulty {
		NORMAL(30), EASY(45), HARD(25), EXTREME(17);

		public final int clues;

		private Difficulty(int clues) {
			this.clues = clues;
		}
	}

	private int[][] board;

	public Sudoku() {
		this("Normal");
	}

	int successfulValidations = 0;
	int backTracked = 0;
	List<Long> timeTaken = new ArrayList<>();
	double time;

	public Sudoku(String diff) {
		try {
			difficulty = Difficulty.valueOf(diff.toUpperCase());
		} catch (IllegalArgumentException e) {
			difficulty = Difficulty.NORMAL;
		}

		generate(5000);
		OptionalDouble average = timeTaken
	            .stream()
	            .mapToDouble(a -> a)
	            .average();
		double timeAverage = average.isPresent() ? average.getAsDouble() : 0; 
		System.out.println("Successfully Validated board: " + successfulValidations + " times");
		System.out.println("Backtracked: " + backTracked + " times");
		System.out.println("Average time per/board: " + timeAverage + "ms");
		System.out.println("Overall time taken: " + time + "ms");


	}

	public int[][] getBoard() {
		return board;
	}

	public void generate(int n) {
		long start = System.currentTimeMillis();
		for(int i = 0; i < n; i++) {
			generate();
		}
		time = System.currentTimeMillis() - start;
	}

	public void generate() {
		
		Random rand = new Random();
		board = new int[9][9];

		int i, j, nChoices, rolled;
		int index = 0;
		int[] coordinate2D;
		boolean[] choicesCreated = new boolean[81];
		ArrayList<Integer>[] choices = (ArrayList<Integer>[]) new ArrayList[81];
		
		long start = System.currentTimeMillis();
		while (index < 81) {

			coordinate2D = convertIndex(index);
			i = coordinate2D[0];
			j = coordinate2D[1];

			if (board[i][j] == 0) {
				choicesCreated[index] = true;
				choices[index] = (new ArrayList<>(getCellChoices(i, j)));
			}
	    	
			nChoices = choices[index].size();

			while (nChoices == 0) {
				index--;
				nChoices = choices[index].size();
				coordinate2D = convertIndex(index);
				i = coordinate2D[0];
				j = coordinate2D[1];
				board[i][j] = 0;
				backTracked++;
			}

			rolled = choices[index].get(rand.nextInt(nChoices));
			choices[index].remove((Integer) rolled);
			board[i][j] = rolled;

			if (!validateCell(i, j)) {
				System.exit(0);
			}
			index++;
		}
		timeTaken.add(System.currentTimeMillis() - start);
		if (validateEntire()) successfulValidations++; 
	}

	// Converts given 1D index to 2D array index i,j
	private int[] convertIndex(int index) {
		int[] coord = new int[2];
		if (index < 9) {
			coord[0] = 0;
			coord[1] = index;
		} else {
			coord[0] = ((int) Math.floor(index / 9));
			coord[1] = (index % 9);
		}
		return coord;
	}

	// Returns List of Available numbers of given cell
	private List<Integer> getCellChoices(int row, int col) {
		List<Integer> rowChoices = getRowChoices(row);
		List<Integer> colChoices = getColChoices(col);
		List<Integer> boxChoices = getBoxChoices(row, col);
		return getUniqueChoice(rowChoices, colChoices, boxChoices);
	}

	// Returns List of Available numbers in 3x3 box of given cell
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
		for (int i = 1; i <= 9; i++) {
			if (seen.contains(i)) {
				Integer n = i;
				seen.remove(n);
			} else
				seen.add(i);
		}
		return seen;
	}

	// Returns List of Available numbers in column of given cell
	private List<Integer> getColChoices(int col) {
		List<Integer> curCol = new ArrayList<>();
		for (int[] roww : board) {
			if (roww[col] > 0) {
				curCol.add(roww[col]);
			}
		}
		for (int i = 1; i <= 9; i++) {
			if (curCol.contains(i)) {
				Integer n = i;
				curCol.remove(n);
			} else
				curCol.add(i);
		}

		return curCol;
	}

	// Returns List of Available numbers in row of given cell
	private List<Integer> getRowChoices(int row) {
		List<Integer> curRow = new ArrayList<>();
		for (int num : board[row]) {
			if (num > 0) {
				curRow.add(num);
			}
		}

		for (int i = 1; i <= 9; i++) {
			if (curRow.contains(i)) {
				Integer n = i;
				curRow.remove(n);
			} else {
				curRow.add(i);
			}
		}

		return curRow;
	}

	// Returns intersection of 3 lists
	private List<Integer> getUniqueChoice(List<Integer> rowChoices, List<Integer> colChoices,
			List<Integer> boxChoices) {
		List<Integer> firstJoin = rowChoices.stream().distinct().filter(colChoices::contains).toList();
		return firstJoin.stream().distinct().filter(boxChoices::contains).toList();
	}

	// Generates 2D Array of Unique coordinates for clues
	private static int[][] getRandomCoords(int clues) {
		// Random Object for random numbers
		Random rand = new Random();

		// 2D Array to store coordinates
		int[][] coordinates = new int[clues][2];

		// 1D Array to cache existing coordinates
		boolean[] seen = new boolean[81];

		for (int counter = 0; counter < clues; counter++) {

			// Random number column and row between (0-8)
			int i = rand.nextInt(9);
			int j = rand.nextInt(9);

			// Convert 2D array index to 1D index
			int index = (i * 9) + j;

			// Check if coordinate already exists
			while (seen[index]) {
				i = rand.nextInt(9);
				j = rand.nextInt(9);
				index = (i * 9) + j;
			}
			int[] coord = { i, j };
			// Cache coordinate
			seen[index] = true;

			// Update 2D array with unique coordinates
			coordinates[counter] = coord;

		}
		sort2DArray(coordinates);

		// Final check if coordinates are unique, if not call method recursively
		return checkRandomCoords(coordinates) ? coordinates : getRandomCoords(clues);
	}

	// Sorts 2D array by converting to 1D index and bubble sorting ASC
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

	// Used for verifying random coordinates are unique
	private static boolean checkRandomCoords(int[][] coordinates) {

		Set<String> seenCoords = new HashSet<>();

		for (int[] coor : coordinates) {
			String xy = "( " + coor[0] + ", " + coor[1] + " )";
			if (seenCoords.contains(xy)) {
				return false;
			} else
				seenCoords.add(xy);
		}

		return true;
	}

	// Returns if cell is constraint compliant
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

	// Checks if cellValue is Unique in 3x3 grid
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

	// Returns if board is constraint compliant
	private boolean validateEntire() {

		boolean valid = true;
		// Loop for each cell - 81
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9 && valid; j++) {

				valid = validateCell(i, j);

			}
		}

		return valid;
	}

	// Displays entire Sudoku Board
	public void displayBoard() {
		for (int[] row : board) {
			System.out.println(Arrays.toString(row));
		}
	}

	// Unfinished Method
	private void fillClues(int[][] randomCoords) {
		for (int iterator = 0; iterator < randomCoords.length; iterator++) {
			int i = randomCoords[iterator][0];
			int j = randomCoords[iterator][1];

		}

	}
}
