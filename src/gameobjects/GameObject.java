package gameobjects;

import gameengine.PhysUtils;
import gameengine.PortalCollisionRCHelper;
import gamestates.GameState;
import gameworlds.Level;

import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public class GameObject {
	/** The object's image **/
	private Image sprite;
	
	private Body body;
	
	private Vec2 dimensions;
	
	private final int maxAngleForGround = 45;
	private final float maxYNormForGround = (float)Math.asin(maxAngleForGround/180*Math.PI);
	private boolean inPortal;
	private Vec2 last;


	public GameObject(String imgid){
		setSprite(AssetManager.requestImage(imgid));
	}
	
	public GameObject(){
	
	}
	
	protected void createBody(Vec2 location, World world, FixtureDef definition, int bodytype) {
		BodyDef bd = new BodyDef();
		bd.position.set(location);

		switch (bodytype){
			case PhysUtils.STATIC: 
				bd.type = BodyType.STATIC;
				break;
			case PhysUtils.DYNAMIC:
				bd.type = BodyType.DYNAMIC;
				bd.linearDamping = 0.1f;
				break;
			case PhysUtils.KINEMATIC:
				bd.type = BodyType.KINEMATIC;
				break;
			case PhysUtils.PORTAL: 
				bd.type = BodyType.STATIC;
				break;
		}
		
		bd.fixedRotation = true;
		setBody(world.createBody(bd));
		getBody().createFixture(definition);
	}
	
	public void update(Level level) {
		ContactEdge edge = getBody().getContactList();
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
		getBody().getWorld().raycast(rch, last, getLocation());
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
			getBody().setTransform(appear, getBody().getAngle());
			getBody().setLinearVelocity(PhysUtils.rotateVector(getBody().getLinearVelocity(), rotateBy));
			System.out.println(PhysUtils.rotateVector(getBody().getLinearVelocity(), rotateBy).length() + " " + getBody().getLinearVelocity().length());
		}
	}
	
	protected FixtureDef createFixture(String shapeid){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = AssetManager.requestShape(shapeid);
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		return fixtureDef;
	}
	
	public Body getBody() {
		return body;
	}
	
	
	public Vec2 getLocation() {
		return getBody().getPosition();
	}
	
	public float getRotation() {
		return getBody().getAngle();
	}
	
	public float getMass() {
		return getBody().getMass();
	}
	
	public boolean isOnGround() {
		ContactEdge edge = this.getBody().getContactList();
		WorldManifold wm = new WorldManifold();
		Vec2 normal;
		while (edge != null) {
			edge.contact.getWorldManifold(wm);
			normal = wm.normal;
			String edgetype = GameState.getLevel().getBodyType(edge.contact.m_fixtureA.getBody());
			if (normal.y > maxYNormForGround || edgetype.equals("movingplatform")) {
				return true;
			}
			edge = edge.next;
		}
		return false;
	}

	public Vec2 getDimensions(){
		return dimensions;
	}
	
	public void setDimensions(Vec2 dim){
		dimensions=dim;
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
		return getBody().toString();
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Body body) {
		this.body = body;
	}
	
}
