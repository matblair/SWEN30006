package gameengine;

import gameobjects.GameObject;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;

public class Camera {
	private Vec2 location, bounds, dimensions; // JBox coords
	
	public Camera(Vec2 bounds) {
		this.bounds = bounds;
		location = new Vec2();
		dimensions = new Vec2();
	}

	public void follow(GameContainer gc, GameObject obj) {
		dimensions.x = PhysUtils.pixelsToMetres(gc.getWidth());
		dimensions.y = PhysUtils.pixelsToMetres(gc.getHeight());
		
		this.location = obj.getBody().getPosition().add(dimensions.mul(0.5f).negate());
		bound(gc);
	}

	private void bound(GameContainer gc) {
		if (location.x < 0) {
			location.x = 0;
		} else if (location.x > bounds.x - dimensions.x) {
			location.x = bounds.x - dimensions.x;
		}
		
		if (location.y < 0) {
			location.y = 0;
		} else if (location.y > bounds.y - dimensions.y) {
			location.y = bounds.y - dimensions.y;
		}
	}
	
	public boolean inView(GameObject obj) {
		// TODO: correct this
		return true;
	}
	
	public Vec2 getDimensions() {
		return dimensions;
	}
	
	public Vec2 getLocation() {
		return location;
	}
}