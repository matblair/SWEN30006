package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class EndLevel extends GameObject{
	private static final String IMGID="ENDGAME";
	private static final String SHAPEID="ENDGAMESHAPE";
	private static final int bodytype = PhysUtils.STATIC;

	/** Create a EndLevel object
	 * 
	 * @param location Location of the end level in the world (centre)
	 * @param world The physics world
	 * @throws SlickException
	 */
	public EndLevel(Vec2 location, World world){
		super(IMGID);
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, bodytype);
	}
	
	/** Create the fixture to be used by the body
	 * 
	 * @param shapeid The ID of the shape to request from AssetManager
	 * @return The FixtureDef created
	 */
	@Override
	protected FixtureDef createFixture(String shapeid) {
		FixtureDef def = super.createFixture(shapeid);
		def.isSensor=true;
		return def;
	}		
}
