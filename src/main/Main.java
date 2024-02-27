package main;

public class Main {

	public static void main(String[] args) {
		
		Sudoku_V2 sudoku = new Sudoku_V2();
		
		sudoku.generate();
		
		sudoku.displayBoard();
		
		sudoku.solve();
		
		System.out.println("Done");
		sudoku.displayBoard();

	}

}
