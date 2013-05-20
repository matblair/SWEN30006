package scoringsystem;

import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public abstract class Achievement {
	private Image image, imageLocked;
	private String name, description, imgid;
	private boolean unlocked;
	private String actype;
	protected float target;
	protected int levelId;

	
	private final float filterAmount = 0.5f;
	
	public Achievement (String name, String description, boolean unlocked, String imgID, String actype, int levelid, float target) {
		this.name = name;
		this.setLevelId(levelid);
		this.actype=actype;
		this.target=target;
		this.description = description;
		this.unlocked = unlocked;
		imgid = imgID;
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

	public String getImgid() {
		return imgid;
	}
	
	public void unlockAchievement(){
		unlocked=true;
	}
	
	public abstract boolean checkUnlock(LevelStats stats);

	public String getActype() {
		return actype;
	}

	public float getTarget() {
		return target;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}


}
