package it.olly.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LinePrinter extends JPanel {
    private static final long serialVersionUID = -2655605155726918512L;

    // configs
    private final static int LINE_WIDTH = 300;
    private final static Color[] colors = new Color[] {Color.GREEN};

    // drawer vars
    private final static int GRID_SIZE = 4; // in pixels. 1 = no grid
    
    // algo vars
    private double point = 0;
    
    private final static double dist(double a, double b) {
    	// calculates the "distance % 1" between 2 points considering the universe: [0,1]
    	// dist(0.1,0.9) will be -0.2 (going from 0.1 to 0->1 to 0.9) on the left (-)
    	double dr,dl;
    	if (a<b) {
    		dr = b-a;
    		dl = a+1-b;
    		return (dr<dl)?dr:-dl;
    	} else {
    		dr = a-b;
    		dl = b+1-a;
    		return (dr<dl)?-dr:dl;
    	}
    	
    }
    
    @Override
    public void paint(Graphics g) {
    	System.out.println("pint = "+point);
        Graphics2D g2d = (Graphics2D) g;
        int sz = Math.max(GRID_SIZE / 2, 1);
        int y = (this.getHeight()-sz)/2;
        
        double dx = 1/40.;
        double tollerance = 1/10.;
        
        for (int x = 0; x < LINE_WIDTH; x += GRID_SIZE) {
                // calculate the color
                Color col = Color.BLACK;
                double px = x/(double)LINE_WIDTH;
                double dst = dist(point,px);
                if (Math.abs(dst)<tollerance && dst<0) {
                	// point is "near" me and at left side (as i moveright)
                	col = colors[0];
                }
                g2d.setColor(col);

                // draw current pixel in window coordinates
                g2d.fillRect(x, y, sz, sz);
        }
        
        point+=dx; // move right
        if (point >=1) point = 0.0;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LineOlly");
        frame.setLayout(new BorderLayout());
        JPanel panel = new LinePrinter();
        panel.setPreferredSize(new Dimension(LINE_WIDTH, GRID_SIZE+10));
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