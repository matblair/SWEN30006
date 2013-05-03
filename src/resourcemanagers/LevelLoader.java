package resourcemanagers;

import gameengine.PhysUtils;
import gameobjects.Player;
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

	public void loadTestLevel(Level level) throws SlickException{
		//Physics Walls
		PhysUtils.addWall(level.getPhysWolrd(), 0, 0, 100, 1); //floor
		PhysUtils.addWall(level.getPhysWolrd(), 0, 0, 0.5f, 20); //left wall
		PhysUtils.addWall(level.getPhysWolrd(), 0, 17.14f, 100, 0); //top
		PhysUtils.addWall(level.getPhysWolrd(), 10, 2.5f, 4, 0.25f);//first thing
		PhysUtils.addWall(level.getPhysWolrd(), 14, 3.95f, 4, 0.25f);//second thing
		PhysUtils.addWall(level.getPhysWolrd(), 19.5f, 0, 0.5f, 20); //right wall
	
		// Dynamic Body
		Player player = new Player("CHELLSPRITE",new Vec2(2, 5), level.getPhysWolrd());
		level.setLevelPlayer(player);
		
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

		final NodeList listResources = doc.getElementsByTagName("Could not load resources");  

		final int totalResources = listResources.getLength();

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

				if(type.equals("CUBE")){  
					addElementAsCube(resourceElement,level);
				}else if(type.equals("WALL")){  
					addElementAsWall(resourceElement,level);
					walls++;
				}else if(type.equals("TURRET")){  
					addElementAsTurret(resourceElement,level);
				}else if(type.equals("PLATFORM")){  
					addElementAsPlatform(resourceElement,level);
				}else if(type.equals("PORTAL")){  
					addElementAsPortal(resourceElement,level);
				}else if(type.equals("SWITCH")){  
					addElementAsSwitch(resourceElement,level);
				}else if(type.equals("BACKGROUNDIMG")){
					level.setBg(new Image(resourceElement.getTextContent()));
					backgroundimg++;
				}else if(type.equals("PLAYER")){
					addElementAsPlayer(resourceElement,level);
					players++;
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

	private void addElementAsWall(Element resourceElement, Level level) {
		Float startx = Float.parseFloat(resourceElement.getAttribute("xStart"));
		Float starty = Float.parseFloat(resourceElement.getAttribute("yStart"));
		Float width = Float.parseFloat(resourceElement.getAttribute("width"));
		Float height = Float.parseFloat(resourceElement.getAttribute("height"));
		PhysUtils.addWall(level.getPhysWolrd(), startx, starty, width, height); //floor

	}

	private void addElementAsCube(Element resourceElement, Level level) {

	}
	
	private void addElementAsPlayer(Element resourceElement, Level level) throws SlickException{
		String imgid = resourceElement.getAttribute("imgid");
		Float xstart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Float ystart = Float.parseFloat(resourceElement.getAttribute("startx"));
		Vec2 startloc = new Vec2(xstart,ystart);
		Player newplayer = new Player(imgid,startloc, level.getPhysWolrd());
		level.setLevelPlayer(newplayer);
	}
}
