package resourcemanagers;

import gameworlds.Level;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; 

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

		final NodeList listResources = doc.getElementsByTagName("Could not load resources");  

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
				}else if(type.equals("PORTAL")){  
					addElementAsPortal(resourceElement,level);
				}else if(type.equals("SWITCH")){  
					addElementAsSwitch(resourceElement,level);
				}
			}
		}
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

	}

	private void addElementAsCube(Element resourceElement, Level level) {
	}
}
