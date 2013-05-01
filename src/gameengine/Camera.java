package gameengine;


import gameobjects.GameObject;
import gameobjects.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


public class Camera{

	/** x and y render coordinates, in logical pixels **/
	private int xrender, yrender;
	/** Centre x and y positions (logical to the screen) **/
	private float ycentre, xcentre;
	/** The current screenwidth and height determined by the GameContainer **/
	private int screenwidth, screenheight;
	/** The unit to follow **/
	private GameObject follow;
	/** The boundaries for our field of view, x coordinates **/
	private int xmin,xmax;
	/** The boundaries for our field of view, y coordinates **/
	private int ymin,ymax;


	public Camera(final TiledMap map, final GameContainer gc) 
			throws SlickException{
	}

	/** Update the camera rendering position for each frame.
	 */
	public void update(final Player player, final TiledMap map)
			throws SlickException{
		/** Normalies the centre point of the character to follow and adjusts as follows **/
		// Update the xrender and yrender position
		setXrender((int)((-1*(follow.getPosition().x-xcentre)%map.getTileWidth())));
		setYrender((int)((-1*(follow.getPosition().y-ycentre)%map.getTileHeight())));
	}

	/** Follows the prescribed unit **/
	public void followUnit(final GameObject unit){
		follow = unit;
	}

	/** Checks whether or not a unit is within the camera's field of view and if so renders it in the
	 * appropriate position relative to the logical frame
	 */
	public void updateFOV(final TiledMap map)
			throws SlickException{
		// We calculate the current field of view.
	}

	/** Checks if a GameObject is within the field of view **/
	public boolean checkRender(final GameObject object)
			throws SlickException {
		if((object.getPosition().x>=(getXmin()+getXrender())) && (object.getPosition().x<= (xmax-getXrender()))){
			if((object.getPosition().y>= (getYmin()+getYrender()))&&(object.getPosition().y<=(ymax-getYrender()))){
				// Then the unit is within bounds of our field of view and must be rendered.
				object.setVisible(true);
				return true;	
			}
		}
		object.setVisible(false);
		return false;
	}


	/** Updatesthe screensize based on the current size of the GameContainer and determines
	 * new tileoffsets accordingly 
	 * @param gc The game container
	 * @param map The map
	 */
	public void updateScreenSize(final GameContainer gc, final TiledMap map){
		setScreenwidth(gc.getWidth());
		setScreenheight(gc.getHeight());

		xcentre = (float) (getScreenwidth()*0.5);
	}

	public int getXrender() {
		return xrender;
	}

	public void setXrender(final int xrender) {
		this.xrender = xrender;
	}

	public int getYrender() {
		return yrender;
	}

	public void setYrender(final int yrender) {
		this.yrender = yrender;
	}
	public int getXmin() {
		return xmin;
	}

	public void setXmin(final int xmin) {
		this.xmin = xmin;
	}

	public int getYmin() {
		return ymin;
	}

	public void setYmin(final int ymin) {
		this.ymin = ymin;
	}

	public int getScreenwidth() {
		return screenwidth;
	}

	public void setScreenwidth(final int screenwidth) {
		this.screenwidth = screenwidth;
	}

	public int getScreenheight() {
		return screenheight;
	}

	public void setScreenheight(final int screenheight) {
		this.screenheight = screenheight;
	}
}
