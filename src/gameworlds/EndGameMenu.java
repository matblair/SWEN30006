package gameworlds;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AchievementLoader;
import resourcemanagers.AssetManager;
import resourcemanagers.HighScoreLoader;
import scoringsystem.GLaDOS;

public class EndGameMenu extends InGameMenu {
	
	/** Boolean value to check if scores have been successfully uploaded **/
	private static boolean uploadedScores = false;
	/** Our background image for the end menu **/
	private Image endbg;
	/** Our level oracles **/
	private static GLaDOS glados;
	/** Strings for display. **/
	private static String FINISHED="Congratulations!";
	private static String SAVING="Saving High Score...";
	private static String CONTINUE="Hit Enter To Continue!";
	private static String TAKE="It took you ";
	private static String LEVELSTATS="Here are your level stats";
	private static String SECONDS=" seconds to finish.";
	

	/** Constructor for end game menu
	 */
	public EndGameMenu(){
		endbg = AssetManager.requestUIElement("PAUSEBG");
	}

	@Override
	/** Our render method for the end game menu
	 * 
	 * @param g The graphics context to render to.
	 * @param gc The Game Container
	 * 
	 */	
	public void Render(Graphics g, GameContainer gc) {
		Color filter = new Color(0,0,0,0.7f);
		g.setColor(filter);
		g.fillRect(0, 0,  gc.getWidth(), gc.getHeight());
		endbg.drawCentered(gc.getWidth()/2, gc.getHeight()/2);
		g.setFont(titlefont);
		g.setColor(Color.black);
		g.drawString(FINISHED, gc.getWidth()/2 - titlefont.getWidth(FINISHED)/2, gc.getHeight()/2 - 150);
		if(glados!=null){
			g.setColor(Color.orange);
			g.setFont(font);
			int i=0;
			g.drawString(LEVELSTATS , gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);
			g.setColor(Color.darkGray);
			i++;
			g.drawString(TAKE + glados.getLevelStats().getTimeInLevel()/1000 + SECONDS, gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);
			i++;
			g.drawString("You fell " + glados.getLevelStats().getDistFallen() + " metres.", gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);
			i++;
			g.drawString("You walked " + glados.getLevelStats().getDistWalked() + " metres.", gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);
			i++;
			g.drawString("You unlocked " + glados.getLevelStats().getAchievementsUnlocked() + " achievements.", gc.getWidth()/2 - AssetManager.requestUIElement("PAUSEBG").getWidth()/2 + INSET, gc.getHeight()/2 - TITLEHEIGHT + (i+2) * SPACING);			
			
			if(!isUploadedScores()){
				g.drawString(SAVING,  gc.getWidth()/2 - font.getWidth(SAVING)/2, gc.getHeight()/2 + endbg.getHeight()/2 - 60);
			}else{
				g.drawString(CONTINUE,  gc.getWidth()/2 - font.getWidth(CONTINUE)/2, gc.getHeight()/2 + endbg.getHeight()/2 - 60);
			}
		}

	}

	@Override
	/** Update the state of the end game menu, handle the score submission
	 *
	 * @param g The graphics context to render to.
	 * @param gc The Game 
	 * @param sbg The StateBasedGame object to switch states
	 * 
	 */	
	public void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(glados!=null && !isUploadedScores()){
			System.out.println("Submitting scores...");
			glados.updateHighScores(glados.getLevelStats().getLevelID());
			glados.updateAchievements(AssetManager.getAchievementMap());
			AssetManager.getLevelUnlocks().put(glados.getLevelStats().getLevelID(), false);
			try {
				HighScoreLoader.saveHighScores();
				AchievementLoader.saveAchievements();
			} catch (SlickException e) {
				e.printStackTrace();
			}
			setUploadedScores(true);
			System.out.println("Scores submitted.");
		}
	}

	
	/** Receive GLaDOS for the level for use in stats.
	 * 
	 * @param glados The level oracle passed from the level.
	 */	
	public static void giveGlados(GLaDOS glados) {
		glados.finaliseStats();
		EndGameMenu.glados=glados;
	}

	/** Check if scores are uploaded
	 * @return uploadedScores
	 */
	public static boolean isUploadedScores() {
		return uploadedScores;
	}

	/** Set the score uploaded status
	 */
	public static void setUploadedScores(boolean uploadedScores) {
		EndGameMenu.uploadedScores = uploadedScores;
	}

	@Override
	/** Input handler if required for the game menu.
	 */
	public void ProcessInput(int key) {
		return;
	}
}
