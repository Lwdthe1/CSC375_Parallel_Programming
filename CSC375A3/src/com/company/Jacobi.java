package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created by keithmartin on 11/25/15.
 */
public class Jacobi extends RecursiveAction {
    static final double EPSILON = 0.001; // convergence criterion
    final Cell root;
    private ForkJoinPool pool = new ForkJoinPool();
    final int maxSteps;
    Jacobi(AlloyRegion region, double[][] A, double[][] B, int firstRow, int lastRow, int firstCol, int lastCol, int maxSteps) {
        this.maxSteps = maxSteps;
        root = new Cell(region, A, B, firstRow, lastRow, firstCol, lastCol);
    }

    public void compute(){
        for (int i = 0; i < maxSteps; ++i) {
            pool.invoke(root);
            //System.out.println("MAXDIFF: " + root.getRawResult());
            if (root.getRawResult() < EPSILON) {
                System.out.println("Converged");
                return;
            }
            else root.reinitialize();
        }
        System.out.println("did not converge");
    }
}
