package gameengine;

import java.io.IOException;

import org.newdawn.slick.openal.DeferredSound;
import org.newdawn.slick.util.ResourceLoader;

import resourcemanagers.SoundController;

public class Sound {
	private DeferredSound sound;
	private boolean music;
	
	// Sound effects
	public static final String JUMP="JUMP";
	public static final String PORTALTRAVEL="PORTALTRANSITION";
	public static final String PORTALOPEN="PORTALOPEN";

	// Music
	public static final String TITLE="TITLE";
	public static final String VILLAGE="VILLAGE";

	public Sound (String path, String style) {
		music = style.equalsIgnoreCase("music");
		try {
			sound = new DeferredSound (path, ResourceLoader.getResourceAsStream(path), DeferredSound.OGG);
			sound.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if (music)
			sound.playAsMusic(1f, 1f, true);
		else
			sound.playAsSoundEffect(1f, (float)SoundController.getVolume()/100, false);
		System.out.println((float)SoundController.getVolume()/100);
	}
}