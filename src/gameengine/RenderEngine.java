package gameengine;
import java.util.ArrayList;
import java.util.Map;

import gameobjects.BigSwitch;
import gameobjects.GameObject;
import gameobjects.Wall;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;
import scoringsystem.AchievementPopup;

public class RenderEngine {
	
	private static final float ACOFFSET = 50;
	private static Image achpopup;
	
	public static <T extends GameObject> void drawGameObjects(Map<String, T> map, Camera cam){
		for (T obj : map.values()) {
			drawGameObject(obj,cam);
		}
	}
	
	public static <T extends GameObject> void drawGameObjects(T[] array, Camera cam){
		for (T obj : array) {
			drawGameObject(obj,cam);
		}
	}
	
	public static void drawBigSwitches (Map<String, BigSwitch> switches, Camera cam) {
		for (BigSwitch s : switches.values()) {
			drawGameObjects(s.getRenderableComponents().toArray(new GameObject[0]), cam);
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
	
	public static void drawWalls(Map<String, Wall> walls, Graphics g, Camera cam){
		for(Wall wl: walls.values()){
			if (!wl.isEnabled())
				continue;
			Vec2 camLoc = cam.getLocation();
			Vec2 camDim = cam.getDimensions();
			Vec2 wallstart = PhysUtils.JBoxToSlickVec(new Vec2(wl.getStart().x-camLoc.x,camDim.y - (wl.getStart().y - camLoc.y)));
			Vec2 wallend = PhysUtils.JBoxToSlickVec(new Vec2(wl.getEnd().x-camLoc.x,camDim.y - (wl.getEnd().y - camLoc.y)));
			g.setColor(Color.magenta);
			g.setLineWidth(3);
			g.drawLine(wallstart.x, wallstart.y, wallend.x, wallend.y);
		}
	}
	
	public static void renderAchievementPopups(ArrayList<AchievementPopup> popups, Graphics g, Camera cam){
		//Ensure we have loaded the achievement popup
		if(achpopup==null){
			achpopup = AssetManager.requestUIElement("ACHPOPUP");
		}
		g.setColor(Color.gray);
		int i=1;
		for(AchievementPopup ac: popups){
			float x = PhysUtils.JBoxToSlickVec(cam.getDimensions()).x-achpopup.getWidth()/2-ACOFFSET/2;
			float y = 0 + ACOFFSET*i + (achpopup.getHeight()/2)*(i-1);
			achpopup.drawCentered(x,y);
			//ac.getImg().drawCentered(x+ac.getImg().getWidth()/2+10, y+ac.getImg().getHeight()/2+10);
			g.drawString(ac.getName(), x-achpopup.getWidth()/3+g.getFont().getWidth("Unlocked!")/2, 0 + ACOFFSET*i + (achpopup.getHeight()/2)*(i-1)-20);
			g.drawString("Unlocked!", x-achpopup.getWidth()/3+g.getFont().getWidth("Unlocked!")/2, 0 + ACOFFSET*i + (achpopup.getHeight()/2)*(i-1));
			i++;
		}
	}
}
