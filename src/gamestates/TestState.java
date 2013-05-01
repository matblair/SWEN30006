package gamestates;

import gameobjects.Player;
import gameworlds.TestWorld;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
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
	private boolean listening=true;
	private Player player;
	private TestWorld level;

	/** The state id for this part **/
    private static int StateId = 1;
    boolean debug, fullscreen;
	private Image bg;
	private World world;
	 // Setup world
    int velocityIterations = 6;
    int positionIterations = 2;
    Body body;
	
    public TestState(final int gameplaystate)
    {
    	super();
    	TestStateToMerge.setId(gameplaystate);
        debug = false;
        fullscreen = false;
    }

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		bg = new Image("assets/levels/floor.png");
		world = new World(new Vec2(0.0f, -9.8f));
	
		// Static Body
	    Vec2 gravity = new Vec2(0,-10);
	    world = new World(gravity);
	    BodyDef groundBodyDef = new BodyDef();
	    groundBodyDef.position.set(0, 0);
	    Body groundBody = world.createBody(groundBodyDef);
	    PolygonShape groundBox = new PolygonShape();
	    groundBox.setAsBox(5,1);
	    groundBody.createFixture(groundBox, 0);
	    
	    // Dynamic Body
	    player = new Player("/assets/sprites/chell.png",new Vec2(0, 5), world);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		bg.draw(0, 0);
		player.draw(gc);
		world.drawDebugData();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		float timeStep = (float)delta/1000;
		world.step(timeStep, velocityIterations, positionIterations);
		
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_RIGHT) && player.getBody().getContactList()==null) {
			player.getBody().applyForceToCenter(new Vec2(50,0));
		} else if ((input.isKeyDown(Input.KEY_RIGHT))){
			player.getBody().applyLinearImpulse(new Vec2((float) 0.3,0), player.getBody().getPosition());
		}
		if (input.isKeyDown(Input.KEY_LEFT) && player.getBody().getContactList()==null) {
			player.getBody().applyForceToCenter(new Vec2(-50,0));
		} else if ((input.isKeyDown(Input.KEY_LEFT))){
			player.getBody().applyLinearImpulse(new Vec2((float) -0.3,0), player.getBody().getPosition());
		}
		if (input.isKeyPressed(Input.KEY_SPACE) && player.getBody().getContactList()!=null){
			player.getBody().applyLinearImpulse(new Vec2(0,60), player.getBody().getPosition());
		}
	}

	@Override
	public int getID() {
		return TestState.StateId;
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
