package gameengine;
import java.util.ArrayList;
import java.util.Map;

import gameobjects.BigSwitch;
import gameobjects.GameObject;
import gameobjects.Player;
import gameobjects.Portal;
import gameobjects.Wall;
import gamestates.GameState;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;
import scoringsystem.AchievementPopup;

public class RenderEngine {
	private static final float ACOFFSET = 50;
	private static Image achpopup, bluearrow, orangearrow;
	
	/** Draw GameObjects from a map
	 * 
	 * @param map Map containing GameObjects to draw
	 * @param cam The camera object that defines the field of view.
	 */
	public static <T extends GameObject> void drawGameObjects(Map<String, T> map, Camera cam){
		for (T obj : map.values()) {
			drawGameObject(obj,cam);
		}
	}
	
	/** Draw an array of GameObjects
	 * 
	 * @param array Array of GameObjects to draw
	 * @param cam The camera object that defines the field of view.
	 */
	public static <T extends GameObject> void drawGameObjects(T[] array, Camera cam){
		for (T obj : array) {
			drawGameObject(obj,cam);
		}
	}
	
	/** Draw BigSwitches if they are in view.
	 * 
	 * @param switches Map of switches to draw
	 * @param cam The camera object that defines the field of view.
	 */
	public static void drawBigSwitches (Map<String, BigSwitch> switches, Camera cam) {
		for (BigSwitch s : switches.values()) {
			drawGameObject(s.getButton(), cam);
			drawGameObject(s.getBase(), cam);
		}
	}
	
	/** Draw a portal on the screen, and if it is out of view, draw a marker that points to the portal
	 * on the edge of the screen.
	 * 
	 * @param ps The portals to draw.
	 * @param cam The camera object that defines the field of view.
	 */
	public static void drawPortals (Portal[] ps, Camera cam) {
		for (Portal p : ps) {
			if (!p.isEnabled())
				continue;
			
			// If portal is in view, draw it
			if (cam.inView(p)) {
				drawGameObject (p, cam);

			// Draw something that points to the portal
			} else {
				Player player = GameState.getLevel().getLevelPlayer();
				Vec2 dir = PhysUtils.unitVector(p.getLocation().sub(player.getLocation()));
				Vec2 drawLoc = player.getLocation().add(dir.mul(1.3f));
				float angle = (float) (-PhysUtils.getAngle(dir) * 180 / Math.PI);

				if (p.getColour() == Portal.BLUE) {
					if (bluearrow == null)
						bluearrow = AssetManager.requestUIElement("BLUEARROW");
					bluearrow.setRotation(angle);
					drawImage(bluearrow, drawLoc, cam);
				} else {
					if (orangearrow == null)
						orangearrow = AssetManager.requestUIElement("ORANGEARROW");
					orangearrow.setRotation(angle);
					drawImage(orangearrow, drawLoc, cam);
				}
			}
		}
	}
	
	/** Draw an image on the screen, relative to camera location
	 * 
	 * @param image The image to draw.
	 * @param location The location in JBox coordinates that defines where to draw.
	 * @param cam The camera object that defines the field of view.
	 */
	public static void drawImage (Image image, Vec2 location, Camera cam) {
		Vec2 camLoc = cam.getLocation();
		Vec2 camDim = cam.getDimensions();
		Vec2 slickRenderPoint = PhysUtils.JBoxToSlickVec(new Vec2(location.x - camLoc.x, camDim.y - (location.y - camLoc.y)));
		image.drawCentered((int) slickRenderPoint.x, (int) slickRenderPoint.y);
	}
	
	/** Render a GameObject to the screen (if it is in view).
	 * 
	 * @param obj The GameObject to render.
	 * @param cam The camera object that defines the field of view.
	 */
	public static <T extends GameObject> void drawGameObject (T obj, Camera cam) {	
		if (cam.inView(obj)) {
			drawGameObject (obj, obj.getLocation(), cam);
		}
		if (obj.isInPortal() && obj.getPortalIn().isOpen()) {
			float rotation = (float) (-(obj.getPortalIn().getRotationDifference()+obj.getRotation()) * 180 / Math.PI);
			drawGameObject (obj, obj.getPortalIn().translateLocation(obj.getLocation()), rotation, cam);
		}
	}
	
	/** Draw a game object at a different location in the world, and with different
	 * rotation.
	 * 
	 * @param obj The GameObject to draw
	 * @param loc The location of the GameObject in the world
	 * @param rotation The rotation to draw with.
	 * @param cam The camera object that defines the field of view.
	 */
	public static <T extends GameObject> void drawGameObject (T obj, Vec2 location, float rotation, Camera cam) {
		Vec2 camLoc = cam.getLocation();
		Vec2 camDim = cam.getDimensions();
		Vec2 slickRenderPoint = PhysUtils.JBoxToSlickVec(new Vec2(location.x - camLoc.x, camDim.y - (location.y - camLoc.y)));
		Image image = obj.getImage();
		image.setRotation(rotation);
		image.drawCentered((int) slickRenderPoint.x, (int) slickRenderPoint.y);
	}
	
	/** Draw a game object at a different location in the world. That is,
	 * override its actual location
	 * 
	 * @param obj The GameObject to draw
	 * @param location The location of the GameObject in the world
	 * @param cam The camera object that defines the field of view.
	 */
	public static <T extends GameObject> void drawGameObject (T obj, Vec2 location, Camera cam) {
		float rotation = (float) (-obj.getRotation() * 180 / Math.PI);
		Vec2 camLoc = cam.getLocation();
		Vec2 camDim = cam.getDimensions();
		Vec2 slickRenderPoint = PhysUtils.JBoxToSlickVec(new Vec2(location.x - camLoc.x, camDim.y - (location.y - camLoc.y)));
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
	
	/** Draw walls as lines. Useful for debugging.
	 * 
	 * @param walls Map that contains walls to draw
	 * @param g Graphics context
	 * @param colour The colour to draw the walls
	 * @param cam The camera that defines the field of view.
	 */
	public static void drawWalls(Map<String, Wall> walls, Graphics g, Color colour, Camera cam){
		g.setColor(colour);
		for(Wall wl: walls.values()){
			if (!wl.isEnabled())
				continue;
			Vec2 camLoc = cam.getLocation();
			Vec2 camDim = cam.getDimensions();
			Vec2 wallstart = PhysUtils.JBoxToSlickVec(new Vec2(wl.getStart().x-camLoc.x,camDim.y - (wl.getStart().y - camLoc.y)));
			Vec2 wallend = PhysUtils.JBoxToSlickVec(new Vec2(wl.getEnd().x-camLoc.x,camDim.y - (wl.getEnd().y - camLoc.y)));
			g.setLineWidth(3);
			g.drawLine(wallstart.x, wallstart.y, wallend.x, wallend.y);
		}
	}
	
	/** Draw achievement popups
	 * 
	 * @param popups ArrayList of achievements to draw
	 * @param g Graphics context
	 * @param cam The camera that defines the field of view.
	 */
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
			ac.getImg().drawCentered(x-achpopup.getWidth()/2+ac.getImg().getWidth()/2+30, y);
			g.drawString(ac.getName(), x-achpopup.getWidth()/3+g.getFont().getWidth("Unlocked!")/2, 0 + ACOFFSET*i + (achpopup.getHeight()/2)*(i-1)-20);
			g.drawString("Unlocked!", x-achpopup.getWidth()/3+g.getFont().getWidth("Unlocked!")/2, 0 + ACOFFSET*i + (achpopup.getHeight()/2)*(i-1));
			i++;
		}
	}
}
