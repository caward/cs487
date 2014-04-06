import java.io.*;
//import java.net.*;
import javax.sound.sampled.*;

public class GameSounds
{
	static private String texplode = "src/Bomb_Exploding.wav";
	private String gamePlay;
	static private String fireMissile = "src/Bomb 3.wav";
	static private String explodeMissile = "src/Bomb_Exploding.wav";
	static private Clip mclip;
	
	public static void tankExplode()
	{
		File soundFile = new File(texplode);
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip;
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void missileLaunch()
	{
		File soundFile = new File(fireMissile);
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
			mclip = AudioSystem.getClip();
			mclip.open(audioIn);
			mclip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void missileExplode()
	{
		File soundFile = new File(explodeMissile);
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip;
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void missileLaunchStop()
	{
		mclip.stop();
	}
}
