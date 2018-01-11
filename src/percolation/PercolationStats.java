package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean, stddev, sqrtT;

    public PercolationStats(int n, int t) {
        // perform `t` independent computational experiments on an `n`-by-`n` grid
        if (n < 1 || t < 1)
            throw new IllegalArgumentException("n: " + n + "or t: " + t + " are less than 1");

        double[] xn = new double[t];

        for (int i = 0; i < t; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
            }
            xn[i] = (double) perc.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(xn);
        stddev = StdStats.stddev(xn);
        sqrtT = Math.sqrt(t);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - (CONFIDENCE_95 * stddev) / sqrtT;
    }

    public double confidenceHi() {
        return mean + (CONFIDENCE_95 * stddev) / sqrtT;
    }

    public static void main(String[] args) { // test client, described below
        int n = Integer.parseInt(args[0]); // n-by-n percolation system
        int t = Integer.parseInt(args[1]); // t computational experiments

        Stopwatch stopwatch = new Stopwatch();
        PercolationStats percTest = new PercolationStats(n, t);
        double elapsedTime = stopwatch.elapsedTime();


        StdOut.println("mean = " + percTest.mean());
        StdOut.println("stddev = " + percTest.stddev());
        StdOut.println("95% confidence interval = [" + percTest.confidenceLo()
                + ", " + percTest.confidenceHi() + "]");
        StdOut.println("Execution time for T=" + t + " and N=" + n + " took " + elapsedTime + "s");
    }
}
