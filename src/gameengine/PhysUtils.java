package gameengine;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class PhysUtils {
	
	public static final int STATIC=0;
	public static final int KINEMATIC=1;
	public static final int DYNAMIC=2; 
	public static final int PORTAL=3;

	private static int scaleFactor = 68;
	
	/** Convert vector from metres to pixels.
	 * 
	 * @param from Vector in metres.
	 * @return Vector in pixels.
	 */
	public static Vec2 JBoxToSlickVec (Vec2 from) {
		Vec2 to = from.mul(scaleFactor);
		return to;
	}

	/** Convert vector from pixels to metres.
	 * 
	 * @param from Vector in pixels.
	 * @return Vector in metres.
	 */
	public static Vec2 SlickToJBoxVec (Vec2 from) {
		Vec2 to = from.mul(1f/scaleFactor);
		return to;
	}
	
	/** Convert from pixels to metres.
	 * 
	 * @param from Value in pixels.
	 * @return Value in metres.
	 */
	public static float pixelsToMetres (float pixels) {
		return pixels / scaleFactor;
	}

	/** Add a wall body to the game world.
	 * 
	 * @param world The JBox world in which the wall should be added
	 * @param x Left coordinate of wall
	 * @param y Bottom coordinate of wall
	 * @param w Width of wall
	 * @param h Height of wall
	 */
	public static void addWall (World world, float x, float y, float w, float h) {
		BodyDef bd = new BodyDef();
		bd.position.set(x + w / 2, y + h / 2);
		Body body = world.createBody(bd);
		PolygonShape box = new PolygonShape();
		box.setAsBox(w/2, h/2);
		body.createFixture(box, 0);
	}
	
	public static void printAllBodyIds(World world){
		System.out.println(world.getBodyCount()+" bodies in the physics world");	
		org.jbox2d.dynamics.Body bodylist = world.getBodyList();
		while(bodylist!=null){
			System.out.println(bodylist + " is the body id");
			bodylist=bodylist.getNext();
		}
	}
}
