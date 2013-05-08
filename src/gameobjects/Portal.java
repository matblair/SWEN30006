package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Portal extends GameObject {
	public static final int BLUE=0, ORANGE=1;
	
	private float height;
	private boolean enabled=false;
	private Portal otherPortal;
	private Wall wall;
	private Wall startSegment, endSegment;
	private World world;
	
	public Portal(String imgid, Vec2 location, World world)
			throws SlickException {
		super(imgid, location, world, PhysUtils.PORTAL);
		getBody().getFixtureList().setSensor(true);
		this.world = world;
		height = getDimensions().y;
	}
	
	public void linkPortals(Portal portal){
		otherPortal = portal;
	}
	
	public void disable() {
		this.wall.enable();
		startSegment.destroy();
		startSegment = null;
		endSegment.destroy();
		endSegment = null;
		getBody().setTransform(new Vec2(-1, 0), 0);
		enabled = false;
	}
	
	public void hitWall (Vec2 loc, Wall wall) throws SlickException{
		if (wall.getLength() < height)
			return;
		
		System.out.println(height / 2);
		
		// Clean up
		if (enabled)
			disable();
		
		Vec2 unitTangent = wall.getUnitTangent();
		// Correct for hits near ends of walls
		if (PhysUtils.distance(loc, wall.getStart()) < height / 2)
			loc = wall.getStart().add(unitTangent.mul(height / 2));
		else if (PhysUtils.distance(loc, wall.getEnd()) < height / 2)
			loc = wall.getEnd().sub(unitTangent.mul(height / 2));
		
		// Add replacement wall segments
		startSegment = new Wall(wall.getStart(), loc.sub(unitTangent.mul(height/2)), world);
		System.out.println(startSegment);
		endSegment = new Wall(loc.add(unitTangent.mul(height/2)), wall.getEnd(), world);
		System.out.println(endSegment);
		
		this.getBody().setTransform(loc, PhysUtils.getAngle(wall.getUnitNormal()));

		this.wall = wall;
		this.wall.disable();
		
		enabled = true;
	}
}
