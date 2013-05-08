package gameengine;
import org.jbox2d.common.Vec2;
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
	
	public static void printAllBodyIds(World world){
		System.out.println(world.getBodyCount()+" bodies in the physics world");	
		org.jbox2d.dynamics.Body bodylist = world.getBodyList();
		while(bodylist!=null){
			System.out.println(bodylist + " is the body id");
			bodylist=bodylist.getNext();
		}
	}
	
	/** Get the distance between two vectors
	 * 
	 * @param v1 First vector
	 * @param v2 Second vector
	 * @return Distance between the two
	 */
	public static float distance(Vec2 v1, Vec2 v2) {
		return v1.sub(v2).length();
	}
	
	/** Get the angle of a vector in radians
	 * 
	 * @param vector The vector whose angle is wanted
	 * @return Angle of vector in radians
	 */
	public static float getAngle(Vec2 vector) {
		return (float) Math.atan2(vector.y, vector.x);
	}
}
