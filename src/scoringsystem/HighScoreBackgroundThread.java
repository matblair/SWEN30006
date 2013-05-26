package scoringsystem;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;
import resourcemanagers.HighScoreLoader;
import resourcemanagers.OnlineHighScoreLoader;

public class HighScoreBackgroundThread implements Runnable {
	
	private static ArrayList<HighScore> toUpdate;
	private static Map<Integer, ArrayList<HighScore>> onlineHighScores = new HashMap<Integer, ArrayList<HighScore>>();
	
	long startTime = System.currentTimeMillis();
	long elapsedTime =0;


	public HighScoreBackgroundThread(){
		toUpdate = new ArrayList<HighScore>();
		elapsedTime = 2*60*1000;
	}
	
	@Override
	public void run() {
		while(true){
			//Add any highscores neccessary to the online database
			if(!toUpdate.isEmpty()){
				OnlineHighScoreLoader.addScoreList(toUpdate);
				toUpdate.clear();
			}
			
			//We need to download onlineHighScores and update changes
			//We do this every 5 minutes.
			if (elapsedTime < 60*1000) {
			    elapsedTime = (new Date()).getTime() - startTime;

			} else {
				//Five minutes has passed, download online server for all levels.
				startTime = System.currentTimeMillis();
				//Clear the hashmap
				onlineHighScores.clear();
				for(int i=0; i<AssetManager.getLevelXmlResources().size() ; i++){
					onlineHighScores.put(i, OnlineHighScoreLoader.getScores(i));
				}
				
				AssetManager.setHighscores(onlineHighScores);	
				System.out.println("Updated high scores");
				try {
					HighScoreLoader.saveHighScores();
				} catch (SlickException e) {
					e.printStackTrace();
				}	
				
				elapsedTime=0;
			}	
		}
	}
	
	public static void addScore(HighScore score){
		System.out.println(score + " " + toUpdate);
		toUpdate.add(score);
	}
}
