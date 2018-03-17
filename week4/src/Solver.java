import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;
import java.util.Stack;

public class Solver {
    private Node answer = null;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("null constructor");
        }

        Comparator<Node> byPriority = (Node o1, Node o2) -> {
            if (o1.priority > o2.priority) return 1;
            if (o1.priority < o2.priority) return -1;
            return 0;
        };

        MinPQ<Node> solvingPQ = new MinPQ<>(byPriority);
        MinPQ<Node> unsolvingPQ = new MinPQ<>(byPriority);
        solvingPQ.insert(createSearchNode(null, initial));
        unsolvingPQ.insert(createSearchNode(null, initial.twin()));

        Node searchNode = null;
        Node unsearchNode = null;

        do {
            searchNode = updatePQ(solvingPQ);
            unsearchNode = updatePQ(unsolvingPQ);
        } while (!searchNode.board.isGoal() && !unsearchNode.board.isGoal());

        if (searchNode.board.isGoal()) {
            answer = searchNode;
        }
    } // find a solution to the initial board (using the A* algorithm)

    private Node updatePQ(MinPQ<Node> pq) {
        Node searchNode = pq.delMin();
        generateNewSearchNodes(pq, searchNode);
        return searchNode;
    }

    private void generateNewSearchNodes(MinPQ<Node> pq, Node node) {
        Board previousBoard = null;
        if (node.predecessor != null){
            previousBoard = node.predecessor.board;
        }
        for (Board suspect : node.board.neighbors()) {
            // Critical optimization
            if (!suspect.equals(previousBoard)) {
                pq.insert(createSearchNode(node, suspect));
            }
        }
    }

    private Node createSearchNode(Node predecessor, Board board) {
        Node newSearchNode = new Node();
        newSearchNode.predecessor = predecessor;
        newSearchNode.board = board;
        newSearchNode.moves = predecessor == null ? 0 : predecessor.moves + 1;
        newSearchNode.heuristic = board.manhattan();
        newSearchNode.priority = newSearchNode.moves + newSearchNode.heuristic;
        return newSearchNode;
    }

    public boolean isSolvable() {
        if (answer != null) {
            return true;
        }
        return false;
    }           // is the initial board solvable?

    public int moves() {
        if (answer != null) {
            return answer.moves;
        }
        return -1;
    }                    // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (answer == null) {
            return null;
        }
        Stack<Board> boardStack = new Stack<>();
        Node rep = answer;

        while (rep != null) {
            boardStack.push(rep.board);
            rep = rep.predecessor;
        }

        Stack<Board> actualOutput = new Stack<>();
        while(!boardStack.empty()){
            actualOutput.push(boardStack.pop());
        }

        return actualOutput;

    }     // sequence of boards in a shortest solution; null if unsolvable

    private class Node {
        Node predecessor;
        Board board;
        int priority;
        int heuristic;
        int moves;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    } // solve a slider puzzle (given below)
}
