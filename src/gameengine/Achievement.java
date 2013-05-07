package gameengine;

import org.newdawn.slick.Image;

public class Achievement {
	private Image image, imageUnfocused;
	private String name, description;
	private boolean unlocked;
	
	private final float filterAmount = 0.5f;
	
	public Achievement (String name, String description, boolean unlocked, String imgID) {
		this.name = name;
		this.description = description;
		this.unlocked = unlocked;
		
		image = ;// Resource loader stuff here
		imageUnfocused = image.copy();
		imageUnfocused.setImageColor(filterAmount, filterAmount, filterAmount);
	}
}
