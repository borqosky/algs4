import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// import java.util.Arrays;
// import java.util.stream.IntStream;

public class Percolation {
    /**
     * create n-by-n grid, with all sites blocked
     * n specifies n by n grid
     */
    private static final byte OPEN = 1;
    private static final byte TOP = 2;
    private static final byte BOTTOM = 4;

    private final WeightedQuickUnionUF uf;
    private final int n;

    private byte[] sites;
    private boolean isPercolate = false;
    private int openSites = 0;

    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException("n " + n + " is less than 1");

        sites = new byte[n * n];
        uf = new WeightedQuickUnionUF(n * n);

        this.n = n;
    }

    /**
     * open site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int current = xyTo1D(row, col);
            byte currentStatus = getStatus(current);
            sites[current] = OPEN;
            openSites++;

            if (validate(row, col - 1, row, col) && isOpen(row, col - 1)) { // left
                int left = xyTo1D(row, col - 1);
                byte leftStatus = getStatus(left);
                uf.union(current, left);
                sites[uf.find(current)] |= leftStatus | currentStatus;
                currentStatus = getStatus(current);
            }
            if (validate(row, col + 1, row, col) && isOpen(row, col + 1)) { // right
                int right = xyTo1D(row, col + 1);
                byte rightStatus = getStatus(right);
                uf.union(current, right);
                sites[uf.find(current)] |= rightStatus | currentStatus;
                currentStatus = getStatus(current);
            }
            if (validate(row -1, col, row, col) && isOpen(row - 1, col)) { // up
                int up = xyTo1D(row - 1, col);
                byte upStatus = getStatus(up);
                uf.union(current, up);
                sites[uf.find(current)] |= upStatus | currentStatus;
                currentStatus = getStatus(current);
            }
            if (validate(row + 1, col, row, col) && isOpen(row + 1, col)) { // down
                int down = xyTo1D(row + 1, col);
                byte downStatus = getStatus(down);
                uf.union(current, down);
                sites[uf.find(current)] |= downStatus | currentStatus;
                currentStatus = getStatus(current);
            }

            if (row == 1)
                sites[uf.find(current)] |= TOP | currentStatus;
            else if (row == n)
                sites[uf.find(current)] |= BOTTOM | currentStatus;

            if ((sites[uf.find(current)] & (TOP | BOTTOM)) == (TOP | BOTTOM))
                isPercolate = true;
        }
    }

    /**
     * is site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("row or column is not between 1 and " + n);
        return (sites[xyTo1D(row, col)] & OPEN) == OPEN;
    }

    /**
     * is site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && (sites[uf.find(xyTo1D(row, col))] & TOP) == TOP;
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
        return openSites > 0 && isPercolate;
    }

    private boolean validate(int nextRow, int nextCol, int row, int col) {
        if (nextRow < 1 || nextCol < 1 || nextRow > n || nextCol > n)
            return false;
        else if (nextCol > col && col == n)
            return false;
        else if (nextRow > row && row == n)
            return false;
        return true;
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private byte getStatus(int p) {
        int root = uf.find(p);
        return sites[root];
    }
}
