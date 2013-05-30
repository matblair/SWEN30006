package gameengine;

import gameobjects.PortalBullet;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class PortalShootRCHelper implements RayCastCallback {
	public Fixture fixture;
	public Vec2 point;
	public PortalBullet pb;
	private float fraction;

	/** Create a new PortalShootRCHelper in the level. Used by portal bullets.
	 * 
	 * @param pb The level in which the raycasting will be done
	 */
	public PortalShootRCHelper (PortalBullet pb) {
		super();
		this.pb = pb;
		this.fraction = 1f;
	}
	
	/** Decide what to do on fixture being found. Called by raycaster in world.
	 * 
	 * @param fixture The fixture being hit
	 * @param point The location in the world the fixture was hit
	 * @param normal The normal vector to the collision
	 * @param fraction The fraction of the raycast required to hit this point
	 * 
	 * @return -1 if ignore, 1 if relevant but want to keep looking.
	 */
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		if (!pb.interacts(fixture.getBody()))
			return -1;
		
		if (fraction < this.fraction) {
			this.fixture = fixture;
			this.point = point.clone();
			this.fraction = fraction;
		}
		
		return 1;
	}
}
