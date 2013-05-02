package gameengine;

import gamestates.MainMenuState;
import gamestates.PauseState;
import gamestates.TestState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;

public class Portal2D extends StateBasedGame {
	public static final int PAUSESTATE = 2;
	public static final int TESTGAMESTATE = 1;
	public static final int MAINMENUSTATE = 0;

    /** Screen width, in pixels. */
    public static final int screenwidth = 800;
    /** Screen height, in pixels. */
    public static final int screenheight = 600;
	/** The app game container **/
    public static AppGameContainer app;
	/** Whether the game is running fullscreen or not **/
	public boolean fullscreen;
    
	public Portal2D() {
		super("Portal 2D");
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.StateBasedGame#initStatesList(org.newdawn.slick.GameContainer)
	 */
	@Override
	public void initStatesList(final GameContainer gc) throws SlickException {
		LoadingList.setDeferredLoading(true);
		this.addState(new MainMenuState());
		this.addState(new TestState());
		this.addState(new PauseState(PAUSESTATE));
	    this.enterState(MAINMENUSTATE);
	}
	
	 /** Start-up method. Creates the game and runs it.
     */
    public static void main(final String[] args)
    throws SlickException
    {
    	app = new AppGameContainer(new Portal2D());
        app.setDisplayMode(screenwidth, screenheight, false);
        app.setShowFPS(false);
        app.start();
     
    }    
    public static void setResolution(final int width, final int height, final boolean key) throws SlickException{
    	app.setDisplayMode(width, height, key);
    }

	public static void setFullscreen() throws SlickException {
		app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
	}
}
