import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Required class from assignment
 *
 * @author eric.wong
 */
public class PercolationStats {
    private static final int OFFSET = 1;
    private static final double CONFIDENCE_INTERVAL = 1.96;
    private final double[] results;
    private final int trials;
    private double meanVal;
    private double stddevVal;
    private double confidenceLoVal;
    private double confidenceHiVal;

    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new java.lang.IllegalArgumentException("Bad Input");
        }
        trials = t;

        int counter = 0;
        results = new double[trials];
        Percolation percolation;
        while (counter < trials) {
            percolation = new Percolation(n);
            int randomX, randomY;
            while (!percolation.percolates()) {
                randomX = StdRandom.uniform(n) + OFFSET;
                randomY = StdRandom.uniform(n) + OFFSET;
                if (!percolation.isOpen(randomX, randomY)) {
                    percolation.open(randomX, randomY);
                }
            }
            // calculate percolation threshold
            results[counter] = (double) percolation.numberOfOpenSites() / (n * n);
            counter++;
        }
    } // perform trials independent experiments on an n-by-n grid

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Incorrect number of parameters");
        }
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.println("mean                        = " + percolationStats.mean());
        StdOut.println("stddev                      = " + percolationStats.stddev());
        StdOut.println("95% confidence interval     = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");
    }

    public double mean() {
        if (meanVal == 0.0d) {
            meanVal = StdStats.mean(results);
        }
        return meanVal;
    } // sample mean of percolation threshold

    public double stddev() {
        if (stddevVal == 0.0d) {
            stddevVal = Double.NaN;
            if (trials != 1) {
                stddevVal = StdStats.stddev(results);
            }
        }
        return stddevVal;
    } // sample standard deviation of percolation threshold

    public double confidenceLo() {
        if (confidenceLoVal == 0.0d) {
            confidenceLoVal = mean() - (CONFIDENCE_INTERVAL * stddev() / Math.sqrt(trials));
        }
        return confidenceLoVal;
    } // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        if (confidenceHiVal == 0.0d) {
            confidenceHiVal = mean() + (CONFIDENCE_INTERVAL * stddev() / Math.sqrt(trials));
        }
        return confidenceHiVal;
    } // high endpoint of 95% confidence interval
}