package gamestates;

import gameengine.Camera;
import gameengine.PhysUtils;
import gameengine.Portal2D;
import gameworlds.Level;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class GameState extends BasicGameState implements KeyListener {
	private static int StateId = Portal2D.TESTGAMESTATE; // State ID
	private Camera cam;
	private boolean listening=true;
	private Level level;

	public GameState()
	{
		super();
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		level = new Level(gc);
		cam = new Camera(PhysUtils.SlickToJBoxVec(new Vec2(level.getBg().getWidth(), level.getBg().getHeight())));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		level.render(g, false, cam, gc);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		//Hello
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_RIGHT) && level.getPlayer().getBody().getContactList()==null) {
			level.getPlayer().getBody().applyForceToCenter(new Vec2(10,0));
		} else if ((input.isKeyDown(Input.KEY_RIGHT))){
			level.getPlayer().getBody().applyLinearImpulse(new Vec2((float) 0.1,0), level.getPlayer().getBody().getPosition());
		}
		if (input.isKeyDown(Input.KEY_LEFT) && level.getPlayer().getBody().getContactList()==null) {
			level.getPlayer().getBody().applyForceToCenter(new Vec2(-10,0));
		} else if ((input.isKeyDown(Input.KEY_LEFT))){
			level.getPlayer().getBody().applyLinearImpulse(new Vec2((float) -0.1,0), level.getPlayer().getBody().getPosition());
		}
		if (input.isKeyPressed(Input.KEY_SPACE) && level.getPlayer().getBody().getContactList()!=null){
			level.getPlayer().getBody().applyLinearImpulse(new Vec2(0,15), level.getPlayer().getBody().getPosition());
		}
		cam.follow(gc, level.getPlayer());
		level.update(0, delta, sbg);
	}

	@Override
	public int getID() {
		return StateId;
	}

	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in LevelState int: " + key);
	}

	@Override
	public void keyReleased(int key, char c) {System.out.println("Key released in LevelState int: " + key);}

	@Override
	public void inputEnded() {listening = false;}

	@Override
	public void inputStarted() {listening = true;}

	@Override
	public boolean isAcceptingInput() {return listening;}

	@Override
	public void setInput(Input input) {input.addKeyListener(this);}

	public static void setId(final int stateId) {
		GameState.StateId = stateId;
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
}
