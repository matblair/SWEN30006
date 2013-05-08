package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class LittleSwitch extends Switch {

	private Vec2 spawnpoint;
	private String cubeid;
	
	private static CompanionCube cube;
	
	public LittleSwitch(String imgloc, Vec2 location, World world, Vec2 spawn, String cubeimgid)
			throws SlickException {
		super(imgloc, location, world, PhysUtils.STATIC);
		this.spawnpoint=spawn;
		this.cubeid=cubeimgid;
		getBody().getFixtureList().setSensor(true);

	}
	
	public void trigger() throws SlickException{
		if(cube!=null){
			// Then destroy the old one.
			GameState.getLevel().removeCube(cube);
			GameState.getLevel().getPhysWorld().destroyBody(cube.getBody());
			cube=null;
		}
		cube = new CompanionCube(cubeid, spawnpoint, GameState.getLevel().getPhysWorld());
		GameState.getLevel().addCube(cube, cube.getBodyId());
		System.out.println("success");
	}


}
