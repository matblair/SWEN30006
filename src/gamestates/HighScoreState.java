package gamestates;

import java.util.ArrayList;

import gameengine.InputManager;
import gameengine.Portal2D;

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
import scoringsystem.HighScore;

public class HighScoreState extends BasicGameState implements KeyListener{
private static int StateId = Portal2D.HIGHSCORESTATE; // State ID

	/** The state id for this part **/

	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font;
	private static Font titleFont;
	private static String TITLE = new String("High Scores");
	private static int MAXLEVEL=14;
	private static int NUMDISPLAY = 5;
	private static final int INSET = 50;
	private static final int TITLEHEIGHT = 150;
	private static final int SPACING = 30;

	private ArrayList<HighScore> scores;
	private Image mainbg, pausebg;

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
		pausebg = AssetManager.requestUIElement("PAUSEBG");
		mainbg = AssetManager.requestUIElement("MAINMENUBG");
	}
	
	/** Method called by Slick when entering the state. Pre-loads high scores.
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
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
		mainbg.draw(gc.getWidth()-mainbg.getWidth(), gc.getHeight()-mainbg.getHeight());
		pausebg.drawCentered(gc.getWidth()/2, gc.getHeight()/2);
		
		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString(TITLE, gc.getWidth()/2 - titleFont.getWidth(TITLE)/2, gc.getHeight()/2 - TITLEHEIGHT);

		g.setFont(font);
		g.setColor(Color.gray);
		String text = "Level " + (currentlevel + 1);
		g.drawString(text, gc.getWidth()/2 - titleFont.getWidth(text)/2, gc.getHeight()/2 - TITLEHEIGHT + 50);
		
		renderScores(gc, g);
	}

	/** Render the high scores using a Graphics context
	 * 
	 * @param gc The GameContainer
	 * @param g The Graphics context to use.
	 */
	private void renderScores(GameContainer gc, Graphics g) {
		if(scores!=null && scores.size()!=0){
			if(NUMDISPLAY>scores.size()){
				NUMDISPLAY=(scores.size());
			}
			for(int i=0; i < NUMDISPLAY; i++){
				g.setColor(Color.darkGray);
				g.drawString(scores.get(i).getName(), gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+3) * SPACING);
				g.setColor(Color.orange);
				g.drawString(String.valueOf(scores.get(i).getScore()),  gc.getWidth()/2 + AssetManager.requestUIElement("PAUSEBG").getWidth()/2 - INSET -
							g.getFont().getWidth(String.valueOf(scores.get(i).getScore())), gc.getHeight()/2 - TITLEHEIGHT + (i+3) * SPACING);
			}
		}else {
			String text = "No High Scores Yet";
			g.drawString(text, gc.getWidth()/2 - g.getFont().getWidth(text)/2, gc.getHeight()/2-40);
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
			if (currentlevel + 1 < MAXLEVEL) {
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