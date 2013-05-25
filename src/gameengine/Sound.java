package gameengine;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;

public class Sound {
	private org.newdawn.slick.Sound sound;
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
		boolean prev = SoundStore.get().isDeferredLoading();
		SoundStore.get().setDeferredLoading(false);
		try {
			sound = new org.newdawn.slick.Sound (path);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		SoundStore.get().setDeferredLoading(prev);
	}

	public void play() {
		if (music)
			sound.loop();
		else
			sound.play();
	}
}
