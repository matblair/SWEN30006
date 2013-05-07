package resourcemanagers;

import gameengine.Achievement;

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

public class AchievementLoader {

	/** A simple xml resource loader **/
	/** Main function reads all data and calls sub functions to create the units**/

	/**Constructor doesn't need to do anything **/
	public AchievementLoader(){	
	}

	public int loadAchievements(final InputStream is, final boolean deferred) throws SlickException {
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

		int resourcenumber=0;

		for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){

			final Node resourceNode = listResources.item(resourceIdx);

			if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
				final Element resourceElement = (Element)resourceNode;
				addAchievement(resourceElement);
			}
		}
		return resourcenumber;
	}

	private void addAchievement(Element resourceElement) {
		String imgid = resourceElement.getAttribute("id");
		String achid = resourceElement.getAttribute("achid");
		String name = resourceElement.getAttribute("name");
		String description = resourceElement.getTextContent();
		boolean unlocked = Boolean.parseBoolean(resourceElement.getAttribute("unlocked"));
		AssetManager.getAchievementMap().put(achid, new Achievement(name, description, unlocked, imgid));
	}
}