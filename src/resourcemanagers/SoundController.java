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
		
	}
}
