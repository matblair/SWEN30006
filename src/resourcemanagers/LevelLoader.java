package resourcemanagers;

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

	/** A basic xml data loader from the slick code tutorials **/
	public void loadResources(final InputStream is) throws SlickException{
		loadResources(is, false);
	}
	public void loadResources(final InputStream is, final boolean deferred) throws SlickException {
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
					addElementAsCube(resourceElement);
				}else if(type.equals("WALL")){  
					addElementAsWall(resourceElement);
				}else if(type.equals("TURRET")){  
					addElementAsTurret(resourceElement);
				}else if(type.equals("PLATFORM")){  
					addElementAsPlatform(resourceElement);
				}else if(type.equals("PORTAL")){  
					addElementAsPortal(resourceElement);
				}else if(type.equals("SWITCH")){  
					addElementAsSwitch(resourceElement);
				}
			}
		}
	}

	private void addElementAsSwitch(Element resourceElement) {
		// TODO Auto-generated method stub

	}

	private void addElementAsPortal(Element resourceElement) {
		// TODO Auto-generated method stub

	}

	private void addElementAsPlatform(Element resourceElement) {
		// TODO Auto-generated method stub

	}

	private void addElementAsTurret(Element resourceElement) {
		// TODO Auto-generated method stub

	}

	private void addElementAsWall(Element resourceElement) {
		// TODO Auto-generated method stub

	}

	private void addElementAsCube(Element resourceElement) {
		// TODO Auto-generated method stub

	}
}
