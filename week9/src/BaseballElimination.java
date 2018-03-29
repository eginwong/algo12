

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BaseballElimination {
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private boolean[] eliminated;
    private Map<Integer, Set<String>> eliminatorList;
    private final Map<String, Integer> teamIndex = new HashMap<>();
    private String[] teamNames;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int totalTeams = in.readInt();
        readInData(in, totalTeams);

        // elimination
        for (int team = 0; team < totalTeams; team++) {
            // trivial elimination
            trivialElimination(team);
            // non-trivial elimination
            nonTrivialElimination(team);
        }
        //    use Ford Fulkerson?
    }         // create a baseball division from given filename in format specified below

    private void nonTrivialElimination(int team) {
        if (!eliminated[team]) {
            // s + t + teams - 1 + possible matches
            int remainingMatches = numberOfMatches();
            int nodes = 1 + 1 + remainingMatches + numberOfTeams();
            int source = 0;
            int sink = nodes - 1;

            FlowNetwork flowNetwork = new FlowNetwork(nodes);

            int nodeCount = 1;
            for (int i = 0; i < numberOfTeams(); i++) {
                // can't play against yourself
                for (int j = i + 1; j < numberOfTeams(); j++) {
                    // nothing to do if you're the team in contention
                    if (i != team && j != team) {
                        // source vertex to game vertex
                        flowNetwork.addEdge(new FlowEdge(source, nodeCount, against[i][j]));
                        // game vertex to team 1 vertex, offset by # of matches and teams
                        flowNetwork.addEdge(new FlowEdge(nodeCount, remainingMatches + i, Double.POSITIVE_INFINITY));
                        // game vertex to team 2 vertex, offset by # of matches and teams
                        flowNetwork.addEdge(new FlowEdge(nodeCount, remainingMatches + j, Double.POSITIVE_INFINITY));

                        nodeCount++;
                    }
                }
            }

            // add edge to target for each team:
            for (int k = 0; k < numberOfTeams(); k++) {
                if (k != team) {
                    double edgeWeight = wins[team] + remaining[team] - wins[k];
                    // for negative weights
                    edgeWeight = edgeWeight > 0 ? edgeWeight : 0;
                    flowNetwork.addEdge(new FlowEdge(remainingMatches + k, sink, edgeWeight));
                }
            }

            FordFulkerson maxFlow = new FordFulkerson(flowNetwork, source, sink);

            // check if team is eliminated by seeing if all flow from source is not full
            for (FlowEdge edge : flowNetwork.adj(source)) {
                if (edge.flow() != edge.capacity()) eliminated[team] = true;
            }

            for (int l = 0; l < numberOfTeams(); l++) {
                if (l != team && maxFlow.inCut(remainingMatches + l)) keepTrackOfEliminators(team, teamNames[l]);
            }
        }
    }

    private void keepTrackOfEliminators(int victim, String abuser) {
        Set<String> result = eliminatorList.containsKey(victim) ? eliminatorList.get(victim) : new TreeSet<>();
        result.add(abuser);
        eliminatorList.put(victim, result);
    }

    private void trivialElimination(int team) {
        for (int target = 0; target < numberOfTeams(); target++) {
            if (team != target && wins[target] > (wins[team] + remaining[team])) {
                eliminated[team] = true;
                keepTrackOfEliminators(team, teamNames[target]);
            }
        }
    }

    private void readInData(In in, int totalTeams) {
        wins = new int[totalTeams];
        losses = new int[totalTeams];
        remaining = new int[totalTeams];
        against = new int[totalTeams][totalTeams];
        eliminated = new boolean[totalTeams];
        teamNames = new String[totalTeams];
        eliminatorList = new HashMap<>();

        // load in initial data
        for (int i = 0; i < totalTeams; i++) {
            teamNames[i] = in.readString();
            teamIndex.put(teamNames[i], i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < totalTeams; j++) {
                against[i][j] = in.readInt();
            }
        }
    }

    private int numberOfMatches() {
        int count = 0;
        for (int i = 0; i < numberOfTeams(); i++) {
            for (int j = i; j < numberOfTeams(); j++) {
                if (i != j) count++;
            }
        }
        return count;
    }

    public int numberOfTeams() {
        return teamIndex.size();
    }                       // number of teams

    public Iterable<String> teams() {
        Bag<String> result = new Bag<>();
        for (String entry : teamIndex.keySet()) {
            result.add(entry);
        }
        return result;
    }                               // all teams

    public int wins(String team) {
        teamCheck(team);
        return wins[teamIndex.get(team)];
    }                     // number of wins for given team

    public int losses(String team) {
        teamCheck(team);
        return losses[teamIndex.get(team)];
    }                   // number of losses for given team

    public int remaining(String team) {
        teamCheck(team);
        return remaining[teamIndex.get(team)];
    }                // number of remaining games for given team

    public int against(String team1, String team2) {
        teamCheck(team1);
        teamCheck(team2);

        return against[teamIndex.get(team1)][teamIndex.get(team2)];
    }   // number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        teamCheck(team);
        return eliminated[teamIndex.get(team)];
    }             // is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        teamCheck(team);
        return eliminatorList.get(teamIndex.get(team));
    } // subset R of teams that eliminates given team; null if not eliminated

    private void teamCheck(String name) {
        if (!teamIndex.containsKey(name)) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}