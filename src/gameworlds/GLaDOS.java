package gameworlds;

import gameengine.Achievement;
import gameengine.HighScore;

import java.util.ArrayList;
// Genetic Lifeform and Disk Operating System
// i.e. stats watcher
import java.util.Map;

public class GLaDOS {
	
	private int inleveltime=0;
	private int levelid;
	
	private float distonground;
	private float distinair;
	
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

	public void updateHighScores(ArrayList<HighScore> arrayList) {
		// TODO Auto-generated method stub
		
	}

	public void updateAchievements(Map<String, Achievement> achievementMap) {
		// TODO Auto-generated method stub
		
	}

	public void printStats() {
		System.out.println(portalno + " in level " + inleveltime/1000);
	}
	
	

}
