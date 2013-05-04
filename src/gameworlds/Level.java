package gameworlds;
import java.util.HashMap;
import java.util.Map;

import gameengine.*;
import gameobjects.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import resourcemanagers.AssetManager;



public class Level {
	/** The resource manager for all resources **/
	AssetManager assetManager;
	
	/** The level id **/
	private int levelid;
	/** Our background image for the level **/
	private Image bg;
	
	/** Our Player **/
	private Player player;
	/** A vector containing all cubes **/
	protected Map<String,CompanionCube> cubes;
	/** A vector containing all walls **/
	protected Map<String,Wall> walls;
	/** A vector containing all doors **/
	protected Map<String,Door> doors;
	/** A vector containing all platforms **/
	protected Map<String,MovingPlatform> platforms;
	/** A vector containing all portals **/
	protected Map<String,Portal> portals;
	/** A vector containing all switches **/
	protected Map<String,LittleSwitch> lilSwitches;
	/** A vector containing all switches **/
	protected Map<String,BigSwitch> bigSwitches;
	
	/** Our physics world **/
	private World world;
	/** Our portal physics world **/
	private World portalWorld;
	/** Our Physics Engine Constants **/
	private int velocityIterations = 6;
	private int positionIterations = 2;
	/** Gravity **/
	private Vec2 gravity = new Vec2(0,-18f);

	public Level(){
		// Create physics worlds
		world = new World(gravity);
		portalWorld = new World(new Vec2(0,0));
		
		//Initialises ArrayLists
		cubes = new HashMap<String,CompanionCube>();
		walls = new HashMap<String,Wall>();
		doors = new HashMap<String,Door>();
		platforms = new HashMap<String,MovingPlatform>();
		portals = new HashMap<String,Portal>();
		lilSwitches = new HashMap<String,LittleSwitch>();
		bigSwitches = new HashMap<String,BigSwitch>();	
	}

	public void render(Graphics g, boolean debug,Camera cam, GameContainer gc) {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObject(player, cam);
		RenderEngine.drawGameObjects(cubes, cam);
		RenderEngine.drawPlatforms(platforms, cam);
		
	}

	public void update(float dir_x, float dir_y, int delta, StateBasedGame sbg) throws SlickException {
		float timeStep = (float)delta/1000;
		world.step(timeStep, velocityIterations, positionIterations);
		player.moveXDir(dir_x, delta);
	}
	
	public Player getLevelPlayer() {
		return player;
	}
	
	public void setLevelPlayer(Player player) {
		this.player = player;
	}

	public Image getBg() {
		return bg;
	}

	public void setBg(Image bg) {
		this.bg = bg;
	}

	public void updateGameState(GameContainer gc) {
		// TODO Auto-generated method stub
	}
	
	public World getPhysWolrd(){
		return world;
	}

	public World getPoralWorld(){
		return portalWorld;
	}
	
	public void addCube(CompanionCube cube, String bodyid){
		cubes.put(bodyid, cube);
	}
	
	public void addWall(Wall wall, String bodyid){
		walls.put(bodyid,wall);
	}
	
	public void addDoor(Door door, String bodyid){
		doors.put(bodyid,door);
	}
	
	public void addPortal(Portal portal, String bodyid){
		portals.put(bodyid,portal);
	}
	
	public void addMovingPlatform(MovingPlatform platform, String bodyid){
		platforms.put(bodyid,platform);
	}
	
	public void addBigSwitch(BigSwitch s, String bodyid){
		bigSwitches.put(bodyid,s);
	}
	
	public void addLilSwitch(LittleSwitch s, String bodyid){
		lilSwitches.put(bodyid,s);
	}

	public int getLevelId() {
		return levelid;
	}
}
