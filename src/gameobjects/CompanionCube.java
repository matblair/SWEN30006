package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class CompanionCube extends GameObject {
	
	private static float cubeRestitution=0.35f;
	
	public CompanionCube(String imgloc, Vec2 location, World world)
			throws SlickException {
	
		super(imgloc, location, world, PhysUtils.DYNAMIC);		
		getBody().setFixedRotation(false);
		getBody().getFixtureList().setRestitution(cubeRestitution);
	}

	public CompanionCube() {
		super();
	}

}
