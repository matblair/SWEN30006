package gamestates;

import gameengine.Portal2D;
import gameworlds.Level;
import gameworlds.Paused;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class LoadingState extends BasicGameState {
	public static boolean startnew = true;
	private static int StateId = Portal2D.LOADSTATE; // State ID
	public static boolean finishedloading=false;
	public static boolean loadnext=false;	
	public static boolean entermenu=false;

	public static Level level;

	/** Constructor. Triggers loading of all resources.
	 * @throws SlickException
	 */
	public LoadingState() throws SlickException
	{
		super();
		AssetManager.loadAllGameAssets();
		AssetManager.loadInput(1);
	}
	
	/** Method called by Slick when entering the state.
	 */
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
	}
	
	/** Method called by Slick to update the state. Handles loading of the level requested,
	 * and then passes it to GameState, then enters GameState.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		if(loadnext){
			loadNextLevel(sbg);
			sbg.enterState(Portal2D.GAMESTATE);
		}
		if(startnew){
			loadLevel(sbg,0);
			startnew=false;
		}
		if(finishedloading){
			GameState.unpause();
			Paused.setDisplayoptions(false);
			Paused.setDisplayquitoption(false);
			Paused.setDisplayscores(false);

			sbg.enterState(Portal2D.GAMESTATE);
		}if(entermenu){
			entermenu=false;
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}
	}
	
	/** Method called by Slick to render the view. Doesn't do anything as state loads
	 * level in a single call and then exits.
	 */
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {		
	}

	/** Load a particular level for GameState and then enter GameState.
	 * 
	 * @param sbg The StateBasedGame
	 * @param levelid ID of the level to load
	 * @throws SlickException
	 */
	public static void loadLevel(StateBasedGame sbg, int levelid) throws SlickException{
		level = AssetManager.loadLevel(levelid);
		level.setLevelId(levelid);
		GameState.setLevel(level);
		GameState.updateCamera();
		sbg.enterState(Portal2D.GAMESTATE);
		finishedloading=true;
	}

	/** Load the next level
	 * 
	 * @param sbg The StateBasedGame
	 * @throws SlickException
	 */
	public static void loadNextLevel(StateBasedGame sbg) throws SlickException{
		finishedloading=false;
		int levelid = GameState.getLevel().getLevelId();
		levelid++;
		if(levelid>(AssetManager.getLevelXmlResources().size()-1)){
			entermenu=true;
		}else{
			level = AssetManager.loadLevel(levelid);
			GameState.setLevel(level);
			GameState.getLevel().setLevelId(levelid);
			finishedloading=true;
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

	/** Reset the currently active level
	 * 
	 * @throws SlickException
	 */
	public static void reloadLevel() throws SlickException {
		int levelid = GameState.getLevel().getLevelId();

		level = AssetManager.loadLevel(levelid);
		GameState.setLevel(level);
		GameState.getLevel().setLevelId(levelid);
		finishedloading=true;	
	}
}
