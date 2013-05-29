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

	public PortalShootRCHelper (PortalBullet pb) {
		super();
		this.pb = pb;
		this.fraction = 1f;
	}
	
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
