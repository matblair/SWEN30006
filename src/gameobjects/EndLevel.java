package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class EndLevel extends GameObject{
	private static final String IMGID="CUBE";
	private static final String SHAPEID="CUBESHAPE";
	private static final int bodytype = PhysUtils.STATIC;

	
	public EndLevel(Vec2 location, World world){
		super(IMGID);
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, bodytype);
	}


	@Override
	protected FixtureDef createFixture(String shapeid) {
		FixtureDef def = super.createFixture(shapeid);
		def.isSensor=true;
		return def;
	}
	
	
}
