package gameworlds;

import gameengine.Achievement;
import gameengine.InputManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class AchievementMenu extends InGameMenu {

	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static int menuItemSelected = 0;
	private Collection<Achievement> achievements;
	protected int levelid;

	private static final int MENU_PAUSEGAME = 0;

	public AchievementMenu(int levelid){
		menuItems.add("Back");
		stringMaps.put("Back", MENU_PAUSEGAME);
		achievements = AssetManager.getAchievements();
		this.levelid=levelid;
	}

	@Override
	public
	void Render(Graphics g, GameContainer gc) {
		
		for(Achievement ac: achievements){
			ac.isUnlocked();
			//Code for showing relevant level??
		}
		
		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.darkGray);
			}
			g.drawString(menuItems.get(i), 430, 340 + i * 30);
		}	
	}

	@Override
	public
	void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(selected!=-1){
			switch (selected){
			case MENU_PAUSEGAME:
				Paused.setDisplayachievements(false);
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