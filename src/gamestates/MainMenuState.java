package gamestates;

import gameengine.InputManager;
import gameengine.Portal2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AssetManager;


public class MainMenuState extends BasicGameState implements KeyListener {
	/** Menu options for selection **/
	private final static int MENU_STARTGAME=1;
	private final static int MENU_LEVELSELECT=5;
	private final static int MENU_ACHIEVEMENTS=2;
	private final static int MENU_HIGHSCORES=3;
	private final static int MENU_OPTIONS=4;
	
	/** The state id for this part **/
	private static int StateId = Portal2D.MAINMENUSTATE;
	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private static Image menubg;
	private int selected =-1;
	private static String titleText = new String("Welcome to Portal 2D");
	private static String subtitleText = new String("Version 1.0");

	private static Vector<String> menuItems = new Vector<String>();
	private static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	private static int menuItemSelected = 0;
	private static Image mainbg;
	private static boolean usernameentered;

	/** Constructor
	 * @throws SlickException
	 */
	public MainMenuState() throws SlickException
	{
		super();
	}
	
	/** Method called by Slick to initialise the state. Loads fonts and menu items.
	 * 
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		font = AssetManager.requestFontResource("RETROFONT");
		menubg = AssetManager.requestUIElement("MENUBG");
		debug = false;
		fullscreen = false;
		
		// Menu items
		menuItems.add("Start Game");
		menuItems.add("Level Select");
		menuItems.add("Achievements");
		menuItems.add("High Scores");
		menuItems.add("Options");
		stringMaps.put("Start Game", MENU_STARTGAME);
		stringMaps.put("Level Select", MENU_LEVELSELECT);
		stringMaps.put("Achievements", MENU_ACHIEVEMENTS);
		stringMaps.put("High Scores", MENU_HIGHSCORES);
		stringMaps.put("Options", MENU_OPTIONS);
		mainbg = AssetManager.requestUIElement("MAINMENUBG");
	}

	/** Method called by Slick when entering the state. Tells LoadingState to start
	 * new game.
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		LoadingState.startnew=true;
	}

	/** Method called by Slick to update the state. Handles entering the selected state
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(usernameentered){
			if(selected!=-1){
				int stateid=Portal2D.MAINMENUSTATE;
				switch (selected){
				case MENU_STARTGAME:
					LoadingState.startnew=true;
					stateid=Portal2D.LOADSTATE;
					break;
				case MENU_ACHIEVEMENTS:
					stateid=Portal2D.ACHIEVEMENTSTATE;
					break;
				case MENU_HIGHSCORES:
					stateid=Portal2D.HIGHSCORESTATE;
					break;
				case MENU_OPTIONS:
					stateid=Portal2D.OPTIONSTATE;
					break;
				case MENU_LEVELSELECT:
					stateid=Portal2D.LEVELSELECTSTATE;
					break;
				}
				selected=-1;
				sbg.enterState(stateid);
			}

			return;
		}
	}

	/** Method called by Slick to render the view
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// Draw background and titles
		mainbg.draw(gc.getWidth()-mainbg.getWidth(), gc.getHeight()-mainbg.getHeight());
		menubg.draw(50,50);
		g.setFont(font);
		g.setBackground(Color.white);
		g.setColor(Color.gray);
		g.drawString(titleText, 140, 380);
		g.drawString(subtitleText, 227, 400);

		if(usernameentered){
			// Draw the menu items
			for (int i = 0; i < menuItems.size(); i++) {
				if (i ==  menuItemSelected) {
					g.setColor(Color.orange);
				} else {
					g.setColor(Color.darkGray);
				}
				g.drawString(menuItems.get(i), 120, 440 + i * 20);
			}
		} else {
			g.drawString("Welcome to Apeture",120, 440);
			g.drawString("Science Testing",120, 460);
			g.drawString("Candidate, please enter",120, 500);
			g.drawString("your name to continue:",120, 520);
			g.setColor(Color.orange);
			g.drawString(Portal2D.name, 120, 560);
		}
	}

	/** Get the ID of the state
	 * 
	 * @return The ID of the state
	 */
	@Override
	public int getID() {
		return MainMenuState.StateId;
	}

	/** Method for handling key presses. Controls navigation in the state
	 * 
	 * @param key Key pressed as integer
	 * @param c Key pressed as character
	 */
	@Override
	public void keyPressed(int key, char c) {
		if(usernameentered){
			if (key == InputManager.NAV_UP) {
				if (menuItemSelected == 0)
					menuItemSelected = menuItems.size() - 1;
				else
					menuItemSelected--;
			} else if (key == InputManager.NAV_DOWN) {
				if (menuItemSelected == menuItems.size() - 1)
					menuItemSelected = 0;
				else
					menuItemSelected++;
			} else if (key == InputManager.SELECT) {
				selected=stringMaps.get(menuItems.get(menuItemSelected));
			}
		} else {
			if(Character.isLetter(c) && Portal2D.name.length()<30){
					Portal2D.name = Portal2D.name + c;
			} 
			
			if(key==Input.KEY_BACK && !Portal2D.name.equals("")){
				Portal2D.name = Portal2D.name.substring(0, Portal2D.name.length()-1);
			}
			
			if(key==InputManager.SELECT && !Portal2D.name.equals("")){
				usernameentered=true;
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {return;}

	@Override
	public void inputEnded() {listening = false;}

	@Override
	public void inputStarted() {listening = true;}

	@Override
	public boolean isAcceptingInput() {return listening;}

	@Override
	public void setInput(Input input) {input.addKeyListener(this);}
}
