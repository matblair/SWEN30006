package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class Door extends GameObject {
	private static final String ANIMID="DOOR";
	private static final String SHAPEID="DOORSHAPE";
	private static final int BODYTYPE = PhysUtils.STATIC;
	
	private Animation animation;
	private boolean isOpen=false;
	private String doorId;

	public Door(Vec2 location, World world, String doorId)
			throws SlickException {
		super();
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, BODYTYPE);
		animation = AssetManager.requestAnimationResources(ANIMID);
		animation.setPingPong(true);
		setDoorId(doorId);
	}
	
	public void update (int delta) {
		animation.update(delta);
		animation.stopAt(isOpen ? animation.getFrameCount()-1 : 0);
		animation.start();
	}
	
	@Override
	public Image getImage() {
		return animation.getCurrentFrame();
	}
	
	public void open() {
		if (!isOpen) {
			isOpen = true;
			getBody().getFixtureList().setSensor(true);
		}
	}
	
	public void close() {
		if (isOpen) {
			isOpen = false;
			getBody().getFixtureList().setSensor(false);
		}
	}
	
	/**
	 * @return the doorId
	 */
	public String getDoorId() {
		return doorId;
	}

	/**
	 * @param doorId the doorId to set
	 */
	public void setDoorId(String doorId) {
		this.doorId = doorId;
	}

	/**
	 * @return the isOpen
	 */
	public boolean isOpen() {
		return isOpen;
	}
}
