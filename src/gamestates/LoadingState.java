package gamestates;

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
	}

	public void loadLevel(StateBasedGame sbg) throws SlickException{
		Level level = AssetManager.loadLevel("TESTLEVEL");
		GameState.setLevel(level);
		sbg.enterState(Portal2D.GAMESTATE);
	}
	
	public void loadNextLevel(){
		
	}
	


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return StateId;
	}

}
