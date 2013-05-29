package gameworlds;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public abstract class InGameMenu {
	/** Our fonts to use for the in game menu **/
	protected static Font font;
	protected static Font titlefont = AssetManager.requestFontResource("TITLEFONT");
	/** Common attributes to each menu **/
	protected int selected =-1;
	protected static String TITLE = "PAUSED";
	
	/** Abstract methods that have to be implemented by each class **/
	abstract public void Render(Graphics g, GameContainer gc);
	abstract public void Update(Graphics g, GameContainer gc, StateBasedGame sbg);
	abstract public void ProcessInput(int key);

}
