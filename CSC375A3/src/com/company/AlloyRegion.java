package com.company;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by keithmartin on 11/26/15.
 */
public class AlloyRegion {

    final private int topLeftS;
    final private int bottomRightT;
    private int width;
    private int length;
    private int numNeighbors = 0;
    private MetalCellComposition region[][];

    public AlloyRegion(int topLeftS, int bottomRightT, int width, int length, double c1, double c2, double c3) {
        this.topLeftS = topLeftS;
        this.bottomRightT = bottomRightT;
        this.width = width;
        this.length = length;
        this.region = initRegion(c1, c2, c3);
    }


    public MetalCellComposition[][] initRegion(double c1, double c2, double c3) {
        MetalCellComposition region[][] = new MetalCellComposition[width][length];
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region[i].length; j++) {
                MetalCellComposition comp = new MetalCellComposition(i, j, c1, c2, c3);
                region[i][j] = comp;
            }
        }
        return region;
    }

    public double[][] createRegions() {
        double doubleRegion[][] = new double[width][length];
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region[i].length; j++) {
                doubleRegion[i][j] = region[i][j].getCellTemp();
            }
        }
        return doubleRegion;
    }

    public MetalCellComposition[][] getRegion() {
        return region;
    }


    public int getTopLeftS() {
        return topLeftS;
    }

    public int getBottomRightT() {
        return bottomRightT;
    }


}
