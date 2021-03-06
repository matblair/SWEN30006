package gameobjects;

import gameengine.FixtureCallback;
import gameengine.PhysUtils;
import gameengine.Sound;
import gamestates.GameState;
import gameworlds.Level;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;
import resourcemanagers.SoundController;

public class Player extends GameObject{
	private static final String SHAPEID="CHELLSHAPE";
	private final static String RUNANIM = "CHELLRUN";
	private final static String FALLIMG = "CHELLFALL";
	private final static String IDLEIMG = "CHELLIDLE";

	// Constants related to player movement
	private static final int BODYTYPE=PhysUtils.DYNAMIC;
	private final float maxRunVelocity = 5;
	private final float accelFactor = 0.02f;
	private final float accelInAir = 0.01f;
	private final float jumpFactor = 10;
	private final float ROTATIONFACTOR = 100;

	private static final float MAXCUBEDIST = 2.0f;
	private static final float DISTCHECK = 2.0f;

	/** Is holding cube? **/
	private boolean holdingcube=false;

	/** Variables for interaction **/
	private CompanionCube cubecarrying;

	// Player image stuff
	private boolean facingleft=false;
	private Animation run;
	private Image fall;
	private Image idle;
	private float rotation;

	/** Constructor
=	 * @param pos Coordinates in metres specifying where the player spawns.
	 * @param world The JBox world in which the players physical body should be added.
	 * @throws SlickException
	 */
	public Player(Vec2 pos, World world) throws SlickException {
		super(IDLEIMG);
		FixtureDef fixture = this.createFixture(SHAPEID);
		this.createBody(pos, world, fixture, BODYTYPE);
		Vec2 dim = new Vec2(108f,102f);
		setDimensions(dim);

		run = AssetManager.requestAnimationResources(RUNANIM);
		run.setAutoUpdate(false);
		System.out.println(run.getFrameCount() + " frames");
		fall = AssetManager.requestImage(FALLIMG);
		idle = AssetManager.requestImage(IDLEIMG);
	}

	/** Tell the player to move in a certain direction.
	 * 
	 * @param dir_x The direction (and scale) of which to move. Should be [-1,1], which corresponds to [FullLeft, FullRight]
	 * @param delta Number of milliseconds since last update.
	 */
	public void moveXDir(float dir_x, float delta){
		if (dir_x < 0) {
			if(!facingleft){
				faceLeft();
			}
		} else if (dir_x > 0) {
			if(facingleft){
				faceRight();
			}
		}

		float xVelocity = getBody().getLinearVelocity().x;
		if (this.isOnGround()) {
			// Allow walk forces when on the ground
			float impulse = getMass() * ((dir_x * maxRunVelocity) - xVelocity) * accelFactor * delta;
			getBody().applyLinearImpulse(new Vec2 (impulse, 0), getLocation());

		} else if (Math.signum(dir_x) != Math.signum(xVelocity) || Math.abs(xVelocity) < (maxRunVelocity - 1)) {
			// If in the air and not already moving quickly
			getBody().applyLinearImpulse(new Vec2 (dir_x * accelInAir * getMass() * delta, 0), getLocation());
		}
	}

	/** Make the player jump.
	 * 
	 */
	public void jump() {
		if (isOnGround()){
			GameState.getLevel().getGlados().jumped();
			float impulse = getMass() * jumpFactor;
			getBody().applyLinearImpulse(new Vec2(0,impulse), getLocation());
			SoundController.play (Sound.JUMP);
		}
	}

	/** Make the player face left.
	 * 
	 */
	public void faceLeft() {
		if(holdingcube){
			cubecarrying.getBody().setTransform(this.getLocation().add(new Vec2(-1,0)), 0);
		}
		facingleft=true;
	}

	/** Make the player face right.
	 * 
	 */
	public void faceRight() {
		if(holdingcube){
			cubecarrying.getBody().setTransform(this.getLocation().add(new Vec2(1,0)), 0);
		}
		facingleft=false;
	}

	/** Update the player
	 * 
	 * @param level The level in which the player resides
	 * @param delta Milliseconds since last update
	 */
	public void update(Level level, int delta) {
		super.update(level);

		// Update the sprite
		if (!isOnGround()) {
			run.restart();
			this.setSprite(fall);
		} else if (Math.abs (this.getBody().getLinearVelocity().x) < 0.1) {
			run.restart();
			this.setSprite(idle);
		} else {
			run.update(delta);
			this.setSprite(run.getCurrentFrame());
		}
		
		if (facingleft)
			this.setSprite(this.getImage().getFlippedCopy(true, false));
		
		// Update rotation
		if (this.didTransition()) {
			rotation = this.getPortalIn().getLinkedPortal().getRotationDifference();
		} else if (rotation > 0) {
			rotation -= Math.min(delta/ROTATIONFACTOR, rotation);
		} else if (rotation < 0) {
			rotation += Math.min(delta/ROTATIONFACTOR, -rotation);
		}
	}

