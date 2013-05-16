package gamestates;

import gameengine.Achievement;
import gameengine.Portal2D;
import gameengine.InputManager;

import java.util.Collection;

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

public class AchievementState extends BasicGameState implements KeyListener {
	/** The state id for this part **/
	private static int StateId = Portal2D.ACHIEVEMENTSTATE; // State ID

	private boolean listening=true;
	boolean debug, fullscreen;
	private static Font font, titleFont;
	private static String titleText = new String("Achievements");
	private static String subtitleText = new String("Version 0.4");

	private Collection<Achievement> achievements;
	private int achievementSelected;
	
	private Image selected;

	private final int itemsPerRow = 5;
	private final int xSpacing = 180;
	private final int ySpacing = 180;
	private final int yStartHeight = 250;

	public AchievementState() throws SlickException {
		super();
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		achievements = AssetManager.getAchievements();
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		font = AssetManager.requestFontResource("RETROFONT");
		titleFont = AssetManager.requestFontResource("TITLEFONT");
		debug = false;
		fullscreen = false;
		selected=AssetManager.requestUIElement("SELECTED");
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if (input.isKeyDown(InputManager.BACK)) {
			System.out.println("leaving");
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString(titleText, 40, 40);

		g.setFont(font);
		g.drawString(subtitleText, 40, 80);

		Image image;
		int index = 0;
		int x, y;
		int width = gc.getWidth();
		String text;

		for (Achievement a : achievements) {
			x = (int) ((float) width / 2 + (index % itemsPerRow - (float) (itemsPerRow - 1) / 2) * xSpacing);
			y = yStartHeight + ySpacing * (int) Math.floor(index / itemsPerRow);

			if (index == achievementSelected) {
				selected.drawCentered(x, y + 30);
				
				text = a.getName();
				g.setColor(Color.darkGray);
				g.drawString(text, 40, 120);
				
				text = a.getDescription();
				g.setColor(Color.gray);
				g.drawString(text, 300, 120);
			}

			if (a.isUnlocked()) {
				image = a.getUnlockedImage();
			} else {
				image = a.getLockedImage();
			}

			image.drawCentered(x, y);

			index++;
		}
	}

	@Override
	public int getID() {
		return AchievementState.StateId;
	}

	@Override
	public void keyPressed(int key, char c) {
		System.out.println("Key pressed in AchievementState int: " + key);
		if (key == InputManager.NAV_LEFT) {
			if (achievementSelected > 0)
				achievementSelected--;
			else
				achievementSelected = achievements.size() - 1;

		} else if (key == InputManager.NAV_RIGHT) {
			if (achievementSelected < achievements.size() - 1)
				achievementSelected++;
			else
				achievementSelected = 0;

		} else if (key == InputManager.NAV_UP) {
			if (achievementSelected >= itemsPerRow)
				achievementSelected -= itemsPerRow;

		} else if (key == InputManager.NAV_DOWN) {
			if ((int) (achievementSelected / itemsPerRow) + itemsPerRow < achievements.size()) 
				achievementSelected += itemsPerRow;
			if (achievementSelected >= achievements.size())
				achievementSelected = achievements.size() - 1;
		}
	}

	@Override
	public void keyReleased(int key, char c) {System.out.println("Key released in AcheivementState int: " + key);}

	@Override
	public void inputEnded() {listening = false;}

	@Override
	public void inputStarted() {listening = true;}

	@Override
	public boolean isAcceptingInput() {return listening;}

	@Override
	public void setInput(Input input) {input.addKeyListener(this);}
}
