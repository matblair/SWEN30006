package gameobjects;

import gameengine.FixtureCallback;
import gameengine.PhysUtils;
import gameengine.PortalCollisionRCHelper;
import gameengine.Sound;
import gamestates.GameState;
import gameworlds.Level;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;
import resourcemanagers.SoundController;

public class GameObject {
	/** The object's image **/
	private Image sprite;

	private Body body;
	private final float DISTDOWN = 0.1f;
	private final float CLIPDIST = 0.2f;
	private Vec2 dimensions;
	private boolean inPortal;
	private Portal portalIn;
	private Vec2 last;
	private static final float MINYEXIT = 6f;

	/** Create a GameObject
	 * 
	 * @param imgid The ID of the image to request from AssetManager
	 */
	public GameObject(String imgid){
		setSprite(AssetManager.requestImage(imgid));
	}

	/** Create a GameObject
	 */
	public GameObject(){
		return;
	}

	/** Create a body in the physics world
	 * 
	 * @param location The location to put the body
	 * @param world The physics world in which to put the body
	 * @param definition A FixtureDef to associate with the body
	 * @param bodytype The BodyType
	 */
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
		this.body = world.createBody(bd);
		getBody().createFixture(definition);
	}
	
	/** Update the GameObject (check for portal transitions)
	 * 
	 * @param level The level in which the GameObject resides
	 */
	public void update(Level level) {
		ContactEdge edge = getBody().getContactList();
		while (edge != null) {
			if (level.getBodyType(edge.other).equals("portal")) {
				Portal[] ps = level.getPortals();
				portalIn = edge.other.toString().equals(ps[Portal.BLUE].getBodyID()) ? ps[Portal.BLUE] : ps[Portal.ORANGE];
				if (PhysUtils.distance(this.getLocation(), portalIn.getBody().getPosition()) < 1.0f) { 
					inPortal = true;
					break;
				}
			}
			inPortal = false;
			edge = edge.next;
		}

		if (inPortal)
			checkPortalTransition(level);

		last = getLocation().clone();
	}
	
	/** Check for transition between portals. Only really need to call if contact with portal is registered)
	 * 
	 * @param level The level in which the GameObject (and everything else) resides.
	 */
	private void checkPortalTransition(Level level) {
		PortalCollisionRCHelper rch = new PortalCollisionRCHelper(level);
		getBody().getWorld().raycast(rch, last, getLocation());
		if (rch.fixture != null) {
			Portal portals[] = level.getPortals();
			String bodyID = rch.fixture.getBody().toString();
			Portal portalHit;
			
			// Get the portal we're going through
			if (bodyID.equals(portals[Portal.BLUE].getBodyID())) {
				System.out.println("entering blue");
				portalHit = portals[Portal.BLUE];
			} else {
				System.out.println("entering orange");
				portalHit = portals[Portal.ORANGE];
			}

			// Calculate transformations
			Portal otherPortal = portalHit.getLinkedPortal();
			portalIn = otherPortal;
			float rotateBy = portalHit.getRotationDifference();
			Vec2 appear = portalHit.translateLocation(this.getLocation());
			Vec2 newVelocity = PhysUtils.rotateVector(getBody().getLinearVelocity(), rotateBy);
			
			// Set min y velocity for exit to avoid falling out of level
			if (otherPortal.getRotation() > 0)
				newVelocity.y = Math.max (newVelocity.y, MINYEXIT * (float) Math.cos (PhysUtils.getAngle(otherPortal.getUnitTangent())));
			
			getBody().setTransform(appear, getBody().getAngle());
			getBody().setLinearVelocity(newVelocity);
			
			// Finally, play the sound!
			SoundController.play(Sound.PORTALTRAVEL);
		}
	}

	/** Create the fixture to be used by the body
	 * 
	 * @param shapeid The ID of the shape to request from AssetManager
	 * @return The FixtureDef created
	 */
	protected FixtureDef createFixture(String shapeid){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = AssetManager.requestShape(shapeid);
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		return fixtureDef;
	}

	/** Get the body of the GameObject
	 * 
	 * @return The body of the GameObject
	 */
	public Body getBody() {
		return body;
	}

	/** Get the location of the GameObject
	 * 
	 * @return The location of the GameObject
	 */
	public Vec2 getLocation() {
		return getBody().getPosition();
	}

	/** Get the rotation of the GameObject
	 * 
	 * @return The rotation of the GameObject in radians
	 */
	public float getRotation() {
		return getBody().getAngle();
	}

	/** Get the mass of the GameObject
	 * 
	 * @return The mass of the GameObject
	 */
	public float getMass() {
		return getBody().getMass();
	}

	/** Check if the GameObject is on the ground
	 * 
	 * @return true if deemed to be on the ground
	 */
	public boolean isOnGround() {
		float xmid = this.getLocation().x;
		float ymid = this.getLocation().y;
		Vec2 dim = PhysUtils.SlickToJBoxVec(this.getDimensions());
		Vec2 upper = new Vec2(xmid+dim.x/2-CLIPDIST, ymid-dim.y/2);
		Vec2 lower = new Vec2(xmid-dim.x/2+CLIPDIST, ymid-dim.y/2-DISTDOWN);
		AABB area = new AABB(lower,upper);
		FixtureCallback callback = new FixtureCallback();
		GameState.getLevel().getWorld().queryAABB(callback, area);
		return callback.isContainsJumpableObject();
	}

	/** Get the dimensions of the GameObject
	 * 
	 * @return The dimensions of the GameObject
	 */
	public Vec2 getDimensions(){
		return dimensions;
	}

	/** Set the dimensions of the GameObject
	 * 
	 * @param dim The dimensions of the GameObject
	 */
	public void setDimensions(Vec2 dim){
		dimensions=dim;
	}

	/** Get the image that represents the GameObject
	 * 
	 * @return The image that represents the GameObject
	 */
	public Image getImage() {
		return sprite;
	}

	/** Set the image to represent the GameObject
	 * 
	 * @param sprite The image to set
	 */
	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	/** Get the ID of the body of the GameObject
	 * @return The BodyID
	 */
	public String getBodyID() {
		return getBody().toString();
	}
	
	/** Get whether the GameObject is in a portal
	 * @return true if in contact with a portal
	 */
	public boolean isInPortal() {
		return inPortal;
	}
	
	/** Get the portal the GameObject is in
	 * @return The Portal object
	 */
	public Portal getPortalIn() {
		return portalIn;
	}
}
