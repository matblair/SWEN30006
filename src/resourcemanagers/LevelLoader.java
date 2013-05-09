package resourcemanagers;

import gameobjects.BigSwitch;
import gameobjects.CompanionCube;
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

	public LevelLoader(){
	}
	
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
				}else if(type.equals("TURRET")){  
					addElementAsTurret(resourceElement,level);
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
				}
			}
		}
	}
	
	private void addElementAsEndLevel(Element resourceElement, Level level) throws SlickException {
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);

		EndLevel end = new EndLevel(startloc, level.getPhysWorld());
		level.addEndLevel(end);
	}

	private void addElementAsDoor(Element resourceElement, Level level) throws SlickException {
		String doorId = resourceElement.getAttribute("doorid");

		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);

		Door door = new Door(startloc, level.getPhysWorld(),doorId);
		level.addDoor(door, door.getBodyId());
	}

	private void addElementAsLittleSwitch(Element resourceElement, Level level) throws SlickException{
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);	
		Float cubex = Float.parseFloat(resourceElement.getAttribute("xCube"));
		Float cubey = Float.parseFloat(resourceElement.getAttribute("yCube"));
		Vec2 cubespawn = new Vec2(cubex,cubey);
		LittleSwitch newswitch = new LittleSwitch(startloc, level.getPhysWorld(), cubespawn);
		level.addLittleSwitch(newswitch, newswitch.getBodyId());
		
	}
	
	private void addElementAsBigSwitch(Element resourceElement, Level level) throws SlickException{
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);
		String doorid = resourceElement.getAttribute("doorid");
		
		BigSwitch bswitch = new BigSwitch(startloc,level.getPhysWorld(), doorid);
		level.addBigSwitch(bswitch, bswitch.getBodyId());
	}

	private void addElementAsPortal(Element resourceElement, Level level) {
		
	}

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
		Platform platform = new Platform(startloc, level.getPhysWorld(),size,0);
		level.addPlatform(platform, platform.getBodyId());
	}
	
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

		Vec2 trackstart = new Vec2(xTrackStart,yTrackStart);
		Vec2 trackfin = new Vec2(xTrackFin, yTrackFin);
		Vec2 startloc = new Vec2(startx,starty);
		MovingPlatform platform = new MovingPlatform(startloc,level.getPhysWorld(), trackstart, trackfin,size);
		level.addMovingPlatform(platform, platform.getBodyId());
	}

	private void addElementAsTurret(Element resourceElement, Level level) {

	}

	private void addElementAsWall(Element resourceElement, Level level) throws SlickException {
		Float startx =   Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Float endx = Float.parseFloat(resourceElement.getAttribute("xEnd"));
		Float endy = Float.parseFloat(resourceElement.getAttribute("yEnd"));
		
		Vec2 start = new Vec2(startx,starty);
		Vec2 end = new Vec2(endx, endy);
		Wall wall = new Wall(start, end, level.getPhysWorld());
		level.addWall(wall, wall.getBodyId());
	}

	private void addElementAsCube(Element resourceElement, Level level) throws SlickException {
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));
		Vec2 startloc = new Vec2(xstart,ystart);
		CompanionCube cube = new CompanionCube(startloc, level.getPhysWorld());
		level.addCube(cube, cube.getBodyId());
	}
	
	private void addElementAsPlayer(Element resourceElement, Level level) throws SlickException{
		String imgid = resourceElement.getAttribute("id");
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));
		System.out.println(xstart + ", " + ystart + ", " + imgid);
		
		Vec2 startloc = new Vec2(xstart,ystart);
		Player newplayer = new Player(startloc, level.getPhysWorld());
		level.setLevelPlayer(newplayer);
		
		//Debug Code
		System.out.println(newplayer.getBody() + " is the players body id");
		System.out.println(newplayer.getBody().getType()+ " is the players body type");
	}
}
