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
	
	/** The local copies and the copies we need to update to the server **/
	private static ArrayList<HighScore> toUpdate;
	private static Map<Integer, ArrayList<HighScore>> onlineHighScores = new HashMap<Integer, ArrayList<HighScore>>();

	/** The timer for the interval at which we refresh from the server **/
	long startTime = System.currentTimeMillis();
	long elapsedTime =0;

	/**Create the high score background thread, initiating
	 * the online high score board and initiating
	 * the array list
	 */
	public HighScoreBackgroundThread(){
		//Initiate online high scores.
		OnlineHighScoreLoader.initiateScores();
		//Initiate the local array list
		toUpdate = new ArrayList<HighScore>();
	}
	
	@Override
	/**The runnable portion of the thread, checks intermittently for new scores and 
	 * adds new scores scheduled for upload to the server as they arrive
	 */
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
	
	/** Add a high score to the list waiting to be updated 
	 * 
	 * @param score
	 */
	public static void addScore(HighScore score){
		System.out.println(score + " " + toUpdate);
		toUpdate.add(score);
	}
}
