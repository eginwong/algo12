import edu.princeton.cs.algs4.*;

import java.util.*;
import java.util.Stack;

public class BaseballElimination {
    private FlowNetwork flowNetwork;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private boolean[] eliminated;
    private Map<String, Integer> teamIndex = new HashMap<>();

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int totalTeams = in.readInt();
        wins = new int[totalTeams];
        losses = new int[totalTeams];
        remaining = new int[totalTeams];
        against = new int[totalTeams][totalTeams];
        eliminated = new boolean[totalTeams];

        int maxWins = 0;
        // load in initial data
        for (int i = 0; i < totalTeams; i++) {
            teamIndex.put(in.readString(), i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            if (wins[i] > maxWins) maxWins = wins[i];

            for (int j = 0; j < totalTeams; j++) {
                against[i][j] = in.readInt();
            }
        }

        // elimination
        for (int team = 0; team < totalTeams; team++) {
            // trivial elimination
            if (maxWins > (wins[team] + remaining[team])) eliminated[team] = true;
                // non-trivial elimination
            else {
                // s + t + teams - 1 + possible matches
                int nodes = 1 + totalTeams + numberOfMatches();
                FlowNetwork flowNetwork = new FlowNetwork(nodes);
                //WHEEE
                //FordFulkerson
            }

        }


        //    use Ford Fulkerson?
    }         // create a baseball division from given filename in format specified below

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
        Bag result = new Bag();
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
        return new Stack<>();
    } // subset R of teams that eliminates given team; null if not eliminated

    private void teamCheck(String name) {
        if (!teamIndex.containsKey(name)) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        //for (String team : division.teams()) {
        //    if (division.isEliminated(team)) {
        //        StdOut.print(team + " is eliminated by the subset R = { ");
        //        for (String t : division.certificateOfElimination(team)) {
        //            StdOut.print(t + " ");
        //        }
        //        StdOut.println("}");
        //    }
        //    else {
        //        StdOut.println(team + " is not eliminated");
        //    }
        //}
    }
}