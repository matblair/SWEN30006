package gamestates;

import java.util.HashMap;
import java.util.Map;

import gameengine.InputManager;
import gameengine.Portal2D;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class LevelSelectState extends BasicGameState {
	private static int StateId = Portal2D.LEVELSELECTSTATE; // State ID

	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font titleFont;
	private static String titleText = new String("Level Select");
	private static String levelSelect = new String("Level ");
	private static boolean enterselected=false;

	private Map<Integer, Boolean> unlockedstate = new HashMap<Integer,Boolean>();
	private int levelselected=1;
	private static int maxlevel;
	
	private Image unknown;

	/** Constructor
	 * @throws SlickException
	 */
	public LevelSelectState() {
		super();
	}

	/** Method called by Slick to initialise the state. Loads fonts.
	 * 
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		unlockedstate = AssetManager.getLevelUnlocks();
		titleFont = AssetManager.requestFontResource("TITLEFONT");
		debug = false;
		fullscreen = false;
		maxlevel = AssetManager.getLevelXmlResources().size();
		unknown = AssetManager.requestUIElement("LEVELLOCKED");
	}
	
	/** Method called by Slick to update the state. Handles entering a level and
	 * navigating back to the Main Menu
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(enterselected){
			enterselected=false;
			if (!unlockedstate.get(levelselected)) {
				LoadingState.startnew=false;
				LoadingState.loadLevel(sbg, levelselected-1);
				sbg.enterState(Portal2D.LOADSTATE);
			}
		}

		Input input = gc.getInput();
		if (input.isKeyDown(InputManager.BACK)) {
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}
	}

	/** Method called by Slick to render the view
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// Draw the main title
		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);

		// Draw preview of level if unlocked or question mark if locked
		g.setFont(titleFont);
		if(!unlockedstate.get(levelselected)){
			
		} else {
			unknown.drawCentered(gc.getWidth()/2, gc.getHeight()/2);
		}
		
		// Draw level number
		String towrite = levelSelect + levelselected;
		g.drawString(towrite, gc.getWidth()/2-(titleFont.getWidth(towrite)/2), (6*gc.getHeight()/2)/4);
	}

	/** Get the ID of the state
	 * 
	 * @return The ID of the state
	 */
	@Override
	public int getID() {
		return LevelSelectState.StateId;
	}

	/** Method for handling key presses. Controls navigation in the state
	 * 
	 * @param key Key pressed as integer
	 * @param c Key pressed as character
	 */
	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in AchievementState int: " + key);
		System.out.println(InputManager.NAV_LEFT + " " + InputManager.NAV_RIGHT);
		if (key == InputManager.NAV_LEFT){
			levelselected--;
			if(levelselected<1){
				levelselected=maxlevel-1;
			}
		}else if (key == InputManager.NAV_RIGHT){ 
			levelselected++;
			if(levelselected>maxlevel-1){
				levelselected=1;
			} 
		}else if (key == InputManager.SELECT) {
			enterselected=true;
		}
	}

	@Override
	public void keyReleased(int key, char c) {return;}

	@Override
	public void inputEnded() {listening = false;}

	@Override
	public void inputStarted() {listening = true;}

	@Override
	public boolean isAcceptingInput() {return listening;}

	@Override
	public void setInput(Input input) {input.addKeyListener(this);}
}
