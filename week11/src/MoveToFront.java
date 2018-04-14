import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256; // radix for extended ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] allChar = initialize();
        while (!BinaryStdIn.isEmpty()) {
            char current = BinaryStdIn.readChar();

            char displaced;
            char counter;
            for (counter = 0, displaced = allChar[0]; allChar[counter] != current; counter++) {
                char temp = allChar[counter];
                allChar[counter] = displaced;
                displaced = temp;
            }
            allChar[counter] = displaced;
            allChar[0] = current;
            BinaryStdOut.write(counter);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] allChar = initialize();
        while (!BinaryStdIn.isEmpty()) {
            char in = BinaryStdIn.readChar();
            BinaryStdOut.write(allChar[in]);
            char outputtedChar = allChar[in];
            // shift everything right
            while (in > 0) {
                allChar[in] = allChar[--in];
            }
            // in will be 0 by now
            allChar[in] = outputtedChar;
        }
        BinaryStdOut.close();
    }

    private static char[] initialize() {
        char[] allChar = new char[R];
        for (char i = 0; i < R; i++) {
            allChar[i] = i;
        }
        return allChar;
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("-")) encode();
            else if (args[0].equals("+")) decode();
            else throw new IllegalArgumentException();
        } else {
            throw new IllegalArgumentException();
        }
    }
}