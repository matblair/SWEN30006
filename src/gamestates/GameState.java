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

	public GameState()
	{
		super();
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		level = new Level();
		cam = new Camera();
		paused = new Paused(level.getLevelId());
		endgame = new EndGameMenu();
		SoundController.initialise();
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		paused.setLevelId(level.getLevelId());
		EndGameMenu.setUploadedScores(false);
		ispaused=false;
		SoundController.play(Sound.INGAME);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		SoundController.stopMusic();
	}

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
			}if( input.isKeyPressed(InputManager.PAUSE)){
				setIspaused(!isPaused());
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

	public static void setId(final int stateId) {
		GameState.StateId = stateId;
	}

	@Override
	public int getID() {
		return StateId;
	}


	public void exitLevel(GameContainer gc, StateBasedGame sbg){		
		sbg.enterState(Portal2D.LOADSTATE);
	}

	public static void setLevel(Level newLevel){
		level=newLevel;
		paused.setLevelId(level.getLevelId());
		updateCamera();
	}

	public static Level getLevel(){
		return level;
	}

	public static void updateCamera(){
		cam.setBounds(PhysUtils.SlickToJBoxVec(new Vec2(level.getBg().getWidth(), level.getBg().getHeight())));
	}

	// Key Listener stuff //
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
	public void keyReleased(int key, char c) {
		System.out.println("Key released in GameState int: " + key);
	}

	@Override
	public void inputEnded() {listening = false;}

	@Override
	public void inputStarted() {listening = true;}

	@Override
	public boolean isAcceptingInput() {return listening;}

	@Override
	public void setInput(Input input) {input.addKeyListener(this);}

	// Mouse listener stuff //
	@Override
	public void mousePressed(int button, int x, int y) {
		Vec2 clickLoc = PhysUtils.SlickToJBoxVec(new Vec2(x, height - y));
		Vec2 playerLoc = level.getLevelPlayer().getLocation();
		Vec2 dir = PhysUtils.unitVector(clickLoc.sub(playerLoc));
		
		PortalBullet pb;
		if (button == Input.MOUSE_LEFT_BUTTON) {
			pb = new PortalBullet(Portal.BLUE, playerLoc, dir);
			level.addPortalBullet(pb, pb.getBodyID());
		} else if (button == Input.MOUSE_RIGHT_BUTTON) {
			pb = new PortalBullet(Portal.ORANGE, playerLoc, dir);
			level.addPortalBullet(pb, pb.getBodyID());
		}
	}
	
	public static void switchPaused() {
		setIspaused(!isPaused());
	}

	public static void destroyLevel() {
		level = null;
	}

	/**
	 * @return the ispaused
	 */
	public static boolean isPaused() {
		return ispaused;
	}

	/**
	 * @param ispaused the ispaused to set
	 */
	public static void setIspaused(boolean ispaused) {
		GameState.ispaused = ispaused;
	}

	/**
	 * @param displayEndGame the displayEndGame to set
	 */
	public static void setDisplayEndGame(boolean displayEndGame) {
		GameState.displayEndGame = displayEndGame;
	}
}
