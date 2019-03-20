package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import user.UserSettings;

/**
 * Singleton class for handling and playing SoundClip:s.
 * @author Henrik Sandström
 */
public class SoundPlayer implements ActionListener {

	private static SoundPlayer instance;	
	private final static String BUTTON_CLICK = "files/sounds/buttonClick.wav";
	private final static String CHECKBOX_CHECK = "files/sounds/checkBoxCheck.wav";
	private final static String PAWN_STEP = "files/sounds/astep.wav";
	private final static String START_GAME = "files/sounds/startGame.wav";
	private final static String GAME_MUSIC = "files/sounds/gameMusic.wav";
	private final static String PAWN_PLACE = "files/sounds/pawnPlace.wav";
	private final static String PAWN_REMOVE = "files/sounds/pawnRemove.wav";
	private final static String WINNER_MUSIC = "files/sounds/winner.wav";
	private final static String LOSER_MUSIC = "files/sounds/loser.wav";
	private final static String SWORD_FIGHT = "files/sounds/swordFight.wav";
	private boolean playAudioEffects, playMusic;
	private int effectsVolume, musicVolume;
	private SoundClip activeMusic;

	/**
	 * Creates a new SoundPlayer object with default volumes.
	 */
	private SoundPlayer() {
		effectsVolume = 0;
		musicVolume = -10;
		UserSettings settings = UserSettings.getInstance();
		updateSoundStatus(settings.playAudioEffects(),settings.playMusic(),
				settings.getEffectsVolume(),settings.getMusicVolume());
	}

	/**
	 * @return SoundPlayer instance.
	 */
	public static SoundPlayer getInstance() {
		if(instance == null) {
			instance = new SoundPlayer();
		}
		return instance;
	}

	/**
	 * Starts a new SoundClip thread that starts & plays the given sound effect file.
	 * @param soundEffect The destination of a sound effect file.
	 */
	private void playSoundEffect(String soundEffect) {
		if(playAudioEffects) {
			new Thread(new SoundClip(soundEffect, effectsVolume, false)).start();
		}
	}

	/**
	 * Starts a new SoundClip thread that starts & plays the given music file.
	 * @param musicFile
	 */
	private void playMusic(String musicFile) {
		if(playMusic && (activeMusic == null || (activeMusic != null && !activeMusic.isPlaying() || 
				(activeMusic.isPlaying() && activeMusic.getSoundPlaying().compareTo(musicFile) != 0)))) {
			if(activeMusic != null) {
				activeMusic.stop();
			}
			activeMusic = new SoundClip(musicFile, musicVolume, true);
			new Thread(activeMusic).start();
		}
	}

	/**
	 * Updates all user settings for the SoundPlayer and updates the settings for any active music SoundClip.
	 * @param playAudioEffects
	 * @param playMusic
	 * @param effectsVolume
	 * @param musicVolume
	 */
	public void updateSoundStatus(boolean playAudioEffects, boolean playMusic, int effectsVolume, int musicVolume) {
		if(activeMusic != null) {
			if(playMusic && activeMusic.isPlaying()) {
				activeMusic.changeVolume(musicVolume);
			}else {
				activeMusic.stop();
			}			
		} 

		this.playAudioEffects = playAudioEffects;
		this.playMusic = playMusic;
		this.effectsVolume = effectsVolume;
		this.musicVolume = musicVolume;
	}
	
	public boolean isPlaying() {
		if(activeMusic != null)	return activeMusic.isPlaying();
		else return false;
		
	}

	public void playPawnStep() {
		playSoundEffect(PAWN_STEP);
	}

	public void playPawnPlace() {
		playSoundEffect(PAWN_PLACE);
	}

	public void playPawnRemove() {
		playSoundEffect(PAWN_REMOVE);
	}

	public void playStartGame() {
		playMusic(START_GAME);
	}	

	public void playGameMusic() {
		playMusic(GAME_MUSIC);
	}	

	public void stopSound() {
		if(activeMusic != null) {
			activeMusic.stop();
		}
	}
	
	public void playWinnerMusic() {
		playMusic(WINNER_MUSIC);
	}
	
	public void playLoserMusic() {
		playMusic(LOSER_MUSIC);
	}
	
	public void playSwordFight() {
		playSoundEffect(SWORD_FIGHT);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			playSoundEffect(BUTTON_CLICK);
		} else if(e.getSource() instanceof JCheckBox) {
			playSoundEffect(CHECKBOX_CHECK);
		}	
	}

	/**
	 * Private class for playing a sound file.
	 * @author Anders Qvist
	 */
	private class SoundClip implements LineListener, Runnable {

		private SourceDataLine line;
		private AudioInputStream currentDecoded, encoded;
		private AudioFormat encodedFormat, decodedFormat;
		private boolean play, loopSound;
		private String soundPlaying;

		/**
		 * Initiates the SoundClip with the given sound file and volume.
		 * @param fileName
		 * @param volume
		 * @param loopSound
		 */
		public SoundClip(String fileName, int volume, boolean loopSound) {
			this.loopSound = loopSound;
			soundPlaying = fileName;

			URL url = null;
			try {
				url = new File(fileName).toURI().toURL();
			} catch (MalformedURLException e1) { }

			try {
				encoded = AudioSystem.getAudioInputStream(url);
			} catch (UnsupportedAudioFileException | IOException e) { }
			encodedFormat = encoded.getFormat();
			decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, encodedFormat.getSampleRate(), 16,
					encodedFormat.getChannels(), encodedFormat.getChannels() * 2, encodedFormat.getSampleRate(), false);
			currentDecoded = AudioSystem.getAudioInputStream(decodedFormat, encoded);

			try {
				line = AudioSystem.getSourceDataLine(decodedFormat);
				line.open(decodedFormat);
				changeVolume(volume);
			} catch (LineUnavailableException e) { }
		}

		/**
		 * Stops playback of the sound.
		 */
		public synchronized void stop() {
			play = false;
			loopSound = false;
		}

		/**
		 * Checks and returns whether or not the SounClip is playing.
		 */
		public synchronized boolean isPlaying() {
			return play;
		}

		/**
		 * Return the currently playing sound file (String).
		 * @return
		 */
		public synchronized String getSoundPlaying() {
			return soundPlaying;
		}

		/**
		 * Changes the volume of the SoundClip.
		 * @param volume int db
		 */
		public synchronized void changeVolume(int volume) {
			((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
		}

		public void run() {
			line.start();
			byte[] b = new byte[32 * 1024];
			int i = 0;
			play = true;
			while (play) {
				try {
					i = currentDecoded.read(b, 0, b.length);
					if (i == -1) {
						play = false;
					} else
						line.write(b, 0, i);
				} catch (IOException e) {
					System.err.println(e);
				}
			}
			line.drain();
			line.stop();

			line.close();
			try {
				currentDecoded.close();
				encoded.close();

			} catch (IOException e) {
				System.err.println(e);
			}

			if(loopSound) {
				playMusic(soundPlaying);
			}
		}

		public void update(LineEvent arg0) {}

	}

}
