package resourcemanagers;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

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

import Achievements.Achievement;
import Achievements.BasicAchievement;

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
		AssetManager.getAchievementMap().put(achid, new BasicAchievement(name, description, unlocked, imgid));
	}
	
	public static void saveAchievements() throws SlickException{	
		File outputfile = new File("assets/xmlresources/achievement.xml");
		OutputStream os = null;
		
		try {
			os = new FileOutputStream(outputfile);
			writeOutput(AssetManager.getAchievementMap(), os);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally	{
			try {
				os.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		return;
	}

	private static void writeOutput(Map<String, Achievement> map, OutputStream os) throws IOException {
		//First write xml header 
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String open = "<resources>\n";
		os.write(header.getBytes(Charset.forName("UTF-8")));
		os.write(open.getBytes(Charset.forName("UTF-8")));		
		
		for(String achid: map.keySet()){
			Achievement towrite = map.get(achid);
			String line = "<resource levelid=\"" + achid + "\" id=\"" + towrite.getImgid() + "\" name=\"" + towrite.getName() + "\" unlocked=\"" + towrite.isUnlocked() + "\">" +towrite.getDescription() +"</resource>\n";
			os.write(line.getBytes(Charset.forName("UTF-8")));
		}
		String close = "</resources>\n";
		os.write(close.getBytes(Charset.forName("UTF-8")));		

		return;
	}
	
}