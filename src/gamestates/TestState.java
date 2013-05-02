package gamestates;

import gameobjects.Player;
import gameworlds.TestWorld;
import gameengine.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class TestState extends BasicGameState implements KeyListener {
	private static int StateId = Portal2D.TESTGAMESTATE; // State ID
	private Image bg;
	private World world;
	private Camera cam;
	
	private boolean listening=true;
	private Player player;
	private TestWorld level;
	
	private int velocityIterations = 6;
	private int positionIterations = 2;

	public TestState()
	{
		super();
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		bg = new Image("assets/levels/bg.png");
		cam = new Camera(PhysUtils.SlickToJBoxVec(new Vec2(bg.getWidth(), bg.getHeight())));

		// Static Body
		Vec2 gravity = new Vec2(0,-9.8f);
		world = new World(gravity);

		PhysUtils.addWall(world, 0, 0, 100, 1); //floor
		PhysUtils.addWall(world, 0, 0, 0.5f, 10); //left wall
		PhysUtils.addWall(world, 0, 8.823f, 100, 0); //top
		
		PhysUtils.addWall(world, 10, 2.5f, 4, 0.25f);
		PhysUtils.addWall(world, 14, 3.5f, 4, 0.25f);

		// Dynamic Body
		player = new Player("/assets/sprites/chell.png",new Vec2(2, 5), world);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObject(player, cam);
		//world.drawDebugData();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		float timeStep = (float)delta/1000;
		world.step(timeStep, velocityIterations, positionIterations);

		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_RIGHT) && player.getBody().getContactList()==null) {
			player.getBody().applyForceToCenter(new Vec2(10,0));
		} else if ((input.isKeyDown(Input.KEY_RIGHT))){
			player.getBody().applyLinearImpulse(new Vec2((float) 0.1,0), player.getBody().getPosition());
		}
		if (input.isKeyDown(Input.KEY_LEFT) && player.getBody().getContactList()==null) {
			player.getBody().applyForceToCenter(new Vec2(-10,0));
		} else if ((input.isKeyDown(Input.KEY_LEFT))){
			player.getBody().applyLinearImpulse(new Vec2((float) -0.1,0), player.getBody().getPosition());
		}
		if (input.isKeyPressed(Input.KEY_SPACE) && player.getBody().getContactList()!=null){
			player.getBody().applyLinearImpulse(new Vec2(0,15), player.getBody().getPosition());
		}
		
		cam.follow(gc, player);
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
		TestState.StateId = stateId;
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
