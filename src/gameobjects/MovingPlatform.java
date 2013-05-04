package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class MovingPlatform extends GameObject {

	public MovingPlatform(String imgid, Vec2 location, float width, float height, World world)
			throws SlickException {
		super(imgid,location,world, PhysUtils.STATIC);
	}

}
