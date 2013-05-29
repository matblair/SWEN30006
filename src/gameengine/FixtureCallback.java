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
	
	/** Method for handling fixture collisions. Used by AABB.
	 * 
	 * @param fixture The fixture being reported
	 */
	public boolean reportFixture(Fixture fixture) {
		String id = fixture.getBody().toString();
		String type = GameState.getLevel().getBodyType(fixture.getBody());
		interactableObjects.add(id);
		
		checkJump(type, fixture);
		
		if(type.equals("cube")){
			cube = GameState.getLevel().getCube(id);
			return false;
		}
		
		return true;
	}
	
	/** Check if the fixture is something the player can jump off
	 * 
	 * @param type Type of object
	 * @param fixture The fixture associated with the object
	 */
	private void checkJump(String type, Fixture fixture) {
		if((type.equals("wall") && !fixture.isSensor()) || type.equals("bigswitch") || type.equals("cube")
				|| type.equals("platform") || type.equals("movingplatform") || type.equals("noportalwall")) {
			containsJumpableObject=true;
			return;
		}
		
		for (Portal p : GameState.getLevel().getPortals()) {
			if (!p.isOpen() && p.getBody().equals(fixture.getBody())) {
				containsJumpableObject=true;
			}
			for (Wall wall : p.getTempWalls()) {
				if (wall.getBodyId().equals(fixture.getBody().toString())) {
					containsJumpableObject=true;
				}
			}
		}
	}

	/** Get the ArrayList of interactable objects in the AABB
	 * @return the interactableObjects
	 */
	public  ArrayList<String> getInteractableObjects() {
		return interactableObjects;
	}
	
	/** Get the cube that was found in the AABB
	 * @return the cube
	 */
	public CompanionCube getCube() {
		return cube;
	}

	/** Query if jumpable object was found
	 * @return true if jumpable object was found
	 */
	public boolean isContainsJumpableObject() {
		return containsJumpableObject;
	}

}
