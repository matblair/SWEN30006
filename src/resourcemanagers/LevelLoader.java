package resourcemanagers;

import gameengine.PhysUtils;
import gameobjects.CompanionCube;
import gameobjects.Player;
import gameobjects.Wall;
import gameworlds.Level;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; 

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
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

	public void loadTestLevel(Level level) throws SlickException{
		//Physics Walls
		PhysUtils.addWall(level.getPhysWolrd(), 0, 0, 100, 1); //floor
		Body bdlist = level.getPhysWolrd().getBodyList();	
		System.out.println("added floor wall with id: " + bdlist);

		PhysUtils.addWall(level.getPhysWolrd(), 0, 0, 0.5f, 20); //left wall
		bdlist = level.getPhysWolrd().getBodyList();	
		System.out.println("added left wall with id: " + bdlist);
		
		PhysUtils.addWall(level.getPhysWolrd(), 0, 17.14f, 100, 0); //top
		bdlist = level.getPhysWolrd().getBodyList();	
		System.out.println("added top wall with id: " + bdlist);
		
		PhysUtils.addWall(level.getPhysWolrd(), 10, 2.5f, 4, 0.25f);//first thing
		bdlist = level.getPhysWolrd().getBodyList();	
		System.out.println("added first platform wall with id: " + bdlist);
		
		PhysUtils.addWall(level.getPhysWolrd(), 14, 3.95f, 4, 0.25f);//second thing
		bdlist = level.getPhysWolrd().getBodyList();	
		System.out.println("added second platform wall with id: " + bdlist);
		
		PhysUtils.addWall(level.getPhysWolrd(), 19.5f, 0, 0.5f, 20); //right wall
		bdlist = level.getPhysWolrd().getBodyList();	
		System.out.println("added right wall with id: " + bdlist);

		// Dynamic Body
		Player player = new Player("CHELLSPRITE",new Vec2(2, 5), level.getPhysWolrd());
		level.setLevelPlayer(player);
		System.out.println(player.getBody() + " is the players body id");
		System.out.println(player.getBody().getType()+ " is the players body type");
		
		level.setBg(new Image("assets/levels/levelone.png"));
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
				}else if(type.equals("SWITCH")){  
					addElementAsSwitch(resourceElement,level);
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

	private void addElementAsSwitch(Element resourceElement, Level level) {

	}

	private void addElementAsPortal(Element resourceElement, Level level) {
		
	}

	private void addElementAsPlatform(Element resourceElement, Level level) {
		
	}

	private void addElementAsTurret(Element resourceElement, Level level) {

	}

	private void addElementAsWall(Element resourceElement, Level level) throws SlickException {
		String imgid = resourceElement.getAttribute("id");
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Float width = Float.parseFloat(resourceElement.getAttribute("width"));
		Float height = Float.parseFloat(resourceElement.getAttribute("height"));
		Vec2 startloc = new Vec2(startx,starty);
		Wall wall = new Wall(imgid,startloc, width, height,level.getPhysWolrd());

		Body bdlist = level.getPhysWolrd().getBodyList();
		wall.setBodyId(bdlist.toString());
		level.addWall(wall);
		System.out.println("added wall with id: " + bdlist + " to "+ startx + " ," + starty + " ," + width + " ," + height);
	
	}

	private void addElementAsCube(Element resourceElement, Level level) throws SlickException {
		String imgid = resourceElement.getAttribute("id");
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));
		Vec2 startloc = new Vec2(xstart,ystart);

		
		CompanionCube cube = new CompanionCube(imgid, startloc, level.getPhysWolrd());
		Body bdlist = level.getPhysWolrd().getBodyList();
		cube.setBodyId(bdlist.toString());
		level.addCube(cube);
	}
	
	private void addElementAsPlayer(Element resourceElement, Level level) throws SlickException{
		String imgid = resourceElement.getAttribute("id");
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("starty"));
		System.out.println(xstart + ", " + ystart + ", " + imgid);
		Vec2 startloc = new Vec2(xstart,ystart);
		
		Player newplayer = new Player(imgid,startloc, level.getPhysWolrd());
		Body bdlist = level.getPhysWolrd().getBodyList();
		newplayer.setBodyId(bdlist.toString());
		level.setLevelPlayer(newplayer);
		
		//Debug Code
		System.out.println(newplayer.getBody() + " is the players body id");
		System.out.println(newplayer.getBody().getType()+ " is the players body type");
	}
}
