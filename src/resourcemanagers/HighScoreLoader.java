package resourcemanagers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

import scoringsystem.HighScore;


public class HighScoreLoader {

	private static Object synlock;

	public HighScoreLoader(){
		synlock = new Object();
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
				resourcenumber++;
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

	public static void saveHighScores() throws SlickException{	
		synchronized (synlock){
			File outputfile = new File("assets/xmlresources/highscores.xml");
			OutputStream os = null;

			try {
				os = new FileOutputStream(outputfile);
				writeOutput(AssetManager.getHighscores(), os);
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
		}

		return;
	}

	private static void writeOutput(Map<Integer, ArrayList<HighScore>> highscores, OutputStream os) throws IOException {

		//First write xml header 
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String open = "<resources>\n";
		os.write(header.getBytes(Charset.forName("UTF-8")));
		os.write(open.getBytes(Charset.forName("UTF-8")));		

		for(int i=0; i<highscores.size(); i++){
			ArrayList<HighScore> hs = highscores.get(i);
			if(hs!=null){
				for(HighScore score: hs){
					String line = "<resource levelid=\"" + i + "\" name=\"" + score.getName() + "\" score=\"" + score.getScore() + "\">highscore</resource>\n";
					os.write(line.getBytes(Charset.forName("UTF-8")));
				}
			}
		}
		String close = "</resources>\n";
		os.write(close.getBytes(Charset.forName("UTF-8")));		

		return;
	}


}
