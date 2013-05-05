package gamestates;

import gameengine.PhysUtils;
import gameengine.Portal2D;
import gameworlds.Level;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class LoadingState extends BasicGameState {
	private static int StateId = Portal2D.LOADSTATE; // State ID
	private static boolean finishedloading=false;
	private static boolean drawnloadingscreen=false;

	public LoadingState() throws SlickException
	{
		super();
		AssetManager.loadAllGameAssets();
		System.out.println("resources loaded");	
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
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		
		if(finishedloading){
			sbg.enterState(Portal2D.GAMESTATE);
		}
	}

	public static void loadLevel(StateBasedGame sbg, int levelid) throws SlickException{
		//Level level = AssetManager.loadTestLevel(); debug code
		Level level = AssetManager.loadLevel(levelid);
		GameState.setLevel(level);
		GameState.updateCamera();
		
		//Debug code
		PhysUtils.printAllBodyIds(GameState.getLevel().getPhysWorld());
		
		sbg.enterState(Portal2D.GAMESTATE);
		finishedloading=true;
	}
	
	public void loadNextLevel(StateBasedGame sbg) throws SlickException{
		finishedloading=false;
		int levelid = GameState.getLevel().getLevelId();
		levelid++;
		loadLevel(sbg,levelid);
	}
	


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return StateId;
	}

}
