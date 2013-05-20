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
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.RopeJoint;
import org.jbox2d.dynamics.joints.RopeJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import sun.tools.tree.ThisExpression;

public class BigSwitch extends GameObject {
	private static final String IMGID="SWITCHSENSOR";
	private static final String SHAPEID="BIGSWITCHSHAPE";
	private static final String LEFTEDGEID = "LEFTEDGEVERTICES";
	private static final String RIGHTEDGEID = "RIGHTEDGEVERTICES";
	
	private final float DEPRESSDIST=0.2f;
	
	private Door door;

	public BigSwitch(Vec2 location, World world, Door door)
			throws SlickException {
		super(IMGID);
		//Create the contact sensor
		FixtureDef fixture = createFixture(SHAPEID);
		FixtureDef leftedge = createFixture(LEFTEDGEID);
		FixtureDef rightedge = createFixture(RIGHTEDGEID);

		this.createBody(location, world, fixture, leftedge, rightedge);
		createSensor(location, world);
		
		this.door = door;
	}
	

	private void createBody(Vec2 location, World world, FixtureDef definition,
			FixtureDef leftedge, FixtureDef rightedge) {
		BodyDef bd = new BodyDef();
		bd.position.set(location);
		
		bd.fixedRotation = true;
		bd.type = BodyType.DYNAMIC;
		setBody(world.createBody(bd));
		getBody().createFixture(definition);

		bd.type=BodyType.STATIC;
		world.createBody(bd).createFixture(leftedge);
		world.createBody(bd).createFixture(rightedge);
		
		bd.position.set(location.x, location.y - DEPRESSDIST);
		bd.type=BodyType.STATIC;
		Body anchor = world.createBody(bd);
		
		System.out.println(location);
		
		PrismaticJointDef jointDef = new PrismaticJointDef();
		Vec2 axis = new Vec2(0, 1);
		jointDef.initialize(anchor, this.getBody(), anchor.getPosition(), axis);
		jointDef.lowerTranslation = -DEPRESSDIST/2;
		jointDef.upperTranslation = DEPRESSDIST/2;
		jointDef.enableLimit = true;
		jointDef.maxMotorForce = 1.0f;
		jointDef.motorSpeed = 0.0f;
		jointDef.enableMotor = true;
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
		ContactEdge edge = contact.getContactList();
		while (edge != null) {
			String type=GameState.getLevel().getBodyType(edge.other);
			if(!(type.equals("wall") || type.equals("bigswitch"))){
				somethingpressing=true;
			}
			edge = edge.next;
		}
		
		if(somethingpressing){
			if(!door.isOpen()){
				door.open();
			}
			
		}else {
			if(door.isOpen()){
				door.close();
			}
		}
	}
}
