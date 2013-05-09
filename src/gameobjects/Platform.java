package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class Platform extends GameObject {
	
	public static final int SHORT=0;
	public static final int LONG=1;
	public static final int MOVING=1;
	
	private static final int[] BODYTYPES = {PhysUtils.STATIC, PhysUtils.KINEMATIC};
	private static final String[] IMGS = {"SMALLPLATFORM", "PLATFORM"};
	private static final String[] SHAPES = {"SHORTPLATFORMSHAPE","LONGPLATFORMSHAPE"};
	
	public Platform(Vec2 location, World world, int type, int moving)
			throws SlickException {
		super(IMGS[type]);
		FixtureDef fixture = createFixture(type);
		System.out.println(fixture.shape);
		this.createBody(location, world, fixture, BODYTYPES[moving]);
	}
	
	private FixtureDef createFixture(int type){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = AssetManager.requestShape(SHAPES[type]);
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		System.out.println(fixtureDef.shape);
		return fixtureDef;
	}
}
