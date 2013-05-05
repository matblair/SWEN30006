package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;
import gameworlds.Level;

import org.jbox2d.collision.WorldManifold;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.JointEdge;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RopeJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends GameObject{
	/** The left and right facing images for the players **/
	private Image sprite_right;
	private Image sprite_left; 

	/** Is holding cube? **/
	private boolean holdingcube=false;
	private float oldcubemass=0;

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
			faceLeft();
		} else if (dir_x > 0) {
			faceRight();
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
			getBody().applyLinearImpulse(new Vec2(0,impulse), getLocation());
		}
	}

	/** Make the player face left.
	 * 
	 */
	public void faceLeft() {
		this.setSprite(sprite_left);
	}

	/** Make the player face right.
	 * 
	 */
	public void faceRight() {
		this.setSprite(sprite_right);
	}

	private void setObject_left(Image sprite_left) {
		this.sprite_left = sprite_left;
	}

	public void interact(World world, Level level){
		if(holdingcube){
			interactWithCube(cubecarrying);
		} else {
			System.out.println("Looking for body IDs of objects in contact with player...");
			ContactEdge edge = level.getLevelPlayer().getBody().getContactList();
			while (edge != null) {
				System.out.println("Found " + edge.other + " of type " + level.isInteractable(edge.other));
				String type=level.isInteractable(edge.other);
				if(type.equals("cube")){
					String bodyId=edge.other.toString();
					interactWithCube(level.getCube(bodyId));
				}
				edge = edge.next;

			}	
		}
	}

	public void interactWithCube(CompanionCube cube){
		if(!holdingcube){
			oldcubemass=cube.getMass();
			MassData massData= new MassData();
			massData.mass=0.0001f;
			cube.getBody().setMassData(massData);
			cube.getBody().setFixedRotation(true);
			PrismaticJointDef def = new PrismaticJointDef();
			def.bodyA=this.getBody();
			def.bodyB=cube.getBody();
			JointType jtype = JointType.PRISMATIC;
			def.type = jtype;
			def.enableLimit=true;
			def.enableMotor=true;
			GameState.getLevel().getPhysWorld().createJoint(def);
			System.out.println(cube.getBody().m_jointList);
			holdingcube=true;
			cubecarrying = cube;
		}
		else if(holdingcube) {
			System.out.println("shold be dropping it");
			MassData massData=new MassData();
			massData.mass=oldcubemass;
			cube.getBody().setMassData(massData);
			cube.getBody().setFixedRotation(false);
			Joint joint=cube.getBody().getJointList().joint;
			GameState.getLevel().getPhysWorld().destroyJoint(joint);
			holdingcube=false;
			cubecarrying=null;

		}
	}

}
