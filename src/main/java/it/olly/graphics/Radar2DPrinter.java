package it.olly.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Radar2DPrinter extends JPanel {
    private static final long serialVersionUID = -2655605155726918512L;

    // configs
    private static final double pi2 = Math.PI * 2;
    private final static int SIZE = 300; // paint size
    private final static Color[] colors = new Color[] { //
            Color.GREEN // color #1
    };
    private final static double LOOP_FRAMES = 60;

    // drawer vars
    private final static int GRID_SIZE = 4; // in pixels. 1 = no grid
    private int i = 0; // 4 debug
    private boolean axis = true;

    // algo vars
    private double shift = 0;

    public static final double getAngle(double x, double y, double centerX, double centerY) {
        x -= centerX;
        y -= centerY;
        double angle = Math.atan2(y, x);
        if (angle < 0) {
            angle = pi2 + angle;
        }
        return angle;
    }

    public static final double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    // distance from a point to a line (with equation y = Ax+B)
    public static final double distPl(double Px, double Py, double A, double B) {
        return Math.abs(Py - (A * Px + B)) / Math.sqrt(1 + A * A);
    }

    @Override
    public void paint(Graphics g) {
        int size = Math.min(this.getWidth(), this.getHeight());
        System.out.println("[" + (i++) + "] -> " + shift);

        Graphics2D g2d = (Graphics2D) g;
        for (int a = 0; a < size; a += GRID_SIZE) {
            for (int b = 0; b < size; b += GRID_SIZE) {

                int x = a - size / 2;
                int y = (size - b) - size / 2;

                // here i have:
                // (a,b) point from 0 to SIZE on window coordinates [zero high left; axis moving right, down]
                // (x,y) point from -SIZE/2 to SIZE/2 on cartesian coordinates [zero in center; axis moving right, up]

                // calculate the distance from this led to the "radar line")
                double radarLineCoeff = Math.tan(shift);
                double distPl = distPl(x, y, radarLineCoeff, 0);

                // check if the radar-line's angle is "similar" to the point's angle
                // in order to light up only half of the radar line
                double xyangle = getAngle(x, y, 0, 0);
                boolean sameQuadrant = false;
                if (Math.abs(xyangle - shift) < Math.PI / 2) {
                    sameQuadrant = true;
                }

                // get the right color [0,color.size)
                Color col = Color.BLACK;
                if (distPl < 2 && sameQuadrant)
                    col = colors[0];
                g2d.setColor(col);

                // draw current pixel in window coordinates
                g2d.fillRect(a, b, Math.max(GRID_SIZE / 2, 1), Math.max(GRID_SIZE / 2, 1));
            }
        }

        // move next step. pi2 is the "complete cycle"
        shift += pi2 / LOOP_FRAMES;
        if (shift >= pi2) { // == LOOP_FRAMES
            shift = 0;
        }

        // draw axis if needed
        if (axis) {
            g2d.setColor(Color.GRAY);
            g2d.drawLine(0, size / 2, size, size / 2);// x
            g2d.drawLine(size / 2, 0, size / 2, size);// y
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RadarOlly");
        frame.setLayout(new BorderLayout());
        JPanel panel = new Radar2DPrinter();
        panel.setPreferredSize(new Dimension(SIZE, SIZE));
        frame.add(panel);
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        (new Thread(() -> {
            while (true) {
                frame.repaint();
                try {
                    Thread.sleep(33); // 30 FPS -> 1/30 -> 0.03333 -> 33millis
                } catch (InterruptedException e) {
                }
            }
        })).start();
    }

}