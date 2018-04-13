import java.util.Arrays;

public class CircularSuffixArray {
    private Integer[] csa;

    public CircularSuffixArray(String s) {
        if (s == null || s.length() == 0) throw new IllegalArgumentException();
        csa = new Integer[s.length()];

        for (int i = 0; i < csa.length; i++) {
            csa[i] = i;
        }

        Arrays.sort(csa, (o1, o2) -> {
            int first = o1;
            int second = o2;

            for (int i = 0; i < csa.length; i++) {
                if (first > csa.length - 1) {
                    first = 0;
                }
                if (second > csa.length - 1) {
                    second = 0;
                }

                if (s.charAt(first) > s.charAt(second)) {
                    return 1;
                } else if (s.charAt(first) < s.charAt(second)) {
                    return -1;
                }

                first++;
                second++;
            }
            return 0;
        });
    }   // circular suffix array of s

    public int length() {
        return csa.length;
    }                    // length of s

    public int index(int i) {
        if (i >= csa.length) throw new IllegalArgumentException();
        return csa[i];
    }                // returns index of ith sorted suffix

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("AAA\n");
        for (int i = 0; i < csa.length(); i++ ) System.out.println(csa.index(i) );
        System.out.println();
        csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++ ) System.out.println(csa.index(i) );
        //    empty
    }  // unit testing (required)
}
