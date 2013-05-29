package gamestates;

import java.util.ArrayList;
import java.util.Collections;

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
import scoringsystem.HighScore;

public class HighScoreState extends BasicGameState implements KeyListener{
private static int StateId = Portal2D.HIGHSCORESTATE; // State ID

	/** The state id for this part **/

	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private static Font titleFont;
	private static String titleText = new String("High Scores");
	private static int MAXLEVEL=18;

	private final int itemsPerRow = 1;
	private final int xSpacing = 180;
	private final int ySpacing = 90;
	private final int yStartHeight = 250;
	private int width;

	private ArrayList<HighScore> scores;

	int currentlevel = 0;

	/** Constructor
	 * @throws SlickException
	 */
	public HighScoreState() throws SlickException
	{
		super();
	}

	/** Method called by Slick to initialise the state
	 */
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		font = AssetManager.requestFontResource("PAUSEFONT");
		titleFont = AssetManager.requestFontResource("TITLEFONT");
		debug = false;
		fullscreen = false;
	}
	
	/** Method called by Slick when entering the state. Pre-loads high scores.
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		width=container.getWidth();
		scores = AssetManager.requestHighScores(currentlevel);
	}

	/** Method called by Slick to update the state. Handles exiting the state.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {		
		Input input = gc.getInput();
		if (input.isKeyDown(InputManager.BACK)) {
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}
	}

	/** Method called by Slick to render the view.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);

		g.setFont(font);
		renderScores(g);
	}

	/** Render the high scores using a Graphics context
	 * 
	 * @param g The Graphics context to use.
	 */
	private void renderScores(Graphics g) {
		int index = 0;
		int x, y;
		String text;
		int levelid;
		float score;
		String scoreS;
		
		// Show high scores
		if(scores!=null && !scores.isEmpty()){
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
				if(index>5){
					break;
				}
			}
			
		// No scores. Show error.
		} else {
			x = (int) ((float) width / 2 + (index % itemsPerRow - (float) (itemsPerRow - 1) / 2) * xSpacing);
			y = yStartHeight + ySpacing * (int) Math.floor(index / itemsPerRow);
			g.setColor(Color.darkGray);
			g.drawString("LEVEL "+ (currentlevel+1), x-10, 120);	
			g.setColor(Color.orange);
			g.drawString("No High Scores For This Level Yet!", x-(font.getWidth("No High Scores For This Level Yet!")/2), y);
		}
	}

	/** Get the ID of the state
	 * 
	 * @return The ID of the state
	 */
	@Override
	public int getID() {
		return HighScoreState.StateId;
	}

	/** Method for handling key presses. Controls navigation in the state.
	 * 
	 * @param key Key pressed as integer
	 * @param c Key pressed as character
	 */
	@Override
	public void keyPressed(int key, char c) {
		if (key == InputManager.NAV_RIGHT) {
			if (currentlevel + 1 < (MAXLEVEL+1)) {
				currentlevel++;
				System.out.println(currentlevel);
				scores = AssetManager.requestHighScores(currentlevel);
				System.out.println(scores);
			}
		} else if (key == InputManager.NAV_LEFT) {
			if (currentlevel > 0){
				currentlevel--;
				scores = AssetManager.requestHighScores(currentlevel);
				System.out.println(scores);
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