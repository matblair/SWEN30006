package gameengine;
import org.jbox2d.common.Vec2;

public class PhysUtils {
	// Constants for defining BodyType
	public static final int STATIC=0;
	public static final int KINEMATIC=1;
	public static final int DYNAMIC=2; 
	public static final int PORTAL=3;

	private static int scaleFactor = 64;
	
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
	
	/** Rotate a vector by a certain number of radians
	 * 
	 * @param vector The vector to rotate
	 * @param angle The angle to rotate by in radians
	 * @return The rotated vector
	 */
	public static Vec2 rotateVector(Vec2 vector, double angle) {
		float cost = (float) Math.cos(angle);
		float sint = (float) Math.sin(angle);
		float x = vector.x;
		float y = vector.y;
		return new Vec2(x * cost - y * sint, x * sint + y * cost);
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
	
	/** Get the unit vector in the same direction of input vector
	 * 
	 * @param vector The vector whose unit tangent is wanted
	 * @return The unit vector
	 */
	public static Vec2 unitVector(Vec2 vector) {
		return vector.mul(1/vector.length());
	}
}
