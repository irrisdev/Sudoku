# Sudoku Solver and Generator

This Java program provides a Sudoku solver and generator. It allows you to solve existing Sudoku puzzles or generate new ones.

## Usage

To use the Sudoku solver and generator, follow these steps:

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/irrisdev/sudoku.git
   ```

2. Navigate to the project directory:

   ```bash
   cd sudoku
   ```

3. Compile the Java files:

   ```bash
   javac main/*.java
   ```

4. Run the `Main` class to generate and solve a Sudoku puzzle:

   ```bash
   java main.Main
   ```

5. The program will generate a new Sudoku puzzle, display the initial board, solve the puzzle, and display the solved board.

## Example

Here's an example of how to use the program:

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
