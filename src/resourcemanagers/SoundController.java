package resourcemanagers;

import gameengine.Sound;

import java.util.Map;

import org.newdawn.slick.openal.SoundStore;

public class SoundController {
	private static Map<String, Sound> sounds;
	
	public static void initialise() {
		sounds = AssetManager.getSoundResources();
	}
	
	public static void play (String id) {
		Sound s = sounds.get(id);
		s.play();
	}
	
	public static void stopMusic() {
		SoundStore.get().pauseLoop();
	}
	
	/**
	 * Set the audio level.
	 * @param volume Number in range [0,100]
	 */
	public static void setVolume(int volume) {
		if (volume < 0)
			volume = 0;
		else if (volume > 100)
			volume=100;
		SoundStore.get().setMusicVolume(volume/100f);
	}
	
	public static int getVolume() {
		int volume = Math.round(100 * SoundStore.get().getMusicVolume());
		return volume;
	}
}
