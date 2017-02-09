package JavaPaint.paintApp;

import JavaPaint.paintUtil.ColorPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import JavaPaint.paintUtil.RelativeMouseInput;

/**
 * One of the tools for the paint class, gets the user input when the PolyLine drawing tool is selected and renders any lines drawn
 * to the screen.
 * 
 * @author Al Ohlinger with starter code from the text by Timothy Wright
 */
public class DrawPolyLine {

    ArrayList<ColorPoint> lines = new ArrayList<ColorPoint>(); // List of ColorPoints to make up the PolyLines
    boolean drawingLine = false; // True when the user is currently in the middle of drawing a PolyLine, false if not
    
    /**
     * Get the user's input and process it.  With this tool, the user continually left clicks to draw connected lines.  To break up
     * the lines, the user inputs a right click.
     * 
     * @param   mouse       The mouse the user is clicking with
     * @param   color       The color that this set of lines should be
     * @return  boolean     True if the user is currently drawing a set of lines, false otherwise
     */
    protected boolean processInput(RelativeMouseInput mouse, Color color) {

        // Check if the left mouse button is clicked
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            // Make a new ColorPoint to add to the lines array
            ColorPoint cp = new ColorPoint(mouse.getPosition(), color);
            lines.add(cp);
            drawingLine = true;
        }
        
        //  Check  if the right mouse button is clicked
        if (mouse.buttonDownOnce(MouseEvent.BUTTON3)) {
            // Add a null value to the lines array to separate sets of lines
            lines.add(null);
            drawingLine = false;
        }
        
        // Return if the user is currently in the middle of drawing a set of lines (after left click, before right click)
        return drawingLine;
    }

    /**
     * Renders all currently drawn sets of lines in the array lines to the screen.
     * 
     * @param   g       The graphics tool to draw to the screen
     * @param   mouse   The mouse the user is clicking with
     * @param   color   The color that this set of lines should be
     */
    protected void render(Graphics g, RelativeMouseInput mouse, Color color) {
        
        // Loop through all points in lines
        for(int i = 0; i < lines.size(); ++i) {
            ColorPoint p1 = lines.get(i);
            ColorPoint p2 = new ColorPoint(mouse.getPosition(), color);
            
            /* Set a preview image to the screen if the user has left clicked, but not right clicked yet, set the second point to where
               the mouse is currently at */
            if (p1 == lines.get(lines.size() - 1))  {
                p2.x = mouse.getPosition().x;
                p2.y = mouse.getPosition().y;
            }
            else {
                p2 = lines.get(i + 1);
            }
            
            // Draw the set of lines to the screen
            if(!(p1 == null || p2 == null)) {
                g.setColor(p1.color);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }  
    }   
}