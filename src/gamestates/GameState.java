package gamestates;


import gameengine.Camera;
import gameengine.InputManager;
import gameengine.PhysUtils;
import gameengine.Portal2D;
import gameobjects.Portal;
import gameworlds.Level;
import gameworlds.Paused;

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
	private static int StateId = Portal2D.GAMESTATE; // State ID
	private static Camera cam;
	private boolean listening=true;
	private static Level level;
	private static Paused paused;
	private static int height;
	private static boolean ispaused;
	
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
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
	
		level.render(g, false, cam, gc);
		if(isIspaused()){
			paused.Render(g,gc);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		height = gc.getHeight();
		int dir_x = 0;
		Input input = gc.getInput();

		if(!isIspaused()){
			if (input.isKeyDown(InputManager.MOVE_RIGHT))
				dir_x++;
			if (input.isKeyDown(InputManager.MOVE_LEFT))
				dir_x--;
			if (input.isKeyPressed(InputManager.JUMP))
				level.getLevelPlayer().jump();
			if (input.isKeyPressed(InputManager.INTERACT)){
				level.getLevelPlayer().interact(level.getPhysWorld(), level);
			}if (input.isKeyPressed(InputManager.SHOOT_BLUE)) {
				level.playerShootPortal(Portal.BLUE, new Vec2 (0,1.5f));
			}if( input.isKeyPressed(InputManager.PAUSE)){
				setIspaused(!isIspaused());
			}
			level.update(dir_x,0, delta, sbg);
			cam.follow(gc, level.getLevelPlayer());
		}else {
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

	public void toggleFullscreen(final GameContainer gc, final boolean fullscreen)
			throws SlickException{    	
		if(fullscreen){
			Portal2D.setFullscreen();
			gc.setFullscreen(fullscreen);
			level.updateGameState(gc);
		}
		else{
			Portal2D.setResolution(Portal2D.screenwidth, Portal2D.screenheight, false);
			gc.setFullscreen(fullscreen);
			level.updateGameState(gc);
		}		
	}

	public void exitLevel(GameContainer gc, StateBasedGame sbg){		
		sbg.enterState(Portal2D.LOADSTATE);
	}

	public static void setLevel(Level newLevel){
		level=newLevel;
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
		if(isIspaused()){
			paused.ProcessInput(key);
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		System.out.println("Key released in GameState int: " + key);
	}


	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		ispaused=false;
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
		try {
			if (button == Input.MOUSE_LEFT_BUTTON)
				level.playerShootPortal(Portal.BLUE, clickLoc.add(cam.getLocation()));
			else if (button == Input.MOUSE_RIGHT_BUTTON)
				level.playerShootPortal(Portal.ORANGE, clickLoc.add(cam.getLocation()));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static void switchPaused() {
		setIspaused(!isIspaused());
	}

	public static void destroyLevel() {
		level = null;
	}

	/**
	 * @return the ispaused
	 */
	public static boolean isIspaused() {
		return ispaused;
	}

	/**
	 * @param ispaused the ispaused to set
	 */
	public static void setIspaused(boolean ispaused) {
		GameState.ispaused = ispaused;
	}
}