	/** Tell the player to try and interact with the world.
	 * 
	 * @param world The world in which the player's body lives
	 * @param level The level in which the player exists
	 * @throws SlickException
	 */
	public void interact(World world, Level level) throws SlickException{
		if(holdingcube){
			dropCube();
		} else {
			CompanionCube cube = getCubesWithinDist();
			if(cube!=null){
				pickupCube(cube);
				return;
			}else{
				//Haven't picked up a cube so check if we can interact with small switch
				ContactEdge edge = level.getLevelPlayer().getBody().getContactList();
				while (edge != null) {
					String type=level.getBodyType(edge.other);
					if(type.equals("lilswitch")){
						String bodyId=edge.other.toString();
						if(PhysUtils.distance(this.getLocation(),level.getSwitch(bodyId).getLocation())<=DISTCHECK){
							level.getSwitch(bodyId).trigger();
						}
					}
					edge = edge.next;
				}	
			}
		}
	}

	/** Try and get a cube nearby
	 * 
	 * @return A companion cube within the maximum distance for pickup, otherwise null.
	 */
	private CompanionCube getCubesWithinDist(){
		float playerx = this.getLocation().x;
		float playery = this.getLocation().y;
		Vec2 upper = new Vec2();
		Vec2 lower = new Vec2();
		if(facingleft){
			upper.set(playerx,(playery+this.getDimensions().y/2));
			lower.set(playerx-DISTCHECK,(playery-this.getDimensions().y/2));

		}else {
			upper.set(playerx+DISTCHECK,(playery+this.getDimensions().y/2));
			lower.set(playerx,(playery-this.getDimensions().y/2));
		}
		AABB area = new AABB(lower,upper);
		FixtureCallback callback = new FixtureCallback();

		GameState.getLevel().getWorld().queryAABB(callback, area);
		CompanionCube cube = callback.getCube();
		if(cube!=null && PhysUtils.distance(this.getLocation(),cube.getLocation())<=DISTCHECK){
			return cube;
		} 
		return null;
	}

	/** All the logic associated with having to pick up a cube
	 * 
	 * @param cube The cube to pick up.
	 */
	private void pickupCube(CompanionCube cube){
		while(!holdingcube){
			GameState.getLevel().getGlados().pickupCube();
			MassData massData=null;
			massData = new MassData();
			massData.mass=0.000001f;			
			cube.getBody().setMassData(massData);
			cube.getBody().setAngularVelocity(0.0f);
			PrismaticJointDef def = new PrismaticJointDef();
			def.bodyA=this.getBody();
			def.bodyB=cube.getBody();
			JointType jtype = JointType.PRISMATIC;
			def.type = jtype;
			def.enableLimit=true;
			def.enableMotor=true;
			def.upperTranslation=1;
			def.collideConnected=true;
			GameState.getLevel().getWorld().createJoint(def);
			holdingcube=true;
			cubecarrying = cube;
		}
	}

	/** Tell the player to drop the cube it is holding.
	 */
	public void dropCube(){
		cubecarrying.getBody().resetMassData();
		cubecarrying.getBody().setFixedRotation(false);
		Joint joint=cubecarrying.getBody().getJointList().joint;
		GameState.getLevel().getWorld().destroyJoint(joint);
		holdingcube=false;
		cubecarrying=null;
	}

	/** Check to make sure the cube is still within a maximum range for carrying
	 */
	public void checkCube(){
		if(holdingcube){
			Vec2 cubepos = cubecarrying.getLocation();
			Vec2 playerpos = this.getLocation();	
			float dist = PhysUtils.distance(cubepos, playerpos);
			if(dist>MAXCUBEDIST){
				dropCube();
			}
		}
	}
	
	/** Get the rotation at which to draw the player
	 * 
	 * @return Rotation in radians
	 */
	public float getRotation() {
		return rotation;
	}
	
	/** Get the cube the player is carrying
	 * 
	 * @return The cube the player is carrying
	 */
	public CompanionCube getCubeCarrying(){
		return cubecarrying;
	}

	/** Check if the player is carrying a cube
	 * 
	 * @return true if the player is carrying a cube
	 */
	public boolean isCarryingCube() {
		if(cubecarrying!=null){
			return true;
		}
		return false;
	}
}
