package JavaPaint.paintUtil;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;

/**
 * MyButton class that takes a button and draws a custom image for it.  8 provided images, more can be added.
 * 
 * @author Al Ohlinger
 */
public class MyButton extends JButton {

    int i;  // Which image we want on the button
    
    /**
     * Constructor for the MyButton class.  Takes a button and puts a custom image/text on it.
     * 
     * @param   label   The text that is on the button
     * @param   i       Which image we want on the button
     */
    public MyButton(String label, int i) {
        super(label);
        this.i = i;
    }

    /**
     * Paints a custom image on the button.  8 different images are provided here.  To get a specific one, pass in the number of the
     * one you want to the constructor.
     * 
     * @param   g   The graphics tool to draw on the button with
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        switch(i) {
            case 0: // Draw a line
                g.drawLine(0, 40, 40, 0);
                break;
            case 1: // Draw a rectangle
                g.drawRect(5, 5, 30, 30);
                break;
            case 2: // Draw a Polyline
                g.drawLine(0, 40, 10, 20);
                g.drawLine(10, 20, 25, 30);
                g.drawLine(25, 30, 40, 0);
                break;
            case 3: // Draw an F
                g.drawLine(10, 10, 10, 35);
                g.drawLine(10, 10, 25, 10);
                g.drawLine(10, 22, 20, 22);
                break;
            case 4: // Draw a blue rectangle
                g.setColor(Color.BLUE);
                g.fillRect(5, 5, 30, 30);
                break;
            case 5: // Draw a red rectangle
                g.setColor(Color.RED);
                g.fillRect(5, 5, 30, 30);
                break;
            case 6: // Draw a green rectangle
                g.setColor(Color.GREEN);
                g.fillRect(5, 5, 30, 30);
                break;
            case 7: // Draw a black rectangle
                g.setColor(Color.BLACK);
                g.fillRect(5, 5, 30, 30);
                break;
            default:
                break;
        }
    }
}