package gameworlds;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public abstract class InGameMenu {
	
	/** The state id for this part **/
	protected boolean listening=true;
	protected boolean debug, fullscreen;
	protected static Font font;
	protected static Font titlefont = AssetManager.requestFontResource("TITLEFONT");
	protected int selected =-1;
	protected static String TITLE = "PAUSED";

	abstract public void Render(Graphics g, GameContainer gc);
	abstract public void Update(Graphics g, GameContainer gc, StateBasedGame sbg);
	abstract public void ProcessInput(int key);
		

}
