package gameobjects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends GameObject{
	private static Player player;
	/** The left and right facing images for the players **/
	private Image object_right;
	private Image object_left; 
	
	public Player(final String imgloc,Vec2 pos, World world) throws SlickException {
		super(imgloc, pos, world);
		object_right=getObject();				
		setObject_left(object_right.getFlippedCopy(true,false));

	}
	
	public static Player getPlayer(final String imgloc,Vec2 pos, World world)
	throws SlickException{
		if(player==null){
			player = new Player(imgloc,pos,world);
		}
		return player;
		
	}

	public void checkDirection(float dir_x) {
		if(dir_x<0){
			System.out.printf("dir_x is %f",dir_x);
			setObject(object_left);
		} else if(dir_x>0){
			setObject(object_right);
		}
	}
	
	
	public void setObject_left(Image object_left) {
		this.object_left = object_left;
	}
	public void setObject_right(Image object_right) {
		this.object_right = object_right;
	}
}
