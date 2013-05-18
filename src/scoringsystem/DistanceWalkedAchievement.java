package scoringsystem;

public class DistanceWalkedAchievement extends Achievement{
	
	private final static String actype="distwalked";


	public DistanceWalkedAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float distWalked) {
		super(name, description, unlocked, imgID, actype,levelId,distWalked);
	}

	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getDistWalked()>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getDistWalked()>=target){
			unlockAchievement();
			isUnlocked=true;

		}
		
		return isUnlocked;
	}

}
