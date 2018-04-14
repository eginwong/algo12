import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // set the radix (why?)
    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();

        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
        int first = 0;

        // while still in csa length, get where the original string is and set as first
        while (first < circularSuffixArray.length() && circularSuffixArray.index(first) != 0) {
            first++;
        }

        BinaryStdOut.write(first);

        // now to get the t[]
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            BinaryStdOut.write(input.charAt((circularSuffixArray.index(i) + circularSuffixArray.length() - 1) % circularSuffixArray.length()));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        // first 4 bits
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();

        // after the key-indexed counting algorithm
        int n = t.length();
        int[] count = new int[R + 1];
        int[] next = new int[n];

        for (int i = 0; i < n; i++) {
            count[t.charAt(i) + 1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }
        // want to create next using the index
        for (int i = 0; i < n; i++) {
            next[count[t.charAt(i)]++] = i;
        }
        for (int i = next[first], counter = 0; counter < n; i = next[i], counter++) {
            BinaryStdOut.write(t.charAt(i));
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("-")) transform();
            else if (args[0].equals("+")) inverseTransform();
            else throw new IllegalArgumentException();
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}