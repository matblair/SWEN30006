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
	
	/** Udpate the camera location such that it follows the player.
	 * Camera is bounded to the edge of the map, so that dead areas beyond the edges of the map shouldn't ever be rendered.
	 * 
	 * @param gc The GameContainer in which the view must be bound.
	 * @param obj The GameObject that the camera should follow.
	 */
	public void follow(GameContainer gc, GameObject obj) {
		dimensions.x = PhysUtils.pixelsToMetres(gc.getWidth());
		dimensions.y = PhysUtils.pixelsToMetres(gc.getHeight());
		
		this.location = obj.getBody().getPosition().add(dimensions.mul(0.5f).negate());
		bound(gc);
	}

	/** Bound the view to the edges of the map. That is, override whatever the camera is following so that dead areas are not shown.
	 * 
	 * @param gc The GameContainer in which the view must be bound.
	 */
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
	
	/** Function to see if object is in the field of view.
	 * 
	 * @param obj Object to check.
	 * @return true if the object is (at least partially) in view.
	 */
	public boolean inView(GameObject obj) {
		// TODO: correct this
		return true;
	}
	
	/** Get the dimensions of the camera field of view.
	 * 
	 * @return Dimensions of field of view in metres.
	 */
	public Vec2 getDimensions() {
		return dimensions;
	}
	
	/** Get the location of the camera (bottom left point).
	 * 
	 * @return Location of camera.
	 */
	public Vec2 getLocation() {
		return location;
	}
}