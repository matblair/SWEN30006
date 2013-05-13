package gameengine;

import gameworlds.Level;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class PortalShootRCHelper implements RayCastCallback {
	public Fixture fixture;
	public Vec2 point;
	public Level level;
	private float fraction;

	public PortalShootRCHelper (Level level) {
		super();
		this.level = level;
		this.fraction = 1f;
	}
	
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		if (!level.portalBulletInteracts(fixture.getBody().toString()))
			return -1;
		
		if (fraction < this.fraction) {
			this.fixture = fixture;
			this.point = point.clone();
			this.fraction = fraction;
		}
		
		return 1;
	}
}
