package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class BigSwitch extends Switch {
	
	private Image contactimg;
	private String doorId;
	private Image renderUp;
	private Image renderDown;
	private Body contact;
	private Vec2 contactdim; // JBox dimensions
	
	private boolean isup;

	public BigSwitch(String imgloc, Vec2 location, World world, String contactid, String doorId, String downid)
			throws SlickException {
		super(imgloc, location, world, PhysUtils.STATIC);
		//Create the contact sensor
		contactimg = AssetManager.requestImage(contactid);
		contactdim = PhysUtils.SlickToJBoxVec(new Vec2(contactimg.getWidth(), contactimg.getHeight()));
		//createSensor(location,world);
		renderUp = AssetManager.requestImage(imgloc);
		renderDown = AssetManager.requestAchiemeventResource(downid);
	}
	
	private void createSensor(Vec2 location, World world) {
		BodyDef bd = new BodyDef();
		bd.position.set(location);
		bd.type = BodyType.STATIC;
		bd.fixedRotation = true;
		contact = world.createBody(bd);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(contactdim.x/2, contactdim.y/2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		fixtureDef.isSensor=true;
		contact.createFixture(fixtureDef);
	}
	
	
	


}
