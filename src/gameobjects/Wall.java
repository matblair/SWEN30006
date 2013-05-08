package gameobjects;

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
		
		//System.out.println(start.x + "," + start.y + " to " + end.x + "," + end.y + " with unit tangent " + getUnitTangent().x + "," + getUnitTangent().y);
	}
	
	public String getBodyId() {
		return bodyID;
	}
	
	public Vec2 getStart() {
		return start;
	}
	
	public Vec2 getUnitTangent() {
		Vec2 wall = end.sub(start);
		return wall.mul(1/wall.length());
	}
}
