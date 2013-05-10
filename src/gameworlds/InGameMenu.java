package gameworlds;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class InGameMenu {
	
	/** The state id for this part **/
	protected boolean listening=true;
	protected boolean debug, fullscreen;
	protected static Font font;
	protected int selected =-1;

	abstract public void Render(Graphics g, GameContainer gc);
	abstract public void Update(Graphics g, GameContainer gc, StateBasedGame sbg);
	abstract public void ProcessInput(int key);
		

}
