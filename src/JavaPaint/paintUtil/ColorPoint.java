package JavaPaint.paintUtil;

import java.awt.Color;
import java.awt.Point;

/**
 * Color point class that extends point but stores a color with it.  Unchanged as provided in assignment instructions.
 * 
 * @author Patrick Cavanaugh (used by Al Ohlinger)
 */
public class ColorPoint extends Point {

    public Color color;

    public ColorPoint(Point p, Color c) {
	super(p);
	color = c;
    }
}