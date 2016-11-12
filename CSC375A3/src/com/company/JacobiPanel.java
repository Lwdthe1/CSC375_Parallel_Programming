package com.company;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Created by keithmartin on 12/3/15.
 */
public class JacobiPanel extends JPanel {

    private Color color1;
    private Color color2;
    private GradientPaint gp;
    private static Graphics2D g2d;
    private static int pixelHeight = 5;
    private static int pixelWidth = 5;
    int columnsW, rowsH;
    List<JPoint> jPoints = new ArrayList();

    public JacobiPanel() {

    }

    public JPoint addPoint(int x, int y, Color c) {
        /*for(JPoint jPoint: jPoints){
            if(jPoint.x == x && jPoint.y == y){
                jPoint.c1 = c;
                jPoint.c2 = c;
                return jPoint;
            }
        }*/
        JPoint jPoint = new JPoint(x, y, c);
        jPoints.add(jPoint);
        return jPoint;
    }

    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("IN");
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        for (int i = 0; i < jPoints.size(); i++) {
            JPoint jPoint = jPoints.get(i);

            if(jPoint !=null) {
                gp = new GradientPaint(10, 10, jPoint.c1, 0, pixelHeight, jPoint.c2);
                g2d.setPaint(gp);
                //getGraphics().drawRect(0,0, 5,5);
                g2d.fillRect(jPoint.x * 5, jPoint.y * 5, pixelWidth, pixelHeight);
                //System.out.println("IN");
            }
        }
    }

    protected class JPoint {
        //GradientPaint gradientPaint;
        int x, y;
        Color c1, c2;

        public JPoint(int x, int y, Color c) {
            this.x = x;
            this.y = y;
            this.c1 = c;
            this.c2 = c;
        }
    }

}
