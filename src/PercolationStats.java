import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final int t;
    private final double[] xn;

    public PercolationStats(int n, int t) {
        // perform `t` independent computational experiments on an `n`-by-`n` grid
        if (n < 1 || t < 1)
            throw new IllegalArgumentException("n: " + n + "or t: " + t + " are less than 1");
        this.t = t;
        this.xn = new double[t];
        for (int i = 0; i < t; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
            }

            xn[i] = (double) perc.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(xn);
    }

    public double stddev() {
        return StdStats.stddev(xn);
    }

    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(t);
    }

    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(t);
    }

    public static void main(String[] args) { // test client, described below
        int n = Integer.parseInt(args[0]); // n-by-n percolation system
        int t = Integer.parseInt(args[1]); // t computational experiments
        PercolationStats percTest = new PercolationStats(n, t);
        StdOut.println("mean = " + percTest.mean());
        StdOut.println("stddev = " + percTest.stddev());
        StdOut.println("95% confidence interval = " + percTest.confidenceLo()
                + ", " + percTest.confidenceHi());
    }
}
