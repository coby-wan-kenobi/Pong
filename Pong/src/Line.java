/*
 * Line
 * Graphical line that can be added to a JFrame
 * Coby Lipovitch
 * ICS4U
 * November 20, 2024
 */

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class Line extends JComponent {
	// Fields
	Color c;
	
	// Constructors
	public Line(int x, int y, int length, int width, Color colour) {
		setSize(width, length);
		c = colour;
		setLocation(x, y);
	}
	
	// Methods
	/**
	 * paint
	 * paints the line
	 */
	public void paint(Graphics g) {
		g.setColor(c);
		g.fillRect(0, 0, (int)getBounds().width, (int)getBounds().height);
	}
	
}