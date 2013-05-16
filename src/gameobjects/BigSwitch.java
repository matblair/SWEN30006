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
import org.jbox2d.dynamics.joints.*;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class BigSwitch extends GameObject {

	private static final String IMGID="SWITCHSENSOR";
	private static final String SHAPEID="BIGSWITCHSHAPE";
	private static final int bodytype = PhysUtils.DYNAMIC;
	private static final String LEFTEDGEID = "LEFTEDGEVERTICES";
	private static final String RIGHTEDGEID = "RIGHTEDGEVERTICES";

	private static Body leftbody;
	private static Body rightbody;
	
	private boolean doorlinkset = false;
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
		FixtureDef fixture = createFixture(SHAPEID);
		FixtureDef leftedge = createFixture(LEFTEDGEID);
		FixtureDef rightedge = createFixture(RIGHTEDGEID);

		
		contactimg=this.getImage();
		renderUp = renderDown = contactimg;
		this.createBody(location, world, fixture, leftedge, rightedge, bodytype);
		contactdim = PhysUtils.SlickToJBoxVec(new Vec2(contactimg.getWidth(), contactimg.getHeight()));
		createSensor(location,world);
		this.doorId=doorId;
	}
	

	private void createBody(Vec2 location, World world, FixtureDef definition,
			FixtureDef leftedge, FixtureDef rightedge, int bodytype2) {
		BodyDef bd = new BodyDef();
		bd.position.set(location);

		switch (bodytype){
			case PhysUtils.STATIC: 
				bd.type = BodyType.STATIC;
				break;
			case PhysUtils.DYNAMIC:
				bd.type = BodyType.DYNAMIC;
				break;
			case PhysUtils.KINEMATIC:
				bd.type = BodyType.KINEMATIC;
				break;
			case PhysUtils.PORTAL: 
				bd.type = BodyType.STATIC;
				break;
		}
		
		bd.fixedRotation = true;
		Vec2 newloc = location.clone();
		setBody(world.createBody(bd));
		getBody().createFixture(definition);

		bd.type=BodyType.STATIC;
		newloc.x = location.x - (float)contactimg.getWidth()/(float)136;
		bd.position.set(newloc);
		leftbody = world.createBody(bd);
		leftbody.createFixture(leftedge);
		
		Vec2 rightloc = location.clone();
		rightloc.x=location.x+(float)contactimg.getWidth()/(float)136;
		bd.position.set(rightloc);
		rightbody = world.createBody(bd);
		rightbody.createFixture(rightedge);
		
		System.out.println(location);
		System.out.println(rightloc);
		System.out.println(newloc);
		
		PrismaticJointDef def = new PrismaticJointDef();
		def.bodyA=this.getBody();
		def.bodyB=leftbody;
		JointType jtype = JointType.PRISMATIC;
		def.type = jtype;	
		def.collideConnected=false;
		def.enableLimit=true;
		def.enableMotor=true;
		def.motorSpeed=0.001f;
		def.upperTranslation=0.3f;
		
		PrismaticJointDef rightdef = new PrismaticJointDef();
		rightdef.bodyA=this.getBody();
		rightdef.bodyB=rightbody;
		rightdef.type = jtype;
		rightdef.collideConnected=false;
		rightdef.enableLimit=true;
		rightdef.enableMotor=true;
		rightdef.motorSpeed=0.001f;
		rightdef.upperTranslation=0.3f;
	
		
		world.createJoint(rightdef);
		world.createJoint(def);
		
		this.getBody().m_mass=0.001f;
		
		System.out.println(rightdef.bodyA.getPosition() + " " + rightdef.bodyB.getPosition());
		System.out.println(getBody().getPosition() + " " + rightbody.getPosition());
		
		System.out.println(def.bodyA.getPosition() + " " + def.bodyB.getPosition());
		System.out.println(getBody().getPosition() + " " + leftbody.getPosition());

		
		System.out.println(world.getJointList().getType());
		
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
