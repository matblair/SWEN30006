package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.*;

import org.newdawn.slick.SlickException;

public class BigSwitch {
	private static final String BASEIMAGE = "BIGSWITCHBASE";
	private static final String SENSORIMAGE = "BIGSWITCHSENSOR";
	private static final String BASESHAPE = "BIGSWITCHBASE";
	private static final String SENSORSHAPE = "BIGSWITCHSENSOR";
	private static final int BUTTONOFFSET = 9;
	private static final int BUTTONTRANSLATION = 4;

	private GameObject base, button, sensor;
	private String doorId;
	private Door doorlink=null;

	/** Create a new BigSwitch
	 * 
	 * @param location Where the switch will be (middle of bottom)
	 * @param world World in which the body will go
	 * @param doorId ID of the Door this switch opens
	 * @throws SlickException
	 */
	public BigSwitch(Vec2 location, World world, String doorId)
			throws SlickException {
		// Store doorID
		this.doorId=doorId;
		
		// Create GameObject components
		base = new GameObject(BASEIMAGE);
		button = new GameObject(SENSORIMAGE);
		sensor = new GameObject();

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
	
	/** Create the joint for the button
	 * 
	 * @param world World to create the joint in
	 * @param button GameObject that is the button
	 * @param base GameObject that is the base
	 */
	private void joinBodies(World world, GameObject button, GameObject base) {
		Vec2 axis = new Vec2(0, 1);
		PrismaticJointDef jd = new PrismaticJointDef();
		jd.initialize(button.getBody(), base.getBody(), button.getLocation(), axis);
		jd.enableLimit = true;
		jd.lowerTranslation = 0f;
		jd.upperTranslation = PhysUtils.pixelsToMetres(BUTTONTRANSLATION);
		jd.motorSpeed = -10;
		jd.maxMotorForce = 10;
		jd.enableMotor = true;
		jd.collideConnected=false;
		world.createJoint(jd);
	}
	
	/** Get the base (for drawing or something)
	 * 
	 * @return The base
	 */
	public GameObject getBase() {
		return base;
	}
	
	/** Get the button (for drawing or something)
	 * 
	 * @return The button
	 */
	public GameObject getButton() {
		return button;
	}
	
	/** Get the bodyID to be associated with this object. Uses the bodyID of the base.
	 * 
	 * @return The bodyID of the base
	 */
	public String getBodyID() {
		return base.getBodyID();
	}

	/** Update the switch. That is, check if the door should be opened or not.
	 * 
	 */
	public void update(){
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