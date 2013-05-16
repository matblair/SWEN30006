package scoringsystem;

public class BasicAchievement extends Achievement {

	public BasicAchievement(String name, String description, boolean unlocked,
			String imgID) {
		super(name, description, unlocked, imgID);
	}


	@Override
	public boolean checkUnlock(LevelStats stats) {
		return false;
	}

}
