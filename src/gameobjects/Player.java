package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;
import gameworlds.Level;

import org.jbox2d.collision.WorldManifold;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends GameObject{
	/** The left and right facing images for the players **/
	private Image sprite_right;
	private Image sprite_left; 
	private boolean facingleft=false;

	/** Is holding cube? **/
	private boolean holdingcube=false;

	/** Variables for interaction **/
	@SuppressWarnings("unused")
	private boolean carryingcube=false;
	private CompanionCube cubecarrying;

	// Constants related to player movement
	private final float maxRunVelocity = 5;
	private final float accelFactor = 0.02f;
	private final float accelInAir = 0.01f;
	private final float jumpFactor = 10;

	/** Constructor
	 * @param imgid The Sprites Image Id to get resource
	 * @param pos Coordinates in metres specifying where the player spawns.
	 * @param world The JBox world in which the players physical body should be added.
	 * @throws SlickException
	 */
	public Player(final String imgloc, Vec2 pos, World world) throws SlickException {
		super(imgloc, pos, world, PhysUtils.DYNAMIC);
		sprite_right=getImage();				
		setObject_left(sprite_right.getFlippedCopy(true,false));
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
			float impulse = getMass() * jumpFactor;
			System.out.println(impulse);
			System.out.println(getLocation());
			getBody().applyLinearImpulse(new Vec2(0,impulse), getLocation());
		}
	}

	/** Make the player face left.
	 * 
	 */
	public void faceLeft() {
		if(holdingcube){
			cubecarrying.getBody().setTransform(this.getLocation().add(new Vec2(-1,0)), 0);
		}
		this.setSprite(sprite_left);
		facingleft=true;
	}

	/** Make the player face right.
	 * 
	 */
	public void faceRight() {
		if(holdingcube){
			cubecarrying.getBody().setTransform(this.getLocation().add(new Vec2(1,0)), 0);
		}
		this.setSprite(sprite_right);
		facingleft=false;
	}

	private void setObject_left(Image sprite_left) {
		this.sprite_left = sprite_left;
	}

	public void interact(World world, Level level){
		if(holdingcube){
			dropCube();
		} else {
			ContactEdge edge = level.getLevelPlayer().getBody().getContactList();
			while (edge != null) {
				System.out.println("Found " + edge.other + " of type " + level.getBodyType(edge.other));
				String type=level.getBodyType(edge.other);
				if(type.equals("cube")){
					String bodyId=edge.other.toString();
					pickupCube(level.getCube(bodyId));
				}
				edge = edge.next;

			}	
		}
	}

	public void teleport(){
		this.getBody().setTransform(new Vec2(2,17), 0f);
	}
	public void teleportHoriz(){
		Vec2 currentvel = this.getBody().getLinearVelocity();
		Float x = currentvel.x;
		Float y = currentvel.y;
		System.out.println(this.getBody().getLinearVelocity());
		Vec2 transVec = new Vec2(-y,-x);
		this.getBody().setTransform(new Vec2(0.5f,14), 0f);
		this.getBody().setLinearVelocity(transVec);
		System.out.println("After: " +this.getBody().getLinearVelocity());

	}


	public void pickupCube(CompanionCube cube){
		while(!holdingcube){
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
			GameState.getLevel().getPhysWorld().createJoint(def);
			holdingcube=true;
			cubecarrying = cube;
		}
	}

	public void dropCube(){
		System.out.println("shold be dropping it");
		cubecarrying.getBody().resetMassData();
		cubecarrying.getBody().setFixedRotation(false);
		Joint joint=cubecarrying.getBody().getJointList().joint;
		GameState.getLevel().getPhysWorld().destroyJoint(joint);
		holdingcube=false;
		cubecarrying=null;
	}
	
	public void enterPortal(Portal portal){
		Vec2 newloc = portal.getLocation();
		Vec2 transformedVel = this.getBody().getLinearVelocity();
		

	}

}
