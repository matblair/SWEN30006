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

public class AchievementState extends BasicGameState implements KeyListener{
	
	/** Menu options for selection **/
	private static int MENU_MAINMENU = 1;
	
	
	private static int StateId = Portal2D.ACHIEVEMENTSTATE; // State ID
	
	
	////////////////////////////////////
	
	/** The state id for this part **/
	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private int selected =-1;
	private static String titleText = new String("Achievement");
	private static String subtitleText = new String("Version 0.1");

	private static Vector<String> menuItems = new Vector<String>();
	private static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	private static int menuItemSelected = 0;
	
	public AchievementState() throws SlickException
	{
		super();
		font = AssetManager.requestFontResource("RETROFONT");
		debug = false;
		fullscreen = false;

	}
    ////////////////////////////////////
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		// TODO Auto-generated method stub
		menuItems.add("Main Menu");
		stringMaps.put("Main Menu", MENU_MAINMENU);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		g.setFont(font);
		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);
		g.drawString(subtitleText, 40, 60);

		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.black);
			}
			g.drawString(menuItems.get(i), 40, 700);
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
		switch(key) {
		case Input.KEY_ENTER:
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
