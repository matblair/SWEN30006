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


public class Stateloader {

	public static String WASTELAND_ID = "WASTELANDSTATE"; //$NON-NLS-1$
	public static String WETLAND_ID = "WETLANDSTATE"; //$NON-NLS-1$
	public static String HOMEWORLD_ID = "HOMEWORLD"; //$NON-NLS-1$
	public static String DESERT_ID = "DESERTWORLDSTATE"; //$NON-NLS-1$
	public static String EASTLAND_ID = "EASTLANDSTATE"; //$NON-NLS-1$
	public static String MAIN_ID = "MAINWORLDSTATE"; //$NON-NLS-1$
	
	public Stateloader(){
	}

	/** A basic xml data loader from the slick code tutorials **/
	public void loadResources(final InputStream is, final ResourceLoader assets) throws SlickException{
		loadResources(is, false, assets);
	}
	public void loadResources(final InputStream is, final boolean deferred, final ResourceLoader assets) throws SlickException {
		final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new SlickException(Messages.getString("Stateloader.error"), e); //$NON-NLS-1$
		}
		Document doc = null;
		try {
			doc = docBuilder.parse (is);
		} catch (final SAXException e) {
			throw new SlickException(Messages.getString("Stateloader.Errormessage"), e); //$NON-NLS-1$
		} catch (final IOException e) {
			throw new SlickException(Messages.getString("Stateloader.error_2"), e); //$NON-NLS-1$
		}

		// normalize text representation
		doc.getDocumentElement ().normalize ();

		final NodeList listResources = doc.getElementsByTagName(Messages.getString("Stateloader.rsrc")); //$NON-NLS-1$

		final int totalResources = listResources.getLength();

		if(deferred){
			LoadingList.setDeferredLoading(true);
		}

		for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
			final Node resourceNode = listResources.item(resourceIdx);
			if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
				final Element resourceElement = (Element)resourceNode;
				final String type = resourceElement.getAttribute(Messages.getString("Stateloader.type")); //$NON-NLS-1$
				if(type.equals(Messages.getString("Stateloader.aggressive"))){ //$NON-NLS-1$
				}else if(type.equals(Messages.getString("Stateloader.object"))){ //$NON-NLS-1$
				}
			}
		}
	}
}
