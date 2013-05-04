package gameworlds;

import java.util.ArrayList;

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
	protected ArrayList<CompanionCube> cubes;
	/** A vector containing all walls **/
	protected ArrayList<Wall> walls;
	/** A vector containing all doors **/
	protected ArrayList<Door> doors;
	/** A vector containing all platforms **/
	protected ArrayList<MovingPlatform> platforms;
	/** A vector containing all portals **/
	protected ArrayList<Portal> portals;
	/** A vector containing all switches **/
	protected ArrayList<LittleSwitch> lilSwitches;
	/** A vector containing all switches **/
	protected ArrayList<BigSwitch> bigSwitches;
	
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
		cubes = new ArrayList<CompanionCube>();
		walls = new ArrayList<Wall>();
		doors = new ArrayList<Door>();
		platforms = new ArrayList<MovingPlatform>();
		portals = new ArrayList<Portal>();
		lilSwitches = new ArrayList<LittleSwitch>();
		bigSwitches = new ArrayList<BigSwitch>();	
	}

	public void render(Graphics g, boolean debug,Camera cam, GameContainer gc) {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObject(player, cam);
		for (CompanionCube cube: cubes) {
			RenderEngine.drawGameObject(cube,cam);
			
		}
		
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
	
	public void addCube(CompanionCube cube){
		cubes.add(cube);
	}
	
	public void addWall(Wall wall){
		walls.add(wall);
	}
	
	public void addDoor(Door door){
		doors.add(door);
	}
	
	public void addPortal(Portal portal){
		portals.add(portal);
	}
	
	public void addMovingPlatform(MovingPlatform platform){
		platforms.add(platform);
	}
	
	public void addBigSwitch(BigSwitch s){
		bigSwitches.add(s);
	}
	
	public void addLilSwitch(LittleSwitch s){
		lilSwitches.add(s);
	}

	public int getLevelId() {
		return levelid;
	}
}
