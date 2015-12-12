package com.company;

import java.awt.*;
import java.util.Random;

/**
 * Created by keithmartin on 11/26/15.
 */
public class MetalCellComposition {

    Random random = new Random();

    private double metal1;
    private double metal2;
    private double metal3;
    private double c1;
    private double c2;
    private double c3;
    private double cellTemp;
    private int x;
    private int y;
    private JacobiPanel.JPoint jPoint;
    private double maxTemp = 1000;

    public MetalCellComposition(int x, int y, double c1, double c2, double c3) {
        double temp1 = random.nextDouble();
        double temp2 = random.nextDouble();
        double temp3 = random.nextDouble();
        this.metal1 = temp1/(temp1 + temp2 + temp3);
        this.metal2 = temp2/(temp1 + temp2 + temp3);
        this.metal3 = temp3/(temp1 + temp2 + temp3);
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.cellTemp = 0;
        this.x = x;
        this.y = y;
        JacobiPanel panel = Main.getPanel();
        this.jPoint = panel.addPoint(x, y, Color.BLACK);
    }

    public double getMetal(int n) {
        switch (n) {
            case 1:
                return metal1;
            case 2:
                return metal2;
            case 3:
                return metal3;
            default:
                return 0;
        }
    }

    public double getCellTemp() {
        return cellTemp;
    }


    public double getThermConstant(int n) {
        switch (n) {
            case 1:
                return c1;
            case 2:
                return c2;
            case 3:
                return c3;
            default:
                return 0;
        }
    }

    public void setCellTemp(double temp) {
        this.cellTemp = temp;
    }

    /**
     * When you set the temp of this region for the first time,
     * create it's JPoint on the JacobiPanel (GUI JPanel).
     *
     * When you change the temp of this region anytime thereafter,
     * grab it's JPoint and change it's color.
     * @param temp
     */
    public void setTemp(double temp){
        this.cellTemp = temp;
        //System.out.println(cellTemp);
        Color color = getColor();
        if(this.jPoint == null) {
            JacobiPanel panel = Main.getPanel();
            this.jPoint = panel.addPoint(x, y, color);
        } else {
            jPoint.c1 = color;
            jPoint.c2 = color;
        }
    }

    public Color getColor() {
        Color color;
        //System.out.println("(" + xPos + "," + yPos + ")temp = " + temp);
        if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 10)) {
            color = hex2Rgb("#660000");//faint red
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 9.1)) {
            color = hex2Rgb("#990000c");//blood red
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 8.2)) {
            color = hex2Rgb("#CC0000");//dark cherry
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 7.3)) {
            color = hex2Rgb("#FF0000");//medium cherry
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 6.4)) {
            color = hex2Rgb("#FF3300");//chery
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 5.5)) {
            color = hex2Rgb("#FF6633");//bright cherry
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 4.6)) {
            color = hex2Rgb("#FF9966");//salmon
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 3.7)) {
            color = hex2Rgb("#FFCC66");//dark orange
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 2.8)) {
            color = hex2Rgb("#FFCC33");//orange
        } else if(cellTemp <= maxTemp / (Main.MAX_TEMP_DIVISOR + 1)) {
            color = hex2Rgb("#FFFF66");//lemon
        } else if(cellTemp <= maxTemp / Main.MAX_TEMP_DIVISOR ) {
            color = hex2Rgb("#FFFF99");//light yellow
        } else {
            color = Color.WHITE;//hottest color
        }
        return color;
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }


}
