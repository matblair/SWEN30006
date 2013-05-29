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
	private static final String DOWNIMGID="SMALLSWITCHDOWN";

	private static final String SHAPEID="SMALLSWITCHSHAPE";
	private int countdown=0;
	private static final int BODYTYPE = PhysUtils.STATIC;

	
	private CompanionCube cube;
	
	public LittleSwitch(Vec2 location, World world, Vec2 spawn)
			throws SlickException {
		super(IMGID);
		this.spawnpoint=spawn;
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, BODYTYPE);
		getBody().getFixtureList().setSensor(true);

	}


	
	public void trigger() throws SlickException{
		if(cube!=null){
			// Then destroy the old one.
			cube.cubeDestroy();
			cube=null;
		}
		cube = new CompanionCube(spawnpoint, GameState.getLevel().getWorld());
		GameState.getLevel().addCube(cube, cube.getBodyID());
		this.setSprite(AssetManager.requestImage(DOWNIMGID));
		countdown=1000;
		System.out.println("success");
	}



	public void updateState(float delta) {
		if(countdown!=0){
			countdown = (int) (countdown-delta);
		} else {
			this.setSprite(AssetManager.requestImage(IMGID));
		}
	}


}
