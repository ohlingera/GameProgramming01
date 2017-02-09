package JavaPaint.paintApp;

import JavaPaint.paintUtil.ColorPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import JavaPaint.paintUtil.RelativeMouseInput;

/**
 * One of the tools for the paint class, gets the user input when the FreeDraw drawing tool is selected and renders any lines drawn to
 * the screen.
 * 
 * @author Al Ohlinger with starter code from the text by Timothy Wright
 */
public class FreeDraw {
            
    ArrayList<ColorPoint> lines = new ArrayList<ColorPoint>(); // List of ColorPoints to make up the lines
    boolean drawingLine; // True when the user is currently drawing, false if not
    ColorPoint cp; /* A color point to be stored in the array lines. It is made up of the current color selected and either the point
                      where the user has just clicked or the point where the user is holding their mouse down at */
    
    /**
     * Get the user's input and process it.  With this tool, the user left clicks to draw a single point or holds hold the mouse to
     * draw a continuous series of points.
     * 
     * @param   mouse       The mouse the user is clicking with
     * @param   color       The color that is currently selected that each point should should be
     * @return  boolean     True if the user is currently drawing, false otherwise
     */
    protected boolean processInput(RelativeMouseInput mouse, Color color) {
        
        // Check if the left mouse button is clicked
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            // Make a new ColorPoint to add to the lines array
            cp = new ColorPoint(mouse.getPosition(), color);
            lines.add(cp);
            drawingLine = true;
        }
        
        /* Check if the left mouse button is held down and if the user has cleared while the mouse button is held down while using this
           tool */
        if (mouse.buttonDown(MouseEvent.BUTTON1) && !lines.isEmpty()) {
            cp = new ColorPoint(mouse.getPosition(), color);
            // If the user holds down the mouse continually at the same point, the point will only be added once to the array.
            if (!(lines.get(lines.size() - 1).x == cp.x && lines.get(lines.size() - 1).y == cp.y)) {
                lines.add(cp);
            }
        }
        else if (drawingLine) {
            // Add a null value to the lines array to separate sets of lines
            lines.add(null);
            drawingLine = false;
        }
        
        // Return if the user is currently drawing (left button clicked or held down)
        return drawingLine;
    }
    
    /**
     * Renders all currently drawn points to the screen
     * 
     * @param   g       The graphics tool to draw to the screen
     * @param   mouse   The mouse the user is clicking with
     */
    protected void render (Graphics g, RelativeMouseInput mouse) {
        
        // Loop through all points in lines
        for(int i = 0; i < lines.size() - 1; ++i) {
            ColorPoint p1 = lines.get(i);
            ColorPoint p2 = lines.get(i + 1);
            
            // Draw all current points in the lines array to the screen
            if(!(p1 == null || p2 == null)) {
                g.setColor(p1.color);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }
}
