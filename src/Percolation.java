
//import app.PercolationStats;
import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    static private final int OPEN = 1, CLOSED = 0;
    static private final int UP = -1, DOWN = 1, LEFT = -1, RIGHT = 1;
    public static void main(String[] args) {
        
        //PercolationStats tst = new PercolationStats(30, 700);
        int nArg = 1;
        int tArg = 10;

        Percolation pTest = new Percolation(nArg);
        pTest.open(1, 1);
        System.out.println("Perculates?: " + pTest.percolates());

/*
        for(int tSamples = 0; tSamples < tArg; tSamples++){
            int row = (int)(StdRandom.uniform()*nArg) + 1; // adjust to startrow = 1
            int col = (int)(StdRandom.uniform()*nArg) + 1;
            pTest.open(row, col);
            System.out.println(pTest.QUarray.count());
            printGrid(pTest, nArg);
        }

        /*   for(int x = 6; x < pTest.gridSize; x++){
            pTest.QUarray.union(x, 5);
        }
       
      System.out.println("before" + pTest.QUarray.count());
      pTest.open(2, 3);
      System.out.println("after" + pTest.QUarray.count());
      printGrid(pTest, nArg);

      System.out.println("before" + pTest.QUarray.count());
      pTest.open(2, 2);
      System.out.println("after" + pTest.QUarray.count());
      printGrid(pTest, nArg);

      pTest.open(3, 2);
      System.out.println(pTest.QUarray.count());
      printGrid(pTest, nArg);
      System.out.println("f2.1 " +pTest.isFull(2, 3));
      
        printGrid(pTest, nArg);
         System.out.println("Perculates?: " + pTest.percolates() + " Sets: " + pTest.QUarray.count() );
         System.out.println("Opensites: " + pTest.numberOfOpenSites());
       */  
    }
    //print grid in aschii for debug
    private static void printGrid(Percolation pTest, int nArg){
        for(int i = 1; i <= nArg; i++){
            String var = "";
            for(int i1 = 1; i1 <= nArg; i1++){
            
                if(pTest.isOpen(i, i1)){
                    var += '1';
                }else{
                    var += '0';
                }
            }
            System.out.println(var);
    
        }
    }
    
    private byte[][] grid;
    private int openSites;
    private int gridSize;
    private int gridSide;
    private int virTop,virBot;
    private WeightedQuickUnionUF QUarray;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n <= 0){ 
            throw new IllegalArgumentException();
        }
        gridSize = n*n;
        gridSide = n;
        grid = new byte[n][n];
        openSites = 0;
        QUarray = new WeightedQuickUnionUF((gridSize)+2); // grid + 2 virtual elements in top and bottom
        virTop = gridSize;
        virBot = gridSize + 1;
        
        
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        //check if row col is within limits (1,1)(n,n)
        if(row <= 0 || col <= 0 || row > gridSide || col > gridSide){
            throw new IllegalArgumentException();
        } 
        
        if(grid[row-1][col-1] != OPEN){
            grid[row-1][col-1] = OPEN;
            openSites++;
            int QUelementIndex = ((row-1)*gridSide)+(col-1);
            if(row == 1){
                // hvis feltet er i row 1 eller row n (top / bund) tilknyttes der et  
                // rod element som bruges i optimeret søgning i perculation fra hele row 0 til bund
                QUarray.union(QUelementIndex, virTop);
            }
            if(row == gridSide){
                QUarray.union(QUelementIndex, virBot);
            }
            //check alle omkredsende felter i grid left righ top bottom for tilknyttede felter
            //via a chain of neighboring (left, right, up, down) open sites
            //add feltet til et eksisterende set hvis der er tilknyttede felter
            if(((row-1+UP) >= 0) && (grid[(row-1+UP)][(col-1)] == OPEN)){
                QUarray.union(QUelementIndex, ((row-1+UP)*gridSide)+(col-1)); //If up is open connect to it
            }
            if(((row-1+DOWN) <= gridSide-1) && (grid[(row-1+DOWN)][(col-1)] == OPEN)){
                QUarray.union(QUelementIndex, ((row-1+DOWN)*gridSide)+(col-1)); //If down is open connect to it
            }
            if(((col-1+LEFT) >= 0) && (grid[(row-1)][(col-1+LEFT)] == OPEN)){
                QUarray.union(QUelementIndex, ((row-1)*gridSide)+(col-1+LEFT)); //If left is open connect to it
            }
            if(((col-1+RIGHT) <= gridSide-1) && (grid[(row-1)][(col-1+RIGHT)] == OPEN)){
                QUarray.union(QUelementIndex, ((row-1)*gridSide)+(col-1+RIGHT)); //If right is open connect to it
            }
        }
        return;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if(row <= 0 || col <= 0 || row > gridSide || col > gridSide){
            throw new IllegalArgumentException();
        } 
        return (grid[row-1][col-1] == OPEN);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if(row <= 0 || col <= 0 || row > gridSide || col > gridSide){
            throw new IllegalArgumentException();
        } 
        return( (QUarray.find(virTop) == QUarray.find( ((row-1)*gridSide)+(col-1) )) 
                    && isOpen(row, col) );
    }

    // returns the number of open sites
    public int numberOfOpenSites(){        
        return ( openSites );
    }

    // does the system percolate?
    public boolean percolates(){
        //if virtual top and virtual bottom in same set
        return( QUarray.find(virTop) == QUarray.find(virBot) );
    }

    // test client (optional)
    //public static void main(String[] args){}
}