package scoringsystem;

import gameengine.Portal2D;
import gameobjects.Player;

import java.util.ArrayList;
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
	}
	
	public void pickupCube(){
		stats.setCubesPickedUp(stats.getCubesPickedUp()+1);
	}
	
	public void createdPortal(){
		stats.setNumberPortals(stats.getNumberPortals()+1);
	}

	public void updateHighScores(int levelid) {
		OnlineHighScoreLoader.addScore(Portal2D.name, stats.getTimeInLevel(), levelid);
		OnlineHighScoreLoader.setNeedupdate(true);
		OnlineHighScoreLoader.getToupdate().add(levelid);
	}

	public void updateAchievements(Map<String, Achievement> achievementMap) {
		for(Achievement ac: achievementMap.values()){
			ac.checkUnlock(stats);
		}
	}

	public void printStats() {
		System.out.println(stats.getNumberPortals() + " in level " + stats.getTimeInLevel()/1000);
	}
	
	

}
