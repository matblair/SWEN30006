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

import scoringsystem.Achievement;
import scoringsystem.CubesAchievement;
import scoringsystem.DistanceWalkedAchievement;
import scoringsystem.FallingAchievement;
import scoringsystem.JumpAchievement;
import scoringsystem.NumberOfPortalsAchievement;
import scoringsystem.TimeFallingAchievement;
import scoringsystem.TimingAchievement;
import scoringsystem.VelocityAchievement;


public class AchievementLoader {

	/** A simple xml resource loader **/
	/** Main function reads all data and calls sub functions to create the units**/

	/**Constructor doesn't need to do anything **/
	public AchievementLoader(){	
	}

	/** Load all achievements from an input stream
	 * 
	 * @param is the input stream to load from
	 * @param deferred whether we are using deffered loading or not
	 * @return
	 * @throws SlickException
	 */
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
				resourcenumber++;
			}
		}
		return resourcenumber;
	}

	/** Parse a resourceElement and add the appropriate achievement 
	 * given the attributes of the resource element
	 * @param resourceElement
	 */
	private void addAchievement(Element resourceElement) {
		String imgid = resourceElement.getAttribute("id");
		int levelid = Integer.parseInt(resourceElement.getAttribute("levelid"));
		String achid = resourceElement.getAttribute("achid");
		String name = resourceElement.getAttribute("name");
		String type = resourceElement.getAttribute("actype");
		float target = Float.parseFloat(resourceElement.getAttribute("target"));
		String description = resourceElement.getTextContent();
		boolean unlocked = Boolean.parseBoolean(resourceElement.getAttribute("unlocked"));
		boolean persistance = Boolean.parseBoolean(resourceElement.getAttribute("persistant"));
		
		if(type.equals("timefallen")){
			AssetManager.getAchievementMap().put(achid, new TimeFallingAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}else if(type.equals("distwalked")){
			AssetManager.getAchievementMap().put(achid, new DistanceWalkedAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}else if(type.equals("jumps")){
			AssetManager.getAchievementMap().put(achid, new JumpAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}else if(type.equals("portals")){
			AssetManager.getAchievementMap().put(achid, new NumberOfPortalsAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}else if(type.equals("velocity")){
			AssetManager.getAchievementMap().put(achid, new VelocityAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}else if(type.equals("timelevel")){
			AssetManager.getAchievementMap().put(achid, new TimingAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}else if(type.equals("cubesused")){
			AssetManager.getAchievementMap().put(achid, new CubesAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}else if(type.equals("distfallen")){
			AssetManager.getAchievementMap().put(achid, new FallingAchievement(name, description, unlocked, imgid, levelid, target,persistance));
		}
	}
	
	/** Get the current achievements and unlocks and write them to file
	 * 
	 * @throws SlickException
	 */
	public static void saveAchievements() throws SlickException{	
		File outputfile = new File("assets/xmlresources/achievements.xml");
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

	/** Helper function for saving achievements, writes an achievement map to a given
	 * output stream
	 * @param map The achievements to save
	 * @param os The output stream to write to
	 * @throws IOException
	 */
	private static void writeOutput(Map<String, Achievement> map, OutputStream os) throws IOException {
		//First write xml header 
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String open = "<resources>\n";
		os.write(header.getBytes(Charset.forName("UTF-8")));
		os.write(open.getBytes(Charset.forName("UTF-8")));		
		
		for(String achid: map.keySet()){
			Achievement towrite = map.get(achid);
			String line = "<resource levelid=\"" + towrite.getLevelId() + "\" id=\"" + towrite.getImgid() + "\" achid=\"" + achid +  "\" target=\"" + towrite.getTarget() + "\" actype=\"" +towrite.getActype() + "\" name=\"" + towrite.getName() + "\" persistant=\"" + towrite.isPersistant() + "\" unlocked=\"" + towrite.isUnlocked() + "\">" +towrite.getDescription() +"</resource>\n";
			os.write(line.getBytes(Charset.forName("UTF-8")));
		}
		String close = "</resources>\n";
		os.write(close.getBytes(Charset.forName("UTF-8")));		
		return;
	}
	
}