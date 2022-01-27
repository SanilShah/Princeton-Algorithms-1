/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int vTop, vBottom;
    private WeightedQuickUnionUF uf;
    private boolean[][] grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if(n<=0)
            throw new IllegalArgumentException("Invalid argument: non-positive N");

        this.grid = new boolean[n][n];
        this.uf = new WeightedQuickUnionUF(n*n+2);
        this.vTop = 0;
        this.vBottom = n*n+1;

        // Connect vtop and vbottom to top and bottom nodes
        if(n>1) {
            for(int k=1; k<=n; k++) {
                this.uf.union(xyto1D(1,k), vTop);
                this.uf.union(xyto1D(n,k), vBottom);
            }
        }
    }

    // return if input is within grid range
    private boolean valid(int i) {
        int n = grid.length;
        return(i>0 && i<=n);
    }

    // convert 2D grid to 1D number. (Horizontal ascending numbering)
    private int xyto1D(int i, int j) {
        int n = grid.length;

        if(!valid(i))
            throw  new IndexOutOfBoundsException("row i out of bounds.");
        if(!valid(j))
            throw  new IndexOutOfBoundsException("column j out of bounds.");
        return ((i-1)*n + j);
    }

    // public void printGrid()
    // {
    //     // Loop through all rows
    //     for (int i = 1; i <= grid.length; i++) {
    //         // Loop through all elements of current row
    //         for (int j = 1; j <= grid.length; j++) {
    //             System.out.print(grid[i-1][j-1] + Integer.toString(xyto1D(i,j)) + " ");
    //         }
    //         System.out.println('\n');
    //     }
    // }

    // // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        grid[row-1][col-1] = true;

        if(grid.length == 1) {
            this.uf.union(xyto1D(row, col), vTop);
            this.uf.union(xyto1D(row, col), vBottom);
        }
        int p = xyto1D(row,col);
        unionNeighbor(p, row+1, col);
        unionNeighbor(p, row-1, col);
        unionNeighbor(p, row, col+1);
        unionNeighbor(p, row, col-1);
    }
    //
    // // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row-1][col-1];
    }

    // // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row,col) && uf.connected(vTop, xyto1D(row, col));
    }
    //
    // // returns the number of open sites
    public int numberOfOpenSites() {
        int openSites = 0;
        for (int i=1; i<= grid.length; i++)
            for (int j = 1; j <= grid.length; j++)
                if (isOpen(i, j))
                    openSites += 1;
        return openSites;
    }

    private void unionNeighbor( int p, int i, int j) {
        if(!valid(i) || !valid(j))
            return;
        if(isOpen(i,j))
            uf.union(p, xyto1D(i,j));
    }
    //
    // // does the system percolate?
    public boolean percolates() {
        return uf.connected(vTop, vBottom);
    }

    // public static void main(String[] args) {
    //     Percolation p1 = new Percolation(3);
    //
    //     p1.open(1,1);
    //     p1.open(2,1);
    //     p1.open(2,2);
    //     p1.open(3,2);
    //     p1.printGrid();
    //     System.out.println(p1.percolates());
    //     System.out.println(p1.numberOfOpenSites());
    // }
}
