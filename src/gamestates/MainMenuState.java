package gamestates;

import gameengine.Portal2D;

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


public class MainMenuState extends BasicGameState implements KeyListener {
	/** The state id for this part **/
	private static int StateId = Portal2D.MAINMENUSTATE;
	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private int enterstate =-1;
	private static String titleText = new String("Welcome to Portal 2D");
	private static String subtitleText = new String("Version 0.1");

	private static Vector<String> menuItems = new Vector<String>();
	private static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	private static int menuItemSelected = 0;



	public MainMenuState() throws SlickException
	{
		super();
		font = AssetManager.requestFontResource("RETROFONT");
		debug = false;
		fullscreen = false;

	}
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		// Load all resources in resource manager//
		System.out.println("here");

		// Menu items
		menuItems.add("Start Game");
		menuItems.add("Achievements");
		menuItems.add("High Scores");
		menuItems.add("Options");
		stringMaps.put("Start Game", Portal2D.GAMESTATE);
		stringMaps.put("Achievements", Portal2D.ACHIEVEMENTSTATE);
		stringMaps.put("High Scores", Portal2D.HIGHSCORESTATE);
		stringMaps.put("Options", Portal2D.OPTIONSTATE);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(titleText, 40, 40);
		g.drawString(subtitleText, 40, 60);

		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.yellow);
			} else {
				g.setColor(Color.white);
			}
			g.drawString(menuItems.get(i), 40, 100 + i * 20);
		}
	

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(enterstate!=-1){
			sbg.enterState(enterstate);
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
			enterstate=stringMaps.get(menuItems.get(menuItemSelected));


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
