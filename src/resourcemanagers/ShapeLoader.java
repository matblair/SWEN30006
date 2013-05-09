package resourcemanagers;

import gameengine.PhysUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ShapeLoader {
	
	private static ArrayList<Vec2> vertices;
	
	public static void addVertex(Element resourceElement){
		float xloc = Float.parseFloat(resourceElement.getAttribute("xloc"));
		float yloc = Float.parseFloat(resourceElement.getAttribute("yloc"));
		Vec2 newvertex = PhysUtils.SlickToJBoxVec(new Vec2(xloc,yloc));
		vertices.add(newvertex);

	}
	
	public static PolygonShape loadShapes(String vertpath){
		vertices = new ArrayList<Vec2>();
		String generalresource = "assets/xmlresources/vertices/";
		// Have to set resource path based on level id, hash map of level ids?
		final File f = new File(generalresource, vertpath);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			loadVertices(is);
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
		
		PolygonShape newshape = new PolygonShape();
		Vec2[] vecarray = new Vec2[vertices.size()];
		newshape.set(vertices.toArray(vecarray), vertices.size());
		return newshape;
		
	}
	
	public static void loadVertices(InputStream is) throws SlickException{
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

		LoadingList.setDeferredLoading(true);

		for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){

			final Node resourceNode = listResources.item(resourceIdx);

			if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
				final Element resourceElement = (Element)resourceNode;
				addVertex(resourceElement);
			}
		}
	}
}
