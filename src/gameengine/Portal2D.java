package gameengine;

import gamestates.AchievementState;
import gamestates.HighScoreState;
import gamestates.LevelSelectState;
import gamestates.LoadingState;
import gamestates.MainMenuState;
import gamestates.OptionState;
import gamestates.GameState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.state.StateBasedGame;

public class Portal2D extends StateBasedGame {
	public static final int LOADSTATE = 0;
	public static final int MAINMENUSTATE = 1;
	public static final int ACHIEVEMENTSTATE = 2;
	public static final int HIGHSCORESTATE =3;
	public static final int OPTIONSTATE = 4;
	public static final int GAMESTATE = 5;
	public static final int LEVELSELECTSTATE= 6;
	
	// Live game key (only swap if you are testing the game for real with real levels) 8b32c6268959f7bf940d3c0e61532aaa0d82434e
	public static final String gameKey="93675768a5117314d0b09841190b9ad54dd59212";
	public static final String name="Mat";
	public static final boolean online = false;

    /** Screen width, in pixels. */
    public static final int screenwidth = 1280;
    /** Screen height, in pixels. */
    public static final int screenheight = 800;
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
		SoundStore.get().init();
		SoundStore.get().setDeferredLoading(false);
		this.addState(new LoadingState());
		this.addState(new MainMenuState());
		this.addState(new GameState());
		this.addState(new AchievementState());
		this.addState(new HighScoreState());
		this.addState(new OptionState());
		this.addState(new LevelSelectState());
	    this.enterState(MAINMENUSTATE);
	}
	
	 /** Start-up method. Creates the game and runs it.
     */
    public static void main(final String[] args)
    throws SlickException
    {
    	app = new AppGameContainer(new Portal2D());
        app.setDisplayMode(screenwidth, screenheight, false);
        app.setShowFPS(true);
        app.setVSync(true);
        app.setTargetFrameRate(60);
        app.start();
    }   
    
    public static void setResolution(final int width, final int height, final boolean key) throws SlickException{
    	app.setDisplayMode(width, height, key);
    }

	public static void setFullscreen() throws SlickException {
		app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
	}
}
