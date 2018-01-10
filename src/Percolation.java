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

        sites = new boolean[n * n + 2];
        uf = new WeightedQuickUnionUF(n * n + 2);

        this.n = n;
        top = 0;
        bottom = n * n + 1;

        sites[top] = true;
        sites[bottom] = true;

        // IntStream.range(0, n).forEach(q -> uf.union(top, q));
        for (int q = 1; q <= n; q++)
            uf.union(top, q);
        // IntStream.range(n * n - n, n * n).forEach(q -> uf.union(bottom, q));
        for (int q = bottom - n; q <= bottom - 1; q++)
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

            int left = findSite(row, col - 1);
            int right = findSite(row, col + 1);
            int up = findSite(row - 1, col);
            int down = findSite(row + 1, col);

            if (isRowOrColumnValid(row, col - 1, row, col) && sites[left]) // left
                uf.union(currPos, left);
            if (isRowOrColumnValid(row, col + 1, row, col) && sites[right]) // right
                uf.union(currPos, right);
            if (isRowOrColumnValid(row -1, col, row, col) && sites[up]) // up
                uf.union(currPos, up);
            if (isRowOrColumnValid(row + 1, col, row, col) && sites[down]) // down
                uf.union(currPos, down);
        }
    }

    /**
     * is site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("row or column is not between 1 and " + n);
        return sites[findSite(row, col)];
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
        return openSites > 0 && uf.connected(top, bottom);
    }

    private boolean isRowOrColumnValid(int nextRow, int nextCol, int row, int col) {
        if (nextRow < 1 || nextCol < 1 || nextRow > n || nextCol > n)
            return false;
        else if (nextCol < col && col == 1)
            return false;
        else if (nextCol > col && col == n)
            return false;
        else if (nextRow < row && row == 1)
            return false;
        else if (nextRow > row && row == n)
            return false;
        return true;
    }

    private int findSite(int row, int col) {
        return (row - 1) * n + (col - 1) + 1;
    }
}
