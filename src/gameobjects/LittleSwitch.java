package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class LittleSwitch extends GameObject {

	private Vec2 spawnpoint;
	
	private static final String IMGID="SMALLSWITCH";
	private static final String SHAPEID="SMALLSWITCHSHAPE";

	private static final int BODYTYPE = PhysUtils.STATIC;

	
	private static CompanionCube cube;
	
	public LittleSwitch(Vec2 location, World world, Vec2 spawn)
			throws SlickException {
		super(IMGID);
		this.spawnpoint=spawn;
		FixtureDef fixture = createFixture();
		this.createBody(location, world, fixture, BODYTYPE);
		getBody().getFixtureList().setSensor(true);

	}

	private FixtureDef createFixture(){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = AssetManager.requestShape(SHAPEID);
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		return fixtureDef;
	}
	
	public void trigger() throws SlickException{
		if(cube!=null){
			// Then destroy the old one.
			GameState.getLevel().removeCube(cube);
			GameState.getLevel().getPhysWorld().destroyBody(cube.getBody());
			cube=null;
		}
		cube = new CompanionCube(spawnpoint, GameState.getLevel().getPhysWorld());
		GameState.getLevel().addCube(cube, cube.getBodyId());
		System.out.println("success");
	}


}
