package gameworlds;

import gameengine.InputManager;
import gameengine.Portal2D;
import gamestates.LoadingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class QuitMenu extends InGameMenu{

	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static int menuItemSelected = 0;
	
	private static final int MENU_MAINMENU = 0;
	private static final int MENU_PAUSEGAME = 1;

	public QuitMenu(){
		menuItems.add("No");
		menuItems.add("Yes");
		stringMaps.put("Yes", MENU_MAINMENU);
		stringMaps.put("No", MENU_PAUSEGAME);
		
	}

	@Override
	public
	void Render(Graphics g, GameContainer gc) {
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
			case MENU_MAINMENU:
				LoadingState.startnew=true;
				sbg.enterState(Portal2D.MAINMENUSTATE);
                break;
			case MENU_PAUSEGAME:
				Paused.setDisplayquitoption(false);
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