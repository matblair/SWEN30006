package gameengine;

import java.util.ArrayList;

import gameobjects.CompanionCube;
import gameobjects.GameObject;
import gamestates.GameState;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

public class FixtureCallback implements QueryCallback{
	
	private static ArrayList<GameObject> interactableObjects = new ArrayList<GameObject>();
	
	public boolean reportFixture(Fixture fixture) {
		CompanionCube cubetype = new CompanionCube();
		if(fixture.getBody().getClass().isInstance(cubetype)){
			String id = fixture.getBody().getClass().toString();
			GameState.getLevel();
		}
		return true;
	}

}
