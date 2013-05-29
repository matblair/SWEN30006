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

public class LevelLockLoader {

	/** A simple xml resource loader **/
	/** Main function reads all data and calls sub functions to create the units**/

	/**Constructor doesn't need to do anything **/
	public LevelLockLoader(){	
	}

	/** Load all level unlocks from an input stream
	 * 
	 * @param is the input stream to load from
	 * @param deferred whether we are using deffered loading or not
	 * @return
	 * @throws SlickException
	 */
	public int loadLevelLocks(final InputStream is, final boolean deferred) throws SlickException {
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
				addLevelLock(resourceElement);
				resourcenumber++;
			}
		}
		return resourcenumber;
	}

	/** Parse a resourceElement and add the appropriate level unlock 
	 * given the attributes of the resource element
	 * @param resourceElement
	 */
	private void addLevelLock(Element resourceElement) {
		boolean locked = Boolean.parseBoolean(resourceElement.getAttribute("locked"));
		int id=Integer.parseInt(resourceElement.getAttribute("id"));
		AssetManager.getLevelUnlocks().put(id, locked);
	}
	
	/** Get the current level unlocks and write them to file
	 * 
	 * @throws SlickException
	 */
	public static void saveLevelUnlocks() throws SlickException{	
		File outputfile = new File("assets/xmlresources/levelunlocks.xml");
		OutputStream os = null;
		
		try {
			os = new FileOutputStream(outputfile);
			writeOutput(AssetManager.getLevelUnlocks(), os);
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

	/** Helper function for saving level unlocks, writes an unlock map to a given
	 * output stream
	 * @param map The unlocks to save
	 * @param os The output stream to write to
	 * @throws IOException
	 */
	private static void writeOutput(Map<Integer, Boolean> map, OutputStream os) throws IOException {
		//First write xml header 
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String open = "<resources>\n";
		os.write(header.getBytes(Charset.forName("UTF-8")));
		os.write(open.getBytes(Charset.forName("UTF-8")));		
		
		for(int levelid: map.keySet()){
			String line = "<resource levelid=\"" + levelid + "\" locked=\"" + map.get(levelid) +"\">" + " Level Unlock "+"</resource>\n";
			os.write(line.getBytes(Charset.forName("UTF-8")));
		}
		String close = "</resources>\n";
		os.write(close.getBytes(Charset.forName("UTF-8")));		
		return;
	}
	
}

