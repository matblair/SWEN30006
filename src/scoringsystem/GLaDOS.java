package scoringsystem;

import gameengine.Portal2D;
import gameobjects.Player;

// Genetic Lifeform and Disk Operating System
// i.e. stats watcher
import java.util.Map;

import org.newdawn.slick.SlickException;



import resourcemanagers.AssetManager;
import resourcemanagers.HighScoreLoader;
import resourcemanagers.OnlineHighScoreLoader;

public class GLaDOS {
	
	private LevelStats stats;
	
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
		if(Portal2D.online){
			OnlineHighScoreLoader.addScore(Portal2D.name, stats.getTimeInLevel(), stats.getLevelID());
			OnlineHighScoreLoader.setNeedupdate(true);
			OnlineHighScoreLoader.getToupdate().add(levelid);
		} else {
			try {
				AssetManager.getHighscores().get(levelid).add(new HighScore(Portal2D.name, stats.getTimeInLevel()/1000, stats.getLevelID()));
				HighScoreLoader.saveHighScores();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void finaliseStats(){
		stats.setDistWalked(stats.getDistWalked()/1000);
		stats.setDistFallen(stats.getDistFallen()/1000);
		stats.setTimeFallen(stats.getTimeFallen()/1000);

	}

	public void updateAchievements(Map<String, Achievement> achievementMap) {
		for(Achievement ac: achievementMap.values()){
			if(!ac.isUnlocked() && ac.checkUnlock(stats)){
				stats.setAchievementsUnlocked(stats.getAchievementsUnlocked()+1);
			}
		}
	}

	public void printStats() {
	}

	public LevelStats getLevelStats() {
		return stats;
	}
	
}
