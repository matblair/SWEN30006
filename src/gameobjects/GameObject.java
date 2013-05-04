package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.collision.WorldManifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;

import resourcemanagers.AssetManager;

public class GameObject {
	/** The object's image **/
	private Image sprite;
	
	

	/** The objects name **/
	private String name;
	private Body body;
	private Vec2 dimensions; // JBox dimensions
	
	private final int maxAngleForGround = 45;
	private final float maxYNormForGround = (float)Math.asin(maxAngleForGround/180*Math.PI);

	public GameObject (String imgid, Vec2 location, World world, int bodytype)
			throws SlickException {
		setSprite(AssetManager.requestImage(imgid));
		dimensions = PhysUtils.SlickToJBoxVec(new Vec2(getImage().getWidth(), getImage().getHeight()));
		createBody(location,world,bodytype);		
		System.out.printf ("(x,y) = (%4.2f,%4.2f)\n", location.x, location.y);
		System.out.printf ("(w,h) = (%4.2f,%4.2f)\n", dimensions.x/2, dimensions.y/2);
	}
	
	private void createBody(Vec2 location, World world, int bodytype){
		BodyDef bd = new BodyDef();
		bd.position.set(location);
		switch (bodytype){
			case PhysUtils.STATIC: 
				bd.type = BodyType.STATIC;
				break;
			case PhysUtils.DYNAMIC:
				bd.type = BodyType.DYNAMIC;
				break;
			case PhysUtils.KINEMATIC:
				bd.type = BodyType.KINEMATIC;
				break;
			case PhysUtils.PORTAL: 
				bd.type = BodyType.STATIC;
				break;
		}
		
		bd.fixedRotation = true;
		body = world.createBody(bd);
		
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(dimensions.x/2, dimensions.y/2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		body.createFixture(fixtureDef);
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vec2 getDimensions() {
		return dimensions;
	}
	
	public Vec2 getLocation() {
		return body.getPosition();
	}
	
	public float getMass() {
		return body.getMass();
	}
	
	public boolean isOnGround() {
		ContactEdge edge = this.getBody().getContactList();
		WorldManifold wm = new WorldManifold();
		Vec2 normal;
		while (edge != null) {
			edge.contact.getWorldManifold(wm);
			normal = wm.normal;
			//System.out.println(edge.contact.m_fixtureB.getBody());
			//System.out.println(normal + " is the normal");
			if (normal.y > maxYNormForGround)
				return true;
			edge = edge.next;
		}
		return false;
	}
	
	/** Returns the top left position of the player
	 * @return Top left position of player as a JBox position
	 */
	public Vec2 getTopLeftLoc() {
		Vec2 addVec = new Vec2(-dimensions.x/2, dimensions.y/2);
		return body.getPosition().add(addVec);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Image getImage() {
		return sprite;
	}
	
	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}
}
