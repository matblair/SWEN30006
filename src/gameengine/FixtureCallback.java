package gameengine;

import java.util.ArrayList;

import gameobjects.CompanionCube;
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
		
		if(type.equals("cube")){
			setCube(GameState.getLevel().getCube(id));
			containsJumpableObject=true;
			return false;
		}else if(type.equals("wall") || type.equals("bigswitch") || type.equals("platform") || type.equals("movingplatform") || type.equals("noportalwalls")){
			containsJumpableObject=true;
		}
		
		return true;
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
