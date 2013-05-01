package gamestates;
import org.jbox2d.common.Vec2;

public class PhysUtils {
		public static Vec2 JBoxToSlickVec (Vec2 from) {
			Vec2 to = new Vec2(from.x * 68, from.y * 68);
			return to;
		}
		
		public static Vec2 SlickToJBoxVec (Vec2 from) {
			Vec2 to = new Vec2(from.x / 68, from.y / 68);
			return to;
		}
}
