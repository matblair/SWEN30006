package resourcemanagers;

import java.util.ArrayList;
import gameengine.Portal2D;
import scoringsystem.HighScore;
import st.mark.highscores.HighscoreBoard;
import st.mark.highscores.HighscoreItem;

public class OnlineHighScoreLoader {

	/** Our object for our online high score board **/
	private static HighscoreBoard hs;

	/** Requests all scores for a given level id
	 * 
	 * @param levelid
	 * @return
	 */
	public static ArrayList<HighScore> getScores(int levelid){
		String id = Integer.toString(levelid);

		ArrayList<HighScore> levelscores = new ArrayList<HighScore>();
		int i=0;
		int max=100;
		while(!hs.getScoreRange(i, max).isEmpty()){
			ArrayList<HighscoreItem> scores = hs.getScoreRange(i, max);
			for(HighscoreItem hs: scores){
				if(hs.getText2().equals(id)){
					levelscores.add(new HighScore(hs.getText1(),Float.parseFloat(hs.getScore()), Integer.parseInt(hs.getText2())));
				}
			}
			i=max;
			max+=100;
		}
		return levelscores;
	}

	/** Sets up the connection to the server using the game key located in the main game
	 *  class
	 */
	public static void initiateScores(){
		hs = new HighscoreBoard(Portal2D.gameKey);
	}

	/** Adds a score to the online high score board
	 * 
	 * @param name
	 * @param time
	 * @param levelid
	 */
	public static void addScore(String name, int time, int levelid){
		hs.addNewScore(time, name, Integer.toString(levelid), "", "", "");
	}

	/** Adds a list of scores to the online score board
	 * 
	 * @param toAdd
	 */
	public static void addScoreList(ArrayList<HighScore> toAdd){
		for(HighScore hs: toAdd){
			addScore(hs.getName(),(int) hs.getScore(),hs.getLevelid());
		}
	}


}
