package gameengine;
import gameobjects.GameObject;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;

public class RenderEngine {
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
	
	public static void drawBG (Image bg, Camera cam) {
		Vec2 slickCamLoc = PhysUtils.JBoxToSlickVec(cam.getLocation());
		Vec2 slickCamDim = PhysUtils.JBoxToSlickVec(cam.getDimensions());
		Vec2 topLeftCoord = new Vec2(slickCamLoc.x, bg.getHeight() - slickCamLoc.y - slickCamDim.y);
		Vec2 bottomRightCoord = new Vec2(slickCamLoc.x + slickCamDim.x, bg.getHeight() - slickCamLoc.y);
		bg.draw(0, 0, 800, 600, topLeftCoord.x, topLeftCoord.y, bottomRightCoord.x, bottomRightCoord.y);
		//bg.draw(0,0);
	}
}
