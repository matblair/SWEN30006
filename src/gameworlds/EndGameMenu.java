package gameworlds;

import gameengine.InputManager;
import gameengine.Portal2D;
import gamestates.GameState;
import gamestates.LoadingState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourcemanagers.AchievementLoader;
import resourcemanagers.AssetManager;
import resourcemanagers.HighScoreLoader;
import scoringsystem.GLaDOS;

public class EndGameMenu extends InGameMenu {

	private static boolean uploadedScores = false;
	private Image endbg;
	private static GLaDOS glados;
	private static String FINISHED="Congratulations!";

	public EndGameMenu(){
		endbg = AssetManager.requestUIElement("PAUSEBG");
	}

	@Override
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
			int ystart=310, i=0;
			g.drawString("Here are your level stats" , 430, ystart+i*30);
			g.setColor(Color.darkGray);
			i++;
			g.drawString("It took you " + glados.getLevelStats().getTimeInLevel()/1000 + " seconds to finish.", 430, ystart+i*30);
			i++;
			g.drawString("You fell " + glados.getLevelStats().getDistFallen() + " metres.", 430, ystart+i*30);
			i++;
			g.drawString("You walked " + glados.getLevelStats().getDistWalked() + " metres.", 430, ystart+i*30);
			i++;
			g.drawString("You unlocked " + glados.getLevelStats().getAchievementsUnlocked() + " achievements.", 430, ystart+i*30);			
			if(!isUploadedScores()){
				g.drawString("Saving High Score..." ,  gc.getWidth()/2 - font.getWidth("Saving High Score...")/2, gc.getHeight()/2 + endbg.getHeight()/2 - 60);
			}else{
				g.drawString("Hit Enter To Continue!" ,  gc.getWidth()/2 - font.getWidth("Hit Enter To Continue!")/2, gc.getHeight()/2 + endbg.getHeight()/2 - 60);
			}
		}

	}

	@Override
	public void Update(Graphics g, GameContainer gc, StateBasedGame sbg) {
		if(glados!=null && !isUploadedScores()){
			System.out.println("Submitting scores...");
			glados.updateHighScores(glados.getLevelStats().getLevelID());
			glados.updateAchievements(AssetManager.getAchievementMap());
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


	
	@Override
	public void ProcessInput(int key) {
	}

	public static void giveGlados(GLaDOS glados) {
		glados.finaliseStats();
		EndGameMenu.glados=glados;
	}

	public static boolean isUploadedScores() {
		return uploadedScores;
	}

	public static void setUploadedScores(boolean uploadedScores) {
		EndGameMenu.uploadedScores = uploadedScores;
	}
}
