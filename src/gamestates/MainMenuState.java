package gamestates;

import gameengine.Portal2D;

import java.util.HashMap;
import java.util.Vector;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.FontLoader;


public class MainMenuState extends BasicGameState implements KeyListener {
	/** The state id for this part **/
    private static int StateId = Portal2D.MAINMENUSTATE;
    private boolean listening=true;
    boolean debug, fullscreen;
    private Font retro;
    private int enterstate =-1;
	private String titleText = new String("Welcome to Portal 2D");
	private String subtitleText = new String("Version 0.1");
	
	private Vector<String> menuItems = new Vector<String>();
	private Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	private int menuItemSelected = 0;
	
	public MainMenuState() throws SlickException
	    {
	    	super();
	    	retro = FontLoader.loadFont("assets/fonts/RETRO.TTF", 18);
	        debug = false;
	        fullscreen = false;
	    }
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		// Menu items
		menuItems.add("Start Game");
		menuItems.add("Achievements");
		menuItems.add("High Scores");
		menuItems.add("Options");
		stringMaps.put("Start Game", 1);
		
		// Listener
		//this.setInput(gc.getInput());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.setFont(retro);
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
}
