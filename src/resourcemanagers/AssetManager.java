package resourcemanagers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
	private static Map<String, Image> imageResources; //All images used for game objects
	private static Map<String, Sound> soundResources; //All sounds resources
	private static Map<String, Vec2> vectorResources; //All vector locations
	private static Map<String, Image> uiElementResources; //All UI Resources
	private static Map<String, String> levelXmlResources; //All the level xml resources
	private static Map<String, Font> fontResources; //All the level xml resources
	private static Map<String, Animation> animationResources; //All the level xml resources
	
	//General File Locations
	private static final String loadinglist ="loadinglist.xml";
	private static final String generalresource = "assets/xmlresources/";

	
	private AssetManager(){	
		// Create all the static variables so that they are not null.
		resourceLoader = new ResourceLoader();
		levelLoader = new LevelLoader();
		imageResources = new HashMap<String, Image>();
		soundResources = new HashMap<String, Sound>();
		vectorResources = new HashMap<String, Vec2>();
		uiElementResources = new HashMap<String, Image>();
		levelXmlResources = new HashMap<String, String>();
		fontResources = new HashMap<String, Font>();
		animationResources = new HashMap<String, Animation>();
	}

	public static AssetManager getAssetManager()
			throws SlickException{
		if(manager==null){
			manager = new AssetManager();
		}
		return manager;
	}

	public void loadAllGameAssets(){
		final File f = new File(loadinglist, generalresource);
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
			final File f = new File(loadinglist, resourcepath);
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
	
	public static Image requestImage(String imgid){
		return imageResources.get(imgid);
	}

	public static Vec2 requestVec(String vecid){
		return vectorResources.get(vecid);
	}
	
	public static Sound requestSound(String soundid){
		return soundResources.get(soundid);
	}
	
	public static Image requestUIElement(String uiid){
		return uiElementResources.get(uiid);
	}
	public static String requestLevelXMLPath(String levelid){
		return levelXmlResources.get(levelid);
	}
	
	public static Font requestFontResource(String fontid){
		return fontResources.get(fontid);
	}
	
	public static Animation requestAnimationResources(String animid){
		return animationResources.get(animid);
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public LevelLoader getLevelLoader() {
		return levelLoader;
	}

	public static Map<String, Image> getImageResources() {
		return imageResources;
	}

	public static Map<String, Sound> getSoundResources() {
		return soundResources;
	}

	public static Map<String, Vec2> getVectorResources() {
		return vectorResources;
	}

	public static Map<String, Image> getUiElementResources() {
		return uiElementResources;
	}

	public static Map<String, String> getLevelXmlResources() {
		return levelXmlResources;
	}

	public static Map<String, Font> getFontResources() {
		return fontResources;
	}

	public static Map<String, Animation> getAnimationResources() {
		return animationResources;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setLevelLoader(LevelLoader levelLoader) {
		this.levelLoader = levelLoader;
	}

	public static void setImageResources(Map<String, Image> imageResources) {
		AssetManager.imageResources = imageResources;
	}

	public static void setSoundResources(Map<String, Sound> soundResources) {
		AssetManager.soundResources = soundResources;
	}

	public static void setVectorResources(Map<String, Vec2> vectorResources) {
		AssetManager.vectorResources = vectorResources;
	}

	public static void setUiElementResources(Map<String, Image> uiElementResources) {
		AssetManager.uiElementResources = uiElementResources;
	}

	public static void setLevelXmlResources(Map<String, String> levelXmlResources) {
		AssetManager.levelXmlResources = levelXmlResources;
	}

	public static void setFontResources(Map<String, Font> fontResources) {
		AssetManager.fontResources = fontResources;
	}

	public static void setAnimationResources(
			Map<String, Animation> animationResources) {
		AssetManager.animationResources = animationResources;
	}
	
}

