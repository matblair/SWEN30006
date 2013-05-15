package gamestates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import gameengine.HighScore;
import gameengine.InputManager;
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
import resourcemanagers.OnlineHighScoreLoader;

public class HighScoreState extends BasicGameState implements KeyListener{
	private static final int TODISPLAY = 5;

	private static int StateId = Portal2D.HIGHSCORESTATE; // State ID

	/** The state id for this part **/

	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private static Font titleFont;
	@SuppressWarnings("unused")
	private int selected =-1;
	private static String titleText = new String("High Scores");
	private static String subtitleText = new String("Version 0.1");
	private static int MAXLEVEL=2;

	private static Vector<String> menuItems = new Vector<String>();
	private static Map<String,Integer> stringMaps = new HashMap<String,Integer>();


	private final int itemsPerRow = 1;
	private final int xSpacing = 180;
	private final int ySpacing = 90;
	private final int yStartHeight = 250;



	private ArrayList<HighScore> scores;
	private static HighScoreState instance = null;

	int currentlevel = 0;

	public HighScoreState() throws SlickException
	{
		super();
	}


	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		scores = OnlineHighScoreLoader.getLocalFirst(0, 10);
	}


	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		font = AssetManager.requestFontResource("PAUSEFONT");
		titleFont = AssetManager.requestFontResource("TITLEFONT");
		debug = false;
		fullscreen = false;

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);

		g.setFont(font);
		g.drawString(subtitleText, 40, 80);

		int index = 0;
		int x, y;
		int width = gc.getWidth();
		String text;
		int levelid;
		float score;
		String scoreS;

		
		if(scores.size()!=0){
			for (HighScore hs : scores) {


				Collections.sort(scores);

				x = (int) ((float) width / 2 + (index % itemsPerRow - (float) (itemsPerRow - 1) / 2) * xSpacing);
				y = yStartHeight + ySpacing * (int) Math.floor(index / itemsPerRow);


				levelid = hs.getLevelid();
				levelid++;
				g.setColor(Color.darkGray);
				g.drawString("LEVEL "+levelid, x-10, 120);


				text = hs.getName();
				g.setColor(Color.gray);
				g.drawString(text, x-50, y);

				score = hs.getScore();
				scoreS = String.valueOf(score);
				g.setColor(Color.gray);
				g.drawString(scoreS, x+ 50, y);

				index++;
			}
		} else {
			x = (int) ((float) width / 2 + (index % itemsPerRow - (float) (itemsPerRow - 1) / 2) * xSpacing);
			y = yStartHeight + ySpacing * (int) Math.floor(index / itemsPerRow);
			g.setColor(Color.darkGray);
			g.drawString("LEVEL "+ 1, x-10, 120);	
			g.setColor(Color.orange);
			g.drawString("No high scores yet for this level!", x-(font.getWidth("Loading High Scores from Server")/2), 160);
			g.drawString("It's yours for the taking!", x-(font.getWidth("Loading High Scores from Server")/2), 200);
			scores = OnlineHighScoreLoader.getLocalFirst(currentlevel-1, 10);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {		
		Input input = gc.getInput();
		if (input.isKeyDown(InputManager.BACK)) {
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}

	}

	@Override
	public int getID() {
		return HighScoreState.StateId;
	}


	@Override
	public void keyPressed(int key, char c) {
		if (key == InputManager.NAV_RIGHT) {

			if (currentlevel +1 < MAXLEVEL+1) {
				currentlevel++;
				scores = OnlineHighScoreLoader.getLocalFirst(currentlevel, TODISPLAY);
			}
		} else if (key == InputManager.NAV_LEFT) {

			if (currentlevel > 0){
				currentlevel--;
				scores = OnlineHighScoreLoader.getLocalFirst(currentlevel, TODISPLAY);
			}	
		}
	}


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
				e.printStackTrace();
			}

		return instance;
	}

}