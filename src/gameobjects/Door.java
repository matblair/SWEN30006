package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class Door extends GameObject {
	private boolean isOpen=false;
	private Image openImage, closedImage;
	private String doorId;
	
	private static final String OPENIMGID="DOOROPEN";
	private static final String CLOSEIMGID="DOORCLOSE";
	private static final String SHAPEID="DOORSHAPE";
	private static final int BODYTYPE = PhysUtils.STATIC;

	public Door(Vec2 location, World world, String doorId)
			throws SlickException {
		super(OPENIMGID);
		FixtureDef fixture = createFixture();
		this.createBody(location, world, fixture, BODYTYPE);
		openImage = AssetManager.requestImage(OPENIMGID);
		closedImage = AssetManager.requestImage(CLOSEIMGID);
		setDoorId(doorId);
	}
	
	private FixtureDef createFixture(){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = AssetManager.requestShape(SHAPEID);
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		return fixtureDef;
	}
	
	public void update (int delta) {
		
	}
	
	@Override
	public Image getImage() {
		if (isOpen()) {
			return openImage;
		} else {
			return closedImage;
		}
	}
	
	public void open() {
		setOpen(true);
		getBody().getFixtureList().setSensor(true);
	}
	
	public void close() {
		setOpen(false);
		getBody().getFixtureList().setSensor(false);
	}
	
	public void toggle() {
		if (isOpen()) {
			close();
		} else {
			open();
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

	/**
	 * @param isOpen the isOpen to set
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
