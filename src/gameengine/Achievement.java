package gameengine;

import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public class Achievement {
	private Image image, imageLocked;
	private String name, description;
	private boolean unlocked;
	
	private final float filterAmount = 0.5f;
	
	public Achievement (String name, String description, boolean unlocked, String imgID) {
		this.setName(name);
		this.setDescription(description);
		this.setUnlocked(unlocked);
		image = AssetManager.requestAchiemeventResource("RISINGSUN");
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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the unlocked
	 */
	public boolean isUnlocked() {
		return unlocked;
	}

	/**
	 * @param unlocked the unlocked to set
	 */
	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
