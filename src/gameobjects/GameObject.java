package gameobjects;

import gameengine.PhysUtils;
import gameengine.PortalCollisionRCHelper;
import gameworlds.Level;

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
	
	private boolean inPortal;
	private Vec2 last;

	public GameObject (String imgid, Vec2 location, World world, int bodytype)
			throws SlickException {
		setSprite(AssetManager.requestImage(imgid));
		dimensions = PhysUtils.SlickToJBoxVec(new Vec2(sprite.getWidth(), sprite.getHeight()));
		createBody(location,world,bodytype);
		last = location;
		System.out.printf ("(x,y) = (%4.2f,%4.2f)\n", location.x, location.y);
		System.out.printf ("(w,h) = (%4.2f,%4.2f)\n", dimensions.x/2, dimensions.y/2);
	}
	
	public GameObject(){
		
	}
	
	public void update(Level level) {
		ContactEdge edge = body.getContactList();
		while (edge != null) {
			if (level.getBodyType(edge.other).equals("portal")) {
				inPortal = true;
				break;
			}
			inPortal = false;
			edge = edge.next;
		}
		
		if (inPortal)
			checkPortalTransition(level);
		
		last = getLocation().clone();
	}
	
	private void checkPortalTransition(Level level) {
		PortalCollisionRCHelper rch = new PortalCollisionRCHelper(level);
		body.getWorld().raycast(rch, last, getLocation());
		if (rch.fixture != null) {
			Portal portals[] = level.getPortals();
			Portal portalHit;
			String bodyID = rch.fixture.getBody().toString();
			
			if (bodyID.equals(portals[Portal.BLUE].getBodyId())) {
				System.out.println("entering blue");
				portalHit = portals[Portal.BLUE];
			} else {
				System.out.println("entering orange");
				portalHit = portals[Portal.ORANGE];
			}
			
			Portal otherPortal = portalHit.getLinkedPortal();
			float rotateBy = PhysUtils.getAngle(otherPortal.getUnitTangent()) - PhysUtils.getAngle(portalHit.getUnitTangent()) + (float) Math.PI;
			float portalHitOffset = rch.point.sub(portalHit.getLocation()).length();
			
			Vec2 remainingTravel = PhysUtils.rotateVector(getLocation().sub(last).mul(rch.fraction), rotateBy);
			System.out.println(remainingTravel + " " + (rch.fraction) + " " + getLocation().sub(last));
			Vec2 appear = otherPortal.getLocation().sub(otherPortal.getUnitTangent().mul(portalHitOffset)).add(remainingTravel);
			body.setTransform(appear, body.getAngle());
			body.setLinearVelocity(PhysUtils.rotateVector(body.getLinearVelocity(), rotateBy));
			System.out.println(PhysUtils.rotateVector(body.getLinearVelocity(), rotateBy).length() + " " + body.getLinearVelocity().length());
		}
	}
	
	private void createBody(Vec2 location, World world, int bodytype) {
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
	
	public float getRotation() {
		return body.getAngle();
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
			if (normal.y > maxYNormForGround) {
				return true;
			}
			edge = edge.next;
		}
		return false;
	}
	
	public boolean isInPortal() {
		return inPortal;
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

	/**
	 * @return the bodyId
	 */
	public String getBodyId() {
		return body.toString();
	}
}
