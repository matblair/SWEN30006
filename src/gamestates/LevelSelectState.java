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

	@SuppressWarnings("unused")
	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font, titleFont;
	private static String titleText = new String("Level Select");
	private static String levelSelect = new String("Level ");
	private static String subtitleText = new String("Use left and right arrow keys to select level:");
	private static boolean enterselected=false;

	private Map<Integer, Boolean> unlockedstate = new HashMap<Integer,Boolean>();
	private int levelselected=1;
	private static int maxlevel;

	private static int screenwidth;
	private static int midx, midy;
	
	private Image unknown;

	public LevelSelectState(){
		unlockedstate = AssetManager.getLevelUnlocks();
		font = AssetManager.requestFontResource("RETROFONT");
		titleFont = AssetManager.requestFontResource("TITLEFONT");
		debug = false;
		fullscreen = false;
		maxlevel = AssetManager.getLevelXmlResources().size();
		unknown = AssetManager.requestUIElement("LEVELLOCKED");

	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		screenwidth = gc.getWidth();
		midx = screenwidth/2;
		midy = gc.getHeight()/2;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);

		g.setFont(font);
		g.drawString(subtitleText, 40, 80);

		g.setFont(titleFont);
		if(!unlockedstate.get(levelselected)){
			
		} else {
			unknown.drawCentered(gc.getWidth()/2, gc.getHeight()/2);
		}
		String towrite = levelSelect + levelselected;
		g.drawString(towrite, midx-(titleFont.getWidth(towrite)/2), (6*midy)/4);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(enterselected && !unlockedstate.get(levelselected)){
			enterselected=false;
			LoadingState.startnew=false;
			LoadingState.loadLevel(sbg, levelselected-1);
			sbg.enterState(Portal2D.LOADSTATE);
		}

		Input input = gc.getInput();
		if (input.isKeyDown(InputManager.BACK)) {
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}
	}

	@Override
	public int getID() {
		return LevelSelectState.StateId;
	}


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

}
