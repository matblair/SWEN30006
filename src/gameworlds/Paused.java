package gameworlds;

import gameengine.InputManager;
import gamestates.GameState;
import gamestates.LoadingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class Paused extends InGameMenu{

	/** Menu options for selection **/
	private final static int MENU_STARTGAME=1;
	private final static int MENU_HIGHSCORES=3;
	private final static int MENU_OPTIONS=4;
	private final static int MENU_QUIT=5;
	private static final int MENU_RESTART = 6;

	/** Options to control state flow **/
	private static boolean displayscores=false;
	private static boolean displayoptions=false;
	private static boolean displayquitoption=false;
	
	/** The level id **/
	@SuppressWarnings("unused")
	private static int levelid;

	/** The sub componenets of the menu as objects **/
	private OptionMenu opmenu = new OptionMenu();
	private HighScoreMenu hsmenu;
	private QuitMenu qmenu = new QuitMenu();

	/** The information to display as well as the item that is selected **/
	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static int menuItemSelected = 0;


	/**	The constructor for the paused state
	 * @param levelid The level id to set
	 * @return void
	 */
	public Paused(int levelid)  {
		Paused.setLevelid(levelid);
		font = AssetManager.requestFontResource("PAUSEFONT");
		pausebg = AssetManager.requestUIElement("PAUSEBG");
		menuItems.add("Resume Game");
		menuItems.add("Restart Level");
		menuItems.add("High Scores");
		menuItems.add("Options");
		hsmenu = new HighScoreMenu(levelid);
		menuItems.add("Exit To Menu");
		stringMaps.put("Resume Game", MENU_STARTGAME);
		stringMaps.put("Restart Level", MENU_RESTART);
		stringMaps.put("High Scores", MENU_HIGHSCORES);
		stringMaps.put("Options", MENU_OPTIONS);
		stringMaps.put("Exit To Menu", MENU_QUIT);

	}

	@Override		
	/** Update the state of the paused menu, delegating update to the correct function
	 * 
	 * @param g The graphics context to render to.
	 * @param gc The Game 
	 * @param sbg The StateBasedGame object to switch states
	 * 
	 * @return void
	 */
	public
	void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(displayscores){
			hsmenu.Update(g, gc, sbg);
		}else if(displayoptions){
			opmenu.Update(g, gc, sbg);
		}else if(displayquitoption){
			qmenu.Update(g, gc, sbg);
		}else{
			try {
				firstMenu();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}		
	}


	@Override
	/**	Process input from the game state
	 * @param key The integer key code representing the pressed key
	 * @return void
	 */
	public void ProcessInput(int key) {
		if(displayscores){
			hsmenu.ProcessInput(key);
		}else if(displayoptions){
			opmenu.ProcessInput(key);
		}else if(displayquitoption){
			qmenu.ProcessInput(key);
		}else{
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

	@Override
	/** Our render method for the option menu
	 * 
	 * @param g The graphics context to render to.
	 * @param gc The Game Container
	 * 
	 * @return void
	 */	
	public
	void Render(Graphics g, GameContainer gc) {
		
		Color filter = new Color(0,0,0,0.7f);
		g.setColor(filter);
		g.fillRect(0, 0,  gc.getWidth(), gc.getHeight());
		
		g.setFont(titlefont);
		g.setColor(Color.black);
		g.drawString(TITLE, gc.getWidth()/2 - titlefont.getWidth(TITLE)/2, gc.getHeight()/2 - TITLEHEIGHT);	
		
		pausebg.drawCentered(gc.getWidth()/2, gc.getHeight()/2);
		
		g.setFont(titlefont);
		g.setColor(Color.black);
		g.drawString(TITLE, gc.getWidth()/2 - titlefont.getWidth(TITLE)/2, gc.getHeight()/2 - TITLEHEIGHT);
		
		g.setFont(font);
		g.setColor(Color.gray);

		if(displayscores){
			hsmenu.Render(g, gc);
		}else if(displayoptions){
			opmenu.Render(g, gc);
		}else if(displayquitoption){
			qmenu.Render(g, gc);
		}else{
			drawFirstMenu(g, gc);
		}		
	}

	/**	Process the switching of the first paused menu
	 * @return void
	 */
	private void firstMenu() throws SlickException{
		if(selected!=-1){
			switch (selected){
			case MENU_STARTGAME:
				GameState.switchPaused();
				break;
			case MENU_HIGHSCORES:
				displayscores=true;
				break;
			case MENU_OPTIONS:
				displayoptions=true;
				break;
			case MENU_QUIT:
				displayquitoption=true;
				break;
			case MENU_RESTART:
				LoadingState.reloadLevel();
				GameState.switchPaused();
				break;
			}
			selected=-1;
		}
	}

	/**	Draw the first menu strings 
	 * 
	 * @param g The graphics context to render to.
	 * 
	 * @return void
	 */
	public void drawFirstMenu(Graphics g, GameContainer gc){
		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.darkGray);
			}
			g.drawString(menuItems.get(i), gc.getWidth()/2 - pausebg.getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);
		}	
	}

	
	/** Set the display quit option menu.
	 * @param displayscores Whether we display or not.
	 * @return void
	 */
	public static void setDisplayscores(boolean displayscores) {
		Paused.displayscores = displayscores;
	}

	/** Set the display option menu
	 * @param displayscores Whether we display or not.
	 * @return void
	 */
	public static void setDisplayoptions(boolean displayoptions) {
		Paused.displayoptions = displayoptions;
	}

	/** Set the display quit option menu.
	 * @param displayscores Whether we display or not.
	 * @return void
	 */
	public static void setDisplayquitoption(boolean displayquitoption) {
		Paused.displayquitoption = displayquitoption;
	}

	/** Intro to level id setting
	 * @param levelid The level id of the current level
	 */
	public void setLevelId(int levelId) {
		Paused.setLevelid(levelId);
		hsmenu.setLevelId(levelId);
	}

	/** Static level id setting.
	 * @param levelid The level id to set
	 */
	public static void setLevelid(int levelid) {
		Paused.levelid = levelid;	
	}

}

