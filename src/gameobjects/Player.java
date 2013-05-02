package gameobjects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Player extends GameObject{

	private static Player player;

	public Player(final String imgloc,Vec2 pos, World world) throws SlickException {
		super(imgloc, pos, world);
	}
	
	public static Player getPlayer(final String imgloc,Vec2 pos, World world)
	throws SlickException{
		if(player==null){
			player = new Player(imgloc,pos,world);
		}
		return player;
		
	}
	public void checkDirection( final double dir_x){
		if(dir_x<0){
			setObject(getObject_left());
		}
		else if (dir_x>0){
			setObject(getObject_right());
		}
	}
}
