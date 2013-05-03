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
import org.newdawn.slick.util.ResourceLoader;

import resourcemanagers.AssetManager;



public class Level {
	/** The resource manager for all resources **/
	AssetManager assetManager;
	
	/** The level id **/
	@SuppressWarnings("unused")
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
	@SuppressWarnings("unused")
	private World portalWorld;
	/** Our Physics Engine Constants **/
	private int velocityIterations = 6;
	private int positionIterations = 2;
	/** Gravity **/
	private Vec2 gravity = new Vec2(0,-18f);

	public Level(GameContainer gc, int id) throws SlickException {
		
		setBg(new Image("assets/levels/bg.png"));
		levelid=id;
		
		// Static Body
		world = new World(gravity);
		
		PhysUtils.addWall(world, 0, 0, 100, 1); //floor
		PhysUtils.addWall(world, 0, 0, 0.5f, 20); //left wall
		PhysUtils.addWall(world, 0, 17.14f, 100, 0); //top
		PhysUtils.addWall(world, 10, 2.5f, 4, 0.25f);//first thing
		PhysUtils.addWall(world, 14, 3.95f, 4, 0.25f);//second thing
		PhysUtils.addWall(world, 19.5f, 0, 0.5f, 20); //right wall
		
		// Dynamic Body
		player = Player.getPlayer("/assets/sprites/chell.png",new Vec2(2, 5), world);
	}

	public void render(Graphics g, boolean debug,Camera cam, GameContainer gc) {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObject(player, cam);
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
}
