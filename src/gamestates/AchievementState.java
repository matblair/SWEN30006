package gamestates;

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
import scoringsystem.Achievement;

public class AchievementState extends BasicGameState implements KeyListener {
	/** The state id for this part **/
	private static int StateId = Portal2D.ACHIEVEMENTSTATE; // State ID
	
	// Constants
	private final int ITEMSPERROW = 5;
	private final int NUMROWS = 3;
	private final int XSPACING = 180;
	private final int YSPACING = 180;
	private final int YSTARTHEIGHT = 280;
	private final int FADEOUTDIST = 100;

	private boolean listening=true;
	private static Font font, titleFont;
	private static String titleText = new String("Achievements");
	private static String subtitleText = new String("Version 1.0");

	private Collection<Achievement> achievements;
	private int achievementSelected;
	
	private Image selected;
	private Image lock;
	private int viewOffset=0, targetRowOffset;

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
		selected=AssetManager.requestUIElement("SELECTED");
		lock = AssetManager.requestUIElement("ACHLOCK");
		targetRowOffset = 0;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if (input.isKeyDown(InputManager.BACK)) {
			System.out.println("leaving");
			sbg.enterState(Portal2D.MAINMENUSTATE);
		}
		
		// Calculate view offset
		while (achievementSelected / ITEMSPERROW < targetRowOffset)
			targetRowOffset--;
		while (achievementSelected / ITEMSPERROW > targetRowOffset + NUMROWS - 1)
			targetRowOffset++;
		if (viewOffset != targetRowOffset * YSPACING) {
			int moveAmount;
			if (Math.abs(targetRowOffset * YSPACING - viewOffset) < delta)
				moveAmount = targetRowOffset * YSPACING - viewOffset;
			else
				moveAmount = (int) Math.signum(targetRowOffset * YSPACING - viewOffset) * delta;
			viewOffset += moveAmount;
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
		float alpha;
		String text;

		for (Achievement a : achievements) {
			x = (int) ((float) width / 2 + (index % ITEMSPERROW - (float) (ITEMSPERROW - 1) / 2) * XSPACING);
			y = YSTARTHEIGHT + YSPACING * (int) Math.floor(index / ITEMSPERROW) - viewOffset;
			if (y > YSPACING * (NUMROWS - 1) + YSTARTHEIGHT) {
				alpha = 1 - ((float) y - (YSPACING * (NUMROWS - 1) + YSTARTHEIGHT)) / FADEOUTDIST;
			} else if (y < YSTARTHEIGHT) {
				alpha = 1 - (float) (YSTARTHEIGHT - y) / FADEOUTDIST;
			} else {
				alpha = 1;
			}
			
			if (index == achievementSelected) {
				selected.drawCentered(x, y + 52);
				
				text = a.getName();
				g.setColor(Color.darkGray);
				g.drawString(text, 40, 120);
				
				text = a.getDescription();
				g.setColor(Color.gray);
				g.drawString(text, 300, 120);
			}

			image = a.getUnlockedImage();
			image.setAlpha(alpha);
			image.drawCentered(x, y);
			
			if (!a.isUnlocked()) {
				lock.setAlpha(alpha);
				lock.drawCentered(x+30, y+30);
			}

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
			if (achievementSelected >= ITEMSPERROW)
				achievementSelected -= ITEMSPERROW;

		} else if (key == InputManager.NAV_DOWN) {
			if ((int) (achievementSelected / ITEMSPERROW) + ITEMSPERROW < achievements.size()) 
				achievementSelected += ITEMSPERROW;
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
