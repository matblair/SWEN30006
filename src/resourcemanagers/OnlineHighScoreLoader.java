package resourcemanagers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gameengine.Portal2D;
import scoringsystem.HighScore;
import st.mark.highscores.HighscoreBoard;
import st.mark.highscores.HighscoreItem;

public class OnlineHighScoreLoader {
	private static final int STARTLOCAL = 3;
	
	public static boolean needupdate=false;
	private static ArrayList<Integer> toupdate = new ArrayList<Integer>();

	private static HighscoreBoard hs;
	
	private static Map<Integer, ArrayList<HighScore>> localcopies;

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
		System.out.println(levelscores.size());
		return levelscores;
	}
	
	public static void initiateScores(){
		hs = new HighscoreBoard(Portal2D.gameKey);
		localcopies = new HashMap<Integer, ArrayList<HighScore>>();
		//Download first few high score levels for faster loading in game, load later levels as we progress.
		for(int i=0; i<STARTLOCAL; i++){
			localcopies.put(i, getTop(i,5));
		}
	}

	public static void addScore(String name, int time, int levelid){
		hs.addNewScore(time, name, Integer.toString(levelid), "", "", "");
	}

	public static ArrayList<HighScore> getTop(int levelid, int number){
		ArrayList<HighScore> temp = getScores(levelid);
		ArrayList<HighScore> fin = new ArrayList<HighScore>();
		if(temp.size()<number){
			number=temp.size();
		}
		for(int i=0; i<number; i++){
			fin.add(temp.get(i));
		}
		
		return fin;
	}
	
	public static ArrayList<HighScore> getLocalFirst(int levelid, int number){
		
		
		if(needupdate){
			if(getToupdate().contains(levelid)){
				localcopies.put(levelid, getTop(levelid,number));
				getToupdate().remove(levelid);
				if(getToupdate().isEmpty()){
					setNeedupdate(false);
				}
			}
		}
		
		if(!localcopies.containsKey(levelid)){
			localcopies.put(levelid, getTop(levelid,number));
		}
		
		return localcopies.get(levelid);		
	}

	/**
	 * @param needupdate the needupdate to set
	 */
	public static void setNeedupdate(boolean needupdate) {
		OnlineHighScoreLoader.needupdate = needupdate;
	}

	/**
	 * @return the toupdate
	 */
	public static ArrayList<Integer> getToupdate() {
		return toupdate;
	}

	/**
	 * @param toupdate the toupdate to set
	 */
	public static void setToupdate(ArrayList<Integer> toupdate) {
		OnlineHighScoreLoader.toupdate = toupdate;
	}
	

}
