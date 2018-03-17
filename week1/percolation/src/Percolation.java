import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int QUICK_UNION_OFFSET = 1;
    private static final int VIRTUAL_OFFSET = 2;
    private static final int VIRTUAL_TOP = 0;
    private int gridSize; // grid size
    private boolean[][] openSites; // which sites are open
    private WeightedQuickUnionUF weightedQuickUnionUF; // which sites are connected to which other sites
    private WeightedQuickUnionUF weightedQuickUnionUFSingle; // to avoid backwash, remove virtual bottom
    private int virtualBottom;
    private int openCounter = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid number");
        } else {
            gridSize = n;
            openSites = new boolean[n + VIRTUAL_OFFSET][n + VIRTUAL_OFFSET];
            virtualBottom = gridSize * gridSize + QUICK_UNION_OFFSET;
            weightedQuickUnionUF = new WeightedQuickUnionUF(virtualBottom + QUICK_UNION_OFFSET);
            weightedQuickUnionUFSingle = new WeightedQuickUnionUF(virtualBottom);

            // need to set up list of open sites.
            for (int row = 0; row < n + VIRTUAL_OFFSET; row++) {
                for (int col = 0; col < n + VIRTUAL_OFFSET; col++) {
                    openSites[row][col] = row == 0 || row == n + 1;
                }
            }
        }
    } // create n-by-n grid, with all sites blocked

    // test client
    public static void main(String[] args) {
        // test client
    }

    // The open() method should do three things.
    public void open(int row, int col) {
        // First, it should validate the indices of the site that it receives.
        if (isValid(row) && isValid(col) && !isOpen(row, col)) {
            // Second, it should somehow mark the site as open.
            openSites[row][col] = true;

            // Third, it should perform some sequence of WeightedQuickUnionUF operations that links the site in question to its open neighbors.
            int newlyOpen = xyTo1D(row, col);

            if (openSites[row - 1][col]) {
                weightedQuickUnionUF.union(newlyOpen, xyTo1D(row - 1, col));
                weightedQuickUnionUFSingle.union(newlyOpen, xyTo1D(row - 1, col));
            }
            if (openSites[row][col - 1]) {
                weightedQuickUnionUF.union(newlyOpen, xyTo1D(row, col - 1));
                weightedQuickUnionUFSingle.union(newlyOpen, xyTo1D(row, col - 1));
            }
            if (openSites[row][col + 1]) {
                weightedQuickUnionUF.union(newlyOpen, xyTo1D(row, col + 1));
                weightedQuickUnionUFSingle.union(newlyOpen, xyTo1D(row, col + 1));
            }
            if (openSites[row + 1][col]) {
                weightedQuickUnionUF.union(newlyOpen, xyTo1D(row + 1, col));
                if (row != gridSize) {
                    weightedQuickUnionUFSingle.union(newlyOpen, xyTo1D(row + 1, col));
                }
            }
            openCounter++;
        }

    } // open site (row, col) if it is not open already

    public boolean isOpen(int row, int col) {
        return isValid(row) && isValid(col) && openSites[row][col];
    } // is site (row, col) open?

    public boolean isFull(int row, int col) {
        // Check if open site
        if (isValid(row) && isValid(col) && isOpen(row, col)) {
            return weightedQuickUnionUFSingle.connected(VIRTUAL_TOP, xyTo1D(row, col));
        } else {
            return false;
        }
    } // is site (row, col) full?

    public int numberOfOpenSites() {
        return openCounter;
    } // number of open sites

    public boolean percolates() {
        return weightedQuickUnionUF.connected(VIRTUAL_TOP, virtualBottom);
    }

    private boolean isValid(int position) {
        if (position <= 0 || position > gridSize) {
            throw new IllegalArgumentException("row index i out of bounds");
        }
        return true;
    }

    private int xyTo1D(int x, int y) {
        // When row is 0
        if (x == VIRTUAL_TOP || y == VIRTUAL_TOP) {
            return VIRTUAL_TOP;
        }
        if (x == (gridSize + 1)) {
            return virtualBottom;
        }
        return x * gridSize + y - gridSize;
    }
}
