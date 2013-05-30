package resourcemanagers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AnimationLoader {
	
	/** An arraylist of images used to create the animation **/
	private static ArrayList<Image> images;
	
	/** The basic class to load an animation of a given duration from an xml file
	 * 
	 * @param path
	 * @param duration
	 * @return anim The animation
	 */
	public static Animation loadAnimation(String path, int duration){
		images = new ArrayList<Image>();
		final File f = new File(path);
		InputStream is = null;
		
		try {
			is = new FileInputStream(f);
			loadImages(is);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final SlickException e) {
			e.printStackTrace();
		} finally	{
			try {
				is.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		Image[] imgarray = images.toArray(new Image[0]);
		Animation anim = new Animation(imgarray, duration, true);
		return anim;
		
	}
	
	/** Adds an image to the array list from the 
	 * asset manager
	 * @param resourceElement
	 */
	public static void addImage(Element resourceElement){
		String imgid = resourceElement.getAttribute("id");
		images.add(AssetManager.requestImage(imgid));
	}
	
	/** Load all the images used to make the animation from the resource 
	 * stream, adding them to the array list
	 * @param is
	 * @throws SlickException
	 */
	public static void loadImages(InputStream is) throws SlickException{
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
		doc.getDocumentElement().normalize();

		final NodeList listResources = doc.getElementsByTagName("resource");  

		final int totalResources = listResources.getLength();

		LoadingList.setDeferredLoading(true);

		for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){

			final Node resourceNode = listResources.item(resourceIdx);

			if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
				final Element resourceElement = (Element)resourceNode;
				addImage(resourceElement);
			}
		}
	}
}
