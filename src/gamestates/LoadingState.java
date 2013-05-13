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

	public LoadingState() throws SlickException
	{
		super();
		AssetManager.loadAllGameAssets();
		AssetManager.loadInput(0);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

	}

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
			GameState.setIspaused(false);
			Paused.setDisplayachievements(false);
			Paused.setDisplayoptions(false);
			Paused.setDisplayquitoption(false);
			Paused.setDisplayscores(false);

			sbg.enterState(Portal2D.GAMESTATE);
		}if(entermenu){
			entermenu=false;
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}
	}

	public static void loadLevel(StateBasedGame sbg, int levelid) throws SlickException{
		//Level level = AssetManager.loadTestLevel(); debug code
		level = AssetManager.loadLevel(levelid);
		GameState.setLevel(level);
		GameState.updateCamera();
		GameState.getLevel().setLevelId(levelid);		
		sbg.enterState(Portal2D.GAMESTATE);
		finishedloading=true;
	}

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



	@Override
	public int getID() {
		return StateId;
	}

	public static void reloadLevel() throws SlickException {
		int levelid = GameState.getLevel().getLevelId();

		level = AssetManager.loadLevel(levelid);
		GameState.setLevel(level);
		GameState.getLevel().setLevelId(levelid);
		finishedloading=true;	
	}

}
