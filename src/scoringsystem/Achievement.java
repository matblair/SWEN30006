package scoringsystem;

import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public abstract class Achievement {
	private Image image;
	private String name, description, imgid;
	private boolean unlocked;
	private String actype;
	protected float target;
	protected int levelId;
	private float diff=0;
	private boolean persistant;
	
	/** Abstract methods that have to be implemented by other classes **/
	public abstract boolean checkUnlock(LevelStats stats);
	public abstract void decrementStats(LevelStats stats);
	
	/** The constructor for the achievements
	 * @param name The name of the achievememt
	 * @param description The achievement's description
	 * @param unlocked Whether the achievement is unlocked or not
	 * @param imgID The image id to load
	 * @param actype the achievement type
	 * @param levelid the level it pertains to 
	 * @param target The target of the achievement
	 * @param persistant Whether or not this achievement is persistant
	 */
	public Achievement (String name, String description, boolean unlocked, String imgID, String actype, int levelid, float target, boolean persistant) {
		this.name = name;
		this.setLevelId(levelid);
		this.actype=actype;
		this.target=target;
		this.description = description;
		this.unlocked = unlocked;
		imgid = imgID;
		image = AssetManager.requestAchiemeventResource(imgID);
		this.persistant=persistant;
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
	
	public Image getImage() {
		return image;
	}

	public String getImgid() {
		return imgid;
	}
	
	public void unlockAchievement(){
		unlocked=true;
	}
	
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

	public boolean isPersistant() {
		return persistant;
	}

	public void setPersistant(boolean persistant) {
		this.persistant = persistant;
	}

	public float getDiff() {
		return diff;
	}

	public void setDiff(float diff) {
		this.diff = diff;
	}



}
