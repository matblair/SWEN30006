package scoringsystem;

import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public abstract class Achievement {
	
	/** The achievement image for this achievement **/
	private Image image;
	/** The name, description and img id for loading of this achievement **/
	private String name, description, imgid;
	/** Whether or not this achievement is unlocked **/
	private boolean unlocked;
	/** The achievement type **/
	private String actype;
	/** The target as a float **/
	protected float target;
	
	/** The level id that this pertains to
	 * -1 signifies a universal achievement
	 * Any other integer specifies a specific level
	 */
	protected int levelId;
	
	/** The difference between last check and current check **/
	private float diff=0;
	/** Whether the state of this achievement should be persistant between game isntances **/
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
	
	/** Get the achievement name for rendering
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/** Get the achievement description for rendering
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/** Get the achievment unlock status
	 *  
	 * @return unlocked
	 */
	public boolean isUnlocked() {
		return unlocked;
	}
	
	/** Get the achievement image for rendering
	 * 
	 * @return image
	 */
	public Image getImage() {
		return image;
	}

	/** Unlock an achievement
	 */
	public void unlockAchievement(){
		unlocked=true;
	}

	/** Get the image id for creating popus
	 * and saving achievements to file
	 *
	 * @return imgid
	 */
	public String getImgid(){
		return this.imgid;
	}

	/** Return the target for saving to file
	 * 
	 * @return target
	 */
	public float getTarget() {
		return target;
	}

	/** Return this level id for saving to file
	 * 
	 * @return levelId
	 */
	public int getLevelId() {
		return levelId;
	}

	/** Set the level id for this achievemetn
	 * 
	 * @param levelId
	 */
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	/** Check if the achievement is persistant when writing and updating achievement 
	 * states
	 * @return persistant
	 */
	public boolean isPersistant() {
		return persistant;
	}

	/** Set the achievement as persistant for use in the constructor
	 * 
	 * @param persistant
	 */
	public void setPersistant(boolean persistant) {
		this.persistant = persistant;
	}

	/** Get the difference for testing unlock state
	 * 
	 * @return diff
	 */
	public float getDiff() {
		return diff;
	}

	/** Set the difference to check unlock
	 * 
	 * @param diff
	 */
	public void setDiff(float diff) {
		this.diff = diff;
	}
	
	/** Return the ac type for writing to file
	 * 
	 * @return actype
	 */
	public String getActype() {
		return this.actype;
	}



}
