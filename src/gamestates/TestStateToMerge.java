package gamestates;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Matt Giuca <mgiuca>
 * Modified by Mathew Blair [mblair]
 */

import gameengine.Portal2D;
import gameworlds.TestWorld;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/** Main class for the Role-Playing Game engine.
 * Handles initialization, input and rendering.
 */
public class TestStateToMerge extends BasicGameState
{
    static AppGameContainer app;
	private TestWorld world;
	/** The state id for this part **/
    private static int StateId = 1;
    
    boolean debug, fullscreen;
    /** Create a new RPG object. 
     * @param gameplaystate */
    public TestStateToMerge(final int gameplaystate)
    {
    	super();
    	TestStateToMerge.setId(gameplaystate);
        debug = false;
        fullscreen = false;
    }
    
    /** Initialise the game state.
     * @param gc The Slick game container object.
     */
    /** Update the game state for a frame.
     * To go into full screen press f, to print debug info to screen press i
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */

    /* (non-Javadoc)
	 * @see org.newdawn.slick.state.BasicGameState#enter(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	@Override
	public void enter(final GameContainer container, final StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		world.updateGameState(container);
  	 
	}

	/** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg)
			throws SlickException {
		 	world = new TestWorld(gc);
		
	}

	@Override
	public void render(final GameContainer gc, final StateBasedGame sbg, final Graphics g)
			throws SlickException {
		// Let World.render handle the rendering.
    	world.render(g,debug);
	}

	@Override
	public void update(final GameContainer gc, final StateBasedGame sbg, final int delta)
			throws SlickException {
	     // Get data about the current input (keyboard state).
        final Input input = gc.getInput();
        // Update the player's movement direction based on keyboard presses.
        double dir_x = 0;
        double dir_y = 0;
        
        if (input.isKeyDown(Input.KEY_DOWN))
            dir_y += 1;
        if (input.isKeyDown(Input.KEY_UP))
            dir_y -= 1;
        if (input.isKeyDown(Input.KEY_LEFT))
            dir_x -= 1;
        if (input.isKeyDown(Input.KEY_RIGHT))
            dir_x += 1;
     
        if(input.isKeyPressed(Input.KEY_F)){
        	fullscreen = (!fullscreen);
            toggleFullscreen(gc, fullscreen);
        }
        // Let World.update decide what to do with this data.
        world.update(dir_x, dir_y, delta, sbg);
    }
    
    public void toggleFullscreen(final GameContainer gc, final boolean fullscreen)
    throws SlickException{    	
    	if(fullscreen){
    		
    		Portal2D.setFullscreen();
    		gc.setFullscreen(fullscreen);
    		world.updateGameState(gc);
    	}
    	else{
    		Portal2D.setResolution(Portal2D.screenwidth, Portal2D.screenheight, false);
    		gc.setFullscreen(fullscreen);
    		world.updateGameState(gc);
    	}		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return StateId;
	}

	public static void setId(final int stateId) {
		TestStateToMerge.StateId = stateId;
		}

}
