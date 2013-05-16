package scoringsystem;

public class DistanceWalkedAchievement extends Achievement{
	
	private float distWalked;
	private int levelId;

	public DistanceWalkedAchievement(String name, String description,
			boolean unlocked, String imgID, float distWalked, int levelId) {
		super(name, description, unlocked, imgID);
		this.distWalked=distWalked;
		this.levelId = levelId;
	}

	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getDistWalked()>=distWalked){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getDistWalked()>=distWalked){
			unlockAchievement();
			isUnlocked=true;

		}
		
		return isUnlocked;
	}

}