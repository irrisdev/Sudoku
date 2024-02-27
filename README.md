# Sudoku Solver and Generator

This Java program provides a Sudoku solver and generator. It allows you to solve existing Sudoku puzzles or generate new ones.

## Example Usage


```java
public class Main {

    public static void main(String[] args) {
        
        // Create a new instance of Sudoku_V2
        Sudoku_V2 sudoku = new Sudoku_V2();
        
        // Generate a new Sudoku puzzle
        sudoku.generate();
        
        // Display the initial board
        sudoku.displayBoard();
        
        // Solve the Sudoku puzzle
        sudoku.solve();
        
        // Display the solved board
        System.out.println("Done");
        sudoku.displayBoard();
    }
}
```

## Note

- The `Sudoku_V2` class contains the implementation of the Sudoku solver and generator.
- The `generate()` method is used to generate a new Sudoku puzzle with a default number of clues (30).
- The `displayBoard()` method is used to display the Sudoku board.
- The `solve()` method is used to solve the Sudoku puzzle.
