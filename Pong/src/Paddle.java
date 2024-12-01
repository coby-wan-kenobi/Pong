/*
 * Paddle
 * Defines a paddle for a pong game
 * Coby Lipovitch
 * ICS4U
 * November 20, 2024
 */

import java.awt.Color;
import csta.ibm.pong.GameObject;

public class Paddle extends GameObject {
	
	// Fields
	private int dy;
	private Color c;
	
	// Constructors
	public Paddle(int width, int height, Color color, int speed) {
		setSize(width, height);
		c = color;
		setColor(c);
		dy = speed;
	}
	
	// Methods
	
	/**
	 * act
	 * defines the paddle's action on each frame
	 */
	public void act() {

	}
	
	/**
	 * moveUp
	 * moves the paddle up
	 */
	public void moveUp() {
		setY(getY() - dy);
	}
	
	/**
	 * moveDown
	 * moves the paddle down
	 */
	public void moveDown() {
		setY(getY() + dy);
	}

}