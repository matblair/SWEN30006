package gameobjects;

import gameengine.PhysUtils;
import gameengine.PortalShootRCHelper;
import gamestates.GameState;
import gameworlds.Level;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class PortalBullet extends GameObject {
	private final float SPEED=50;
	private final String BLUEID="BLUEBULLET", ORANGEID="ORANGEBULLET";

	private Level level;
	private World world;
	private Vec2 last;
	private int colour;
	private boolean collided=false;

	/** Create a PortalBullet object
	 * 
	 * @param colour The colour of the portal that will be created by this bullet
	 * @param start Location of the bullet to start in the world (centre)
	 * @param direction Unit vector in the direction of the shot
	 * @throws SlickException
	 */
	public PortalBullet (int colour, Vec2 start, Vec2 direction) {
		super();
		
		level = GameState.getLevel();
		world = level.getWorld();
		this.colour = colour;
		if (colour == Portal.BLUE)
			setSprite(AssetManager.requestImage(BLUEID));
		else
			setSprite(AssetManager.requestImage(ORANGEID));
		
		Shape shape = new CircleShape();
		shape.m_radius = 0.5f;
		FixtureDef fd = new FixtureDef();
		fd.isSensor = true;
		fd.shape = shape;
		createBody(start, world, fd, PhysUtils.KINEMATIC);
		getBody().setLinearVelocity(direction.mul(SPEED));
		last = start;
	}
	
	/** Update the portal bullet
	 */
	public void update() {
		try {
			shoot (last, this.getLocation());
		} catch (SlickException e) {
			e.printStackTrace();
		}
		last = this.getLocation().clone();
	}
	
	/** Trigger a limited range shot and check for relevant collisions
	 * 
	 * @param from Location to shoot from
	 * @param to Location to shoot to
	 * @throws SlickException
	 */
	public void shoot (Vec2 from, Vec2 to) throws SlickException {
		PortalShootRCHelper rch = new PortalShootRCHelper(this);
		world.raycast(rch, from, to);
		
		if (rch.fixture == null)
			return;
		
		collided = true;
		if (level.getBodyType(rch.fixture.getBody()).equals("wall")) {
			Wall wall = level.getWalls().get(rch.fixture.getBody().toString());
			Vec2 loc = rch.point;
			level.getGlados().createdPortal();
			level.getPortals()[colour].hitWall(loc, wall);
		}
	}
	
	/** Check if the portal bullet has collided with something
	 * 
	 * @return true if collision has occurred
	 */
	public boolean hasCollided() {
		return collided;
	}
	
	/** Check whether the portal bullet interacts with a particular body
	 * 
	 * @param b The body to check
	 * @return true if the portal bullet interacts
	 */
	public boolean interacts (Body b) {
		String type = level.getBodyType(b);
		if (type.equals("wall") || type.equals("cube") || type.equals("noportalwall") || type.equals("dissipationfield")
				|| type.equals("platform") || type.equals("movingplatform") || type.equals("bigswitch"))
			return true;
		Door door = level.getDoors().get(b.toString());
		if (door != null && !door.isOpen())
			return true;
		return false;
	}
	
	/** Destroy this portal bullet
	 */
	public void destroy() {
		world.destroyBody(this.getBody());
	}
}
