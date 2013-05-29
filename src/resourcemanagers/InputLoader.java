package resourcemanagers;

import gameengine.InputManager;

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


public class InputLoader {

	/** Constructor, isn't required to do anything **/
	public InputLoader(){
	}

	/** Load all input mappings for a given input stream 
	 *  assigning the correct key maps to the input manager
	 * @param is
	 * @param deferred
	 * @throws SlickException
	 */
	public void loadInput(final InputStream is, final boolean deferred) throws SlickException {
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
		doc.getDocumentElement().normalize ();

		final NodeList listResources = doc.getElementsByTagName("resource");  

		final int totalResources = listResources.getLength();
		System.out.println(totalResources + " total resources");
		if(deferred){
			LoadingList.setDeferredLoading(true);
		}

		for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
			final Node resourceNode = listResources.item(resourceIdx);
			if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
				final Element resourceElement = (Element)resourceNode;
				final String type = resourceElement.getAttribute("type");  
				System.out.println(type);
				if(type.equals("BACK")){  
					InputManager.BACK = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("SELECT")){ 
					InputManager.SELECT = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));
				}else if(type.equals("NAVLEFT")){ 
					InputManager.NAV_LEFT = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("NAVRIGHT")){ 
					InputManager.NAV_RIGHT = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("NAVDOWN")){ 
					InputManager.NAV_DOWN = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("NAVUP")){ 
					InputManager.NAV_UP = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("LEFT")){ 
					InputManager.MOVE_LEFT = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("RIGHT")){ 
					InputManager.MOVE_RIGHT = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("JUMP")){ 
					InputManager.JUMP = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("PAUSE")){ 
					InputManager.PAUSE = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("SHOOT_BLUE")){ 
					InputManager.SHOOT_BLUE = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("SHOOT_ORANGE")){ 
					InputManager.SHOOT_ORANGE = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}else if(type.equals("INTERACT")){
					InputManager.INTERACT = Integer.parseInt(resourceElement.getAttribute("key"));
					System.out.println(resourceElement.getAttribute("key"));

				}
			}
		}
	}
}
