package gameobjects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class MovingPlatform extends Platform{
	
	private static final float HALF = 0.5f;
	private static float DTHETA=0.5f;

	private float theta=0;
	private Vec2 start;
	private Vec2 finish;
	private Vec2 mid;
	private boolean xtrans=false, ytrans=false;
	private boolean continuousMovement;
	private boolean shouldStop=false;

	/** Create a MovingPlatform object
	 * 
	 * @param location The starting location of the object
	 * @param world The world in which to create the body
	 * @param spos The location of one end of the track
	 * @param fpos The location of the second end of the track
	 * @param type The type of platform (small/large)
	 * @param continuousMovement Whether the platform should keep moving (loop)
	 * @throws SlickException
	 */
	public MovingPlatform(Vec2 location, World world, Vec2 spos, Vec2 fpos, int type, boolean continuousMovement) throws SlickException {
		super(location, world, type, Platform.MOVING);
		this.continuousMovement = continuousMovement;
		start = spos;
		finish = fpos;
		mid = new Vec2((start.x+((finish.x-start.x)/2)), start.y+((finish.y-start.y)/2));
		//Check which path we run along and update features accoringdly.
		if((start.x-finish.x)!=0f){
			xtrans=true;
		}if((start.y-finish.y)!=0f){
			ytrans=true;
		}	
		
		if(this.getLocation().x==finish.x || this.getLocation().y==finish.y){
			theta=(float)(2*Math.PI);
		}
	}

	/** Update the moving platform (calculate new velocity)
	 * 
	 * @param delta Milliseconds since last update
	 */
	public void update(float delta){
		theta+=(DTHETA*(delta/1000));
		if(continuousMovement){
			if(theta>2*Math.PI){
				theta=(float)(theta-2*Math.PI);
			}
		}else {
			if(theta>2*Math.PI){
				if(shouldStop){
					theta=(float)(2*Math.PI);
				}else {
					theta=(float)(theta-2*Math.PI);
					shouldStop=true;
				}
			}
		}
		
		double difx = (Math.abs(start.x-finish.x))*HALF;
		double dify = (Math.abs(start.y-finish.y))*HALF;
		float targetx=0, targety=0;

		if(xtrans && ytrans){
			targetx=(float)(mid.x + (Math.cos(theta)*difx));
			targety=(float)(mid.y + Math.cos(theta)*dify);
		}else if(xtrans){
			targetx=(float)(mid.x + (Math.cos(theta)*difx));
			targety=this.getLocation().y;
		}else if(ytrans){
			targety=(float)(mid.y + Math.cos(theta)*dify);
			targetx=this.getLocation().x;
		}
		getBody().setLinearVelocity(new Vec2(((targetx-this.getLocation().x)),((targety-this.getLocation().y))));
	}

}
