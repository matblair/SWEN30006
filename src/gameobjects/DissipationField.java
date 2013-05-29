package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import resourcemanagers.AssetManager;

public class DissipationField extends GameObject {
	
	private static final String ANIMID="FIELD";
	private static final String SHAPEID="FIELDSHAPE";
	private static final int BODYTYPE = PhysUtils.STATIC;
	private static final float DIST = 0.5f;
	private Animation animation;
	
	public DissipationField(Vec2 location, World world)
			throws SlickException {
		super();
		FixtureDef fixture = createFixture(SHAPEID);
		fixture.isSensor=true;
		this.createBody(location, world, fixture, BODYTYPE);
		animation = AssetManager.requestAnimationResources(ANIMID);
		animation.start();
	}
	
	public void update(int delta){
		animation.update(delta);
	
		// Check contacts
		ContactEdge edge = this.getBody().getContactList();
		while (edge != null) {
			String type=GameState.getLevel().getBodyType(edge.other);
			if(type.equals("player") && PhysUtils.distance(this.getLocation(), GameState.getLevel().getLevelPlayer().getLocation())<=DIST){
				System.out.println("Should be clearing portals here");
				GameState.getLevel().clearPortals();
			} else if (type.equals("cube") && PhysUtils.distance(this.getLocation(), edge.other.getPosition())<=DIST){
				if(GameState.getLevel().getLevelPlayer().isCarryingCube() && GameState.getLevel().getLevelPlayer().getCubeCarrying().getBodyID().equals(edge.other.toString())){
					GameState.getLevel().getLevelPlayer().dropCube();
				}
				GameState.getLevel().removeCube(edge.other.toString());
			}
			edge = edge.next;
		}
		this.setSprite(animation.getCurrentFrame());
		return;
	}
	
	@Override
	public Image getImage() {
		return animation.getCurrentFrame();
	}
}
