package gameengine;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class RayCastHelper implements RayCastCallback {
	public Fixture fixture;
	public Vec2 point;
	
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		this.fixture = fixture;
		this.point = point;
		return 0;
	}
}
