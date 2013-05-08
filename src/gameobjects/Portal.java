package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Portal extends GameObject {
	public static final int BLUE=0, ORANGE=1;
	private boolean enabled=false;
	private Portal otherconnection;
	private Wall wall;
	
	public Portal(String imgid, Vec2 location, World world)
			throws SlickException {
		super(imgid, location, world, PhysUtils.PORTAL);
		getBody().getFixtureList().setSensor(true);
	}
	
	public void linkPortals(Portal portal){
		otherconnection = portal;
	}
	
	public void hitWall (Vec2 loc, Wall wall){
		// Re-enable old wall
		if (this.wall != null)
			this.wall.enable();
		
		enabled = true;
		this.getBody().setTransform(loc, PhysUtils.getAngle(wall.getUnitNormal()));
		System.out.println(wall.getUnitNormal() + " " + PhysUtils.getAngle(wall.getUnitNormal()));

		this.wall = wall;
		this.wall.disable();
	}
}
