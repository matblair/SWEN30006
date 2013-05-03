package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class MovingPlatform extends GameObject {

	public MovingPlatform(String imgloc, Vec2 location, World world)
			throws SlickException {
		super(imgloc, location, world, PhysUtils.KINEMATIC);
		// TODO Auto-generated constructor stub
	}

}
