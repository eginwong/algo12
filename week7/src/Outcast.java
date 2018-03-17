import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private static WordNet local;

    public Outcast(WordNet wordnet) {
        local = wordnet;
    }        // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int worstDistance = 0;
        String outcast = nouns[0];

        for (int i = 0; i < nouns.length; i++) {
            int distance = 0;
            for (int j = 0; j < nouns.length; j++) {
                distance += local.distance(nouns[i], nouns[j]);
            }
            if (distance > worstDistance) {
                outcast = nouns[i];
                worstDistance = distance;
            }
        }

        return outcast;
    }  // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }  // see test client below
}