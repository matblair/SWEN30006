package gameengine;

import gameworlds.Level;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class PortalShootRCHelper implements RayCastCallback {
	public Fixture fixture;
	public Fixture closestFixture;
	public Vec2 point;
	public Level level;
	public Vec2 playerLoc;
	
	public PortalShootRCHelper (Level level) {
		super();
		this.level = level;
		playerLoc = level.getLevelPlayer().getLocation();
	}
	
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		if (!level.portalBulletInteracts(fixture.getBody().toString()))
			return -1;
		
		this.fixture = fixture;
		this.point = point.clone();
		
		if(PhysUtils.distance(playerLoc,point)<PhysUtils.distance(playerLoc, this.point)){
			closestFixture = fixture;
			System.out.println(closestFixture);
		}
		
		return 0;
	}
}
