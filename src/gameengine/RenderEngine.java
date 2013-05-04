package gameengine;
import java.util.Map;

import gameobjects.CompanionCube;
import gameobjects.GameObject;
import gameobjects.MovingPlatform;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;

public class RenderEngine {
	/** Render a GameObject to the screen (if it is in view).
	 * 
	 * @param obj The GameObject to render.
	 * @param cam The camera object that defines the field of view.
	 */
	
	public static void drawGameObjects(Map<String, CompanionCube> cubes, Camera cam){
		for (CompanionCube obj : cubes.values()) {
			drawGameObject(obj,cam);
		}
	}
	
	public static void drawGameObject (GameObject obj, Camera cam) {		
		if (!cam.inView(obj)) {
			return;
		}
		
		Vec2 camLoc = cam.getLocation();
		Vec2 camDim = cam.getDimensions();
		Vec2 objLoc = obj.getLocation();
		Vec2 slickRenderPoint = PhysUtils.JBoxToSlickVec(new Vec2(objLoc.x - camLoc.x, camDim.y - (objLoc.y - camLoc.y)));
		obj.getImage().drawCentered(slickRenderPoint.x, slickRenderPoint.y);
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
		//bg.draw(0,0);
	}

	public static void drawPlatforms(Map<String, MovingPlatform> platforms,
			Camera cam) {
		for (MovingPlatform obj : platforms.values()) {
			drawGameObject(obj,cam);
		}		
	}
	
	
}
