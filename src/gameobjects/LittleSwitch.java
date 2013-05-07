package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class LittleSwitch extends Switch {

	private static String doorId;
	
	public LittleSwitch(String imgloc, Vec2 location, World world, String door)
			throws SlickException {
		super(imgloc, location, world, PhysUtils.STATIC);
		setDoorId(door);
		
	}

	/**
	 * @return the doorId
	 */
	public static String getDoorId() {
		return doorId;
	}

	/**
	 * @param doorId the doorId to set
	 */
	public static void setDoorId(String doorId) {
		LittleSwitch.doorId = doorId;
	}

}
