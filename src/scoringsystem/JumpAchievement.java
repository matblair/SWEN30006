package scoringsystem;

public class JumpAchievement extends Achievement {
	private final static String actype="jumps";


	public JumpAchievement(String name, String description, boolean unlocked,
			String imgID, int levelId, float target,boolean persistant) {
		super(name, description, unlocked, imgID,actype,levelId, target,persistant);
	}


	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getJumps()>=target){
				unlockAchievement();
				isUnlocked=true;
			}
		} else if (levelId==stats.getLevelID() && stats.getJumps()>=target){
			unlockAchievement();
			isUnlocked=true;
		}	
		
		return isUnlocked;
	}
	
	@Override
	public void decrementStats(LevelStats stats) {
		setDiff(stats.getJumps()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getJumps()+getDiff());
	}
	

}
