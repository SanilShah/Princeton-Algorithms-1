/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double mu, std, conLo, conHi;

    private double[] run;

    public PercolationStats(int n, int t) {
        if(n <= 0)
            throw new IllegalArgumentException("Invalid Argument: non-positive N.");
        if(t <= 0)
            throw new IllegalArgumentException("Invalid Argument: non-positive T.");

        this.run = new double[t];

        for(int i=0; i<t; i++) {
            this.run[i] = simulation(n);
        }

        if (t == 1) {
            mu = this.run[0];
            std = Double.NaN;
        }
        else {
            mu = StdStats.mean(this.run);
            std = StdStats.stddev(this.run);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mu;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return std;
    }

    //low endpoint of 95% confidence interval
    public double confidenceLo() {
        return  mu - ((1.96*std) / Math.sqrt(this.run.length));
    }
    //
    // high endpoint of 95% confidence interval
     public double confidenceHi() {
         return  mu + ((1.96*std) / Math.sqrt(this.run.length));
     }

    private double simulation(int n) {
        int i, j, count = 0;
        double p;

        Percolation perc = new Percolation(n);

        do {
            i = StdRandom.uniform(1, n+1);
            j = StdRandom.uniform(1, n+1);

            if(!perc.isOpen(i,j)) {
                perc.open(i,j);
                count++;
            }
        }
        while (!perc.percolates());

        p = (count * 1.0) / (n*n);

        return p;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);

        System.out.println("mean                     =" + ps.mean());
        System.out.println("stdev                    =" + ps.stddev());
        System.out.println("95% confidence interval  =" + "[" + ps.confidenceLo() +", " + ps.confidenceHi() + "]");
    }
}
