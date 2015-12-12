package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by keithmartin on 11/25/15.
 */
public class Cell extends ForkJoinTask<Double> {

    volatile double maxDiff;
    JacobiPanel panel = new JacobiPanel();
    private Color red = new Color(221, 17, 17);
    private final double[][] RegionA;
    private final double[][] RegionB;
    private final int loRow;
    private final int hiRow;
    private final int loCol;
    private final int hiCol;
    private int count = 0;
    private int steps = 0;
    private AlloyRegion region;
    private ForkJoinPool pool = new ForkJoinPool();
    private double a [][];
    private double b [][];


    Cell(AlloyRegion region, double[][] RegionA, double[][] RegionB,
         int loRow, int hiRow, int loCol, int hiCol) {
        this.RegionA = RegionA;   this.RegionB = RegionB;
        this.loRow = loRow; this.hiRow = hiRow;
        this.loCol = loCol; this.hiCol = hiCol;
        this.region = region;
    }





    public Double getRawResult() {
        return maxDiff;
    }


    protected void setRawResult(Double value) {
        maxDiff = value;
    }

    @Override
    protected boolean exec() {
        ArrayList <TemperatureCalculator> tasks = new ArrayList<TemperatureCalculator>();
        boolean doFlipAtoB = (steps++ % 2) == 0;
        a = (doFlipAtoB)? RegionA : RegionB;
        b = (doFlipAtoB)? RegionB : RegionA;
        double md = 0.0;
        for (int i = loRow; i <= hiRow; ++i) {
            for (int j = loCol; j <= hiCol; j++) {
                if (i == 0 && j == 0) {
                    b[i][j] = region.getTopLeftS();
                    region.getRegion()[i][j].setCellTemp(region.getTopLeftS());
                } else if (i == hiRow - 1 && j == hiCol - 1) {
                    b[i][j] = region.getBottomRightT();
                    region.getRegion()[i][j].setCellTemp(region.getBottomRightT());
                } else {
                    //System.out.println("[ " + i + " " + j + " " + region.getRegion()[i][j-1].getCellTemp());
                    //Calculate cell temp for b[i][j] below
                    TemperatureCalculator temp = new TemperatureCalculator(i);
                    tasks.add(temp);
                }
                md = Math.max(md, Math.abs(b[i][j] - a[i][j]));
            }
        }
        pool.invokeAll(tasks);
        setRawResult(md);
        return true;
    }


    public double[][] getRegionB() {
        return RegionB;
    }

    public class TemperatureCalculator implements Callable <Double> {

        private int i;

        public TemperatureCalculator(int i) {
            this.i = i;
        }



        public void calculateTemperature(int i) {

            double sigmaTemperatureFinal = 0;
            double sigmaTemperature = 0;
            //System.out.println(hiCol);

            for (int j = 1; j <= hiCol; j++) {
                int numN = 1;
                double numNeighbors = 0;
                sigmaTemperatureFinal = 0;
                sigmaTemperature = 0;
                while(numN != 4) {
                    if (!(i - 1 < 0)) {
                        sigmaTemperature += region.getRegion()[i - 1][j].getCellTemp() * (region.getRegion()[i - 1][j].getMetal(numN));
                        numNeighbors++;
                    }
                    if (!(i + 1 > hiRow - 1)) {
                        sigmaTemperature += region.getRegion()[i + 1][j].getCellTemp() * (region.getRegion()[i + 1][j].getMetal(numN));
                        numNeighbors++;
                    }
                    if (!(j - 1 < 0)) {
                        //System.out.println("[" + i + " " + j + " " + region.getRegion()[i][j - 1].getCellTemp());
                        sigmaTemperature += region.getRegion()[i][j - 1].getCellTemp() * (region.getRegion()[i][j - 1].getMetal(numN));
                        numNeighbors++;
                    }
                    if (!(j + 1 > hiCol - 1)) {
                        sigmaTemperature += region.getRegion()[i][j + 1].getCellTemp() * (region.getRegion()[i][j + 1].getMetal(numN));
                        numNeighbors++;
                    }
                    sigmaTemperatureFinal += (region.getRegion()[i][j].getThermConstant(numN) * sigmaTemperature) / numNeighbors;
                    numN++;
                }
                region.getRegion()[i][j].setCellTemp(sigmaTemperatureFinal);
                b[i][j] = sigmaTemperatureFinal;
                region.getRegion()[i][j].setTemp(sigmaTemperatureFinal);
                //System.out.println("[" + i + " " + j + " " + sigmaTemperatureFinal);
            }
        }

        @Override
        public Double call() throws Exception {
            this.calculateTemperature(i);
            return null;
        }

        /*public double getResult() {
            return sigmaTemperatureFinal;
        }*/
    }
}
