package gameworlds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gameengine.*;
import gameobjects.*;
import gamestates.GameState;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import scoringsystem.AchievementPopup;
import scoringsystem.GLaDOS;

public class Level {

	/** The level id **/
	private int levelid;
	/** Our background image for the level **/
	private Image bg;
	private Image fg;

	/** Our Player **/
	private Player player;
	/** A vector containing all cubes **/
	private Map<String,CompanionCube> cubes;
	/** A vector containing all walls **/
	private Map<String,Wall> walls;
	/** Dissipation Fields **/
	private Map<String, DissipationField> dissipationFields;
	/** A vector containing all doors **/
	private Map<String,Door> doors;
	/** A vector containing all static platforms **/
	private Map<String,Platform> platforms;
	/** A vector containing all moving platforms **/
	private Map<String,MovingPlatform> movingplatforms;
	/** A vector containing all portals **/
	private Portal[] portals = new Portal[2];
	/** A map containing active portal bullets */
	private Map<String, PortalBullet> portalbullets;
	/** A vector containing all switches **/
	private Map<String,LittleSwitch> lilSwitches;
	/** A vector containing all switches **/
	private Map<String,BigSwitch> bigSwitches;
	/** Walls that can't be interacted with **/
	private Map<String, Wall> noportalwalls;
	/** Our end point **/
	private EndLevel levelend;

	/** Our level oracle and achievement data **/
	private GLaDOS glados;
	private ArrayList<AchievementPopup> achievementPopups;

	/** Our physics world **/
	private World world;
	/** Our Physics Engine Constants **/
	private final int velocityIterations = 8;
	private final int positionIterations = 4;
	/** Gravity **/
	private final Vec2 gravity = new Vec2(0,-18f);
	
	private Vec2[] ds = new Vec2[2];
	private Vec2[] dd = new Vec2[2];

	public Level() throws SlickException{
		// Create physics world
		world = new World(gravity);

		// Initialises ArrayLists
		cubes = new HashMap<String,CompanionCube>();
		walls = new HashMap<String,Wall>();
		dissipationFields = new HashMap<String, DissipationField>();
		doors = new HashMap<String,Door>();
		platforms = new HashMap<String,Platform>();
		movingplatforms = new HashMap<String,MovingPlatform>();
		lilSwitches = new HashMap<String,LittleSwitch>();
		bigSwitches = new HashMap<String,BigSwitch>();	
		noportalwalls = new HashMap<String,Wall>();
		portalbullets = new HashMap<String, PortalBullet>();
		
		// Set up portal links
		portals[Portal.ORANGE] = new Portal(Portal.ORANGE, new Vec2(-1,0), world);
		portals[Portal.BLUE] = new Portal(Portal.BLUE, new Vec2(-1,0), world);
		portals[Portal.ORANGE].linkPortals(portals[Portal.BLUE]);
		portals[Portal.BLUE].linkPortals(portals[Portal.ORANGE]);
		
		// Set up deferred loading for portal bullets
		ds[0] = null;
		ds[1] = null;
		
		achievementPopups = new ArrayList<AchievementPopup>();
		glados = new GLaDOS(this.levelid);
	}

	public void update(final float dir_x, final float dir_y, final int delta, final StateBasedGame sbg) throws SlickException {
		final float timeStep = (float)delta/1000;
		player.moveXDir(dir_x, delta);
		
		// Stupid hack to add portals at level creation. Can't use normal way because for some
		// reason portal bullets don't move despite having non-zero velocity.
		if (ds[Portal.BLUE]!=null) {
			PortalBullet pb = new PortalBullet(Portal.BLUE, ds[Portal.BLUE], dd[Portal.BLUE]);
			this.addPortalBullet(pb, pb.getBodyID());
			ds[Portal.BLUE] = null;
		}
		if (ds[Portal.ORANGE]!=null) {
			PortalBullet pb = new PortalBullet(Portal.ORANGE, ds[Portal.ORANGE], dd[Portal.ORANGE]);
			this.addPortalBullet(pb, pb.getBodyID());
			ds[Portal.ORANGE] = null;
		}
		
		// Update dynamic objects (for portals)
		player.update(this, delta);
		for (CompanionCube c : cubes.values().toArray(new CompanionCube[cubes.size()])) {
			c.update(this);
			c.update(delta);
			if (c.isDestroyed()){
				world.destroyBody(c.getBody());
				cubes.remove(c.getBodyID());		
			}
		}
		
		// Update the physics world
		world.step(timeStep, velocityIterations, positionIterations);
		player.checkCube();
		
		// Update other game objects
		for (BigSwitch bs: bigSwitches.values()){
			bs.updateState();
		}
		for(final LittleSwitch ls: lilSwitches.values()){
			ls.updateState(delta);
		}
		for(final MovingPlatform pl: movingplatforms.values()){
			pl.updatePos(delta);
		}
		for (Door d : doors.values()) {
			d.update(delta);
		}
		for (Portal p : portals) {
			p.update(delta);
		}
		for (PortalBullet pb : portalbullets.values().toArray(new PortalBullet[0])) {
			pb.update();
			if (pb.hasCollided()) {
				portalbullets.remove(pb.getBodyID());
				pb.destroy();
			}
		}
		for (DissipationField field: dissipationFields.values()){
			field.update(delta);
		}
		
		glados.updateTesting(delta,player);

		if(levelend.getBody().m_contactList!=null){
			final String contactbodyb = levelend.getBody().m_contactList.contact.m_fixtureB.m_body.toString();
			final String playerid = player.getBody().toString();
			if(contactbodyb.equals(playerid) && PhysUtils.distance(player.getLocation(), levelend.getLocation())<=0.7f){
				EndGameMenu.giveGlados(glados);				
				GameState.setDisplayEndGame(true);
			}
		}

	}

