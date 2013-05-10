package gameworlds;

import gameengine.InputManager;
import gamestates.GameState;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;

public class Paused extends InGameMenu{

	private Image pausebg;

	/** Menu options for selection **/
	private final static int MENU_STARTGAME=1;
	private final static int MENU_ACHIEVEMENTS=2;
	private final static int MENU_HIGHSCORES=3;
	private final static int MENU_OPTIONS=4;
	private final static int MENU_QUIT=5;

	private static boolean displayachievements=false;
	private static boolean displayscores=false;
	private static boolean displayoptions=false;
	private static boolean displayquitoption=false;

	private OptionMenu opmenu = new OptionMenu();
	private HighScoreMenu hsmenu = new HighScoreMenu();
	private QuitMenu qmenu = new QuitMenu();
	private AchievementMenu acmenu = new AchievementMenu();

	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static int menuItemSelected = 0;


	public Paused()  {
		font = AssetManager.requestFontResource("PAUSEFONT");
		debug = false;
		pausebg = AssetManager.requestUIElement("PAUSEBG");
		fullscreen = false;
		menuItems.add("Resume Game");
		menuItems.add("Achievements");
		menuItems.add("High Scores");
		menuItems.add("Options");
		menuItems.add("Exit To Menu");
		stringMaps.put("Resume Game", MENU_STARTGAME);
		stringMaps.put("Achievements", MENU_ACHIEVEMENTS);
		stringMaps.put("High Scores", MENU_HIGHSCORES);
		stringMaps.put("Options", MENU_OPTIONS);
		stringMaps.put("Exit To Menu", MENU_QUIT);

	}

	@Override		
	public
	void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(displayachievements){
			acmenu.Update(g, gc, sbg);
		}else if(displayscores){
			hsmenu.Update(g, gc, sbg);
		}else if(displayoptions){
			opmenu.Update(g, gc, sbg);
		}else if(displayquitoption){
			qmenu.Update(g, gc, sbg);
		}else{
			firstMenu();
		}		
	}


	@Override
	public void ProcessInput(int key) {
		if(displayachievements){
			acmenu.ProcessInput(key);
		}else if(displayscores){
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
	public
	void Render(Graphics g, GameContainer gc) {
		Color filter = new Color(0,0,0,0.7f);
		g.setColor(filter);
		g.fillRect(0, 0,  gc.getWidth(), gc.getHeight());

		pausebg.drawCentered(gc.getWidth()/2, gc.getHeight()/2);
		g.setFont(font);
		g.setColor(Color.gray);

		if(displayachievements){
			acmenu.Render(g, gc);
		}else if(displayscores){
			hsmenu.Render(g, gc);
		}else if(displayoptions){
			opmenu.Render(g, gc);
		}else if(displayquitoption){
			qmenu.Render(g, gc);
		}else{
			drawFirstMenu(g);
		}		
	}


	private void firstMenu(){
		if(selected!=-1){
			switch (selected){
			case MENU_STARTGAME:
				GameState.switchPaused();
				break;
			case MENU_ACHIEVEMENTS:
				displayachievements=true;
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
			}
			selected=-1;
		}
	}


	public void drawFirstMenu(Graphics g){
		for (int i = 0; i < menuItems.size(); i++) {
			if (i ==  menuItemSelected) {
				g.setColor(Color.orange);
			} else {
				g.setColor(Color.darkGray);
			}
			g.drawString(menuItems.get(i), 430, 340 + i * 30);
		}	
	}

	public static Vector<String> getMenuItems() {
		return menuItems;
	}

	public static void setMenuItems(Vector<String> menuItems) {
		Paused.menuItems = menuItems;
	}

	public static Map<String, Integer> getStringMaps() {
		return stringMaps;
	}

	public static void setStringMaps(Map<String, Integer> stringMaps) {
		Paused.stringMaps = stringMaps;
	}

	public static int getMenuItemSelected() {
		return menuItemSelected;
	}

	public static void setMenuItemSelected(int menuItemSelected) {
		Paused.menuItemSelected = menuItemSelected;
	}

	/**
	 * @return the selected
	 */
	public int getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(int selected) {
		this.selected = selected;
	}

	public static void setDisplayachievements(boolean displayachievements) {
		Paused.displayachievements = displayachievements;
	}

	public static void setDisplayscores(boolean displayscores) {
		Paused.displayscores = displayscores;
	}

	public static void setDisplayoptions(boolean displayoptions) {
		Paused.displayoptions = displayoptions;
	}

	public static void setDisplayquitoption(boolean displayquitoption) {
		Paused.displayquitoption = displayquitoption;
	}

}

