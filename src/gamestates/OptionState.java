package gamestates;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import gameengine.InputManager;
import gameengine.Portal2D;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class OptionState extends BasicGameState implements KeyListener{
	private static int StateId = Portal2D.OPTIONSTATE; // State ID

	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static int mainMenuItemSelected = 0;
	
	protected static Vector<Vec2> screenSizes = new Vector<Vec2>();


	private static boolean displayscreensize=false;
	private static boolean displaysoundlevel=false;
	private static boolean displayinputload=false;
	private static boolean backtomenu=false;

	private static int screenItemSelected=0;
	private static int soundLevelSelected=0;
	private static int inputItemSelected=0;

	private static final int MENU_SCREENSELECT=0;
	private static final int MENU_SOUNDLEVEL=1;
	private static final int MENU_DISPLAYINPUT=2;

	private static final int SOUNDINTERVAL = 1;

	private static int MAXSOUNDLEVEL=100;

	////////////////////////////////////

	/** The state id for this part **/
	boolean debug, fullscreen;
	@SuppressWarnings("unused")
	private static Font font;
	private static Font title;
	private static Font mediumfont;
	@SuppressWarnings("unused")
	private int selected =-1;
	private static String titleText = new String("Options!");

	public OptionState(){
		font = AssetManager.requestFontResource("RETROFONT");
		mediumfont = AssetManager.requestFontResource("PAUSEFONT");
		title = AssetManager.requestFontResource("TITLEFONT");
		debug = false;
		fullscreen = false;
	}
	

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		backtomenu=false;
		mainMenuItemSelected=0;
		soundLevelSelected = (int) (container.getSoundVolume());
	}


	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		menuItems.add("Screen Size");
		menuItems.add("Audio Level");
		menuItems.add("Controller Select");
		stringMaps.put("Screen Size", MENU_SCREENSELECT);
		stringMaps.put("Audio Level", MENU_SOUNDLEVEL);
		stringMaps.put("Controller Select", MENU_DISPLAYINPUT);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.setFont(title);
		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);
		g.setFont(mediumfont);
		if(displayscreensize){
			g.drawString(String.valueOf(screenItemSelected), 400,150);
		}else if(displaysoundlevel){
			g.drawString(String.valueOf(soundLevelSelected), 400,200);
		}else if (displayinputload){
			g.drawString(String.valueOf(inputItemSelected), 400,250);
		}


		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  mainMenuItemSelected) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.darkGray);
			}
			g.drawString(menuItems.get(i), 40, 150 + i * 50);
		}	


	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(backtomenu){
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}	
		System.out.println(mainMenuItemSelected);
		switch (mainMenuItemSelected){
		case MENU_SCREENSELECT:
			displayscreensize=true;
			displaysoundlevel=false;
			displayinputload=false;
			break;
		case MENU_SOUNDLEVEL:
			displayscreensize=false;
			displaysoundlevel=true;
			displayinputload=false;
			gc.setSoundVolume((float)soundLevelSelected);
			break;
		case MENU_DISPLAYINPUT:
			displayscreensize=false;
			displaysoundlevel=false;
			displayinputload=true;			
			break;
		}
		return;
	}

	@Override
	public int getID() {
		return StateId;
	}

	public void updateKeys(int newkey) throws SlickException{
		AssetManager.loadInput(newkey);
	}

	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in AchievementState int: " + key);
		System.out.println(InputManager.NAV_LEFT + " " + InputManager.NAV_RIGHT);
		if (key == InputManager.NAV_UP) {
			if (mainMenuItemSelected == 0)
				mainMenuItemSelected=(menuItems.size() - 1);
			else
				mainMenuItemSelected--;
		} else if (key == InputManager.NAV_DOWN) {
			if (mainMenuItemSelected == menuItems.size() - 1)
				mainMenuItemSelected=0;
			else
				mainMenuItemSelected++;
		}else if (key == InputManager.NAV_LEFT){
			if(displayscreensize){

			}else if(displaysoundlevel){
				if(soundLevelSelected>0){
				soundLevelSelected = soundLevelSelected-SOUNDINTERVAL;
				}
			}else if (displayinputload){
				if(inputItemSelected > 0){
					inputItemSelected=inputItemSelected-1;
					try {
						AssetManager.loadInput(inputItemSelected);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
			}
		}else if (key == InputManager.NAV_RIGHT){ 
			if(displayscreensize){

			}else if(displaysoundlevel){
				if(soundLevelSelected<MAXSOUNDLEVEL){
				soundLevelSelected = soundLevelSelected+SOUNDINTERVAL;
				}
			}else if (displayinputload){
				if(inputItemSelected < (AssetManager.getInputResources().size()-1)){
					inputItemSelected=inputItemSelected+1;
					try {
						AssetManager.loadInput(inputItemSelected);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}

			}
		}else if (key == InputManager.SELECT) {
			selected=(stringMaps.get(menuItems.get(mainMenuItemSelected)));
		} else if (key == InputManager.BACK){
			backtomenu=true;
		}
	}
	
}
