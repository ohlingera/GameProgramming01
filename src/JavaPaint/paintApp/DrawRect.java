package JavaPaint.paintApp;

import JavaPaint.paintUtil.ColorPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import JavaPaint.paintUtil.RelativeMouseInput;

/**
 * One of the tools for the paint class, gets the user input when the rectangle drawing tool is selected and renders any rectangles
 * drawn to the screen.
 * 
 * @author Al Ohlinger with starter code from the text by Timothy Wright
 */
public class DrawRect {

    ArrayList<ColorPoint> rects = new ArrayList<ColorPoint>(); //List of ColorPoints to make up the rectangles
    boolean isSecondPoint = false; /* User draws rectangles by clicking twice, if it is the second click, we insert a null value in
                                      rects.  This keeps track of if it is the second click. */
    
    /**
     * Get the user's input and process it.  With this tool, the user clicks twice to draw the rectangle between the two opposite
     * corners.
     * 
     * @param   mouse       The mouse the user is clicking with
     * @param   color       The color that this rectangle should be
     * @return  boolean     True if the user has selected the first point but not the second yet and is currently drawing the
     *                      rectangle. False if otherwise.
     */
    protected boolean processInput(RelativeMouseInput mouse, Color color) {

        // Check if the left mouse button is clicked
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            // Make a new ColorPoint to add to the rects array
            ColorPoint cp = new ColorPoint(mouse.getPosition(), color);
            rects.add(cp);
            // If it's the second point of the rectangle, add a null value to the array to separate them.
            if (isSecondPoint) {
                rects.add(null);
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
     * Renders all currently drawn rectangles in rects to the screen.
     * 
     * @param   g       The graphics tool to draw to the screen
     * @param   mouse   The mouse the user is clicking with
     * @param   color   The color that this rectangle should be
     */
    protected void render(Graphics g, RelativeMouseInput mouse, Color color) {
        
        // Loop through all points in rects
        for(int i = 0; i < rects.size(); ++i) {
            ColorPoint p1 = rects.get(i);
            ColorPoint p2 = new ColorPoint(mouse.getPosition(), color);
            
            /* Set a preview image to the screen if the user has clicked the first point but not the second, set the second point to
               where the mouse is currently at */
            if (p1 == rects.get(rects.size() - 1))  {
                p2.x = mouse.getPosition().x;
                p2.y = mouse.getPosition().y;
            }
            else {
                p2 = rects.get(i + 1);
            }
            
            if(!(p1 == null || p2 == null)) {
                g.setColor(p1.color);
                // Draw the rectangle to the screen if the first point is in the top left
                if (p2.x - p1.x >= 0 && p2.y - p1.y >= 0) {
                    g.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
                }
                // First point is in bottom left
                else if (p2.x - p1.x >= 0) {
                    g.drawRect(p1.x, p2.y, p2.x - p1.x, p1.y - p2.y);
                }
                // Top right
                else if (p2.y - p1.y >= 0) {
                    g.drawRect(p2.x, p1.y, p1.x - p2.x, p2.y - p1.y);
                }
                // Bottom right
                else {
                    g.drawRect(p2.x, p2.y, p1.x - p2.x, p1.y - p2.y);
                }
            }
        }  
    }   
}