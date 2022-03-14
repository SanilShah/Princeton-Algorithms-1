import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {

    // HashMap to store synsets
    private final HashMap<Integer, String> synSets;
    // HashMap to store all nouns and related synset ids
    private final HashMap<String, Bag<Integer>> synMap;
    private final Digraph wordNet;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synSets = new HashMap<Integer, String>();
        synMap = new HashMap<String, Bag<Integer>>();
        int count = readSynSets(synsets);
        wordNet = new Digraph(count);
        readHypernyms(hypernyms);

        // Check if input is rooted DAG
        DirectedCycle dc = new DirectedCycle(wordNet);
        if (dc.hasCycle())
            throw new IllegalArgumentException("Input graph has a cycle.");

        // Shortest Ancestral Path
        sap = new SAP(wordNet);

        // Check if only one node of out-degree zero exists. I.e. the graph is singly rooted.
        int rootno = 0;
        for (int v = 0; v < count; v++) {
            if (wordNet.outdegree(v) == 0)
                rootno++;
        }
        if (rootno != 1)
            throw new IllegalArgumentException("input has " + rootno + "roots.");

    }

    // Helper
    private void readHypernyms(String hypernyms) {
        if (hypernyms == null) {
            throw new IllegalArgumentException("Hypernyms argument is null.");
        }
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int v = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int w = Integer.parseInt(parts[i]);
                wordNet.addEdge(v, w);
            }
        }

    }

    // Helper
    private int readSynSets(String synsets) {
        if (synsets == null)
            throw new IllegalArgumentException("Synsets argument is null");
        In in = new In(synsets);
        int count = 0;
        while (in.hasNextLine()) {
            count++;
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            synSets.put(id, parts[1]);
            String[] nouns = parts[1].split(" ");
            for (String n : nouns) {
                if (synMap.get(n) != null) {
                    Bag<Integer> bag = synMap.get(n);
                    bag.add(id);
                }
                else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    synMap.put(n, bag);
                }
            }
        }
        return count;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Word is null.");
        return synMap.containsKey(word);
    }

    private void validateNoun(String noun) {
        if (!synMap.containsKey(noun))
            throw new IllegalArgumentException("Noun is not in wordNet");
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);

        Bag<Integer> idsA = synMap.get(nounA);
        Bag<Integer> idsB = synMap.get(nounB);

        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
     public String sap(String nounA, String nounB) {
         validateNoun(nounA);
         validateNoun(nounB);

         Bag<Integer> idsA = synMap.get(nounA);
         Bag<Integer> idsB = synMap.get(nounB);

         int id = sap.ancestor(idsA, idsB);
         return synSets.get(id);
     }

    // // do unit testing of this class
    // public static void main(String[] args)
}
