package scoringsystem;

import gameengine.Portal2D;
import gameobjects.Player;
import gamestates.GameState;

import java.util.ArrayList;
import java.util.Map;
import org.newdawn.slick.SlickException;

import resourcemanagers.AchievementLoader;
import resourcemanagers.AssetManager;

public class GLaDOS {

	/** The level stats as a level stats object **/
	private LevelStats stats;
	/** The timer for the duration of achievement popups **/
	private static final int TIMER=5000;

	/** Instantiate glados and create a new level stats for each level 
	 * 
	 * @param levelid
	 */
	public GLaDOS(int levelid){
		stats = new LevelStats(levelid);
	}
	
	/** Update the testing procedures, calculate the various different 
	 * stats achieved by the player and set them in stats. Then pass those stats
	 * to check if we have unlocked any achievements
	 * @param delta
	 * @param player
	 */
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
	
	/** Updates the stats counter for cubes picked up
	 */
	public void pickupCube(){
		stats.setCubesPickedUp(stats.getCubesPickedUp()+1);
	}

	/** Updates the stats counter for portals created
	 */
	public void createdPortal(){
		stats.setNumberPortals(stats.getNumberPortals()+1);
	}
	
	/** Update the stats counter for jumps **/
	public void jumped(){
		stats.setJumps(stats.getJumps()+1);
	}

	/** Add a copy of the high score locally so it can be displayed immediately 
	 * and queue for upload to the high score server 
	 * @param levelid
	 */
	public void updateHighScores(int levelid) {

		if(!AssetManager.getHighscores().containsKey(levelid)){
			ArrayList<HighScore> newarray = new ArrayList<HighScore>();
			AssetManager.getHighscores().put(levelid,newarray);
		}
		HighScore toAdd = new HighScore(Portal2D.name, stats.getTimeInLevel()/1000, stats.getLevelID());
		AssetManager.getHighscores().get(levelid).add(toAdd);
		HighScoreBackgroundThread.addScore(toAdd);
	}
	
	/** Finalise the stats in seconds rather than milliseconds and
	 * save all achievements achieved for that level
	 */
	public void finaliseStats(){
		stats.setDistWalked(stats.getDistWalked()/1000);
		stats.setDistFallen(stats.getDistFallen()/1000);
		stats.setTimeFallen(stats.getTimeFallen()/1000);
		try {
			AchievementLoader.saveAchievements();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	/** Update all achievements by checking if each that hasn;t unlocked has unlocked by passing it 
	 * stats. If an achievement does unlock, create an achievement popup for it and add it to the 
	 * level popup collection
	 * @param achievementMap
	 */
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
	
	/** Return the level stats 
	 * 
	 * @return stats
	 */
	public LevelStats getLevelStats() {
		return stats;
	}

}
