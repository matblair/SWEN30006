package resourcemanagers;

import gameengine.Sound;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jbox2d.collision.shapes.Shape;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ResourceLoader {

	/** A simple xml resource loader **/
	/** Main function reads all data and calls sub functions to create the units**/
	/**Constructor doesn't need to do anything **/
	public ResourceLoader(){	
	}

	/** Load all resources into the array lists in the asset manager by parsing a given input stream 
	 *  and calling the requisite delgate functions
	 * @param is
	 * @param deferred
	 * @return
	 * @throws SlickException
	 */
	public int loadResources(final InputStream is, final boolean deferred) throws SlickException {
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

				final String type = resourceElement.getAttribute("type");  

				if(type.equals("IMAGE")){ 
					resourcenumber++;
					addElementAsImage(resourceElement);
				}else if(type.equals("SOUND")){  
					addElementAsSound(resourceElement);
					resourcenumber++;
				}else if(type.equals("UIELEMENT")){  
					addElementAsUIElement(resourceElement);
					resourcenumber++;
				}else if(type.equals("LEVELXML")){  
					addElementAsLevelXML(resourceElement);
					resourcenumber++;
				}else if(type.equals("ANIMATION")){
					addElementAsAnimation(resourceElement);
					resourcenumber++;
				}else if(type.equals("FONT")){
					addElementAsFont(resourceElement);
					resourcenumber++;
				}else if(type.equals("INPUTXML")){
					addElementAsInputMap(resourceElement);
					resourcenumber++;
				}else if(type.equals("ACHIMAGE")){
					addElementAsAchievementImage(resourceElement);
				}else if(type.equals("SHAPE")){
					addElementAsShape(resourceElement);
				}
			}
		}
		return resourcenumber;
	}

	/** Add the xml file for a particular input mapping to the Asset Manager
	 * 
	 * @param resourceElement
	 */
	private void addElementAsInputMap(Element resourceElement) {	
		String xml = resourceElement.getTextContent();
		int id = Integer.parseInt(resourceElement.getAttribute("id"));
		AssetManager.getInputResources().put(id,xml);
	}

	/** Add the level xml file parsed from the resource element to the asset manager
	 * 
	 * @param resourceElement
	 */
	private void addElementAsLevelXML(Element resourceElement) {	
		String xml=resourceElement.getTextContent();
		boolean locked = Boolean.parseBoolean(resourceElement.getAttribute("locked"));
		int id=Integer.parseInt(resourceElement.getAttribute("id"));
		AssetManager.getLevelUnlocks().put(id,locked);
		AssetManager.getLevelXmlResources().put(id, xml);
	}
	
	/** Add an image to the asset manager by parsing the resource element
	 * 
	 * @param resourceElement
	 * @throws DOMException
	 * @throws SlickException
	 */
	private void addElementAsImage(Element resourceElement) throws DOMException, SlickException {
		Image newimg= new Image(resourceElement.getTextContent());
		String imgid= resourceElement.getAttribute("id");
		AssetManager.getImageResources().put(imgid, newimg);
	}
	
	/** Add an achievement image to the correct hash map by parsing the resource element
	 * 
	 * @param resourceElement
	 * @throws DOMException
	 * @throws SlickException
	 */
	private void addElementAsAchievementImage(Element resourceElement) throws DOMException, SlickException {
		Image newimg= new Image(resourceElement.getTextContent());
		String imgid= resourceElement.getAttribute("id");
		AssetManager.getAchievementResources().put(imgid, newimg);
	}

	/** Add a .ogg sound file to the Asset Manager by parsing the resource element 
	 * 
	 * @param resourceElement
	 * @throws DOMException
	 * @throws SlickException
	 */
	private void addElementAsSound(Element resourceElement) throws DOMException, SlickException {
		Sound sound = new Sound (resourceElement.getTextContent(), resourceElement.getAttribute("style"));
		String id = resourceElement.getAttribute("id");
		AssetManager.getSoundResources().put(id, sound);
	}

	/** Add a user interface element by parsing the resource element and adding it to the correct hash map
	 * 
	 * @param resourceElement
	 * @throws DOMException
	 * @throws SlickException
	 */
	private void addElementAsUIElement(Element resourceElement) throws DOMException, SlickException {
		Image newimg=new Image(resourceElement.getTextContent());
		String uiid=resourceElement.getAttribute("id");
		AssetManager.getUiElementResources().put(uiid, newimg);
	}
	
	/** Add the requested animation to the hash map by delegating to animation loader with the appropraite path
	 * and then placing the returned animation in the hash map
	 * 
	 * @param resourceElement
	 */
	private void addElementAsAnimation(Element resourceElement) {
		String animpath=resourceElement.getTextContent();
		String animid=resourceElement.getAttribute("id");
		int duration=Integer.parseInt(resourceElement.getAttribute("duration"));
		Animation newanim=AnimationLoader.loadAnimation(animpath, duration);
		AssetManager.getAnimationResources().put(animid, newanim);
	}
	
	/** Parse the resource element and use the font loader to load a font
	 * and add it to the appropriate hash map
	 * @param resourceElement
	 * @throws DOMException
	 * @throws SlickException
	 */
	private void addElementAsFont(Element resourceElement) throws DOMException, SlickException {
		float fontsize = (float)Integer.parseInt(resourceElement.getAttribute("fontsize"));
		Font font= FontLoader.loadFont(resourceElement.getTextContent(), fontsize);
		String fontid=resourceElement.getAttribute("id");
		AssetManager.getFontResources().put(fontid, font);
	}
	
	/** Use the shape loader to load a shape from a given vertices xml path then
	 * add that shape to the correct hash map
	 * 
	 * @param resourceElement
	 * @throws DOMException
	 * @throws SlickException
	 */
	private void addElementAsShape(Element resourceElement) throws DOMException, SlickException {
		String shapeid = resourceElement.getAttribute("id");
		String vertpath = resourceElement.getAttribute("vertpath");
		Shape shapecreate = ShapeLoader.loadShapes(vertpath);
		AssetManager.getShapes().put(shapeid,shapecreate);	
	}
}