	public void render(final Graphics g, final boolean debug,final Camera cam, final GameContainer gc) {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObjects(dissipationFields, cam);
		RenderEngine.drawGameObjects(portalbullets, cam);
		RenderEngine.drawGameObject(levelend, cam);
		RenderEngine.drawGameObjects(lilSwitches, cam);
		RenderEngine.drawGameObject(player, cam);
		RenderEngine.drawBigSwitches(bigSwitches,cam);
		RenderEngine.drawGameObjects(cubes, cam);
		RenderEngine.drawGameObjects(doors, cam);
		RenderEngine.drawGameObjects(platforms, cam);
		RenderEngine.drawGameObjects(movingplatforms, cam);
		if(fg!=null){
			RenderEngine.drawBG(fg, cam);
		}
		RenderEngine.drawPortals(portals, cam);
		RenderEngine.drawGameObject(levelend, cam);
		RenderEngine.drawWalls(walls, g, Color.magenta, cam);
		RenderEngine.drawWalls(noportalwalls, g, Color.green, cam);
		if(!achievementPopups.isEmpty()){
			RenderEngine.renderAchievementPopups(achievementPopups, g,cam);
		}

	}

	public void removeCube(final String string) {
		if(cubes.get(string)!=null){
			cubes.get(string).cubeDestroy();
		}

	}

	public void addBigSwitch(final BigSwitch s, final String bodyid){
		bigSwitches.put(bodyid,s);
	}

	public void addCube(final CompanionCube cube, final String bodyid){
		cubes.put(bodyid, cube);
	}
	
	public void addDissipationField(DissipationField field, String bodyID) {
		dissipationFields.put(bodyID,field);
	}

	public void addDoor(final Door door, final String bodyid){
		doors.put(bodyid,door);
	}

	public void addEndLevel(final EndLevel end) {
		levelend = end;
	}

	public void addLittleSwitch(final LittleSwitch s, final String bodyid){
		lilSwitches.put(bodyid,s);
	}

	public void addMovingPlatform(final MovingPlatform platform, final String bodyid){
		movingplatforms.put(bodyid,platform);
	}

	public void addPlatform(final Platform platform, final String bodyid){
		platforms.put(bodyid,platform);
	}
	
	public void addPortalBullet(PortalBullet pb, String bodyID) {
		portalbullets.put(bodyID, pb);
	}
	
	/** Method for telling level to later add portal bullets. Stupid hack, but for
	 * some stupid reason, it is required.
	 * @param c Colour of PortalBullet
	 * @param start Start location
	 * @param dir Direction of travel
	 */
	public void deferPortalBullet(int c, Vec2 start, Vec2 dir){
		ds[c] = start;
		dd[c] = dir;
	}

	public void addPortallessWall(Wall wall, String bodyId) {
		noportalwalls.put(bodyId, wall);
	}

	public void addWall(final Wall wall, final String bodyid){
		walls.put(bodyid,wall);
	}

	public Image getBg() {
		return bg;
	}

	public String getBodyType (Body other){
		final String key = other.toString();
		String type="";
		if(player.getBodyID().equals(key)){
			type="player";
		}else if(cubes.containsKey(key)){
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
		}else if(noportalwalls.containsKey(key)){
			type="noportalwall";
		}else if (portals[0].getBody().equals(other) | portals[1].getBody().equals(other)){
			type="portal";
		}else if(movingplatforms.containsKey(key)){
			type="movingplatform";
		}else if(dissipationFields.containsKey(key)){
			type="dissipationfield";
		}else if(portalbullets.containsKey(key)){
			type="portalbullet";
		}
		return type;
	}

	public CompanionCube getCube(final String bodyId){
		return cubes.get(bodyId);
	}

	public Collection<Door> getDoorCollection() {
		return doors.values();
	}

	public Map<String, Door> getDoors() {
		return doors;
	}

	public GLaDOS getGlados() {
		return glados;
	}

	public int getLevelId() {
		return levelid;
	}

	public Player getLevelPlayer() {
		return player;
	}

	public World getWorld(){
		return world;
	}

	public Portal[] getPortals() {
		return portals;
	}
	
	public Map<String, Wall> getWalls() {
		return walls;
	}

	public LittleSwitch getSwitch(final String bodyId) {
		return lilSwitches.get(bodyId);
	}

	public void setBg(final Image bg) {
		this.bg = bg;
	}

	public void setFg(final Image image) {
		fg=image;
	}

	public void setLevelId(final int id) {
		levelid = id;
		glados.getLevelStats().setLevelID(id);
		Paused.setLevelid(id);
	}

	public void setLevelPlayer(final Player player) {
		this.player = player;
	}

	public ArrayList<AchievementPopup> getAchievementPopups() {
		return achievementPopups;
	}

	public void setAchievementPopups(ArrayList<AchievementPopup> achievementPopups) {
		this.achievementPopups = achievementPopups;
	}

	public void clearPortals() {
		for (Portal p : portals)
			try {
				if(p.isEnabled()){
					p.disable();
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
	}

	public Image getFg() {
		return fg;
	}
}
