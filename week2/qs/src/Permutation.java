import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        Iterator<String> iterator = randomizedQueue.iterator();

        int output = 0;
        int totalOutput = Integer.parseInt(args[0]);
        while (iterator.hasNext() && output < totalOutput) {
            StdOut.println(iterator.next());
            output++;
        }
    }
    // Command-line input.  You may assume that 0 ≤ k ≤ n, where n is the number of string on standard input.
    // StdRandom.uniform(n)
}