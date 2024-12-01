/*
 * Ball
 * Defines a ball for a pong game
 * Coby Lipovitch
 * ICS4U
 * November 20, 2024
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import csta.ibm.pong.GameObject;

public class Ball extends GameObject {
	// Fields
	private double dx;
	private double dy;
	private int size;
	private Color c;
	private double initSpeed;
	private double maxSpeed;
	private boolean freeze = false;

	

	// Constructors
	public Ball(int size, Color colour, int initSpeed, int maxSpeed) {
		this.size = size;
		setSize(size, size);
		c = colour;
		setColor(c);
		this.initSpeed = initSpeed;
		this.maxSpeed = maxSpeed;
		dx = initSpeed;
		dy = initSpeed;
	}
	
	
	// Methods
	/**
	 * initBall
	 * initializes the ball's speed for a new play
	 * code for vertical speed taken from ponggame.org and converted from javascript to java by ChatGPT
	 */
	public void initBall(boolean left) {
		
		// set horizontal speed
		dx = initSpeed;
		if (left) dx *= -1;
		
		// pick random vertical speed 
		double randomAngleFactor = Math.floor(100 * Math.random()) / 100;
		double adjustedAngle = randomAngleFactor * Math.PI / 4;
		dy = ((Math.random() < 0.5) ? 1 : -1) * Math.sin(adjustedAngle) * maxSpeed;
		
	}

	/**
	 * toggleFreeze
	 * toggles whether the ball moves or not
	 */
	public void toggleFreeze() {
		freeze = !freeze;
	}
	
	/**
	 * act
	 * moves the ball on each frame
	 */
	public void act() {
		if (freeze) return;
		setX((int)(getX() + dx));
		setY((int)(getY() + dy));
	}
	
	/**
	 * paint
	 * Draws the ball on each frame
	 * Credit: https://stackoverflow.com/questions/1094539/how-to-draw-a-decent-looking-circle-in-java
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(c);
		Shape circle = new Ellipse2D.Double(0, 0, size, size);
		g2d.fill(circle);
	}
	
	/**
	 * bouncePaddle
	 * bounces the ball off a paddle
	 * Code taken from this website: ponggame.org
	 * Code converted from javascript to java by chatGPT
	 */
	public void bouncePaddle(Paddle p) {
		
		// Get the vertical centers of the ball and paddle
		double ballCenterY = getY() + size/2;
		double paddleCenterY = p.getY() + p.getHeight()/2;
	
		// Calculate the difference and normalize by half the paddle height
		double angleFactor = (ballCenterY - paddleCenterY) / (p.getHeight() / 2);
	
		// Clamp the angle factor to ensure it stays within [-1, 1]
		angleFactor = Math.max(-1, Math.min(1, angleFactor));
		
		// Convert angleFactor to radians and adjust the speed
		double adjustedAngle = angleFactor * Math.PI / 4;
		
		// Update horizontal and vertical velocity
		boolean isMovingLeft = (dx < 0);
		dx = (isMovingLeft ? 1 : -1) * Math.cos(adjustedAngle) * maxSpeed;
		dy = Math.sin(adjustedAngle) * maxSpeed;

	}
	
	/**
	 * bounceY
	 * changes the ball's vertical direction
	 */
	public void bounceFloor() {
		dy = -dy;
	}

	/**
	 * goingLeft
	 * returns true if the ball is going left, false otherwise
	 */
	public boolean goingLeft() {
		return (dx < 0);
	}
	
}