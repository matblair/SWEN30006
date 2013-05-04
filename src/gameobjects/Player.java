package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends GameObject{
	/** The left and right facing images for the players **/
	private Image sprite_right;
	private Image sprite_left; 
	
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
	
	public void interact(World world){
		ContactEdge edge = this.getBody().getContactList();
		WorldManifold wm = new WorldManifold();
		while (edge != null) {
			edge.contact.getWorldManifold(wm);
			//We want to check if any of the bodys we are in contact with are interactabe and if so 
			// do the right thin.
		}
		
	}
	
}
