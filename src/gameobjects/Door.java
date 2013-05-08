package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class Door extends GameObject {
	private boolean isOpen=false;
	private Image openImage, closedImage;

	public Door(String imgid, Vec2 location, World world, String openImageId)
			throws SlickException {
		super(imgid, location, world, PhysUtils.STATIC);
		closedImage = AssetManager.requestImage("DOORCLOSE");
		openImage = AssetManager.requestImage("DOOROPEN");
	}
	
	public void update (int delta) {
		
	}
	
	@Override
	public Image getImage() {
		if (isOpen) {
			return openImage;
		} else {
			return closedImage;
		}
	}
	
	public void open() {
		isOpen = true;
		getBody().getFixtureList().setSensor(true);
	}
	
	public void close() {
		isOpen = false;
		getBody().getFixtureList().setSensor(false);
	}
	
	public void toggle() {
		if (isOpen) {
			close();
		} else {
			open();
		}
	}
}
