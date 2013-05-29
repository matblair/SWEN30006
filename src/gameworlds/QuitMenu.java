package gameworlds;

import gameengine.InputManager;
import gameengine.Portal2D;
import gamestates.GameState;
import gamestates.LoadingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class QuitMenu extends InGameMenu{

	/** The items to display for the menu **/
	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static int menuItemSelected = 0;
	
	/** Contsants for menu selection **/
	private static final int MENU_MAINMENU = 0;
	private static final int MENU_PAUSEGAME = 1;

	/**	Process input from the game state 
	 * @return void
	 */
	public QuitMenu(){
		menuItems.add("No");
		menuItems.add("Yes");
		stringMaps.put("Yes", MENU_MAINMENU);
		stringMaps.put("No", MENU_PAUSEGAME);
		
	}

	@Override
	/** Our render method for the quit menu
	 * 
	 * @param g The graphics context to render to.
	 * @param gc The Game Container
	 * 
	 * @return void
	 */	
	public void Render(Graphics g, GameContainer gc) {
				
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
	/** Update the state of the quit menu and exit if neccessary
	 * 
	 * @param g The graphics context to render to.
	 * @param gc The Game 
	 * @param sbg The StateBasedGame object to switch states
	 * 
	 * @return void
	 */
	public void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(selected!=-1){
			switch (selected){
			case MENU_MAINMENU:
				GameState.getLevel().getGlados().finaliseStats();
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
	/**	Process input from the game state
	 * 
	 * @param key The integer key code representing the pressed key
	 * 
	 * @return void
	 */
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
