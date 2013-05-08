package gameengine;

import gameworlds.Level;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class RayCastHelper implements RayCastCallback {
	public Fixture fixture;
	public Vec2 point;
	public Level level;
	
	public RayCastHelper (Level level) {
		super();
		this.level = level;
	}
	
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		if (!level.portalBulletInteracts(fixture.getBody().toString()))
			return -1;
		
		System.out.println("found target");
		this.fixture = fixture;
		this.point = point;
		return 0;
	}
}
