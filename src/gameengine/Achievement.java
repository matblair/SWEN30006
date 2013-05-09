package gameengine;

import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public class Achievement {
	private Image image, imageLocked;
	private String name, description;
	private boolean unlocked;
	
	private final float filterAmount = 0.5f;
	
	public Achievement (String name, String description, boolean unlocked, String imgID) {
		this.name = name;
		this.description = description;
		this.unlocked = unlocked;
		image = AssetManager.requestAchiemeventResource(imgID);
		imageLocked = image.copy();
		imageLocked.setImageColor(filterAmount, filterAmount, filterAmount);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}
	
	public Image getUnlockedImage() {
		return image;
	}
	
	public Image getLockedImage() {
		return imageLocked;
	}
}
