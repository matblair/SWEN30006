package gameengine;

import gameworlds.Level;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class PortalCollisionRCHelper implements RayCastCallback {
	public Fixture fixture;
	public Vec2 point;
	public float fraction;
	public Level level;
	
	/** Create a new PortalCollisionRCHelper in the level. Used for portal transitions.
	 * 
	 * @param level The level in which the raycasting will be done
	 */
	public PortalCollisionRCHelper (Level level) {
		super();
		this.level = level;
	}
	
	/** Decide what to do on fixture being found. Called by raycaster in world.
	 * 
	 * @param fixture The fixture being hit
	 * @param point The location in the world the fixture was hit
	 * @param normal The normal vector to the collision
	 * @param fraction The fraction of the raycast required to hit this point
	 * 
	 * @return -1 if keep looking, 0 if done.
	 */
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		if (!level.getBodyType(fixture.getBody()).equals("portal"))
			return -1;
		
		this.fixture = fixture;
		this.point = point;
		this.fraction = fraction;
		return 0;
	}
}
