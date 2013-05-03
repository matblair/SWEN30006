package resourcemanagers;

import gameworlds.Level;

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
	private static  ResourceLoader resourceLoader = new ResourceLoader();
	/** The level loader **/
	private static LevelLoader levelLoader = new LevelLoader();

	/**Creates a resource repository of commonly used items **/
	private static Map<String, Image> imageResources = new HashMap<String, Image>(); //All images used for game objects
	private static Map<String, Sound> soundResources = new HashMap<String, Sound>(); //All sounds resources
	private static Map<String, Vec2> vectorResources = new HashMap<String, Vec2>(); //All vector locations
	private static Map<String, Image> uiElementResources = new HashMap<String, Image>(); //All UI Resources
	private static Map<String, String> 	levelXmlResources = new HashMap<String, String>(); //All the level xml resources
	private static Map<String, Font> fontResources = new HashMap<String, Font>(); //All the level xml resources
	private static Map<String, Animation> animationResources = new HashMap<String, Animation>();; //All the level xml resources

	private static int totalresources=0;

	//General File Locations
	private static final String loadinglist ="loadinglist.xml";
	private static final String generalresource = "assets/xmlresources/";


	private AssetManager(){				
	}

	public static AssetManager getAssetManager()
			throws SlickException{
		if(manager==null){
			manager = new AssetManager();
		}
		return manager;
	}

	public static void loadAllGameAssets(){
		final File f = new File(generalresource, loadinglist);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			totalresources=resourceLoader.loadResources(is, true);
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
		System.out.println(imageResources.size() +" images loaded.");
		System.out.println(soundResources.size() +" sounds loaded.");
		System.out.println(vectorResources.size() +" vectors loaded.");
		System.out.println(uiElementResources.size() +" ui elements loaded.");
		System.out.println(levelXmlResources.size() +" xml levels loaded.");
		System.out.println(fontResources.size() +" fonts loaded.");
		System.out.println(animationResources.size() +" animations loaded.");
		System.out.println(totalresources +" resources loaded in total");
	}

	public static Level loadLevel( final String levelid) throws SlickException{
		String levelxml = AssetManager.requestLevelXMLPath(levelid);
		Level level = new Level();

		// Have to set resource path based on level id, hash map of level ids?
		final File f = new File(generalresource, levelxml);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			levelLoader.loadLevel(is, true, level);
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

		return level;
	}

	public static Level loadTestLevel() throws SlickException{
		Level level = new Level();
		levelLoader.loadTestLevel(level);
		return level;
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
		AssetManager.resourceLoader = resourceLoader;
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

