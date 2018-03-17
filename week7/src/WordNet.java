import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {
    private static Digraph G;
    private static SAP sap;
    private static HashMap<String, Bag<Integer>> nounToIds;
    private static HashMap<Integer, String> idSysnet;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // The constructor should throw a java.lang.IllegalArgumentException if the input does not correspond to a rooted DAG.

        processSynset(synsets);
        G = getDigraph(hypernyms);
        testForRootedDAG();
        sap = new SAP(G);
    }

    private void processSynset(String synsets) {
        nounToIds = new HashMap<>();
        idSysnet = new HashMap<>();
        // next steps - read in the synsets and get the right count.
        In synsetFileReader = new In(synsets);
        while (!synsetFileReader.isEmpty()) {
            String[] criteria = synsetFileReader.readLine().split(",");
            int synId = Integer.parseInt(criteria[0]);
            idSysnet.put(synId, criteria[1]);
            String[] nouns = criteria[1].split(" ");
            for (String noun : nouns) {
                Bag<Integer> bag = nounToIds.containsKey(noun) ? nounToIds.get(noun) : new Bag<>();
                bag.add(synId);
                nounToIds.put(noun, bag);
            }
        }
    }

    private void testForRootedDAG() {
        DirectedCycle testForDAG = new DirectedCycle(G);
        if (testForDAG.hasCycle()) throw new IllegalArgumentException();

        //    how to test if no root?
        int rooted = 0;
        for (int i = 0; i < G.V(); i++) {
            // check that the other adjacent vertices have nowhere to go
            if (G.outdegree(i) == 0) rooted++;
            // if(!G.adj(i).iterator().hasNext()) rooted++;
        }
        if (rooted != 1) throw new IllegalArgumentException("not rooted or too many roots");
    }

    private Digraph getDigraph(String hypernyms) {
        In hypernymFileReader = new In(hypernyms);
        Digraph hypernymWorld = new Digraph(idSysnet.size());
        while (!hypernymFileReader.isEmpty()) {
            String[] criteria = hypernymFileReader.readLine().split(",");
            for (int i = 1; i < criteria.length; i++) {
                hypernymWorld.addEdge(Integer.parseInt(criteria[0]), Integer.parseInt(criteria[i]));
            }
        }
        return hypernymWorld;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return sap.length(nounToIds.get(nounA), nounToIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return idSysnet.get(sap.ancestor(nounToIds.get(nounA), nounToIds.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet example = new WordNet("./in/synsets.txt", "./in/hypernyms8WrongBFS.txt");
        assert (example.isNoun("yoke"));
        assert (!example.isNoun("yokez"));
        // empty
    }
}