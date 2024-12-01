/*
 * Pong
 * Creates a playable ping pong graphical game
 * Coby Lipovitch
 * ICS4U
 * November 24, 2024
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import csta.ibm.pong.Game;

public class Pong extends Game {
	// Fields
	
	// Game Objects
	private Ball ball;
	private Paddle p1;
	private Paddle p2;
	private JLabel[] scoreboard;
	private JLabel pausedLabel;
	private SoundIcon soundIcon;
	private HelpIcon helpIcon;

	// Ball variables
	private final int BALL_WIDTH = 15;
	private final int BALL_INITIAL_SPEED = 5;
	private final int BALL_MAX_SPEED = 10;

	// Paddle variables
	private final int PADDLE_WIDTH = BALL_WIDTH;
	private final int PADDLE_HEIGHT = 75;
	private final int PADDLE_SPEED = 10;

	// Default Window variables
	private final int INSET_TOP = 28; //26;
	private final int INSET_BOTTOM = 0; //3;
	
	// Screen variables
	private final int FRAME_WIDTH = 650;
	private final int FRAME_HEIGHT = 480 + INSET_TOP + INSET_BOTTOM;
	private final int MARGIN_WIDTH = BALL_WIDTH;
	private final int GAME_AREA_HEIGHT = FRAME_HEIGHT - MARGIN_WIDTH*2;
	private final int GAME_TOP = MARGIN_WIDTH;
	private final int GAME_BOTTOM = FRAME_HEIGHT - INSET_TOP - INSET_BOTTOM - MARGIN_WIDTH;
	
	private final int FONT_SIZE = 68;

	// Colours
	private static final Color GREY = new Color(215, 215, 215);
	
	// Game Variables
	private final int WINNING_SCORE = 10;
	private boolean isPaused = false;
	private boolean gameOver = false;
	private boolean leftStarts = true;
	private final int WAIT_TIME = 59;
	private int countdown = 0;
	
	// Constructors
	private Pong() {
		super();
		setJMenuBar(null);
		setSize(FRAME_WIDTH, FRAME_HEIGHT); // custom frame dimensions
		setLocationRelativeTo(null); // makes frame appear in center of the screen
	}
	
	// Methods
	/**
	 * setUp
	 * initializes game objects
	 */
	public void setup() {
		
		// set up frame
		Image icon = Toolkit.getDefaultToolkit().getImage("Resources/pongIcon.png");
		setIconImage(icon);
		if (Taskbar.isTaskbarSupported()) Taskbar.getTaskbar().setIconImage(icon);
		setResizable(false);
		setDelay(17); // 60 fps
		addKeyPresses();
		
		// set up top and bottom margins
		Line topMargin = new Line(0, 0, MARGIN_WIDTH, FRAME_WIDTH, GREY);
		Line bottomMargin = new Line(0, GAME_BOTTOM, MARGIN_WIDTH, FRAME_WIDTH, GREY);
		add(topMargin);
		add(bottomMargin);
		topMargin.repaint();
		bottomMargin.repaint();
		
		// set up divider line
		Line[] divider = new Line[GAME_AREA_HEIGHT / BALL_WIDTH / 2];
		for (int i = 0; i < divider.length; i++) {
			divider[i] = new Line(FRAME_WIDTH/2 - BALL_WIDTH / 2, MARGIN_WIDTH + i*BALL_WIDTH * 2, BALL_WIDTH, BALL_WIDTH, GREY);
			add(divider[i]);
			divider[i].repaint();
		}

		// set up ball
		ball = new Ball(BALL_WIDTH, GREY, BALL_INITIAL_SPEED, BALL_MAX_SPEED);
		resetBall();
		add(ball);

		// set up paddles
		p1 = new Paddle(PADDLE_WIDTH, PADDLE_HEIGHT, GREY, PADDLE_SPEED);
		p1.setY(GAME_AREA_HEIGHT/2 - p1.getHeight() / 2);
		p1.setX(ball.getWidth() * 2);
		add(p1);

		p2 = new Paddle(PADDLE_WIDTH, PADDLE_HEIGHT, GREY, PADDLE_SPEED);
		p2.setY(GAME_AREA_HEIGHT/ 2 - p2.getHeight() / 2);
		p2.setX(FRAME_WIDTH - ball.getWidth() * 2 - p2.getWidth());
		add(p2);

		// set up scoreboard
		scoreboard = new JLabel[2];
		
		scoreboard[0] = createScoreLabel();
		scoreboard[0].setBounds(FRAME_WIDTH / 2 - BALL_WIDTH*8 - 40, MARGIN_WIDTH + BALL_WIDTH, BALL_WIDTH*8, FONT_SIZE);
		scoreboard[0].setHorizontalAlignment(SwingConstants.RIGHT);
		add(scoreboard[0]);
		scoreboard[0].repaint();

		
		scoreboard[1] = createScoreLabel();
		scoreboard[1].setBounds(FRAME_WIDTH / 2 + 40, BALL_WIDTH*2, BALL_WIDTH*8, FONT_SIZE);
		scoreboard[1].setHorizontalAlignment(SwingConstants.LEFT);
		add(scoreboard[1]);
		scoreboard[1].repaint();

		// set up paused label
		pausedLabel = new JLabel("Paused, press P   to continue");
		pausedLabel.setFont(new Font("Courier New", Font.BOLD, BALL_WIDTH*2));
		pausedLabel.setBounds(BALL_WIDTH*2, GAME_BOTTOM - MARGIN_WIDTH - (BALL_WIDTH*4), FRAME_WIDTH, BALL_WIDTH*4);
		pausedLabel.setHorizontalAlignment(SwingConstants.LEFT);
		pausedLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		pausedLabel.setForeground(GREY);
		pausedLabel.setVisible(false);
		add(pausedLabel);

		// set up mute button
		try {
			soundIcon = new SoundIcon(GREY, FONT_SIZE);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		soundIcon.setBounds(FRAME_WIDTH - BALL_WIDTH*3 - PADDLE_WIDTH - FONT_SIZE, MARGIN_WIDTH + BALL_WIDTH, FONT_SIZE, FONT_SIZE);
		add(soundIcon);
		
		// add mouse functionality to mute button
		soundIcon.addMouseListener(new MouseAdapter() {
			
			@Override
            public void mousePressed(MouseEvent e) {
                
				if (!isPaused) {
					
					if (soundIcon.isTransparent()) return; // doesn't let you click the button if the button is not visible
					else {
						soundIcon.setOpaque();
					}
				
				}
				
				try {
					AudioPlayer.toggleMute();
				} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				}
                soundIcon.toggleIcon();
            }

		}
		
		);

		// set up help button
		try {
			helpIcon = new HelpIcon(GREY, FONT_SIZE);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}

		helpIcon.setBounds(BALL_WIDTH*3 + PADDLE_WIDTH, MARGIN_WIDTH + PADDLE_WIDTH, FONT_SIZE, FONT_SIZE);
		helpIcon.setVisible(false);
		add(helpIcon);

		// add mouse functionality to help button
		helpIcon.addMouseListener(new MouseAdapter() {
			
			@Override
            public void mousePressed(MouseEvent e) {
                
				if (isPaused) showInstructions();
            }

		}
		
		);
		
	}
	
	/**
	 * togglePause
	 * changes whether the game is paused
	 */
	private void togglePause() throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		
		if (gameOver) return;
		if (isPaused) {
			startGame();
			pausedLabel.setVisible(false);
			helpIcon.setVisible(false);
			soundIcon.setTransparent();
		} else {
			stopGame();
			pausedLabel.setVisible(true);
			helpIcon.setVisible(true);
			soundIcon.setOpaque();
		}
		AudioPlayer.paddleHitSound();
		isPaused = !isPaused;
	}

	/**
	 * resetGame
	 * resets the game
	 */
	private void resetGame() throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		if (isPaused) togglePause();
		if (gameOver) {
			gameOver = false;
			startGame();
		} else {
			AudioPlayer.scoreSound();
		}
		resetScore();
		leftStarts = true;
		if (countdown == 0) triggerCountdown();
		else countdown = WAIT_TIME;
	}

	/**
	 * resetBall
	 * resets ball after each round
	 */
	private void resetBall() {
		ball.setX(FRAME_WIDTH/2 - ball.getWidth()/2);
		ball.setY(GAME_AREA_HEIGHT/2 - ball.getHeight()/2);
		ball.initBall(leftStarts);
		leftStarts = !leftStarts;
	}
	
	/**
	 * createScoreLabel
	 * returns a score label object
	 */
	private JLabel createScoreLabel() {
		JLabel temp = new JLabel("0");
		temp.setFont(new Font("Courier New", Font.BOLD, FONT_SIZE));
		temp.setForeground(GREY);
		temp.setVerticalAlignment(SwingConstants.TOP);
		return temp;
	}

	/**
	 * getScore
	 * returns a player's score
	 */
	private int getScore(int player) {
		return Integer.parseInt(scoreboard[player - 1].getText());
	}

	/**
	 * playerScored
	 * increments a player's score on their score label
	 */
	private void playerScored(int player) {
		scoreboard[player - 1].setText("" + (getScore(player) + 1));
		scoreboard[player - 1].repaint();
	}

	/**
	 * resetScore
	 * resets score labels
	 */
	private void resetScore() {
		for (JLabel i : scoreboard) i.setText("0");
	}

	/**
	 * triggerCoundown
	 * starts a buffer time where there is no ball in play
	 */
	private void triggerCountdown() {
		countdown = WAIT_TIME;
		resetBall();
		ball.toggleFreeze();
		ball.setVisible(false);
	}
	
	/**
	 * playerWin
	 * triggers a game over and displays a dialog showing the winner and giving the user the option to replay or quit
	 */
	private void playerWin(int player) {
		gameOver = true;
		soundIcon.setTransparent();
		WinDialog d = new WinDialog(this, player);
		if (d.getChoice() == 1) System.exit(0);
	}
	
	/**
	 * score
	 * gives a player a point and starts a new round; ends the game if a player wins
	 */
	private void score(int player) throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		
		AudioPlayer.scoreSound();
		
		switch (player) {
			case 1: 
				playerScored(1); 
				
				if (getScore(1) == WINNING_SCORE) {
					playerWin(1);
				}
				
				break;
				
			case 2: 
				playerScored(2); 
				
				if (getScore(2) == WINNING_SCORE) {
					playerWin(2);
				}
				
				break;
		}
		
		if (gameOver) {
			resetGame();
		} else {
			triggerCountdown();
		}
	}

	/**
	 * quitDialog
	 * displays a dialog asking the user if they want to quit the program
	*/
	private void quitDialog() throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		if (!isPaused) togglePause();
		int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit Pong", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,null);
		if (choice == JOptionPane.YES_OPTION) System.exit(0);
	}

	/**
	 * showInstructions
	 * Displays a dialog containing the instructions and controls for the game
	 */
	private void showInstructions() {

		String instructions = 
			"<html>" +
			"<style>td {text-align: center;} table {width: 75%;}</style>" +
			"<center><h3>How to play Pong</h3>" +
			"<p>Score points against the other player by hitting the ball past their paddle.</p>" + 
			"<p>First player to reach " + WINNING_SCORE + " points wins!<br></p>" +
			"<h3>Controls:</h3>" +
        	"<table>" + 
				"<tr>" +
					"<th>Player 1:</b></th>" +
					"<th><b>Player 2:</b></th>" +
				"</tr>" +
				"<tr>" +
					"<td><b>Z</b> (up) / <b>X</b> (down)</td>" +
        			"<td><<b>N</b> (up) / <b>M</b> (down)</td>" + 
				"</tr>" +
			"</table>" +
			"<p><br>Press <b>P</b> to pause.</p>" +
			"<p>Press <b>R</b> to restart.</p>" +
			"<p>Press <b>T</b> to mute/unmute audio.</p>" +
			"<p>Press <b>H</b> to show instructions." + 
			"<p>Press <b>Q</b> to quit." +
			"</center>" + 
			"</html>";
		JOptionPane.showMessageDialog(this, instructions, "Instructions", JOptionPane.PLAIN_MESSAGE);

	}
	
	/**
	 * act
	 * defines the actions of the game on each frame
	 */
	public void act() {
		
		// fade away sound icon
		if (!soundIcon.isTransparent()) soundIcon.reduceTransparency(3);
		
		// move p1
		if (ZKeyPressed() && p1.getY() > GAME_TOP) {
			p1.moveUp();
		}
		
		if (XKeyPressed() && p1.getY() < GAME_BOTTOM - p1.getHeight()) {
			p1.moveDown();
		}
		
		// move p2
		if (NKeyPressed() && p2.getY() > GAME_TOP) {
			p2.moveUp();
		}
		
		if (MKeyPressed() && p2.getY() < GAME_BOTTOM - p2.getHeight()) {
			p2.moveDown();
		}

		// buffer after a point is scored
		if (countdown > 0) {
			if (countdown == 1) {
				ball.setVisible(true);
				ball.toggleFreeze();
			}
			countdown--;
			return; // code below is not run if the game is in the countdown state
		}

		// bounce off top/bottom
		if (ball.getY() <= GAME_TOP || ball.getY() >= GAME_BOTTOM - BALL_WIDTH) {
			if (ball.getY() < GAME_TOP) ball.setY(GAME_TOP); // make sure ball stays in upper bound
			if (ball.getY() > GAME_BOTTOM - BALL_WIDTH) ball.setY(GAME_BOTTOM - BALL_WIDTH); // make sure ball stays in lower bound
			ball.bounceFloor();
			try {
				AudioPlayer.wallHitSound();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
		
		// bounce off paddles
		if (ball.collides(p1) && ball.goingLeft()) {
			ball.bouncePaddle(p1);
			try {
				AudioPlayer.paddleHitSound();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}

		if (ball.collides(p2) && !ball.goingLeft()) {
			ball.bouncePaddle(p2);
			try {
				AudioPlayer.paddleHitSound();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
		
		// scoring
		if (ball.getX() <= 0 - ball.getWidth()) {
			try {
				score(2);
				return;
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
		
		if (ball.getX() >= FRAME_WIDTH) {
			try {
				score(1);
				return;
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	/**
	 * addKeyPresees
	 * implements keyboard functionality
	 * R: reset game 
	 * T: mute sound effects 
	 * P: pause game
	 * H: Show instructions
	 * Q: Quit program
	 */
	private void addKeyPresses() {
			
		addKeyListener(new KeyListener() {
				
			public void keyTyped(KeyEvent e) {
			}
				
			public void keyPressed(KeyEvent e) {
				
				char pressed = Character.toUpperCase(e.getKeyChar());
				
				switch (pressed) {
				
				case 'R': // reset
					try {
						resetGame();
					} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
						e1.printStackTrace();
					}
					break;
				case 'P': // pause
					try {
						togglePause();
					} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {			
						e1.printStackTrace();
					}
					break;
				case 'T': // mute audio
					if (gameOver) return;
					try {
						AudioPlayer.toggleMute();
					} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
						e1.printStackTrace();
					}
					soundIcon.toggleIcon();
					soundIcon.setOpaque();
					break;
				case 'H': // show instructions
					if (!isPaused)
						try {
							togglePause();
						} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
							e1.printStackTrace();
						}
					showInstructions();
					break;
				case 'Q': // quit the program
						try {
							quitDialog();
						} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
							e1.printStackTrace();
						}
						break;
				}
				
			}
		
			public void keyReleased(KeyEvent e) {
			}
			
		});
		
	}
	
	public static void main(String[] args) {
		Pong p = new Pong();
		p.setVisible(true);
		p.initComponents();		
	}
	
}