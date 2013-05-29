package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;
import gameworlds.Level;

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
		
	public boolean isDestroyed() {
		return destroyed;
	}

	public CompanionCube(Vec2 location, World world)
			throws SlickException {
		super(IMGID);	
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, bodytype);
		getBody().setFixedRotation(false);
		animation = AssetManager.requestAnimationResources(ANIMID);
		animation.setLooping(false);
	}
	
	@Override
	protected FixtureDef createFixture(String shapeid) {
		FixtureDef def = super.createFixture(shapeid);
		def.restitution=cubeRestitution;
		return def;
	}

	public void update (int delta) {
		if(destroying){
			animation.update(delta);
			if(animation.isStopped()){
				destroyed=true;
			}
			this.setSprite(animation.getCurrentFrame());
		}	
	}
	

	public void cubeDestroy(){
		animation.start();
		destroying=true;
	}
	
}
