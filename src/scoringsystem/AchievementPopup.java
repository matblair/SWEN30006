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
		return img;
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
