package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class BigSwitch extends GameObject {

	private static final String IMGID="SWITCHSENSOR";
	private static final String SHAPEID="BIGSWITCHSHAPE";
	private static final int bodytype = PhysUtils.STATIC;

	private static boolean doorlinkset = false;
	private Image contactimg;
	private String doorId;
	private Image renderUp;
	private Image renderDown;
	private Body contact;
	private Vec2 contactdim; // JBox dimensions
	private Door doorlink;
	private boolean somethingpressing;


	public BigSwitch(Vec2 location, World world, String doorId)
			throws SlickException {
		super(IMGID);
		//Create the contact sensor
		FixtureDef fixture = createFixture();
		contactimg=this.getImage();
		renderUp = renderDown = contactimg;
		this.createBody(location, world, fixture, bodytype);
		contactdim = PhysUtils.SlickToJBoxVec(new Vec2(contactimg.getWidth(), contactimg.getHeight()));
		createSensor(location,world);
		this.doorId=doorId;
	}
	
	private FixtureDef createFixture(){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = AssetManager.requestShape(SHAPEID);
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		return fixtureDef;

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

	public void updateState(){
		somethingpressing=false;
		if(doorlinkset==false){
			for(Door door: GameState.getLevel().getDoorCollection()){
				if(door.getDoorId().equals(this.doorId)){
					doorlink=door;
					doorlinkset=true;
				}
			}
		}
		System.out.println(doorlink.isOpen());


		ContactEdge edge = contact.getContactList();
		while (edge != null) {
			String type=GameState.getLevel().getBodyType(edge.other);
			if(!(type.equals("wall") || type.equals("bigswitch"))){
				somethingpressing=true;
			}
			edge = edge.next;
		}
		if(somethingpressing){
			if(!doorlink.isOpen()){
				doorlink.open();
				setSprite(renderDown);
			}
		}else {
			if(doorlink.isOpen()){
				doorlink.close();
				setSprite(renderUp);
			}
		}
	}
}
