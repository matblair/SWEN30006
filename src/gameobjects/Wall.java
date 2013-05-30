package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Wall {
	private Vec2 start, end;
	private Body body;
	private String bodyID;
	
	/** Create a new Wall object. Start to finish vector cross with imaginary vector pointing
	 * into the screen has to point into the level, not back into the wall (use right hand rule).
	 * 
	 * @param start Vector at start of the wall
	 * @param end Vector at end of the wall
	 * @param world The world in which the body should be created
	 * @throws SlickException
	 */
	public Wall(Vec2 start, Vec2 end, World world)
			throws SlickException {
		this.start = start;
		this.end = end;
		
		Vec2 loc = start.add(end).mul(0.5f);
		Vec2 halfVec = end.sub(start).mul(0.5f);
		
		BodyDef bd = new BodyDef();
		bd.position.set(loc);
		bd.type = BodyType.STATIC;
		body = world.createBody(bd);
		bodyID = body.toString();
		
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(halfVec.negate(), halfVec);
		FixtureDef fd = new FixtureDef();
		fd.shape = edgeShape;
		fd.friction=0.3f;
		body.createFixture(fd);
	}
	
	/** Enable the wall
	 */
	public void enable() {
		body.getFixtureList().setSensor(false);
	}
	
	/** Disable the wall
	 */
	public void disable() {
		body.getFixtureList().setSensor(true);
	}
	
	/** Check if the wall is enabled (not a sensor)
	 * 
	 * @return true if the wall is enabled
	 */
	public boolean isEnabled() {
		return !body.getFixtureList().isSensor();
	}
	
	/** Destroy the wall
	 */
	public void destroy() {
		body.getWorld().destroyBody(body);
	}
	
	/** Get the ID of the Body of the wall
	 * 
	 * @return The ID
	 */
	public String getBodyId() {
		return bodyID;
	}
	
	/** Get the start point of the wall
	 * 
	 * @return The start point
	 */
	public Vec2 getStart() {
		return start;
	}
	
	/** Get the end point of the wall
	 * 
	 * @return The end point
	 */
	public Vec2 getEnd() {
		return end;
	}
	
	/** Get the length of the wall
	 * 
	 * @return The length point
	 */
	public float getLength() {
		return PhysUtils.distance(start, end);
	}
	
	/** Get a unit vector tangent to the portal
	 * 
	 * @return Tangent unit vector
	 */
	public Vec2 getUnitTangent() {
		Vec2 wall = end.sub(start);
		return PhysUtils.unitVector(wall);
	}
	
	/** Get a unit vector normal to the portal
	 * 
	 * @return Normal unit vector
	 */
	public Vec2 getUnitNormal() {
		Vec2 tangent = getUnitTangent();
		return new Vec2(-tangent.y, tangent.x);
	}
	
	/** Convert the wall to a string, and get useful information about it
	 */
	@Override
	public String toString() {
		return "Wall from " + getStart() + " to " + getEnd() + " with unit tangent " + getUnitTangent() + " and unit normal " + getUnitNormal();
	}
}
