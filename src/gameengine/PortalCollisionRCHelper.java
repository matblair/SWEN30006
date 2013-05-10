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
	
	public PortalCollisionRCHelper (Level level) {
		super();
		this.level = level;
	}
	
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
