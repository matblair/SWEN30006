package resourcemanagers;

import gameengine.Achievement;
import gameengine.HighScore;
import gameworlds.Level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jbox2d.collision.shapes.Shape;
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
	/** The input manager **/
	private static InputLoader inputloader = new InputLoader();
	/** The achievement loader **/
	private static AchievementLoader achievementLoader = new AchievementLoader();
	/** The highscore loader **/
	private static HighScoreLoader highscoreLoader = new HighScoreLoader();;
	

	/**Creates a resource repository of commonly used items **/
	private static Map<String, Image> imageResources = new HashMap<String, Image>(); //All images used for game objects
	private static Map<String, Sound> soundResources = new HashMap<String, Sound>(); //All sounds resources
	private static Map<String, Vec2> vectorResources = new HashMap<String, Vec2>(); //All vector locations
	private static Map<String, Image> uiElementResources = new HashMap<String, Image>(); //All UI Resources
	private static Map<Integer, String> levelXmlResources = new HashMap<Integer, String>(); //All the level xml resources
	private static Map<Integer, String> inputXmlResources = new HashMap<Integer, String>(); //All the level xml resources
	private static Map<String, Font> fontResources = new HashMap<String, Font>(); //All the level xml resources
	private static Map<String, Animation> animationResources = new HashMap<String, Animation>();; //All the level xml resources
	private static Map<String, Image> achievementResources = new HashMap<String, Image>(); //All images used for game objects
	private static Map<String, Achievement> achievements = new HashMap<String, Achievement>(); //All images used for game objects
	private static Map<Integer, ArrayList<HighScore>> highscores = new HashMap<Integer, ArrayList<HighScore>>();
	private static Map<String, Shape> shapeDefinitions = new HashMap<String, Shape>();
	private static Map<Integer, Boolean> levelUnlocks = new HashMap<Integer,Boolean>();

	
	private static int totalresources=0;

	//General File Locations
	private static final String loadinglist ="loadinglist.xml";
	private static final String generalresource = "assets/xmlresources/";
	private static final String achievementxml = "achievements.xml";
	private static final String highscorexml = "highscores.xml";
	


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
		
		loadAchievements();
		loadHighScores();
		
		System.out.println(imageResources.size() +" images loaded.");
		System.out.println(soundResources.size() +" sounds loaded.");
		System.out.println(vectorResources.size() +" vectors loaded.");
		System.out.println(uiElementResources.size() +" ui elements loaded.");
		System.out.println(levelXmlResources.size() +" xml levels loaded.");
		System.out.println(inputXmlResources.size() +" input xml loaded.");
		System.out.println(fontResources.size() +" fonts loaded.");
		System.out.println(animationResources.size() +" animations loaded.");
		System.out.println(shapeDefinitions.size() + " shapes loaded");
		System.out.println(totalresources +" resources loaded in total");
	}

	public static void loadAchievements(){
		final File f = new File(generalresource, achievementxml);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			achievementLoader.loadAchievements(is, false);
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
	
	public static void loadHighScores(){
		final File f = new File(generalresource, highscorexml);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			highscoreLoader.loadHighScores(is, false);
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
	
	public static Level loadLevel(final int levelid) throws SlickException{
		String levelxml = AssetManager.requestLevelXMLPath(levelid);
		Level level = new Level();

		// Have to set resource path based on level id, hash map of level ids?
		final File f = new File(generalresource, levelxml);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			levelLoader.loadLevel(is, false, level);
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
	
	public static void loadInput(int inputtype) throws SlickException{
		String inputxml = AssetManager.requestInputXMLPath(inputtype);
		System.out.println(inputxml);
		// Have to set resource path based on level id, hash map of level ids?
		final File f = new File(generalresource, inputxml);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			inputloader.loadInput(is, false);
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

	public static Image requestImage(String imgid){
		return imageResources.get(imgid);
	}
	public static Image requestAchiemeventResource(String imgid){
		return achievementResources.get(imgid);
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
	public static String requestLevelXMLPath(int levelid){
		return levelXmlResources.get(levelid);
	}
	public static String requestInputXMLPath(int inputid){
		return inputXmlResources.get(inputid);
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
	
	public static Map<String, Image> getAchievementResources() {
		return achievementResources;
	}

	public static Collection<Achievement> getAchievements() {
		return achievements.values();
	}
	
	public static Map<String, Achievement> getAchievementMap() {
		return achievements;
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

	public static Map<Integer, String> getLevelXmlResources() {
		return levelXmlResources;
	}
	public static Map<Integer, String> getInputResources() {
		return inputXmlResources;
	}

	public static Map<String, Font> getFontResources() {
		return fontResources;
	}

	public static Map<String, Animation> getAnimationResources() {
		return animationResources;
	}

	public static Map<String, Shape> getShapes(){
		return shapeDefinitions;
	}

	public static void setAnimationResources(
			Map<String, Animation> animationResources) {
		AssetManager.animationResources = animationResources;
	}

	public static Map<Integer, ArrayList<HighScore>> getHighscores() {
		return highscores;
	}

	public static void setHighscores(Map<Integer, ArrayList<HighScore>> highscores) {
		AssetManager.highscores = highscores;
	}
	
	public static ArrayList<HighScore> requestHighScores(int levelid){
		return highscores.get(levelid);
	}

	public static Shape requestShape(String shapeid) {
		return shapeDefinitions.get(shapeid);
	}

	public static Map<Integer, Boolean> getLevelUnlock() {
		return levelUnlocks;
	}
}

