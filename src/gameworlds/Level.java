package gameworlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gameengine.*;
import gameobjects.*;
import gamestates.LoadingState;

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
	private Image fg;
	
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

	/** Our end point **/
	protected EndLevel levelend;
	
	/** Our physics world **/
	private final World world;
	/** Our Physics Engine Constants **/
	private final int velocityIterations = 8;
	private final int positionIterations = 4;
	/** Gravity **/
	private final Vec2 gravity = new Vec2(0,-18f);
	


	public Level() throws SlickException{
		// Create physics worlds
		world = new World(gravity);
		
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

	public void render(final Graphics g, final boolean debug,final Camera cam, final GameContainer gc) {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObject(levelend, cam);
		RenderEngine.drawGameObjects(lilSwitches, cam);
		RenderEngine.drawGameObject(player, cam);
		RenderEngine.drawGameObjects(bigSwitches,cam);
		RenderEngine.drawGameObjects(cubes, cam);
		RenderEngine.drawGameObjects(doors, cam);
		RenderEngine.drawGameObjects(platforms, cam);
		RenderEngine.drawGameObjects(movingplatforms, cam);
		if(fg!=null){
			RenderEngine.drawBG(fg, cam);
		}
		RenderEngine.drawGameObjects(portals, cam);
	}

	public void update(final float dir_x, final float dir_y, final int delta, final StateBasedGame sbg) throws SlickException {
		final float timeStep = (float)delta/1000;
		player.moveXDir(dir_x, delta);
		
		// Update dynamic objects (for portals)
		player.update(this);
		for (GameObject o : cubes.values()) {
			o.update(this);
		}
		
		world.step(timeStep, velocityIterations, positionIterations);
		player.checkCube();
		for(final BigSwitch bs: bigSwitches.values()){
			bs.updateState();
		}
		for(final MovingPlatform pl: movingplatforms.values()){
			pl.updatePos(delta);
		}
		
		if(levelend.getBody().m_contactList!=null){
			final String contactbodyb = levelend.getBody().m_contactList.contact.m_fixtureB.m_body.toString();
			final String playerid = player.getBody().toString();
			if(contactbodyb.equals(playerid)){
				LoadingState.loadNextLevel(sbg);
				sbg.enterState(Portal2D.LOADSTATE);
			}
		}
	}
	
	public void playerShootPortal(int color, Vec2 target) throws SlickException {
		PortalShootRCHelper rch = new PortalShootRCHelper(this);
		Vec2 dir = target.sub(player.getLocation());
		dir.mulLocal(1/dir.length());
		world.raycast(rch, player.getLocation(), player.getLocation().add(dir.mul(10)));
		if (rch.fixture == null)
			return;
		
		if (this.getBodyType(rch.fixture.getBody()).equals("wall")) {
			final Wall wall = walls.get(rch.fixture.getBody().toString());
			final Vec2 loc = rch.point;
			System.out.println(wall.getStart() + " " + wall.getUnitTangent());
			portals[color].hitWall(loc, wall);
		}
	}
	
	public boolean portalBulletInteracts(final String bodyID) {
		if (walls.containsKey(bodyID) | cubes.containsKey(bodyID) | platforms.containsKey(bodyID))
			return true;
		return false;
	}
	
	public Player getLevelPlayer() {
		return player;
	}
	
	public void setLevelPlayer(final Player player) {
		this.player = player;
	}

	public Image getBg() {
		return bg;
	}

	public void setBg(final Image bg) {
		this.bg = bg;
	}

	public void updateGameState(final GameContainer gc) {
		// TODO Auto-generated method stub
	}
	
	public World getPhysWorld(){
		return world;
	}

	public void addCube(final CompanionCube cube, final String bodyid){
		cubes.put(bodyid, cube);
	}
	
	public void addWall(final Wall wall, final String bodyid){
		walls.put(bodyid,wall);
	}
	
	public void addDoor(final Door door, final String bodyid){
		doors.put(bodyid,door);
	}
	
	public void addPlatform(final Platform platform, final String bodyid){
		platforms.put(bodyid,platform);
	}
	
	public void addMovingPlatform(final MovingPlatform platform, final String bodyid){
		movingplatforms.put(bodyid,platform);
	}
	
	public void addBigSwitch(final BigSwitch s, final String bodyid){
		bigSwitches.put(bodyid,s);
	}
	
	public void addLittleSwitch(final LittleSwitch s, final String bodyid){
		lilSwitches.put(bodyid,s);
	}

	public int getLevelId() {
		return levelid;
	}
	
	public void setLevelId(final int id) {
		levelid = id;
	}
	
	public CompanionCube getCube(final String bodyId){
		return cubes.get(bodyId);
	}
	
	public String getBodyType(final Body other){
		final String key = other.toString();
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
		} else if (portals[0].getBody().equals(other) | portals[1].getBody().equals(other)){
			type="portal";
		}else if(movingplatforms.containsKey(key)){
			type="movingplatform";
		}
		return type;
	}

	public LittleSwitch getSwitch(final String bodyId) {
		return lilSwitches.get(bodyId);
	}
	
	public Portal[] getPortals() {
		return portals;
	}

	public void removeCube(final CompanionCube cube) {
		cubes.remove(cube.getBodyId());
	}
	
	public Map<String, Door> getDoors() {
		return doors;
	}
	
	public Collection<Door> getDoorCollection() {
		return doors.values();
	}

	public void addEndLevel(final EndLevel end) {
		levelend = end;
	}

	public void setFg(final Image image) {
		fg=image;
	}

	public Object getCam() {
		// TODO Auto-generated method stub
		return null;
	}
}
