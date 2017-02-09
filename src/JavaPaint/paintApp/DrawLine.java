package JavaPaint.paintApp;

import JavaPaint.paintUtil.ColorPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import JavaPaint.paintUtil.RelativeMouseInput;

/**
 * One of the tools for the paint class, gets the user input when the line drawing tool is selected and renders any lines drawn to the
 * screen.
 * 
 * @author Al Ohlinger with starter code from the text by Timothy Wright
 */
public class DrawLine {

    ArrayList<ColorPoint> lines = new ArrayList<ColorPoint>(); // List of ColorPoints to make up the lines
    boolean isSecondPoint = false; /* User draws lines by clicking twice, if it is the second click, we insert a null value in lines.
                                      This keeps track of if it is the second click. */
    /**
     * Get the user's input and process it.  With this tool, the user clicks twice to draw the line between the two points selected.
     * 
     * @param   mouse       The mouse the user is clicking with
     * @param   color       The color that this line should be
     * @return  boolean     True if the user has selected the first point but not the second yet and is currently drawing the line.
     *                      False if otherwise.
     */
    protected boolean processInput(RelativeMouseInput mouse, Color color) {

        // Check if the left mouse button is clicked
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            // Make a new ColorPoint to add to the lines array
            ColorPoint cp = new ColorPoint(mouse.getPosition(), color);
            lines.add(cp);
            // If it's the second point of the line, add a null value to the array to separate them.
            if (isSecondPoint) {
                lines.add(null);
                isSecondPoint = false;
            }
            else {
                isSecondPoint = true;
            }
        }
        
        // Return to currently drawing (between points one and two)
        return isSecondPoint;
    }

    /**
     * Renders all currently drawn lines in the array lines to the screen.
     * 
     * @param   g       The graphics tool to draw to the screen
     * @param   mouse   The mouse the user is clicking with
     * @param   color   The color that this line should be
     */
    protected void render(Graphics g, RelativeMouseInput mouse, Color color) {        

        // Loop through all points in lines
        for(int i = 0; i < lines.size(); ++i) {
            ColorPoint p1 = lines.get(i);
            ColorPoint p2 = new ColorPoint(mouse.getPosition(), color);
            
            /* Set a preview image to the screen if the user has clicked the first point but not the second, set the second point to
               where the mouse is currently at */
            if (p1 == lines.get(lines.size() - 1))  {
                p2.x = mouse.getPosition().x;
                p2.y = mouse.getPosition().y;
            }
            else {
                p2 = lines.get(i + 1);
            }
            
            // Draw the line to the screen
            if(!(p1 == null || p2 == null)) {
                g.setColor(p1.color);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }  
    }   
}