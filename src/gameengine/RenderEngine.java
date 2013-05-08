package gameengine;

import java.util.Map;

import gameobjects.GameObject;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;

public class RenderEngine {
	public static <T extends GameObject> void drawGameObjects(Map<String, T> map, Camera cam){
		for (T obj : map.values()) {
			drawGameObject(obj,cam);
		}
	}
	
	/** Render a GameObject to the screen (if it is in view).
	 * 
	 * @param obj The GameObject to render.
	 * @param cam The camera object that defines the field of view.
	 */
	public static <T extends GameObject> void drawGameObject (T obj, Camera cam) {		
		if (!cam.inView(obj)) {
			return;
		}
		
		float rotation = (float) (-obj.getRotation() * 180 / Math.PI);
		Vec2 camLoc = cam.getLocation();
		Vec2 camDim = cam.getDimensions();
		Vec2 objLoc = obj.getLocation();
		Vec2 slickRenderPoint = PhysUtils.JBoxToSlickVec(new Vec2(objLoc.x - camLoc.x, camDim.y - (objLoc.y - camLoc.y)));
		Image image = obj.getImage();
		image.setRotation(rotation);
		image.drawCentered((int) slickRenderPoint.x, (int) slickRenderPoint.y);
	}
	
	/** Draw a background image. Will also work for foreground overlays.
	 * 
	 * @param bg The image to draw.
	 * @param cam The camera object that defines the field of view.
	 */
	public static void drawBG (Image bg, Camera cam) {
		Vec2 slickCamLoc = PhysUtils.JBoxToSlickVec(cam.getLocation());
		Vec2 slickCamDim = PhysUtils.JBoxToSlickVec(cam.getDimensions());
		Vec2 topLeftCoord = new Vec2(slickCamLoc.x, bg.getHeight() - slickCamLoc.y - slickCamDim.y);
		Vec2 bottomRightCoord = new Vec2(slickCamLoc.x + slickCamDim.x, bg.getHeight() - slickCamLoc.y);
		bg.draw(0, 0, slickCamDim.x, slickCamDim.y, topLeftCoord.x, topLeftCoord.y, bottomRightCoord.x, bottomRightCoord.y);
	}
}
