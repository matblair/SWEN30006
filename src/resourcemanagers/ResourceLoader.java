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

public class ResourceLoader {
	
	/** A simple xml resource loader **/
	/** Main function reads all data and calls sub functions to create the units**/

	private static ResourceLoader manager;
	/**Constructor doesn't need to do anything **/
	private ResourceLoader(){	
	}
	
	public static ResourceLoader getResourceManagerXML()
	throws SlickException{
		if(manager==null){
			manager = new ResourceLoader();
		}
		return manager;
	}
	
	public void loadResources(final InputStream is) throws SlickException{
	  loadResources(is, false);
	}
		public void loadResources(final InputStream is, final boolean deferred) throws SlickException {
			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = null;
			try {
				docBuilder = docBuilderFactory.newDocumentBuilder();
			} catch (final ParserConfigurationException e) {
				throw new SlickException(Messages.getString("ResourceLoader.ErrorMessage"), e);  
			}
			Document doc = null;
	        try {
				doc = docBuilder.parse (is);
			} catch (final SAXException e) {
				throw new SlickException(Messages.getString("ResourceLoader.ErrorMessage"), e);  
			} catch (final IOException e) {
				throw new SlickException(Messages.getString("ResourceLoader.ErrorMessage"), e);  
			}
	 
			// normalize text representation
	        doc.getDocumentElement ().normalize ();
	 
	        final NodeList listResources = doc.getElementsByTagName(Messages.getString("ResourceLoader.rsrc"));  
	 
	        final int totalResources = listResources.getLength();
	 
	        if(deferred){
	        	LoadingList.setDeferredLoading(true);
	        }
	 
	 
	        for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
	 
	        	final Node resourceNode = listResources.item(resourceIdx);
	 
	        	if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
	        		final Element resourceElement = (Element)resourceNode;
	 
	        		final String type = resourceElement.getAttribute(Messages.getString("ResourceLoader.type"));  
	 
	        		if(type.equals(Messages.getString("ResourceLoader.character"))){  
	        		}else if(type.equals(Messages.getString("ResourceLoader.object_key"))){  
	        		}
	        	}
	        }
	 
		}
}
