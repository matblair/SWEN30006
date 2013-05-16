package scoringsystem;

import gameengine.Portal2D;
import gameobjects.Player;

// Genetic Lifeform and Disk Operating System
// i.e. stats watcher
import java.util.Map;



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
		//Update falling or dist travelled on ground velocity
		if(player.isOnGround()){
			stats.setDistWalked(stats.getDistWalked() + player.getBody().getLinearVelocity().length()*(delta/1000));
		}else {
			stats.setDistFallen(stats.getDistFallen() + player.getBody().getLinearVelocity().length()*(delta/1000));
		}
	}
	
	public void pickupCube(){
		stats.setCubesPickedUp(stats.getCubesPickedUp()+1);
	}
	
	public void createdPortal(){
		stats.setNumberPortals(stats.getNumberPortals()+1);
	}
	
	public void updateHighScores(int levelid) {
		OnlineHighScoreLoader.addScore(Portal2D.name, stats.getTimeInLevel(), stats.getLevelID());
		OnlineHighScoreLoader.setNeedupdate(true);
		OnlineHighScoreLoader.getToupdate().add(levelid);
	}

	public void updateAchievements(Map<String, Achievement> achievementMap) {
		for(Achievement ac: achievementMap.values()){
			if(ac.checkUnlock(stats)){
				stats.setAchievementsUnlocked(stats.getAchievementsUnlocked()+1);
			}
		}
	}

	public void printStats() {
		System.out.println(stats.getNumberPortals() + " in level " + stats.getTimeInLevel()/1000);
	}

	public LevelStats getLevelStats() {
		return stats;
	}
	
}
