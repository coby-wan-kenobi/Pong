/*
 * HelpIcon
 * help button icon
 * Coby Lipovitch
 * ICS4U
 * November 29, 2024
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HelpIcon extends JLabel {
    
    // Fields    
    // Character code point
    private static final String HELP = "e887";

    // Constructors
    public HelpIcon(Color colour, int size) throws FontFormatException, IOException {
       
    	super(""+Character.toChars(Integer.parseInt(HELP,16))[0], SwingConstants.CENTER);
        setFont(Font.createFont(Font.TRUETYPE_FONT, new File("Resources/MaterialIcons-Regular.ttf")).deriveFont((float)size)); // Google Material Icons font: fonts.google.com/icons
        setForeground(colour);
        setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);

    }

}
