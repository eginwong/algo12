import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Board {
    private final int[][] tiles;

    public Board(int[][] blocks) {
        tiles = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                tiles[i][j] = blocks[i][j];
            }
        }
    }          // construct a board from an n-by-n array of blocks

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return tiles.length;
    }                // board dimension n

    public int hamming() {
        int delta = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != ((i * tiles.length) + j + 1) && tiles[i][j] != 0) {
                    delta++;
                }
            }
        }
        return delta;
        // No, hamming() should return the number of blocks out of position and manhattan() should return the sum of the Manhattan distances between the blocks and their goal positions. Recall that the blank square is not considered a block. You will compute the priority function in Solver by calling hamming() or manhattan() and adding to it the number of moves.
    }                  // number of blocks out of place

    public int manhattan() {
        int delta = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != ((i * tiles.length) + j + 1) && tiles[i][j] != 0) {
                    int modulo = tiles[i][j] % tiles.length;
                    int expectedX = modulo == 0 ? tiles.length - 1 : modulo - 1;
                    int expectedY = (tiles[i][j] - 1) / tiles.length;
                    delta += Math.abs((i - expectedY)) + Math.abs((j - expectedX));
                }
            }
        }
        return delta;
    }                // sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        return hamming() == 0;
    }               // is this board the goal board?

    public Board twin() {
        List<Integer> coordinates = new ArrayList<>();
        int x = 0;
        int y = 0;
        do {
            if (tiles[x][y] != 0) {
                coordinates.add(x);
                coordinates.add(y);
            }
            if (y < tiles.length - 1) {
                y++;
            } else {
                x++;
                y = 0;
            }
        }
        while (coordinates.size() != 4);
        return new Board(swap(coordinates.get(0), coordinates.get(1), coordinates.get(2), coordinates.get(3)));
    }                   // a board that is obtained by exchanging any pair of blocks

    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] copy = duplicateTiles();
        int temp = copy[row1][col1];
        // do a swap
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = temp;
        return copy;
    }

    private int[][] duplicateTiles() {
        int[][] copy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        Board testBoard;
        try {
            testBoard = (Board) y;
        } catch (ClassCastException c) {
            return false;
        }
        return toString().equals(testBoard.toString());
    }       // does this board equal y?

    public Iterable<Board> neighbors() {
        Stack<Board> stackIterable = new Stack<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    if (i > 0) {
                        stackIterable.push(new Board(swap(i, j, i - 1, j)));
                    }
                    if (i < tiles.length - 1) {
                        stackIterable.push(new Board(swap(i, j, i + 1, j)));
                    }
                    if (j > 0) {
                        stackIterable.push(new Board(swap(i, j, i, j - 1)));
                    }
                    if (j < tiles.length - 1) {
                        stackIterable.push(new Board(swap(i, j, i, j + 1)));
                    }
                }
            }
        }
        return stackIterable;
        // How do I return an Iterable<Board>? Add the items you want to a Stack<Board> or Queue<Board> and return that. Of course, your client code should not depend on whether the iterable returned is a stack or queue (because it could be some any iterable).
    }     // all neighboring boards

    public String toString() {
        // example
        StringBuilder s = new StringBuilder();
        int n = dimension();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }              // string representation of this board (in the output format specified below)

    public static void main(String[] args) {
        //    empty body
    } // unit tests (not graded)
}
