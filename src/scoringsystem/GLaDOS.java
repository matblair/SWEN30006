package scoringsystem;

import gameengine.Portal2D;
import gameobjects.Player;
import gamestates.GameState;

import java.util.ArrayList;
// Genetic Lifeform and Disk Operating System
// i.e. stats watcher
import java.util.Map;
import org.newdawn.slick.SlickException;

import resourcemanagers.AchievementLoader;
import resourcemanagers.AssetManager;
import resourcemanagers.HighScoreLoader;

public class GLaDOS {

	private LevelStats stats;
	private static final int TIMER=5000;

	public GLaDOS(int levelid){
		stats = new LevelStats(levelid);
	}

	public void updateTesting(int delta, Player player){
		//Update timer
		stats.setTimeInLevel(stats.getTimeInLevel()+delta);

		//Update velocity
		if(player.getBody().getLinearVelocity().length()>stats.getMaxVelocity()){
			stats.setMaxVelocity(player.getBody().getLinearVelocity().length());
		}

		//Update falling or dist traveled on ground velocity
		if(player.isOnGround()){
			stats.setDistWalked(stats.getDistWalked() + Math.abs(player.getBody().getLinearVelocity().x)*(delta));
		}else {
			float vely = player.getBody().getLinearVelocity().y;
			if(vely<0){
				stats.setDistFallen(stats.getDistFallen() + Math.abs(vely)*(delta));
				stats.setTimeFallen(stats.getTimeFallen() + delta);
			}
		}

		//Update Achievements
		updateAchievements(AssetManager.getAchievementMap());
		//Decrement Achievement counters
		if(!GameState.getLevel().getAchievementPopups().isEmpty()){
			for(int i=0; i<GameState.getLevel().getAchievementPopups().size(); i++){
				GameState.getLevel().getAchievementPopups().get(i).setTimer(GameState.getLevel().getAchievementPopups().get(i).getTimer()-delta);
				if(GameState.getLevel().getAchievementPopups().get(i).getTimer()<=0){
					GameState.getLevel().getAchievementPopups().remove(GameState.getLevel().getAchievementPopups().get(i));
				}
			}
		}	
	}

	public void pickupCube(){
		stats.setCubesPickedUp(stats.getCubesPickedUp()+1);
	}

	public void createdPortal(){
		stats.setNumberPortals(stats.getNumberPortals()+1);
	}

	public void jumped(){
		stats.setJumps(stats.getJumps()+1);
	}

	public void updateHighScores(int levelid) {

		if(!AssetManager.getHighscores().containsKey(levelid)){
			ArrayList<HighScore> newarray = new ArrayList<HighScore>();
			AssetManager.getHighscores().put(levelid,newarray);
		}
		HighScore toAdd = new HighScore(Portal2D.name, stats.getTimeInLevel()/1000, stats.getLevelID());
		AssetManager.getHighscores().get(levelid).add(toAdd);
		HighScoreBackgroundThread.addScore(toAdd);
	}

	public void finaliseStats(){
		stats.setDistWalked(stats.getDistWalked()/1000);
		stats.setDistFallen(stats.getDistFallen()/1000);
		stats.setTimeFallen(stats.getTimeFallen()/1000);
		try {
			AchievementLoader.saveAchievements();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateAchievements(Map<String, Achievement> achievementMap) {
		for(Achievement ac: achievementMap.values()){
			if(!ac.isUnlocked() && ac.checkUnlock(stats)){
				stats.setAchievementsUnlocked(stats.getAchievementsUnlocked()+1);
				GameState.getLevel().getAchievementPopups().add(new AchievementPopup(TIMER,ac.getImgid(), ac.getName()));
			}
			if(ac.isPersistant()){
				ac.decrementStats(stats);
			}
		}
	}

	public void printStats() {
	}

	public LevelStats getLevelStats() {
		return stats;
	}

}
