package resourcemanagers;

import gameengine.HighScore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

public class HighScoreLoader {
	
	public HighScoreLoader(){
		
	}
	
	public int loadHighScores(final InputStream is, final boolean deferred) throws SlickException {
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
				addHighScore(resourceElement);
			}
		}
		return resourcenumber;
	}

	private void addHighScore(Element resourceElement) {
		int levelid = Integer.parseInt(resourceElement.getAttribute("levelid"));
		
		// Create the array list for that level if it doesn't exist.
		if(!AssetManager.getHighscores().containsKey(levelid)){
			ArrayList<HighScore> newarray = new ArrayList<HighScore>();
			AssetManager.getHighscores().put(levelid,newarray);
		}
		
		String name = resourceElement.getAttribute("name");
		float score = Float.parseFloat(resourceElement.getAttribute("score"));
		AssetManager.getHighscores().get(levelid).add(new HighScore(name,score,levelid));

	}
	

}
