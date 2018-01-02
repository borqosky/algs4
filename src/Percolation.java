import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// import java.util.Arrays;
// import java.util.stream.IntStream;

public class Percolation {
    /**
     * create n-by-n grid, with all sites blocked
     * n specifies n by n grid
     */
    private final WeightedQuickUnionUF uf;
    private final int n, top, bottom;

    private boolean[] sites;
    private int openSites = 0;

    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException("n " + n + " is less than 1");

        sites = new boolean[n * n];
        uf = new WeightedQuickUnionUF(n * n + 2);

        this.n = n;
        top = 0;
        bottom = n * n + 1;

        // IntStream.range(0, n).forEach(q -> uf.union(top, q));
        for (int q = 1; q <= n; q++)
            uf.union(top, q);
        // IntStream.range(n * n - n, n * n).forEach(q -> uf.union(bottom, q));
        for (int q = bottom - 1; q <= n * n - n; q--)
            uf.union(bottom, q);
    }

    /**
     * open site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int currPos = findSite(row, col);
            sites[currPos] = true;
            openSites++;

            int[][] positions  = {{row, col}, {row, col - 1}, {row, col + 1}, {row - 1, col}, {row + 1, col}};
            // positions = Arrays.stream(positions).filter(p -> p >= 0 && p < sites.length).filter(p -> sites[p]).toArray();

            for (int[] nextPos: positions) {
                if (isRowOrColumnValid(nextPos, row, col)) {
                    int nextRow = nextPos[0];
                    int nextColumn = nextPos[1];
                    int next = findSite(nextRow, nextColumn);
                    if (nextRow == n)
                        if (!percolates())
                            uf.union(next, bottom);
                    else if (isOpen(nextRow, nextColumn))
                        uf.union(next, currPos);
                    else if (!isFull(nextRow, nextColumn) && uf.connected(next, top))
                        uf.union(next, top);
                }
            }
        }
    }

    /**
     * is site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        int site = findSite(row, col);
        validate(site);
        return sites[site-1];
    }

    /**
     * is site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && uf.connected(top, findSite(row, col));
    }

    /**
     * number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    private boolean isRowOrColumnValid(int[] nextPos, int row, int col) {
        int nextSite = findSite(nextPos[0], nextPos[1]);

        int r = nextSite % n;
        int nextRow = r + 1;
        int nextCol = n - r;

        return row == nextRow || col == nextCol;
    }

    private int findSite(int row, int col) {
        return (row - 1) * n + (col - 1) + 1;
    }

    private void validate(int s) {
        if (s < 1 || s > n * n)
            throw new IllegalArgumentException("site " + s + " is not between 0 and " + n);
    }
}
