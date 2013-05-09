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

	public MovingPlatform(Vec2 location, World world, Vec2 spos, Vec2 fpos, int type) throws SlickException {
		super(location, world, type, Platform.MOVING);
		start = spos;
		finish = fpos;
		mid = new Vec2((start.x+((finish.x-start.x)/2)), start.y+((finish.y-start.y)/2));
		//Check which path we run along and update features accoringdly.
		if((start.x-finish.x)!=0f){
			xtrans=true;
		}if((start.y-finish.y)!=0f){
			ytrans=true;
		}		
	}

	public void updatePos(float delta){
		theta+=(DTHETA*(delta/1000));

		if(theta>2*Math.PI){
			theta=0;
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
		getBody().setLinearVelocity(new Vec2(((targetx-this.getLocation().x)/delta),((targety-this.getLocation().y)/delta)));
	}

}
