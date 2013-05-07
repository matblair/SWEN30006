package gamestates;

import gameengine.Portal2D;
import gameengine.InputManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class AchievementState extends BasicGameState implements KeyListener{
	
	/** Menu options for selection **/
	private static int MENU_MAINMENU = 1;
	




	private static int StateId = Portal2D.ACHIEVEMENTSTATE; // State ID

	/** The state id for this part **/
	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private int selected =-1;
	private static String titleText = new String("Achievement");
	private static String subtitleText = new String("Version 0.1");
	private static String description = new String("Description");

	private static Vector<String> menuItems = new Vector<String>();
	private static Vector<String> achievementItems = new Vector<String>();
	private static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	private static int menuItemSelected = 0;
	private static int achievementItemSelected = 1;



	
	ArrayList<String> achievements = null;
	
	public AchievementState() throws SlickException
	{
		super();
		font = AssetManager.requestFontResource("RETROFONT");
		debug = false;
		fullscreen = false;
		//achievements = (ArrayList<String>) AssetManager.getVectorResources();
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		// TODO Auto-generated method stub
		menuItems.add("Main Menu");
		stringMaps.put("Main Menu", MENU_MAINMENU);


		achievementItems.add("achievement 1");
		achievementItems.add("achievement 2");
		achievementItems.add("achievement 3");
		achievementItems.add("achievement 4");
		achievementItems.add("achievement 5");
		achievementItems.add("achievement 6");
		achievementItems.add("achievement 7");
		achievementItems.add("achievement 8");
		achievementItems.add("achievement 9");
		achievementItems.add("achievement 10");
		achievementItems.add("achievement 11");
		achievementItems.add("achievement 12");
		achievementItems.add("achievement 13");
		achievementItems.add("achievement 14");
		achievementItems.add("achievement 15");
		stringMaps.put("achievement 1", 11);
		stringMaps.put("achievement 2", 12);
		stringMaps.put("achievement 3", 13);
		stringMaps.put("achievement 4", 14);
		stringMaps.put("achievement 5", 15);
		stringMaps.put("achievement 6", 21);
		stringMaps.put("achievement 7", 22);
		stringMaps.put("achievement 8", 23);
		stringMaps.put("achievement 9", 24);
		stringMaps.put("achievement 10", 25);
		stringMaps.put("achievement 11", 31);
		stringMaps.put("achievement 12", 32);
		stringMaps.put("achievement 13", 33);
		stringMaps.put("achievement 14", 34);
		stringMaps.put("achievement 15", 35);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		g.setFont(font);


		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);
		g.drawString(subtitleText, 40, 60);

		g.drawString(description, 400, 60);

		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.yellow);
			} else {
				g.setColor(Color.white);
			}
			g.drawString(menuItems.get(i), 40, 700);
		}
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j= 0; j< 5; j++){
				if (j+count ==  achievementItemSelected ) {
					g.setColor(Color.green);
				} else {
					g.setColor(Color.red);
				}
				g.drawString(achievementItems.get(j+count), 40 + j *200, 300+ i*100);
			}
			count +=5;
		}
	}




	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		if(selected!=-1){
			int stateid=Portal2D.ACHIEVEMENTSTATE;
			switch (selected){
			case 1:
				LoadingState.loadLevel(sbg,0);
				stateid=Portal2D.MAINMENUSTATE;
                break;

			case 11:
				description = "description 1";
				break; 
			case 12:
				description = "description 2";
				break; 
			case 21:
				description = "description 6";
				break; 

			}
			selected=-1;
			sbg.enterState(stateid);
		}
		return;

	}

	@Override
	public int getID() {
		return AchievementState.StateId;
	}

	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in AchievementState int: " + key);
		if (key == InputManager.NAV_LEFT) {
			if (achievementItemSelected == 0)
				achievementItemSelected = achievementItems.size() - 1;
			else
				achievementItemSelected--;
		} else if (key == InputManager.NAV_RIGHT) {
			if (achievementItemSelected == achievementItems.size() - 1)
				achievementItemSelected = 0;
			else
				achievementItemSelected++;
		} else if (key == InputManager.NAV_UP) {
			achievementItemSelected -= 5;
		} else if (key == InputManager.NAV_DOWN) {
			achievementItemSelected += 5;
		} else if (key == InputManager.SELECT) {
			selected=stringMaps.get(menuItems.get(menuItemSelected));
		}
	}

	@Override
	public void keyReleased(int key, char c) {System.out.println("Key released in AcheivementState int: " + key);}

	@Override
	public void inputEnded() {listening = false;}

	@Override
	public void inputStarted() {listening = true;}

	@Override
	public boolean isAcceptingInput() {return listening;}

	@Override
	public void setInput(Input input) {input.addKeyListener(this);}

	public static Font getFont() {
		return font;
	}
	public static void setFont(Font newfont) {
		font = newfont;
	}
	public static Vector<String> getMenuItems() {
		return menuItems;
	}
	public static void setMenuItems(Vector<String> menu) {
		menuItems = menu;
	}
	public static Map<String, Integer> getStringMaps() {
		return stringMaps;
	}
	public static void setStringMaps(Map<String, Integer> strings) {
		stringMaps = strings;
	}

}
