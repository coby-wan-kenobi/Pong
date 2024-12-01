/*
 * SoundIcon
 * Icon that indicates whether or not audio is muted
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

public class SoundIcon extends JLabel {
    
    // Fields
    private Color colour;
    
    // Character code points
    private static final String VOLUME_OFF = "e050";
    private static final String VOLUME_ON = "e04f";

    // Constructors
    public SoundIcon(Color colour, int size) throws FontFormatException, IOException {
       
    	super(""+Character.toChars(Integer.parseInt(VOLUME_OFF,16))[0], SwingConstants.CENTER);
        setFont(Font.createFont(Font.TRUETYPE_FONT, new File("Resources/MaterialIcons-Regular.ttf")).deriveFont((float)size)); // Google Material Icons font: fonts.google.com/icons
        setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
        this.colour = colour;
        setTransparent();

    }

    // Methods
    /**
     * toggleIcon
     * switches whether the icon is muted or volume on
     */
    public void toggleIcon() {
		if (getText().equals(""+Character.toChars(Integer.parseInt(VOLUME_ON,16))[0])) setText(""+Character.toChars(Integer.parseInt(VOLUME_OFF,16))[0]);
		else setText(""+Character.toChars(Integer.parseInt(VOLUME_ON,16))[0]);
	}

    /**
     * reduceTransparency
     * reduces the transparency of the icon
     */
    public void reduceTransparency(int amount) {
        colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha() - amount);
        setForeground(colour);
    }

    /**
     * setOpaque
     * sets the icon to fully opaque
     */
    public void setOpaque() {
        colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), 255);
        setForeground(colour);
    }

    /**
     * setTransparent
     * sets the icon to fully transparent
     */
    public void setTransparent() {
        colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), 0);
        setForeground(colour);
    }

    /**
     * isTransparent
     * returns true if icon is transparent, returns false otherwise
     */
    public boolean isTransparent() {
        return (colour.getAlpha() == 0);
    }

}
