package resourcemanagers;

import gameengine.Sound;

import java.util.Map;

import org.newdawn.slick.openal.SoundStore;

public class SoundController {
	private static Map<String, Sound> sounds;
	
	public static final int MAXLEVEL=100;
	
	public static void initialise() {
		sounds = AssetManager.getSoundResources();
	}
	
	public static void play (String id) {
		Sound s = sounds.get(id);
		//s.play();
	}
	
	public static void stopMusic() {
		SoundStore.get().pauseLoop();
	}
	
	/** Set the audio level.
	 * @param volume Number in range [0,100]
	 */
	public static void setVolume(int volume) {
		if (volume < 0)
			volume = 0;
		else if (volume > 100)
			volume=100;
		
		float v = (float)volume/MAXLEVEL;
		SoundStore.get().setMusicVolume(v);
		SoundStore.get().setSoundVolume(v);
	}
	
	/** Get the audio level.
	 * @return Number in range [0,100]
	 */
	public static int getVolume() {
		int volume = Math.round(MAXLEVEL * SoundStore.get().getMusicVolume());
		return volume;
	}
}
