package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Wall extends GameObject {

	public Wall(String imgid, Vec2 location, float width, float height, World world)
			throws SlickException {
		super();
		PhysUtils.addWall(world, location.x, location.y, width, height);
		// TODO Auto-generated constructor stub
	}

}
