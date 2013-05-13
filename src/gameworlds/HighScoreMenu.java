package gameworlds;

import gameengine.HighScore;
import gameengine.InputManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class HighScoreMenu extends InGameMenu{
	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static ArrayList<HighScore> scores;
	protected static int menuItemSelected = 0;
	
	private static final int MENU_PAUSEGAME = 0;
	private static int NUMDISPLAY = 5;

	public HighScoreMenu(int levelid){
		menuItems.add("Back");
		stringMaps.put("Back", MENU_PAUSEGAME);
		scores = AssetManager.requestHighScores(levelid);
		if(NUMDISPLAY>scores.size()){
			NUMDISPLAY=scores.size()-1;
		}
	}

	@Override
	public
	void Render(Graphics g, GameContainer gc) {
		for(int i=0; i < NUMDISPLAY; i++){
			g.setColor(Color.darkGray);
			g.drawString(scores.get(i).getName(), 430, 340 + i*30);
			g.setColor(Color.orange);
			g.drawString(String.valueOf(scores.get(i).getScore()), 770, 340 + i*30);
		}
		
		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.darkGray);
			}
			g.drawString(menuItems.get(i), 430, 520 + i * 30);
		}	
	}

	@Override
	public
	void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(selected!=-1){
			switch (selected){
			case MENU_PAUSEGAME:
				Paused.setDisplayscores(false);
                break;
			}
			selected=-1;
		}
	}

	@Override
	public void ProcessInput(int key) {
		if (key == InputManager.NAV_UP) {
			if (menuItemSelected == 0)
				menuItemSelected=(menuItems.size() - 1);
			else
				menuItemSelected--;
		} else if (key == InputManager.NAV_DOWN) {
			if (menuItemSelected == menuItems.size() - 1)
				menuItemSelected=0;
			else
				menuItemSelected++;
		} else if (key == InputManager.SELECT) {
			selected=(stringMaps.get(menuItems.get(menuItemSelected)));
		}	
	}	
}
