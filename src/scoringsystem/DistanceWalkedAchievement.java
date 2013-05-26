package scoringsystem;

public class DistanceWalkedAchievement extends Achievement{
	
	private final static String actype="distwalked";


	public DistanceWalkedAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float distWalked,boolean persistant) {
		super(name, description, unlocked, imgID, actype,levelId,distWalked,persistant);
	}

	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getDistWalked()/1000>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getDistWalked()/1000>=target){
			unlockAchievement();
			isUnlocked=true;

		}
		
		return isUnlocked;
	}

	@Override
	public void decrementStats(LevelStats stats) {
		setDiff(stats.getDistWalked()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getDistWalked()+getDiff());
	}
}
