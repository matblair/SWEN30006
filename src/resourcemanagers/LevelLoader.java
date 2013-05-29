package resourcemanagers;

import gameobjects.BigSwitch;
import gameobjects.CompanionCube;
import gameobjects.DissipationField;
import gameobjects.Door;
import gameobjects.EndLevel;
import gameobjects.LittleSwitch;
import gameobjects.MovingPlatform;
import gameobjects.Platform;
import gameobjects.Player;
import gameobjects.Wall;
import gameworlds.Level;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; 

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LevelLoader {

	/** The constructor for the level loader **/
	public LevelLoader(){
	}
	
	/** Loads all attributes for a level calling delegate functions to handle resource element parsing for 
	 *  each of the different type of level objects
	 * @param is
	 * @param deferred
	 * @param level
	 * @throws SlickException
	 */
	public void loadLevel(final InputStream is, final boolean deferred, Level level) throws SlickException {
		final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new SlickException("Could not load resources", e);  
		}
		Document doc = null;
		try {
			doc = docBuilder.parse (is);
		} catch (final SAXException e) {
			throw new SlickException("Could not load resources", e);  
		} catch (final IOException e) {
			throw new SlickException("Could not load resources", e);  
		}

		// normalize text representation
		doc.getDocumentElement ().normalize ();

		final NodeList listResources = doc.getElementsByTagName("resource");  

		final int totalResources = listResources.getLength();
		if(deferred){
			LoadingList.setDeferredLoading(true);
		}


		
		for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
			final Node resourceNode = listResources.item(resourceIdx);
			if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
				final Element resourceElement = (Element)resourceNode;
				final String type = resourceElement.getAttribute("type");  
				if(type.equals("CUBE")){  
					addElementAsCube(resourceElement,level);
				}else if(type.equals("WALL")){ 
					addElementAsWall(resourceElement,level);
				}else if(type.equals("PLATFORM")){  
					addElementAsPlatform(resourceElement,level);
				}else if(type.equals("MOVINGPLATFORM")){  
					addElementAsMovingPlatform(resourceElement,level);
				}else if(type.equals("PORTAL")){  
					addElementAsPortal(resourceElement,level);
				}else if(type.equals("LITTLESWITCH")){  
					addElementAsLittleSwitch(resourceElement,level);
				}else if(type.equals("BIGSWITCH")){  
					addElementAsBigSwitch(resourceElement,level);
				}else if(type.equals("DOOR")){  
					addElementAsDoor(resourceElement,level);
				}else if(type.equals("BACKGROUNDIMG")){
					level.setBg(new Image(resourceElement.getTextContent()));
				}else if(type.equals("PLAYER")){
					addElementAsPlayer(resourceElement,level);
				}else if(type.equals("ENDZONE")){
					addElementAsEndLevel(resourceElement,level);
				}else if(type.equals("FOREGROUNDIMG")){
					level.setFg(new Image(resourceElement.getTextContent()));
				}else if(type.equals("FIELD")){
					addElementAsField(resourceElement, level);
				}
			}
		}
	}
	
	/** Adds a dissipation field to the level from a resource element read from file
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsField(Element resourceElement, Level level) throws SlickException {
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);
		
		DissipationField field = new DissipationField(startloc,level.getWorld());
		level.addDissipationField(field, field.getBodyID());
	}

	/** Add an end level object to the level from a resource element
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsEndLevel(Element resourceElement, Level level) throws SlickException {
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);

		EndLevel end = new EndLevel(startloc, level.getWorld());
		level.addEndLevel(end);
	}

	/** Adds a door to the level from the attributes described in the resource element
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsDoor(Element resourceElement, Level level) throws SlickException {
		String doorId = resourceElement.getAttribute("doorid");

		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);

		Door door = new Door(startloc, level.getWorld(),doorId);
		level.addDoor(door, door.getBodyID());
	}

	/** Adds a little siwtch to the level from the attributes described in the resource element
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsLittleSwitch(Element resourceElement, Level level) throws SlickException{
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);	
		Float cubex = Float.parseFloat(resourceElement.getAttribute("xCube"));
		Float cubey = Float.parseFloat(resourceElement.getAttribute("yCube"));
		Vec2 cubespawn = new Vec2(cubex,cubey);
		LittleSwitch newswitch = new LittleSwitch(startloc, level.getWorld(), cubespawn);
		level.addLittleSwitch(newswitch, newswitch.getBodyID());
		
	}
	
	/** Adds a big switch to the level from the attributes described in the resource element
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsBigSwitch(Element resourceElement, Level level) throws SlickException{
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);
		String doorid = resourceElement.getAttribute("doorid");
		BigSwitch bswitch = new BigSwitch(startloc,level.getWorld(), doorid);
		level.addBigSwitch(bswitch, bswitch.getBodyID());
	}

	/** Adds a predefined poratl to the level from the attributes described in the resource element
	 * 
	 * @param resourceElement
	 * @param level
	 */
	private void addElementAsPortal(Element resourceElement, Level level) {
		
	}

	/** Adds a platform to the level using the attributes described in the resource element
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsPlatform(Element resourceElement, Level level) throws SlickException{
		String type = resourceElement.getAttribute("id");
		int size;
		if(type.equals("SMALLPLATFORM")){
			size=Platform.SHORT;
		}else{
			size=Platform.LONG;
		}
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);
		Platform platform = new Platform(startloc, level.getWorld(),size,0);
		level.addPlatform(platform, platform.getBodyID());
	}
	
	/** Add a moving platform to the level from the attributes described in the resource element
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsMovingPlatform(Element resourceElement, Level level) throws SlickException{
	
		String type = resourceElement.getAttribute("id");
		int size;
		if(type.equals("SMALLPLATFORM")){
			size=Platform.SHORT;
		}else{
			size=Platform.LONG;
		}
		
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Float xTrackStart = Float.parseFloat(resourceElement.getAttribute("trackxstart"));
		Float yTrackStart = Float.parseFloat(resourceElement.getAttribute("trackystart"));
		Float xTrackFin = Float.parseFloat(resourceElement.getAttribute("trackxfin"));
		Float yTrackFin = Float.parseFloat(resourceElement.getAttribute("trackyfin"));
		boolean contMovement = Boolean.parseBoolean(resourceElement.getAttribute("contMovement"));
		Vec2 trackstart = new Vec2(xTrackStart,yTrackStart);
		Vec2 trackfin = new Vec2(xTrackFin, yTrackFin);
		Vec2 startloc = new Vec2(startx,starty);
		MovingPlatform platform = new MovingPlatform(startloc,level.getWorld(), trackstart, trackfin,size, contMovement);
		level.addMovingPlatform(platform, platform.getBodyID());
	}

	/** Adds a wall to the level from the attributes described in the resource element
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsWall(Element resourceElement, Level level) throws SlickException {
		Float startx =   Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Float endx = Float.parseFloat(resourceElement.getAttribute("xEnd"));
		Float endy = Float.parseFloat(resourceElement.getAttribute("yEnd"));
		String type = resourceElement.getAttribute("id");
		
		Vec2 start = new Vec2(startx,starty);
		Vec2 end = new Vec2(endx, endy);
		Wall wall = new Wall(start, end, level.getWorld());
		
		if(type.equals("NOPORTALWALL")){
			level.addPortallessWall(wall, wall.getBodyId());
		}else {
			level.addWall(wall, wall.getBodyId());
		}
	}

	/** Adds a cube to the level from the attributes described in the resource loader
	 * 
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsCube(Element resourceElement, Level level) throws SlickException {
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));
		Vec2 startloc = new Vec2(xstart,ystart);
		CompanionCube cube = new CompanionCube(startloc, level.getWorld());
		level.addCube(cube, cube.getBodyID());
	}
	
	/** Adds the players initial starting position to the world from the attributes
	 * described in the given resource element
	 * @param resourceElement
	 * @param level
	 * @throws SlickException
	 */
	private void addElementAsPlayer(Element resourceElement, Level level) throws SlickException{
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));		
		Vec2 startloc = new Vec2(xstart,ystart);
		Player newplayer = new Player(startloc, level.getWorld());
		level.setLevelPlayer(newplayer);
	}
}
