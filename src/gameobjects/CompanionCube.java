package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;


public class CompanionCube extends GameObject {
	
	private static float cubeRestitution=0.35f;
	
	private static final String IMGID="CUBE";
	private static final String SHAPEID="CUBESHAPE";
	private static final int bodytype = PhysUtils.DYNAMIC;
	
	public CompanionCube(Vec2 location, World world)
			throws SlickException {
		super(IMGID);	
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, bodytype);
		getBody().setFixedRotation(false);
	}
	
	@Override
	protected FixtureDef createFixture(String shapeid) {
		FixtureDef def = super.createFixture(shapeid);
		def.restitution=cubeRestitution;
		return def;
	}
	
}
