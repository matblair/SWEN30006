package gameengine;

import java.util.ArrayList;

import gameobjects.CompanionCube;
import gamestates.GameState;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Fixture;

public class FixtureCallback implements QueryCallback{
	
	private  ArrayList<String> interactableObjects = new ArrayList<String>();
	private CompanionCube cube;
	
	public boolean reportFixture(Fixture fixture) {
		String idd = fixture.getBody().toString();
		String type = GameState.getLevel().getBodyType(fixture.getBody());
		System.out.println(type);
		if(type.equals("cube")){
			interactableObjects.add(idd);
			setCube(GameState.getLevel().getCube(idd));
			return false;
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

}
