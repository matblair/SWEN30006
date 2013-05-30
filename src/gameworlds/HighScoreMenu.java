package gameworlds;

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
import scoringsystem.HighScore;

public class HighScoreMenu extends InGameMenu{
	/** Array lists and vectors of things to display **/
	protected static Vector<String> menuItems = new Vector<String>();
	protected static Map<String,Integer> stringMaps = new HashMap<String,Integer>();
	protected static ArrayList<HighScore> scores;
	
	/** Static variables for controlling display options and control flow **/
	private static int levelid;
	protected static int menuItemSelected = 0;
	private static boolean firstupdate=false;
	private static final int MENU_PAUSEGAME = 0;
	private static int NUMDISPLAY = 5;

	/** Constructor the High Score menu
	 * 
	 * @param levelid The levelid (as an integer)
	 */
	public HighScoreMenu(int levelid){
		menuItems.add("Back");
		stringMaps.put("Back", MENU_PAUSEGAME);
		scores = AssetManager.requestHighScores(levelid);
	}

	@Override
	/** Our render method for the high score menu system
	 * 
	 * @param g The graphics context to render to.
	 * @param gc The Game Container
	 * 
	 * @return void
	 */	
	public void Render(Graphics g, GameContainer gc) {
		if(!firstupdate){
			if(scores.size()!=0){
				if(NUMDISPLAY>scores.size()){
					NUMDISPLAY=(scores.size());
				}
				for(int i=0; i < NUMDISPLAY; i++){
					g.setColor(Color.darkGray);
					g.drawString(scores.get(i).getName(), gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);
					g.setColor(Color.orange);
					g.drawString(String.valueOf(scores.get(i).getScore()),  gc.getWidth()/2 + AssetManager.requestUIElement("PAUSEBG").getWidth()/2 - INSET -
								g.getFont().getWidth(String.valueOf(scores.get(i).getScore())), gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);
				}
			}else {
				g.drawString("No High Scores Yet", 430, 320);

			}

			for (int i = 0; i < menuItems.size(); i++) {
				if (i ==  menuItemSelected) {
					g.setColor(Color.orange);
				} else {
					g.setColor(Color.darkGray);
				}
				g.drawString(menuItems.get(i),gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 + AssetManager.requestUIElement("PAUSEBG").getHeight()/2 - 2*INSET);
			}	
		} else {
			g.drawString("Updating High Scores",  gc.getWidth()/2 - 3*INSET, gc.getHeight()/2 - TITLEHEIGHT + (2) * SPACING);
		}
	}

	@Override
	/** Update the state of the end game menu, handle loading scores for the menu and switching back
	 * 
	 * @param g The graphics context to render to.
	 * @param gc The Game 
	 * @param sbg The StateBasedGame object to switch states
	 * 
	 * @return void
	 */
	public	void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(!AssetManager.getHighscores().containsKey(levelid)){
			ArrayList<HighScore> newarray = new ArrayList<HighScore>();
			AssetManager.getHighscores().put(levelid,newarray);
		}

		scores = AssetManager.getHighscores().get(levelid);	
		firstupdate=false;

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
	/** Input handler if required for the game menu.
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

	/** Set the end game menu
	 * @param levelId The leve id of the level we are creating
	 */
	public void setLevelId(int levelId) {
		HighScoreMenu.levelid=levelId;
		firstupdate=true;
	}
	
	

}
