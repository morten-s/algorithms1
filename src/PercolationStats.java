import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
    private double[] meanArray;
    private int trials;
    private double meanNumber, stddevNumber;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int t){
        if(n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        Percolation pTest;
        trials = t;
        meanNumber = 0;
        stddevNumber = 0;
        meanArray = new double[trials]; 
        for(int i = 0; i < trials; i++){
            pTest = new Percolation(n);
            int rolls = 0;
            while(!pTest.percolates()){
                int row = StdRandom.uniform(1, n+1); // adjust to startrow = 1
                int col = StdRandom.uniform(1, n+1);
                pTest.open(row, col);
                rolls++;
            }
            // fraction mean number opensites/gridsize
            meanArray[i] = (double)pTest.numberOfOpenSites() / (double)(n*n);
        //    System.out.println("xover:" + ((double)pTest.numberOfOpenSites() / (double)(n*n)) + " Opensites: " + pTest.numberOfOpenSites());
           /*
            System.out.println(pTest.QUarray.count());
            printGrid(pTest, nArg);*/
        }
    	//System.out.println("Trials: " + trials + "gridsize: " + n + "*" + n);
    }

    // sample mean of percolation threshold
    public double mean(){
        if( meanNumber == 0){
            meanNumber = StdStats.mean(meanArray);
        }
        return ( meanNumber );
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        if( stddevNumber == 0){
            stddevNumber = StdStats.stddev(meanArray);
        }
        return ( stddevNumber );
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return ( mean() - ((1.96f * stddev()) / Math.sqrt(trials)) );
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return ( mean() + ((1.96f * stddev()) / Math.sqrt(trials)) );
    }

   // test client (see below)
   public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats pstat = new PercolationStats(n, t);
        StdOut.println("mean = " + pstat.mean());
        StdOut.println("stddev = " + pstat.stddev());
        StdOut.println("95% confidence interval = ["+ pstat.confidenceLo() + ", " + pstat.confidenceHi() + "]");

   }

}