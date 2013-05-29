package gameobjects;

import gameengine.PhysUtils;
import gameengine.Sound;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;
import resourcemanagers.SoundController;

public class Door extends GameObject {
	private static final String ANIMID="DOOR";
	private static final String SHAPEID="DOORSHAPE";
	private static final int BODYTYPE = PhysUtils.STATIC;
	
	private Animation animation;
	private boolean isOpen=false;
	private String doorId;

	/** Create a Door object
	 * 
	 * @param location Location of the field to start in the world (centre)
	 * @param world The physics world
	 * @param doorId The ID to associate with the door (for linking with switches)
	 * @throws SlickException
	 */
	public Door(Vec2 location, World world, String doorId)
			throws SlickException {
		super();
		FixtureDef fixture = createFixture(SHAPEID);
		this.createBody(location, world, fixture, BODYTYPE);
		animation = AssetManager.requestAnimationResources(ANIMID);
		animation.setPingPong(true);
		this.doorId = doorId;
	}
	
	/** Update the door
	 * 
	 * @param delta Milliseconds since last update.
	 */
	public void update (int delta) {
		animation.update(delta);
		animation.stopAt(isOpen ? animation.getFrameCount()-1 : 0);
		animation.start();
	}
	
	/** Get the current image representing the door.
	 */
	@Override
	public Image getImage() {
		return animation.getCurrentFrame();
	}
	
	/** Open the door
	 */
	public void open() {
		if (!isOpen) {
			isOpen = true;
			getBody().getFixtureList().setSensor(true);
			SoundController.play(Sound.DOOROPEN);
		}
	}
	
	/** Close the door
	 */
	public void close() {
		if (isOpen) {
			isOpen = false;
			getBody().getFixtureList().setSensor(false);
			SoundController.play(Sound.DOORCLOSE);
		}
	}
	
	/** Get the door's ID
	 * @return the door's ID
	 */
	public String getDoorId() {
		return doorId;
	}
	
	/** Check if the door is open.
	 * @return true if the door is open
	 */
	public boolean isOpen() {
		return isOpen;
	}
}
