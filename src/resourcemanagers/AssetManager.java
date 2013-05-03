package resourcemanagers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class AssetManager {
	/** End Role will be to load all resources at start of game that are universal, call level loader to load new levels **/
	private static AssetManager manager;
	/** The resource loader **/
	private  ResourceLoader resourceLoader;
	/** The level loader **/
	private LevelLoader levelLoader;

	/**Creates a resource repository of commonly used items **/
	private Map<String, Image> imageResources; //All images used for game objects
	private Map<String, Sound> soundResources; //All sounds resources
	private Map<String, Vec2> vectorResources; //All vector locations
	private Map<String, Image> uiElementResources; //All UI Resources
	private Map<String, String> levelXmlResources; //All the level xml resources
	private Map<String, Font> fontResources; //All the level xml resources
	private Map<String, Animation> animationResources; //All the level xml resources
	/** Added some comments to test merging **/
	
	//General File Locations
	@SuppressWarnings("unused")
	private static final String xmlresources = "assets/xmlresources/";
	private static final String generalresource = "assets/";
	private static final String xmlpath ="";

	// World loader
	private static LevelLoader loader;

	private AssetManager(){	
		loader = new LevelLoader();
		resourceLoader = new ResourceLoader();
		levelLoader = new LevelLoader();
		//Initialise all the maps.
	}

	public static AssetManager getResourceManager()
			throws SlickException{
		if(manager==null){
			manager = new AssetManager();
		}
		return manager;
	}

	public void loadAllGameAssets(){
		final File f = new File(xmlpath, generalresource);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			resourceLoader.loadResources(is, true);
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
	}
	
	public void loadLevel( final int levelid){
		String resourcepath = "";
		// Have to set resource path based on level id, hash map of level ids?
		if(resourcepath!=null){
			final File f = new File(xmlpath, resourcepath);
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				levelLoader.loadResources(is, true);

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
		}
	}
	
	public Image requestImage(String imgid){
		return imageResources.get(imgid);
	}

	public Vec2 requestVec(String vecid){
		return vectorResources.get(vecid);
	}
	
	public Sound requestSound(String soundid){
		return soundResources.get(soundid);
	}
	
	public Image requestUIElement(String uiid){
		return uiElementResources.get(uiid);
	}
	public String requestLevelXMLPath(String levelid){
		return levelXmlResources.get(levelid);
	}
	
	public Font requestFontResource(String fontid){
		return fontResources.get(fontid);
	}
	
	public Animation requestAnimationResources(String animid){
		return animationResources.get(animid);
	}
	
}

