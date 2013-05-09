package gameworlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gameengine.*;
import gameobjects.*;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Level {
	
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
	/** A vector containing all static platforms **/
	protected Map<String,Platform> platforms;
	/** A vector containing all moving platforms **/
	protected Map<String,MovingPlatform> movingplatforms;

	/** A vector containing all portals **/
	protected Portal[] portals = new Portal[2];
	/** A vector containing all switches **/
	protected Map<String,LittleSwitch> lilSwitches;
	/** A vector containing all switches **/
	protected Map<String,BigSwitch> bigSwitches;
	
	/** A map containing all interactable objects **/
	protected static Map<String, GameObject> interactableObjects;
	/** A map containing all static objects **/
	protected static Map<String, GameObject> staticObjects;

	
	/** Our physics world **/
	private World world;
	/** Our portal physics world **/
	private World portalWorld;
	/** Our Physics Engine Constants **/
	private int velocityIterations = 8;
	private int positionIterations = 4;
	/** Gravity **/
	private Vec2 gravity = new Vec2(0,-18f);
	
	private JBoxDebugDraw dd;
	

	public Level() throws SlickException{
		// Create physics worlds
		world = new World(gravity);
		portalWorld = new World(new Vec2(0,0));
		
		//Initialises ArrayLists
		cubes = new HashMap<String,CompanionCube>();
		walls = new HashMap<String,Wall>();
		doors = new HashMap<String,Door>();
		platforms = new HashMap<String,Platform>();
		movingplatforms = new HashMap<String,MovingPlatform>();

		lilSwitches = new HashMap<String,LittleSwitch>();
		bigSwitches = new HashMap<String,BigSwitch>();	
		portals[Portal.ORANGE] = new Portal("ORANGEPORTAL", new Vec2(-1,0), world);
		portals[Portal.BLUE] = new Portal("BLUEPORTAL", new Vec2(-1,0), world);
		portals[Portal.ORANGE].linkPortals(portals[Portal.BLUE]);
		portals[Portal.BLUE].linkPortals(portals[Portal.ORANGE]);
	}

	public void render(Graphics g, boolean debug,Camera cam, GameContainer gc) {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObjects(lilSwitches, cam);
		RenderEngine.drawGameObject(player, cam);
		RenderEngine.drawGameObjects(bigSwitches,cam);
		RenderEngine.drawGameObjects(cubes, cam);
		RenderEngine.drawGameObjects(doors, cam);
		RenderEngine.drawGameObjects(portals, cam);
		RenderEngine.drawGameObjects(platforms, cam);
		RenderEngine.drawGameObjects(movingplatforms, cam);

	}

	public void update(float dir_x, float dir_y, int delta, StateBasedGame sbg) throws SlickException {
		float timeStep = (float)delta/1000;
		player.moveXDir(dir_x, delta);
		world.step(timeStep, velocityIterations, positionIterations);
		player.checkCube();
		for(BigSwitch bs: bigSwitches.values()){
			bs.updateState();
		}
		for(MovingPlatform pl: movingplatforms.values()){
			pl.updatePos(delta);
		}
	}
	
	public void playerShootPortal(int color, Vec2 target) throws SlickException {
		RayCastHelper rch = new RayCastHelper(this);
		Vec2 dir = target.sub(player.getLocation());
		dir.mulLocal(1/dir.length());
		world.raycast(rch, player.getLocation(), player.getLocation().add(dir.mul(100)));
		if (rch.fixture == null)
			return;
		
		if (this.getBodyType(rch.fixture.getBody()).equals("wall")) {
			Wall wall = walls.get(rch.fixture.getBody().toString());
			Vec2 loc = rch.point;
			System.out.println(wall.getStart() + " " + wall.getUnitTangent());
			portals[color].hitWall(loc, wall);
		}
	}
	
	public boolean portalBulletInteracts(String bodyID) {
		if (walls.containsKey(bodyID) | cubes.containsKey(bodyID) | platforms.containsKey(bodyID))
			return true;
		return false;
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
	
	public World getPhysWorld(){
		return world;
	}

	public World getPortalWorld(){
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
	
	public void addPlatform(Platform platform, String bodyid){
		platforms.put(bodyid,platform);
	}
	
	public void addMovingPlatform(MovingPlatform platform, String bodyid){
		movingplatforms.put(bodyid,platform);
	}
	
	public void addBigSwitch(BigSwitch s, String bodyid){
		bigSwitches.put(bodyid,s);
	}
	
	public void addLittleSwitch(LittleSwitch s, String bodyid){
		lilSwitches.put(bodyid,s);
	}

	public int getLevelId() {
		return levelid;
	}
	
	public CompanionCube getCube(String bodyId){
		return cubes.get(bodyId);
	}
	
	public String getBodyType(Body other){
		String key = other.toString();
		String type="";
		if(cubes.containsKey(key)){
			type="cube";
		}else if(bigSwitches.containsKey(key)){
			type="bigswitch";
		}else if(lilSwitches.containsKey(key)){
			type="lilswitch";
		}else if(doors.containsKey(key)){
			type="door";
		}else if(platforms.containsKey(key)){
			type="platform";
		}else if(walls.containsKey(key)){
			type="wall";
		}else if(movingplatforms.containsKey(key)){
			type="movingplatform";
		}
		return type;
	}

	public LittleSwitch getSwitch(String bodyId) {
		return lilSwitches.get(bodyId);
	}

	public void removeCube(CompanionCube cube) {
		cubes.remove(cube.getBodyId());
	}
	
	public Map<String, Door> getDoors() {
		return doors;
	}
	
	public Collection<Door> getDoorCollection() {
		return doors.values();
	}
}
