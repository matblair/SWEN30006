package gameengine;

import java.util.ArrayList;

import gameobjects.CompanionCube;
import gameobjects.Portal;
import gameobjects.Wall;
import gamestates.GameState;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Fixture;

public class FixtureCallback implements QueryCallback{
	
	private  ArrayList<String> interactableObjects = new ArrayList<String>();
	private CompanionCube cube;
	private boolean containsJumpableObject=false;
	
	public boolean reportFixture(Fixture fixture) {
		String id = fixture.getBody().toString();
		String type = GameState.getLevel().getBodyType(fixture.getBody());
		interactableObjects.add(id);
		
		checkJump(type, fixture);
		
		if(type.equals("cube")){
			setCube(GameState.getLevel().getCube(id));
			return false;
		}
		
		return true;
	}
	
	private void checkJump(String type, Fixture fixture) {
		if((type.equals("wall") && !fixture.isSensor()) || type.equals("bigswitch") || type.equals("cube")
				|| type.equals("platform") || type.equals("movingplatform") || type.equals("noportalwalls")) {
			containsJumpableObject=true;
			return;
		}
		
		for (Portal p : GameState.getLevel().getPortals()) {
			if (!p.isOpen() && p.getBody().equals(fixture.getBody())) {
				containsJumpableObject=true;
			} else {
				for (Wall wall : p.getTempWalls()) {
					if (wall.getBodyId().equals(type)) {
						containsJumpableObject=true;
					}
				}
			}
		}
	}

	/**
	 * @return the interactableObjects
	 */
	public  ArrayList<String> getInteractableObjects() {
		return interactableObjects;
	}

	/**
	 * @return the cube
	 */
	public CompanionCube getCube() {
		return cube;
	}

	/**
	 * @param cube the cube to set
	 */
	public void setCube(CompanionCube cube) {
		this.cube = cube;
	}

	/**
	 * @return the containsJumpableObject
	 */
	public boolean isContainsJumpableObject() {
		return containsJumpableObject;
	}

}
