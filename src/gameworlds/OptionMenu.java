package gameworlds;

import gameengine.InputManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;
import resourcemanagers.SoundController;

public class OptionMenu extends InGameMenu {

	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static int mainMenuItemSelected = 0;

	private static final int MENU_SOUNDLEVEL=0;
	private static final int MENU_DISPLAYINPUT=1;

	private static boolean displayscreensize=false;
	private static boolean displaysoundlevel=false;
	private static boolean displayinputload=false;

	private static int screenItemSelected=0;
	private static int soundLevelSelected=0;
	private static int inputItemSelected=0;
	

	private static ArrayList<Image> volimages= new ArrayList<Image>();
	private static ArrayList<Image> inputimages= new ArrayList<Image>();


	private static final int SOUNDINTERVAL=5;

	public OptionMenu(){
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
		soundLevelSelected = SoundController.getVolume();
	}

	@Override
	public
	void Render(Graphics g, GameContainer gc) {
		if (displayinputload){
			AssetManager.requestUIElement("PAUSEBG").drawCentered(gc.getWidth()/2, gc.getHeight()/2);
			drawInput(gc);
		}else {
			if(displayscreensize){
				g.drawString(String.valueOf(screenItemSelected), 800,340);
			}else if(displaysoundlevel){
				drawVolume();
			}
			for (int i = 0; i < menuItems.size(); i++) {
				if (i ==  mainMenuItemSelected) {
					g.setColor(Color.orange);
				} else {
					g.setColor(Color.darkGray);
				}
				g.drawString(menuItems.get(i), 430, 340 + i * 30);
			}
		}
	}


	@Override
	public
	void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		switch (mainMenuItemSelected){
		case MENU_SOUNDLEVEL:
			displayscreensize=false;
			displaysoundlevel=true;
			displayinputload=false;
			gc.setSoundVolume(soundLevelSelected);
			break;
		case MENU_DISPLAYINPUT:
			displayscreensize=false;
			displaysoundlevel=false;
			displayinputload=true;			
			break;
		}
		return;
	}

	public void drawInput(GameContainer gc){
		inputimages.get(inputItemSelected).drawCentered(gc.getWidth()/2, gc.getHeight()/2);
	}

	@Override
	public void ProcessInput(int key) {
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
		}else if (key == InputManager.SELECT) {
			selected=(stringMaps.get(menuItems.get(mainMenuItemSelected)));
		} else if (key == InputManager.BACK){
			Paused.setDisplayoptions(false);
		}
	}	

	public void updateKeys(int newkey) throws SlickException{
		AssetManager.loadInput(newkey);
	}

	public void drawVolume(){
		int imagelaod = (int)(soundLevelSelected/10);
		volimages.get(imagelaod).drawCentered(750, 370);
	}


}
