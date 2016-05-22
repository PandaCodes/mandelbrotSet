import java.awt.*;

public class Mondelbrot {
    private Dimension size;
    private int matrix[][];
    static private final int rMinSq = 4;
    public final int kMax = 400;//167717215;

    public Mondelbrot (Dimension size) {
        this.size = size;
        matrix = new int[size.width][size.height];
    }

    public int[][] lastMatrix() { return matrix; }
    public int[][] createMatrix(double x0, double y0, double m) {
        for (int i = 0; i < size.width; i++)
            for (int j = 0; j < size.height; j++){
               matrix[i][j] = isDiverges(x0 + m*i, y0 + m*j);
            }
        return  matrix;
    }

    private  int isDiverges(double x, double y) {
        double phi = Math.atan2(y, x - 0.25);
        double R = Math.sqrt((x-0.25)*(x-0.25) + y*y);
        double RCor = 0.5 - 0.5*Math.cos(phi);
        if (R <= RCor) return kMax;
        double xk = x;
        double yk = y;
        double hlp;
        for (int i = 0; i < kMax; i++) {
            hlp= xk;
            xk = xk*xk - yk*yk + x;
            yk = 2*hlp*yk + y;
            if (xk*xk + yk*yk > rMinSq)
                return i;
        }
        return kMax;
    }
}
