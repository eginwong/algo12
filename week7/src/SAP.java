import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

public class SAP {
    private static final String NULL_INPUT = "null input";
    private Digraph localDigraph;

    // How can I make the data type SAP immutable?
    // You can (and should) save the associated digraph in an instance variable. However, because our Digraph data type is mutable, you must first make a defensive copy by calling the copy constructor.

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException(NULL_INPUT);
        localDigraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (invalidVertex(v) || invalidVertex(w)) throw new IllegalArgumentException(NULL_INPUT);
        int ancestor = ancestor(v, w);

        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(localDigraph, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(localDigraph, w);

        if (ancestor > -1) return forV.distTo(ancestor) + forW.distTo(ancestor);
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (invalidVertex(v) || invalidVertex(w)) throw new IllegalArgumentException(NULL_INPUT);
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(localDigraph, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(localDigraph, w);
        return findAncestor(forV, forW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (invalidVertex(v) || invalidVertex(w)) throw new IllegalArgumentException(NULL_INPUT);
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(localDigraph, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(localDigraph, w);

        int ancestor = findAncestor(forV, forW);

        if (ancestor > -1) return forV.distTo(ancestor) + forW.distTo(ancestor);

        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (invalidVertex(v) || invalidVertex(w)) throw new IllegalArgumentException(NULL_INPUT);

        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(localDigraph, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(localDigraph, w);

        return findAncestor(forV, forW);
    }

    private int findAncestor(BreadthFirstDirectedPaths forV, BreadthFirstDirectedPaths forW) {
        int shortAncestor = -1;
        int shortestLength = Integer.MAX_VALUE;

        Stack<Integer> ancestors = new Stack<>();
        // get all the ancestors
        for (int i = 0; i < localDigraph.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i)) ancestors.push(i);
        }

        if (ancestors.isEmpty()) return shortAncestor;

        for (int ancestor : ancestors) {
            int totalDist = forV.distTo(ancestor) + forW.distTo(ancestor);
            if (totalDist < shortestLength) {
                shortAncestor = ancestor;
                shortestLength = totalDist;
            }
        }
        return shortAncestor;
    }

    private boolean invalidVertex(Iterable<Integer> v) {
        if(v == null) return true;
        for (int i : v) {
            if (invalidVertex(i)) return true;
        }
        return false;
    }

    private boolean invalidVertex(int vertex) {
        return vertex < 0 || vertex > localDigraph.V();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

// Corner cases.  All methods should throw a java.lang.IllegalArgumentException if any argument is null or if any argument vertex is invalid—not between 0 and localDigraph.V() - 1.