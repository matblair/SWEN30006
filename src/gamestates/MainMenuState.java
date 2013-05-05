package gamestates;

import gameengine.Portal2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;


public class MainMenuState extends BasicGameState implements KeyListener {
	/** Menu options for selection **/
	private static int MENU_STARTGAME=1;
	private static int MENU_ACHIEVEMENTS=2;
	private static int MENU_HIGHSCORES=3;
	private static int MENU_OPTIONS=4;
	private static int MENU_TESTGAME=5;

	
	
	/** The state id for this part **/
	private static int StateId = Portal2D.MAINMENUSTATE;
	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private static Image menubg;
	private static Image menutile;
	private int selected =-1;
	private static String titleText = new String("Welcome to Portal 2D");
	private static String subtitleText = new String("Version 0.4");

	private static Vector<String> menuItems = new Vector<String>();
	private static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	private static int menuItemSelected = 0;



	public MainMenuState() throws SlickException
	{
		super();
		font = AssetManager.requestFontResource("RETROFONT");
		menubg = AssetManager.requestUIElement("MENUBG");
		menutile = AssetManager.requestUIElement("MENUTILING");
		debug = false;
		fullscreen = false;

	}
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		// Load all resources in resource manager//
		// Menu items
		
		menuItems.add("Start Game");
		menuItems.add("Achievements");
		menuItems.add("High Scores");
		menuItems.add("Options");
		menuItems.add("Test World");
		stringMaps.put("Start Game", MENU_STARTGAME);
		stringMaps.put("Achievements", MENU_ACHIEVEMENTS);
		stringMaps.put("High Scores", MENU_HIGHSCORES);
		stringMaps.put("Options", MENU_OPTIONS);
		stringMaps.put("Test World",  MENU_TESTGAME);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		menubg.draw(50,50);
		g.setFont(font);
		g.setBackground(Color.white);
		g.setColor(Color.gray);
		g.drawString(titleText, 140, 380);
		g.drawString(subtitleText, 227, 400);

		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.darkGray);
			}
			g.drawString(menuItems.get(i), 120, 440 + i * 20);
		}
	

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(selected!=-1){
			int stateid=Portal2D.MAINMENUSTATE;
			switch (selected){
			case 1:
				LoadingState.loadLevel(sbg,0);
				stateid=Portal2D.LOADSTATE;
                break;
			case 2:
				stateid=Portal2D.ACHIEVEMENTSTATE;
                break;
			case 3:
				stateid=Portal2D.HIGHSCORESTATE;
                break;
			case 4:
				stateid=Portal2D.OPTIONSTATE;
                break;
			case 5:
				LoadingState.loadLevel(sbg,1);
				stateid=Portal2D.LOADSTATE;
                break;
			}
			selected=-1;
			sbg.enterState(stateid);
		}
		return;
	}

	@Override
	public int getID() {
		return MainMenuState.StateId;
	}

	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in MainMenuState int: " + key);
		switch(key) {
		case Input.KEY_UP:
			if (menuItemSelected == 0)
				menuItemSelected = menuItems.size() - 1;
			else
				menuItemSelected--;
			break;
		case Input.KEY_DOWN:
			if (menuItemSelected == menuItems.size() - 1)
				menuItemSelected = 0;
			else
				menuItemSelected++;
			break;
		case Input.KEY_ENTER:
			selected=stringMaps.get(menuItems.get(menuItemSelected));


		}
	}

	@Override
	public void keyReleased(int key, char c) {System.out.println("Key released in MainMenuState int: " + key);}

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
