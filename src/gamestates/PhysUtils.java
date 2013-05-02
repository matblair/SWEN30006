package gamestates;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class PhysUtils {
	private static int scaleFactor = 68;
	
	public static Vec2 JBoxToSlickVec (Vec2 from) {
		Vec2 to = new Vec2(from.x * scaleFactor, from.y * scaleFactor);
		return to;
	}

	public static Vec2 SlickToJBoxVec (Vec2 from) {
		Vec2 to = new Vec2(from.x / scaleFactor, from.y / scaleFactor);
		return to;
	}
	
	public static float pixelsToMetres (float pixels) {
		return pixels / scaleFactor;
	}

	public static void addWall (World world, float x, float y, float w, float h) {
		BodyDef bd = new BodyDef();
		bd.position.set(x + w / 2, y + h / 2);
		Body body = world.createBody(bd);
		PolygonShape box = new PolygonShape();
		box.setAsBox(w/2, h/2);
		body.createFixture(box, 0);
	}
}
