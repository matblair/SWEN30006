package resourcemanagers;

import gameobjects.BigSwitch;
import gameobjects.CompanionCube;
import gameobjects.Door;
import gameobjects.LittleSwitch;
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
		System.out.println(totalResources + " total resources");
		if(deferred){
			LoadingList.setDeferredLoading(true);
		}

		int players=0;
		int walls=0;
		int backgroundimg=0;
		
		for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
			final Node resourceNode = listResources.item(resourceIdx);
			if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
				final Element resourceElement = (Element)resourceNode;
				final String type = resourceElement.getAttribute("type");  
				System.out.println(type);
				if(type.equals("CUBE")){  
					addElementAsCube(resourceElement,level);
				}else if(type.equals("WALL")){ 
					walls++;
					addElementAsWall(resourceElement,level);
				}else if(type.equals("TURRET")){  
					addElementAsTurret(resourceElement,level);
				}else if(type.equals("PLATFORM")){  
					addElementAsPlatform(resourceElement,level);
				}else if(type.equals("PORTAL")){  
					addElementAsPortal(resourceElement,level);
				}else if(type.equals("LITTLESWITCH")){  
					addElementAsLittleSwitch(resourceElement,level);
				}else if(type.equals("BIGSWITCH")){  
					addElementAsBigSwitch(resourceElement,level);
				}else if(type.equals("DOOR")){  
					addElementAsDoor(resourceElement,level);
				}else if(type.equals("BACKGROUNDIMG")){
					backgroundimg++;
					level.setBg(new Image(resourceElement.getTextContent()));
				}else if(type.equals("PLAYER")){
					players++;
					addElementAsPlayer(resourceElement,level);
				}
			}
		}
		
		System.out.println(players +" players added");
		System.out.println(backgroundimg +" backgrounds added");
		System.out.println(walls +" walls added");

	}

	private void addElementAsDoor(Element resourceElement, Level level) throws SlickException {
		String openid = resourceElement.getAttribute("id");
		String closedimage = resourceElement.getAttribute("closedid");
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		//String doorid = resourceElement.getAttribute("doorid");
		Vec2 startloc = new Vec2(startx,starty);

		Door door = new Door(closedimage, startloc, level.getPhysWorld(), openid);
		level.addDoor(door, door.getBodyId());
	}

	private void addElementAsLittleSwitch(Element resourceElement, Level level) throws SlickException{
		String imgid = resourceElement.getAttribute("id");
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);	
		Float cubex = Float.parseFloat(resourceElement.getAttribute("xCube"));
		Float cubey = Float.parseFloat(resourceElement.getAttribute("yCube"));
		Vec2 cubespawn = new Vec2(cubex,cubey);
		String cubeid = resourceElement.getAttribute("cubeid");	
		LittleSwitch newswitch = new LittleSwitch(imgid, startloc, level.getPhysWorld(), cubespawn, cubeid);
		level.addLittleSwitch(newswitch, newswitch.getBodyId());
		
	}
	
	private void addElementAsBigSwitch(Element resourceElement, Level level) throws SlickException{
		String imgid = resourceElement.getAttribute("id");
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Vec2 startloc = new Vec2(startx,starty);
		
		String contactid = resourceElement.getAttribute("contactimg");
		String downid = resourceElement.getAttribute("downid");
		String doorid = resourceElement.getAttribute("doorid");
		
		BigSwitch bswitch = new BigSwitch(imgid,startloc,level.getPhysWorld(),contactid, downid, doorid);
		level.addBigSwitch(bswitch, bswitch.getBodyId());
	}

	private void addElementAsPortal(Element resourceElement, Level level) {
		
	}

	private void addElementAsPlatform(Element resourceElement, Level level) throws SlickException{
		String imgid = resourceElement.getAttribute("id");
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Float width = Float.parseFloat(resourceElement.getAttribute("width"));
		Float height = Float.parseFloat(resourceElement.getAttribute("height"));
		Vec2 startloc = new Vec2(startx,starty);
		Platform platform = new Platform(imgid, startloc, width, height, level.getPhysWorld());
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
		String imgid = resourceElement.getAttribute("id");
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));
		Vec2 startloc = new Vec2(xstart,ystart);
		CompanionCube cube = new CompanionCube(imgid, startloc, level.getPhysWorld());
		level.addCube(cube, cube.getBodyId());
	}
	
	private void addElementAsPlayer(Element resourceElement, Level level) throws SlickException{
		String imgid = resourceElement.getAttribute("id");
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));
		System.out.println(xstart + ", " + ystart + ", " + imgid);
		
		Vec2 startloc = new Vec2(xstart,ystart);
		Player newplayer = new Player(imgid,startloc, level.getPhysWorld());
		level.setLevelPlayer(newplayer);
		
		//Debug Code
		System.out.println(newplayer.getBody() + " is the players body id");
		System.out.println(newplayer.getBody().getType()+ " is the players body type");
	}
}
