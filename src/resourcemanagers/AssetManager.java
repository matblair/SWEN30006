package resourcemanagers;

import gameengine.Portal2D;
import gameengine.Sound;
import gameworlds.Level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jbox2d.collision.shapes.Shape;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import scoringsystem.Achievement;
import scoringsystem.HighScore;

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
	private static HighScoreLoader highscoreLoader = new HighScoreLoader();
	/** The level unlock loader **/
	private static LevelLockLoader lockLoader = new LevelLockLoader();

	/**Creates a resource repository of commonly used items **/
	private static Map<String, Image> imageResources = new HashMap<String, Image>(); //All images used for game objects
	private static Map<String, Sound> soundResources = new HashMap<String, Sound>(); //All sounds resources
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


	/** The total number of resources loaded **/
	private static int totalresources=0;

	/**General File Locations for loading **/
	private static final String loadinglist ="loadinglist.xml";
	private static final String generalresource = "assets/xmlresources/";
	private static final String achievementxml = "achievements.xml";
	private static final String highscorexml = "highscores.xml";
	private static final String levelunlocks = "levelunlocks.xml";

	/** The contstructor using the singleton pattern 
	 * to ensure that only one instance can be called 
	 * @return manager The Asset Manager
	 * @throws SlickException
	 */
	public static AssetManager getAssetManager()
			throws SlickException{
		if(manager==null){
			manager = new AssetManager();
		}
		return manager;
	}

	/** Load all the game assets from the loading list 
	 * described in the static locations above
	 */
	public static void loadAllGameAssets(){

		try {
			Image loading = new Image("assets/sprites/uielements/loading.png");
			loading.drawCentered(Portal2D.app.getHeight()/2, Portal2D.app.getWidth()/2);
		} catch (SlickException e1) {
			e1.printStackTrace();
		}


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
		loadLevelUnlocks();
		//Load local highscores
		loadHighScores();

		if(Portal2D.debug){
			System.out.println(imageResources.size() +" images loaded.");
			System.out.println(soundResources.size() +" sounds loaded.");
			System.out.println(uiElementResources.size() +" ui elements loaded.");
			System.out.println(levelXmlResources.size() +" xml levels loaded.");
			System.out.println(inputXmlResources.size() +" input xml loaded.");
			System.out.println(fontResources.size() +" fonts loaded.");
			System.out.println(animationResources.size() +" animations loaded.");
			System.out.println(shapeDefinitions.size() + " shapes loaded");
			System.out.println(totalresources +" resources loaded in total");
		}
	}


	/** Load the game achievements using the achievement loader
	 */
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
	
	/** Load the level unlocks using the level unlock loader
	 */
	public static void loadLevelUnlocks(){
		final File f = new File(generalresource, levelunlocks);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			lockLoader.loadLevelLocks(is, false);
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

	/** Load the high scores using the high score loader
	 * 
	 */
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

	/** Load a level by retrieving the level xml file using
	 * the given level id and then calling level loader
	 * @param levelid
	 * @return level The level created
	 * @throws SlickException
	 */
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

	/** Load the input from the input xml paths and then 
	 * call the input manager to parse the file
	 * @param inputtype
	 * @throws SlickException
	 */
	public static void loadInput(int inputtype) throws SlickException{
		String inputxml = AssetManager.requestInputXMLPath(inputtype);
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

	/** Request an image by ID 
	 * @param imgid
	 * @return Image
	 */
	public static Image requestImage(String imgid){
		return imageResources.get(imgid);
	}

	/** Request an achievement by id
	 * 
	 * @param imgid
	 * @return Image
	 */
	public static Image requestAchiemeventResource(String imgid){
		return achievementResources.get(imgid);
	}

	/** Request a particular sound for a given sound id
	 * 
	 * @param soundid
	 * @return sound
	 */
	public static Sound requestSound(String soundid){
		return soundResources.get(soundid);
	}

	/** Request a particular ui element for a ui element id
	 * 
	 * @param uiid
	 * @return Image 
	 */
	public static Image requestUIElement(String uiid){
		return uiElementResources.get(uiid);
	}

	/** Request a level xml path for a given level id
	 * 
	 * @param levelid
	 * @return string
	 */
	public static String requestLevelXMLPath(int levelid){
		return levelXmlResources.get(levelid);
	}

	/** Request input xml path for a given input identifier
	 * 
	 * @param inputid
	 * @return string
	 */
	public static String requestInputXMLPath(int inputid){
		return inputXmlResources.get(inputid);
	}

	/** Get a particular font by font id
	 * 
	 * @param fontid
	 * @return font
	 */
	public static Font requestFontResource(String fontid){
		return fontResources.get(fontid);
	}

	/** Get all animations for a given animation id
	 *  
	 * @param animid
	 * @return animation
	 */
	public static Animation requestAnimationResources(String animid){
		return animationResources.get(animid).copy();
	}

	/** Get all image resources to add to
	 * 
	 * @return imageResources
	 */
	public static Map<String, Image> getImageResources() {
		return imageResources;
	}

	/** Get the achievement map of all achievements to add to
	 * 
	 * @return achievementResources
	 */
	public static Map<String, Image> getAchievementResources() {
		return achievementResources;
	}

	/**  Get a collection of achievements for use in achievement rendering
	 * 
	 * @return achievements.values()
	 */
	public static Collection<Achievement> getAchievements() {
		return achievements.values();
	}

	/** Get the achievement map of all achievements to add to
	 * 
	 * @return achievements
	 */
	public static Map<String, Achievement> getAchievementMap() {
		return achievements;
	}

	/** Get sound resources to add to
	 * 
	 * @return soundResources
	 */
	public static Map<String, Sound> getSoundResources() {
		return soundResources;
	}

	/** Get uielement resources to add to
	 * @return  uiElementResources;
	 */
	public static Map<String, Image> getUiElementResources() {
		return uiElementResources;
	}

	/** Get level xml resources to add to
	 * 
	 * @return levelXmlResources
	 */
	public static Map<Integer, String> getLevelXmlResources() {
		return levelXmlResources;
	}

	/** Get all xml resources for input to add to
	 * 
	 * @return inputXmlResources
	 */
	public static Map<Integer, String> getInputResources() {
		return inputXmlResources;
	}

	/** Get all font resources to add to
	 * 
	 * @return fontResources the font resources
	 */ 
	public static Map<String, Font> getFontResources() {
		return fontResources;
	}

	/** Get all aniamtions to add to
	 * 
	 * @return get the animation resources
	 */
	public static Map<String, Animation> getAnimationResources() {
		return animationResources;
	}

	/** Get all shapes to add to
	 * 
	 * @return shapes The shape definitions
	 */
	public static Map<String, Shape> getShapes(){
		return shapeDefinitions;
	}

	/** Set the animation resources
	 * 
	 * @param animationResources
	 */
	public static void setAnimationResources(
			Map<String, Animation> animationResources) {
		AssetManager.animationResources = animationResources;
	}

	/** Get the high score map
	 * 
	 * @return highscore The highscore map
	 */
	public static Map<Integer, ArrayList<HighScore>> getHighscores() {
		return highscores;
	}

	/** Set the high score map
	 * 
	 * @param highscores
	 */
	public static void setHighscores(Map<Integer, ArrayList<HighScore>> highscores) {
		synchronized (AssetManager.highscores){
			AssetManager.highscores = highscores;
		}
	}

	/** Request all high scores on file for a given level id
	 * 
	 * @param levelid
	 * @return highscores The levelid
	 */
	public static ArrayList<HighScore> requestHighScores(int levelid){
		synchronized (AssetManager.highscores){
			ArrayList<HighScore> hs = highscores.get(levelid);
			if (hs != null)
				Collections.sort(hs);
			return hs;
		}
	}


	/** Request a shap for fixture definitions
	 * 
	 * @param shapeid
	 * @return shape
	 */
	public static Shape requestShape(String shapeid) {
		return shapeDefinitions.get(shapeid);
	}

	/** Check if a level is unlocked
	 * 
	 * @return levelUnlocks
	 */
	public static Map<Integer, Boolean> getLevelUnlocks() {
		return levelUnlocks;
	}
}

