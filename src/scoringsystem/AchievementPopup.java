package scoringsystem;

import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public class AchievementPopup {
	/** The timer for how long we display this for **/
	private int timer;
	/** The image for the achivement popup **/
	private Image img;
	/** The achievement name for display **/
	private String name;

	/** The constructor that sets the appropraite values
	 * 
	 * @param time
	 * @param imgid
	 * @param name
	 */
	public AchievementPopup(int time, String imgid, String name){
		this.timer=time;
		this.img = AssetManager.requestAchiemeventResource(imgid).getScaledCopy(32, 32);;
		this.name=name;
	}

	/** Return the timer to check if it should still be displayed
	 * 
	 * @return
	 */
	public int getTimer() {
		return timer;
	}

	/** Set the timer to the current elapsed time
	 * 
	 * @param timer
	 */
	public void setTimer(int timer) {
		this.timer = timer;
	}

	/** Get the image for rendering
	 * 
	 * @return
	 */
	public Image getImg() {
		return img;
	}

	/** Return the name for rendering
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}


}
