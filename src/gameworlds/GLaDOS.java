package gameworlds;

import gameengine.HighScore;
import gameengine.Portal2D;

import java.util.ArrayList;
// Genetic Lifeform and Disk Operating System
// i.e. stats watcher
import java.util.Map;

import Achievements.Achievement;

import resourcemanagers.OnlineHighScoreLoader;

public class GLaDOS {
	
	private int inleveltime=0;
	private int levelid;
		
	private int portalno;
	
	public GLaDOS(int levelid){
		this.levelid = levelid;
	}
	
	public void updateTesting(int delta){
		inleveltime += delta;
	}
	
	public void createdPortal(){
		portalno++;
	}

	public void updateHighScores(int levelid) {
		OnlineHighScoreLoader.addScore(Portal2D.name, inleveltime, levelid);
		OnlineHighScoreLoader.setNeedupdate(true);
		OnlineHighScoreLoader.getToupdate().add(levelid);
	}

	public void updateAchievements(Map<String, Achievement> achievementMap) {
		for(Achievement ac: achievementMap.values()){
			ac.checkUnlock();
		}
	}

	public void printStats() {
		System.out.println(portalno + " in level " + inleveltime/1000);
	}
	
	

}
