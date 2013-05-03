package gameobjects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends GameObject{
	/** The left and right facing images for the players **/
	private Image sprite_right;
	private Image sprite_left; 
	private static Player player;
	
	// Constants related to player movement
	private final float maxRunVelocity = 5;
	private final float accelFactor = 0.02f;
	private final float accelInAir = 0.01f;
	private final float jumpFactor = 10;
	
	public Player(final String imgloc, Vec2 pos, World world) throws SlickException {
		super(imgloc, pos, world);
		sprite_right=getImage();				
		setObject_left(sprite_right.getFlippedCopy(true,false));
	}

	public static Player getPlayer(final String imgloc,Vec2 pos, World world)
	throws SlickException{
		if(player==null){
			player = new Player(imgloc,pos,world);
		}
		return player;
		
	}
	
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
	
	public void jump() {
		if (isOnGround()){
			float impulse = getMass() * jumpFactor;
			getBody().applyLinearImpulse(new Vec2(0,impulse), getLocation());
		}
	}
	
	public void faceLeft() {
		this.setSprite(sprite_left);
	}
	
	public void faceRight() {
		this.setSprite(sprite_right);
	}
	
	public void setObject_left(Image sprite_left) {
		this.sprite_left = sprite_left;
	}
	
	public void setObject_right(Image sprite_right) {
		this.sprite_right = sprite_right;
	}
}
