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


public class InputLoader {

	public InputLoader(){
	}

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
		doc.getDocumentElement ().normalize ();

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
					
				}else if(type.equals("SELECT")){ 
					
				}else if(type.equals("NAVLEFT")){ 

				}else if(type.equals("NAVRIGHT")){ 

				}else if(type.equals("NAVDOWN")){ 

				}else if(type.equals("NAVUP")){ 

				}else if(type.equals("LEFT")){ 

				}else if(type.equals("RIGHT")){ 
					
				}else if(type.equals("UP")){ 
					
				}else if(type.equals("DOWN")){ 
					
				}else if(type.equals("JUMP")){ 
					
				}else if(type.equals("PAUSE")){ 
					
				}else if(type.equals("SHOOT")){ 
					
				}else if(type.equals("INTERACT")){
					
				}
			}
		}
	}
}
