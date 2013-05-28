package scoringsystem;

import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public class AchievementPopup {
	 private int timer;
	 private Image img;
	 private String name;
	 
	 public AchievementPopup(int time, String imgid, String name){
		 this.timer=time;
		 this.img = AssetManager.requestAchiemeventResource(imgid);
		 this.setName(name);
	 }

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public Image getImg() {
		Image newimg = img.getScaledCopy(32, 32);
		return newimg;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	 
	
}
