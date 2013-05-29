package gamestates;

import gameengine.Camera;
import gameengine.InputManager;
import gameengine.PhysUtils;
import gameengine.Portal2D;
import gameengine.Sound;
import gameobjects.Portal;
import gameobjects.PortalBullet;
import gameworlds.EndGameMenu;
import gameworlds.Level;
import gameworlds.Paused;
import resourcemanagers.SoundController;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameState extends BasicGameState implements KeyListener, MouseListener {
	private static boolean finishedEndGame = false;
	private static int StateId = Portal2D.GAMESTATE; // State ID
	private static Camera cam;
	private boolean listening=true;
	private static Level level;
	private static Paused paused;
	private static EndGameMenu endgame;
	private static int height;
	private static boolean ispaused = false;
	private static boolean displayEndGame = false;

	/** Constructor
	 * @throws SlickException
	 */
	public GameState()
	{
		super();
	}

	/** Method called by Slick to initialise the state
	 * 
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		level = new Level();
		cam = new Camera();
		paused = new Paused(level.getLevelId());
		endgame = new EndGameMenu();
		SoundController.initialise();
	}
	
	/** Method called by Slick when entering the state
	 * 
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		paused.setLevelId(level.getLevelId());
		EndGameMenu.setUploadedScores(false);
		ispaused=false;
		SoundController.play(Sound.INGAME);
	}
	
	/** Method called by Slick when entering the state. Stops any music.
	 * 
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		SoundController.stopMusic();
	}

	/** Method called by Slick to update the state. Handles most input, including player movement,
	 * camera update and pause menu navigation
	 * 
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		height = gc.getHeight();
		int dir_x = 0;
		Input input = gc.getInput();

		if(!isPaused() && !displayEndGame){
			if (input.isKeyDown(InputManager.MOVE_RIGHT))
				dir_x++;
			if (input.isKeyDown(InputManager.MOVE_LEFT))
				dir_x--;
			if (input.isKeyPressed(InputManager.JUMP))
				level.getLevelPlayer().jump();
			if (input.isKeyPressed(InputManager.INTERACT)){
				level.getLevelPlayer().interact(level.getWorld(), level);
			}if( input.isKeyDown(InputManager.PAUSE)){
				switchPaused();
			}
			level.update(dir_x,0, delta, sbg);
			cam.follow(gc, level.getLevelPlayer());
			
		} else if (displayEndGame){
			endgame.Update(gc.getGraphics(),gc,sbg);
			if(finishedEndGame){
				try {
					LoadingState.loadNextLevel(sbg);
				} catch (SlickException e) {
					e.printStackTrace();
				}
				System.out.println("Pressed Enter!");
				GameState.setDisplayEndGame(false);
				finishedEndGame=false;
				sbg.enterState(Portal2D.LOADSTATE);
			}
			
		} else {
			paused.Update(gc.getGraphics(), gc, sbg);
		}
	}

	/** Method called by Slick to render the view. Draws overlay if the game is paused.
	 * 
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
	
		level.render(g, false, cam, gc);
		if(isPaused()){
			paused.Render(g,gc);
		} else if(displayEndGame){
			endgame.Render(g,gc);
		}
	}

	/** Get the ID of the state
	 * 
	 * @return The ID of the state
	 */
	@Override
	public int getID() {
		return StateId;
	}

	/** Method to be called when a particular level has been beaten
	 * 
	 * @param gc The GameContainer
	 * @param sbg The StateBasedGame
	 */
	public void exitLevel(GameContainer gc, StateBasedGame sbg){		
		sbg.enterState(Portal2D.LOADSTATE);
	}

	/** Set information regarding which level is to be played
	 * 
	 * @param newLevel The ID of the new level
	 */
	public static void setLevel(Level newLevel){
		level=newLevel;
		paused.setLevelId(level.getLevelId());
		updateCamera();
	}

	/** Get the level being played
	 * 
	 * @return The level being played
	 */
	public static Level getLevel(){
		return level;
	}

	/** Update the camera. Sets the camera to bound based on foreground image
	 * 
	 */
	public static void updateCamera(){
		Image i = level.getFg();
		cam.setBounds(PhysUtils.SlickToJBoxVec(new Vec2(i.getWidth(), i.getHeight())));
	}

	/** Method for handling key presses. Controls pause menu navigation and end game menu.
	 * 
	 * @param key Key pressed as integer
	 * @param c Key pressed as character
	 */
	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in GameState int: " + key);
		if(isPaused()){
			paused.ProcessInput(key);
		}
		
		if(key==InputManager.SELECT && EndGameMenu.isUploadedScores()){
			finishedEndGame=true;
		}
	}

	@Override
	public void keyReleased(int key, char c) {return;}

	@Override
	public void inputEnded() {listening = false;}

	@Override
	public void inputStarted() {listening = true;}

	@Override
	public boolean isAcceptingInput() {return listening;}

	@Override
	public void setInput(Input input) {input.addKeyListener(this);}

	/** Method for handling mouse clicks. Controls portal shooting.
	 * 
	 * @param button Button clicked as integer
	 * @param x x location of click
	 * @param y y location of click
	 */
	@Override
	public void mousePressed(int button, int x, int y) {
		Vec2 clickLoc = PhysUtils.SlickToJBoxVec(new Vec2(x, height - y)).add(cam.getLocation());
		Vec2 playerLoc = level.getLevelPlayer().getLocation();
		Vec2 dir = PhysUtils.unitVector(clickLoc.sub(playerLoc));
		
		// Shoot a portal bullet based on which mouse button was pressed
		PortalBullet pb;
		if (button == Input.MOUSE_LEFT_BUTTON) {
			pb = new PortalBullet(Portal.BLUE, playerLoc, dir);
			level.addPortalBullet(pb, pb.getBodyID());
		} else if (button == Input.MOUSE_RIGHT_BUTTON) {
			pb = new PortalBullet(Portal.ORANGE, playerLoc, dir);
			level.addPortalBullet(pb, pb.getBodyID());
		}
	}
	
	/** Unpause the game
	 */
	public static void unpause() {
		ispaused = false;
	}
	
	/** Toggle pause status
	 */
	public static void switchPaused() {
		ispaused = !ispaused;
	}
	
	/** Check if the game is paused
	 * @return true if paused
	 */
	public static boolean isPaused() {
		return ispaused;
	}

	/** Remove the reference to the level.
	 */
	public static void destroyLevel() {
		level = null;
	}

	/** Set whether the end game dialogue box should be shown
	 * @param displayEndGame true if the end game box should be shown
	 */
	public static void setDisplayEndGame(boolean displayEndGame) {
		GameState.displayEndGame = displayEndGame;
	}
}
