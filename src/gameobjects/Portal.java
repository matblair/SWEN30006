package gameobjects;

import java.util.ArrayList;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class Portal extends GameObject {
	public static final int BLUE=0, ORANGE=1;
	private final String BLUEANIMID="BLUEPORTAL", ORANGEANIMID="ORANGEPORTAL";
	private static final int BEFORE=0, AFTER=1;
	private int relativeLoc;
	
	private static final String SHAPEID="PORTALSHAPE";
	private static final int BODYTYPE=PhysUtils.STATIC;
	
	private float height;
	private boolean enabled=false, sameWall=false;
	private Portal otherPortal;
	private Wall wall;
	private Wall startSegment, sharedSegment, endSegment;
	private World world;
	private Animation animation;
	private int colour;
	
	/** Create a Portal object
	 * 
	 * @param colour The colour of this portal (use public static ints)
	 * @param location Location of the cube to start in the world (centre)
	 * @param world The physics world
	 * @throws SlickException
	 */
	public Portal(int colour, Vec2 location, World world)
			throws SlickException {
		super();
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, BODYTYPE);
		this.world = world;
		this.setDimensions(new Vec2(0.2f, 2.5f));
		height = this.getDimensions().y;
		
		String animID = (colour==BLUE) ? BLUEANIMID : ORANGEANIMID;
		this.colour = colour;
		animation = AssetManager.requestAnimationResources(animID);
		animation.setLooping(false);
	}
	
	/** Update the portal
	 * 
	 * @param delta Milliseconds since last update
	 */
	public void update (int delta) {
		animation.update (delta);
		this.setSprite(animation.getCurrentFrame());
	}
	
	/** Get the colour of the portal
	 * 
	 * @return The colour of the portal
	 */
	public int getColour() {
		return colour;
	}
	
	/** Link the portal to another portal
	 * 
	 * @param portal The portal to link to
	 */
	public void linkPortals(Portal portal){
		otherPortal = portal;
	}
	
	/** Get the portal this one links to
	 * 
	 * @return The linked portal
	 */
	public Portal getLinkedPortal() {
		return otherPortal;
	}
	
	/** Get a unit vector tangent to the portal
	 * 
	 * @return Unit vector tangent to the portal
	 */
	public Vec2 getUnitTangent() {
		return wall.getUnitTangent();
	}
	
	/** Disable the portal
	 * @throws SlickException
	 */
	public void disable() throws SlickException {
		if (this.wall == otherPortal.wall) {
			if (relativeLoc == BEFORE) {
				startSegment.destroy();
				startSegment = null;
				otherPortal.startSegment.enable();
			} else {
				endSegment.destroy();
				endSegment = null;
				otherPortal.endSegment.enable();
			}
			if (sharedSegment != null) {
				sharedSegment.destroy();
				sharedSegment = null;
				otherPortal.sharedSegment = null;
			}
			this.wall = null;
			
		} else {
			this.wall.enable();
			this.wall = null;
			startSegment.destroy();
			startSegment = null;
			endSegment.destroy();
			endSegment = null;
		}
		
		getBody().setTransform(new Vec2(-1, 0), 0);
		this.close();
		enabled = false;
	}
	
	/** Open the portal
	 */
	public void open() {
		if (this.isEnabled() & otherPortal.isEnabled()) {
			this.getBody().getFixtureList().setSensor(true);
			otherPortal.getBody().getFixtureList().setSensor(true);
		}
	}
	
	/** Close the portal
	 */
	private void close() {
		this.getBody().getFixtureList().setSensor(false);
		otherPortal.getBody().getFixtureList().setSensor(false);
	}
	
	/** Check if the portal is open (requires other portal to be open too)
	 * 
	 * @return true if the portal is open
	 */
	public boolean isOpen() {
		return this.getBody().getFixtureList().isSensor();
	}
	
	/** Check if the portal is enabled (does not require other portal to be enabled)
	 * 
	 * @return true if the portal is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/** Get an ArrayList of temporary walls created by this portal
	 * 
	 * @return an ArrayList of temporary walls created by this portal
	 */
	public ArrayList<Wall> getTempWalls() {
		ArrayList<Wall> walls = new ArrayList<Wall>();
		if (startSegment != null && startSegment.isEnabled())
			walls.add(startSegment);
		if (endSegment != null && endSegment.isEnabled())
			walls.add(endSegment);
		if (sharedSegment != null && sharedSegment.isEnabled())
			walls.add(sharedSegment);
		
		return walls;
	}
	
	/** Tell the portal that it has hit a new wall
	 * 
	 * @param loc The location at which the portal hit the wall
	 * @param wall The wall that the portal hit
	 * @throws SlickException
	 */
	public void hitWall (Vec2 loc, Wall wall) throws SlickException {
		Wall targetWall = wall;
		int relativeLoc = -1;
		
		// Logic to do with getting two portals on one wall
		if (otherPortal.wall == wall) {
			if (PhysUtils.unitVector(loc.sub(otherPortal.getLocation())).add(wall.getUnitTangent()).length() > 1) {
				relativeLoc = AFTER;
				otherPortal.relativeLoc = BEFORE;
				targetWall = otherPortal.endSegment;
			} else {
				relativeLoc = BEFORE;
				otherPortal.relativeLoc = AFTER;
				targetWall = otherPortal.startSegment;
			}
			sameWall = true;
			otherPortal.sameWall = true;
		} else {
			sameWall = false;
			otherPortal.sameWall = false;
		}
		
		// Don't continue if the portal won't fit on the wall
		if (targetWall.getLength() < height)
			return;
		
		// Clean up
		if (isEnabled())
			disable();
		
		Vec2 unitTangent = targetWall.getUnitTangent();
		// Correct for hits near ends of walls
		if (PhysUtils.distance(loc, targetWall.getStart()) < height / 2)
			loc = targetWall.getStart().add(unitTangent.mul(height / 2));
		else if (PhysUtils.distance(loc, targetWall.getEnd()) < height / 2)
			loc = targetWall.getEnd().sub(unitTangent.mul(height / 2));
		
		// Add replacement wall segments
		if (sameWall) {
			if (relativeLoc == BEFORE) {
				startSegment = new Wall(targetWall.getStart(), loc.sub(unitTangent.mul(height/2)), world);
				sharedSegment = new Wall(loc.add(unitTangent.mul(height/2)), targetWall.getEnd(), world);
				endSegment = new Wall(loc.add(unitTangent.mul(height/2)), wall.getEnd(), world);
				endSegment.disable();
				otherPortal.startSegment.disable();
				otherPortal.sharedSegment = sharedSegment;
				
			} else {
				endSegment = sharedSegment = new Wall(loc.add(unitTangent.mul(height/2)), targetWall.getEnd(), world);
				sharedSegment = new Wall(targetWall.getStart(), loc.sub(unitTangent.mul(height/2)), world);
				startSegment = new Wall(wall.getStart(), loc.sub(unitTangent.mul(height/2)), world);
				startSegment.disable();
				
				otherPortal.endSegment.disable();
				otherPortal.sharedSegment = sharedSegment;
			}
			
			this.relativeLoc = relativeLoc;
		
		} else {
			startSegment = new Wall(wall.getStart(), loc.sub(unitTangent.mul(height/2)), world);
			System.out.println(startSegment);
			endSegment = new Wall(loc.add(unitTangent.mul(height/2)), wall.getEnd(), world);
			System.out.println(endSegment);
		}
		
		// Rotate to be in parallel with wall
		this.getBody().setTransform(loc, PhysUtils.getAngle(wall.getUnitNormal()));

		// General housekeeping
		this.wall = wall;
		this.wall.disable();
		animation.restart();
		enabled = true;
		this.open();
	}
	
	/** Get the translated location (eg: for traveling through portals)
	 * 
	 * @param from The location to go from
	 * @return The translated location
	 */
	public Vec2 translateLocation(Vec2 from) {
		Vec2 vecToFrom = from.sub(getLocation());
		Vec2 newLoc = otherPortal.getLocation().add(PhysUtils.rotateVector(vecToFrom, getRotationDifference()));
		return newLoc;
	}
	
	/** Get the rotation difference that is introduced when traveling between the two portals
	 * 
	 * @return The rotation in radians
	 */
	public float getRotationDifference() {
		return PhysUtils.getAngle(otherPortal.getUnitTangent()) - PhysUtils.getAngle(this.getUnitTangent()) + (float) Math.PI;
	}
}
