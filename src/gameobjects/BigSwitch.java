package gameobjects;

import java.util.ArrayList;

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

public class BigSwitch {
	private static final String BASEIMAGE = "BIGSWITCHBASE";
	private static final String SENSORIMAGE = "BIGSWITCHSENSOR";
	private static final String BASESHAPE = "BIGSWITCHBASE";
	private static final String SENSORSHAPE = "BIGSWITCHSENSOR";
	private static final int BUTTONOFFSET = 11;

	private GameObject base, button, sensor;
	private ArrayList<GameObject> renderableGO;
	private String doorId;
	private Door doorlink=null;

	public BigSwitch(Vec2 location, World world, String doorId)
			throws SlickException {
		// Store doorID
		this.doorId=doorId;
		
		// Create GameObject components
		base = new GameObject(BASEIMAGE);
		button = new GameObject(SENSORIMAGE);
		sensor = new GameObject();
		renderableGO = new ArrayList<GameObject>();
		renderableGO.add(base);
		renderableGO.add(button);

		// Create the sensors
		FixtureDef basefd = base.createFixture(BASESHAPE);
		FixtureDef sensorfd = sensor.createFixture(SENSORSHAPE);

		// Create the bodies
		Vec2 buttonloc = location.add(new Vec2 (0, PhysUtils.pixelsToMetres(BUTTONOFFSET)));
		base.createBody(location, world, basefd, PhysUtils.STATIC);
		button.createBody(buttonloc, world, sensorfd, PhysUtils.DYNAMIC);
		sensor.createBody(buttonloc, world, sensorfd, PhysUtils.STATIC);
		sensor.getBody().getFixtureList().setSensor(true);
		
		// Join the bodies
		joinBodies (world, button, base);
	}
	
	private void joinBodies(World world, GameObject button, GameObject base) {
		Vec2 axis = new Vec2(0, 1);
		PrismaticJointDef jd = new PrismaticJointDef();
		jd.initialize(button.getBody(), base.getBody(), button.getLocation(), axis);
		jd.enableLimit = true;
		jd.lowerTranslation = -3f;
		jd.upperTranslation = 4f;
		jd.motorSpeed = 10000;
		jd.maxMotorForce = 0.1f;
		jd.enableMotor = true;
		jd.collideConnected=false;
		world.createJoint(jd);
	}
	
	public ArrayList<GameObject> getRenderableComponents() {
		return renderableGO;
	}
	
	public String getBodyID() {
		return base.getBodyID();
	}

	public void updateState(){
		// Set the door connection if not already set
		if(doorlink==null)
			for(Door door: GameState.getLevel().getDoorCollection())
				if(door.getDoorId().equals(this.doorId))
					doorlink=door;
		
		// Check contacts
		ContactEdge edge = sensor.getBody().getContactList();
		while (edge != null) {
			String type=GameState.getLevel().getBodyType(edge.other);
			if(type.equals("player") || type.equals("cube")){
				doorlink.open();
				return;
			}
			edge = edge.next;
		}
		
		// Could not find contact
		if (edge == null)
			doorlink.close();
	}
}