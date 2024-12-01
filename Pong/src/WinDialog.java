/*
 * WinDialog
 * A Dialog window after a game is ended. Gives the player the option to either play again or quit.
 * Coby Lipovitch
 * ICS4U
 * November 29, 2024
 */
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

class WinDialog extends JOptionPane {
	// Fields
	private final int CHOICE;
	
	// Constructors
	public WinDialog(JFrame owner, int player) {
		
		String[] options = {"Play Again", "Quit"};
        JLabel messageLabel = new JLabel("Player " + player + " wins!", SwingConstants.CENTER);
		CHOICE = JOptionPane.showOptionDialog(owner, messageLabel, "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		
	}
	
	// Methods
	/**
	 * getChoice
	 * returns the option the user selected
	 * input: none
	 * output: 1 = Play again, 2 = Quit
	 */
	public int getChoice() {
		return CHOICE;
	}
	
}