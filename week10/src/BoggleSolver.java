import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {

    private static final int MAX_STRING_LENGTH = 8;
    private static final int MIN_VALID_STRING = 3;
    private final TST<Integer> dictionaryTrie;
    private final int[] scores = new int[9];

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        initScores();

        dictionaryTrie = new TST<>();
        // populate dictionary
        for (String s : dictionary) {
            dictionaryTrie.put(s, getScore(s.length()));
        }
    }

    private void initScores() {
        scores[0] = 0;
        scores[1] = 0;
        scores[2] = 0;
        scores[MIN_VALID_STRING] = 1;
        scores[4] = 1;
        scores[5] = 2;
        scores[6] = MIN_VALID_STRING;
        scores[7] = 5;
        scores[MAX_STRING_LENGTH] = 11;
    }

    private int getScore(int length) {
        length = length > MAX_STRING_LENGTH ? MAX_STRING_LENGTH : length;
        return scores[length];
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> validWords = new TreeSet<>();

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] visited = new boolean[board.rows()][board.cols()];
                dfs(board, i, j, visited, validWords, "");
            }
        }
        return validWords;
    }

    private void dfs(BoggleBoard board, int i, int j, boolean[][] visited, Set<String> validWords, String s) {
        if (visited[i][j]) return;

        char letter = board.getLetter(i, j);
        s += letter == 'Q' ? "QU" : letter;
        visited[i][j] = true;

        // check if word is eligible and exists in dictionary
        if (s.length() >= MIN_VALID_STRING && dictionaryTrie.contains(s)) {
            validWords.add(s);
        }

        // are there still words in the dictionary with this prefix?
        if (dictionaryTrie.keysWithPrefix(s).iterator().hasNext()) {
            //    CONTINUE TO DFS

            if (i > 0) {
                // go up
                dfs(board, i - 1, j, visited, validWords, s);

                // diagonal up left
                if (j > 0) {
                    dfs(board, i - 1, j - 1, visited, validWords, s);
                }
                // diagonal up right
                if (j < board.cols() - 1) {
                    dfs(board, i - 1, j + 1, visited, validWords, s);
                }
            }

            if (j > 0) {
                //    go left
                dfs(board, i, j - 1, visited, validWords, s);
            }

            if (j < board.cols() - 1) {
                //    go right
                dfs(board, i, j + 1, visited, validWords, s);
            }

            if (i < board.rows() - 1) {
                //    go down
                dfs(board, i + 1, j, visited, validWords, s);

                // diagonal down left
                if (j > 0) {
                    dfs(board, i + 1, j - 1, visited, validWords, s);
                }
                // diagonal down right
                if (j < board.cols() - 1) {
                    dfs(board, i + 1, j + 1, visited, validWords, s);
                }
            }
        }
        visited[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || !dictionaryTrie.contains(word)) return 0;
        return dictionaryTrie.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}