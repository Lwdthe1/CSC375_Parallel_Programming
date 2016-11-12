package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static final int ROWS = 100;
    private static final int COLUMNS = ROWS * 2;
    private static JacobiPanel alloyPanel;
    private static ActionListener actionListener;
    private static Timer timer;
    private static String frameName;
    private static int repaintPanelFrequency;
    public static double MAX_TEMP_DIVISOR = 1.11;

    public static void main(String[] args) {
        initGUI();

        AlloyRegion region = new AlloyRegion(1000, 500, COLUMNS, ROWS, 1.0, 1.0, 1.0);
        double regionA [][];
        double regionB [][];
        regionA = region.createRegions();
        regionB = region.createRegions();
        Jacobi jacobi = new Jacobi(region, regionA, regionB, 0, COLUMNS - 1, 0, ROWS - 1, 10000);
        jacobi.compute();
    }

    private static void initGUI() {
        frameName = "Lincoln's Jacobi";
        JFrame frame = new JFrame(frameName);
        alloyPanel = new JacobiPanel();
        alloyPanel.setBackground(Color.black);
        frame.add(alloyPanel);
        frame.setSize(COLUMNS, ROWS);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alloyPanel.repaint();
            }
        };

        repaintPanelFrequency = 1000 * 1;//how often to trigger the timer
        timer = new Timer(repaintPanelFrequency, actionListener);
        //trigger the timer for the first time in 2 seconds
        timer.setInitialDelay(1000 * 2);
        timer.start();//start the timer
    }

    public static JacobiPanel getPanel() {
        return alloyPanel;
    }
}
