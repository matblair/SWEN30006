package gamestates;

import java.util.ArrayList;
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
import org.newdawn.slick.Image;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;
import resourcemanagers.SoundController;

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

	private static final int MENU_SOUNDLEVEL=0;
	private static final int MENU_DISPLAYINPUT=1;
	
	private static ArrayList<Image> volimages= new ArrayList<Image>();
	private static ArrayList<Image> inputimages= new ArrayList<Image>();

	private static final int SOUNDINTERVAL = 5;

	private static Font title;
	private static Font mediumfont;
	private static String titleText = new String("Options");
	
	/** Constructor
	 * @throws SlickException
	 */
	public OptionState(){
		super();
	}


	/** Method called by Slick to initialise the state. Loads fonts, and menu items
	 * and associated assets
	 */
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// Load the fonts
		mediumfont = AssetManager.requestFontResource("PAUSEFONT");
		title = AssetManager.requestFontResource("TITLEFONT");
		
		// Add menu items
		menuItems.add("Audio Level");
		menuItems.add("Controller Select");
		stringMaps.put("Audio Level", MENU_SOUNDLEVEL);
		stringMaps.put("Controller Select", MENU_DISPLAYINPUT);
		volimages.add(AssetManager.requestUIElement("VOL"));
		volimages.add(AssetManager.requestUIElement("VOL0"));
		volimages.add(AssetManager.requestUIElement("VOL1"));
		volimages.add(AssetManager.requestUIElement("VOL2"));
		volimages.add(AssetManager.requestUIElement("VOL3"));
		volimages.add(AssetManager.requestUIElement("VOL4"));
		volimages.add(AssetManager.requestUIElement("VOL5"));
		volimages.add(AssetManager.requestUIElement("VOL6"));
		volimages.add(AssetManager.requestUIElement("VOL7"));
		volimages.add(AssetManager.requestUIElement("VOL8"));
		volimages.add(AssetManager.requestUIElement("VOL9"));
		inputimages.add(AssetManager.requestUIElement("INPUTONE"));
		inputimages.add(AssetManager.requestUIElement("INPUTTWO"));
	}

	/** Method called by Slick when entering the state. Partially resets state.
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		
		backtomenu=false;
		mainMenuItemSelected=0;
		soundLevelSelected = SoundController.getVolume();
	}
	
	/** Method called by Slick to update the state. Handles exiting the state and 
	 * which option needs to be shown.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(backtomenu){
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}	
		switch (mainMenuItemSelected){
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

	/** Method called by Slick to render the view
	 */
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
			drawVolume();
		}else if (displayinputload){
			drawInput();
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

	/** Method to draw representation of current volume.
	 */
	public void drawVolume(){
		int imagelaod = (int)(soundLevelSelected/10);
		Image newimage = volimages.get(imagelaod).getScaledCopy(400, 200);
		newimage.drawCentered(750, 370);
	}

	/** Method to draw input information.
	 */
	public void drawInput(){
		inputimages.get(inputItemSelected).drawCentered(750, 300);
	}
	
	/** Get the ID of the state
	 * 
	 * @return The ID of the state
	 */
	@Override
	public int getID() {
		return StateId;
	}

	/** Get AssetManager to modify the input.
	 * 
	 * @param newkey The key associated with the input to be loaded.
	 * @throws SlickException
	 */
	public void updateKeys(int newkey) throws SlickException{
		AssetManager.loadInput(newkey);
	}

	/** Method for handling key presses. Controls navigation and changing of selected
	 * menu item in the state.
	 * 
	 * @param key Key pressed as integer
	 * @param c Key pressed as character
	 */
	@Override
	public void keyPressed(int key, char c) {
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
					SoundController.setVolume(soundLevelSelected);
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
				if(soundLevelSelected<SoundController.MAXLEVEL){
					soundLevelSelected = soundLevelSelected+SOUNDINTERVAL;
					SoundController.setVolume(soundLevelSelected);
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
		} else if (key == InputManager.BACK){
			backtomenu=true;
		}
	}

}
