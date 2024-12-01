/*
 * AudioPlayer
 * Creates and plays audio files for the pong game
 * Coby Lipovitch
 * ICS4U
 * November 22, 2024
 */
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	
	// Fields
	private static final String PATH_PREFIX = "Resources/";
	
	private static boolean mute = false;
	
	
	// Methods
	
	/**
	 * toggleMute
	 * toggles whether audio is muted
	 */
	public static void toggleMute() throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		mute = !mute;
		if (!mute) paddleHitSound();
	}
	
	/**
	 * playSound
	 * Creates and plays a sound file
	 */
	public static void playSound(String fileName) throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException{
		
		if (mute) return;
		File url = new File(PATH_PREFIX + fileName);
		Clip clip = AudioSystem.getClip();
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.start();

		// InputStream is = getClass().getResourceAsStream(PATH_PREFIX + fileName);
		// BufferedInputStream bis = new BufferedInputStream(is); 
		// AudioInputStream audioInput = AudioSystem.getAudioInputStream(bis);
		// Clip clip = AudioSystem.getClip();
		// clip.open(audioInput);
		// clip.start();
		
	}
	
	/**
	 * paddleHitSound
	 * plays a paddle hit sound effect
	 */
	public static void paddleHitSound() throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException{
		
		playSound("paddle_hit.wav");
		
	}
	
	/**
	 * wallHitSound
	 * plays a wall hit sound effect
	 */
	public static void wallHitSound() throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException{
		
		playSound("wall_hit.wav");
		
	}
	
	/**
	 * scoreSound
	 * plays a score sound effect
	 */
	public static void scoreSound() throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException{
		
		playSound("score.wav");
		
	}
	
}
