package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;


public class CompanionCube extends GameObject {
	
	private static float cubeRestitution=0.35f;
	private static final String ANIMID="CUBEDESTROY";
	private static final String IMGID="CUBE";
	private static final String SHAPEID="CUBESHAPE";
	private static final int bodytype = PhysUtils.DYNAMIC;
	
	private Animation animation;
	private boolean destroying;
	private boolean destroyed;

	/** Create a CompanionCube object
	 * 
	 * @param location Location of the cube to start in the world (centre)
	 * @param world The physics world
	 * @throws SlickException
	 */
	public CompanionCube(Vec2 location, World world)
			throws SlickException {
		super(IMGID);	
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, bodytype);
		getBody().setFixedRotation(false);
		animation = AssetManager.requestAnimationResources(ANIMID);
		animation.setLooping(false);
	}
	
	/** Create the fixture to be used by the body
	 * 
	 * @param shapeid The ID of the shape to request from AssetManager
	 * @return The FixtureDef created
	 */
	@Override
	protected FixtureDef createFixture(String shapeid) {
		FixtureDef def = super.createFixture(shapeid);
		def.restitution=cubeRestitution;
		return def;
	}

	/** Update the cube's state.
	 * 
	 * @param delta Milliseconds since last update
	 */
	public void update (int delta) {
		if(destroying){
			animation.update(delta);
			if(animation.isStopped()){
				destroyed=true;
			}
			this.setSprite(animation.getCurrentFrame());
		}	
	}
	
	/** Check if the cube is destroyed
	 * 
	 * @return true if destroyed
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/** Destroy the cube.
	 */
	public void cubeDestroy(){
		animation.start();
		destroying=true;
	}
	
}
