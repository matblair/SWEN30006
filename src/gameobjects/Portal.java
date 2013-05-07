package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Portal extends GameObject {

	Portal otherconnection;

	
	public Portal(String imgloc, Vec2 location, World world)
			throws SlickException {
		super(imgloc, location, world, PhysUtils.PORTAL);
		// TODO Auto-generated constructor stub
	}
	
	public void linkPortals(Portal portal){
		otherconnection = portal;
	}

}
