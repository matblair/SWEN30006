package gamestates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import gameengine.Portal2D;

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

public class HighScoreState extends BasicGameState implements KeyListener{
	private static int StateId = Portal2D.HIGHSCORESTATE; // State ID
	private static int MENU_MAINMENU=1;
	
	/** The state id for this part **/
	
	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private int selected =-1;
	private static String titleText = new String("High Scores");
	private static String subtitleText = new String("Version 0.1");

	private static Vector<String> menuItems = new Vector<String>();
	private static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	private static int menuItemSelected = 0;

	ArrayList<Integer> scores = null;
	private static HighScoreState instance = null;
	
	
	
	public HighScoreState() throws SlickException
	{
		super();
		font = AssetManager.requestFontResource("RETROFONT");
		debug = false;
		fullscreen = false;
		
		scores = new ArrayList<Integer>();
		
		for (int i=0; i< 10; i++){
			scores.add(new Integer(0));
		}
		
		for (int j=0; j< 10; j++){
			Random rn = new Random();
			int value = rn.nextInt(2000);
			scores.set(j, value);
		} 
		Collections.sort(scores);
	}
	
	
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
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
		int k = 150;
		for (int j=scores.size()-1; j>=0; j--){
			g.setColor(Color.cyan);
			g.drawString(String.valueOf(scores.get(j)), 40, k);
			k+=20;
			
		}
		
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		if(selected!=-1){
			int stateid=Portal2D.HIGHSCORESTATE;
			switch (selected){
			case 1:
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
		return HighScoreState.StateId;
	}

	
	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in HighScoreState int: " + key);
		switch(key) {
		case Input.KEY_ENTER:
			selected=stringMaps.get(menuItems.get(menuItemSelected));
		case Input.KEY_RIGHT:
			scores = new ArrayList<Integer>();
			
			for (int i=0; i< 10; i++){
				scores.add(new Integer(0));
			}
			
			for (int j=0; j< 10; j++){
				Random rn = new Random();
				int value = rn.nextInt(2000);
				scores.set(j, value);
			} 
			Collections.sort(scores);
		case Input.KEY_LEFT:
			scores = new ArrayList<Integer>();
			
			for (int i=0; i< 10; i++){
				scores.add(new Integer(0));
			}
			
			for (int j=0; j< 10; j++){
				Random rn = new Random();
				int value = rn.nextInt(2000);
				scores.set(j, value);
			} 
			Collections.sort(scores);
		}
	}

	@Override
	public void keyReleased(int key, char c) {System.out.println("Key released in HighScoreState int: " + key);}

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
	
	public static HighScoreState getInstance()
    {
        if(instance == null)
			try {
				instance = new HighScoreState();
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  
        return instance;
    }
  
    public boolean addScore(int score)
    {
        for(int idx = 0; idx < scores.size(); idx++)
        {
            if(score > scores.get(idx)) {
                scores.add(idx, new Integer(score));
                scores.remove(scores.size()-1);
  
                return true;
            }
  
        }
  
        return false;
    }
  
    public ArrayList<Integer> getScores()
    {
        return scores;
    }
	
	
	
	
	
	
}
