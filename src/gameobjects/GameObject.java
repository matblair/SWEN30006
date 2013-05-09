package gameobjects;

import gameengine.PhysUtils;
import gamestates.GameState;

import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.newdawn.slick.Image;

import resourcemanagers.AssetManager;

public class GameObject {
	/** The object's image **/
	private Image sprite;
	
	/** The objects body id **/
	private static String bodyId;
	private Body body;
	
	private Vec2 dimensions;
	
	private final int maxAngleForGround = 45;
	private final float maxYNormForGround = (float)Math.asin(maxAngleForGround/180*Math.PI);


	public GameObject(String imgid){
		setSprite(AssetManager.requestImage(imgid));
	}
	
	public GameObject(){
	
	}
	
	protected void createBody(Vec2 location, World world, FixtureDef definition, int bodytype) {
		BodyDef bd = new BodyDef();
		bd.position.set(location);
		
		switch (bodytype){
			case PhysUtils.STATIC: 
				bd.type = BodyType.STATIC;
				break;
			case PhysUtils.DYNAMIC:
				bd.type = BodyType.DYNAMIC;
				break;
			case PhysUtils.KINEMATIC:
				bd.type = BodyType.KINEMATIC;
				break;
			case PhysUtils.PORTAL: 
				bd.type = BodyType.STATIC;
				break;
		}
		
		bd.fixedRotation = true;
		body = world.createBody(bd);
		setBodyId(body.toString());
		body.createFixture(definition);
	}
	
	protected FixtureDef createFixture(String shapeid){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = AssetManager.requestShape(shapeid);
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		return fixtureDef;
	}
	
	public Body getBody() {
		return body;
	}
	
	
	public Vec2 getLocation() {
		return body.getPosition();
	}
	
	public float getRotation() {
		return body.getAngle();
	}
	
	public float getMass() {
		return body.getMass();
	}
	
	public boolean isOnGround() {
		ContactEdge edge = this.getBody().getContactList();
		WorldManifold wm = new WorldManifold();
		Vec2 normal;
		while (edge != null) {
			edge.contact.getWorldManifold(wm);
			normal = wm.normal;
			String edgetype = GameState.getLevel().getBodyType(edge.contact.m_fixtureA.getBody());
			if (normal.y > maxYNormForGround || edgetype.equals("movingplatform")) {
				return true;
			}
			edge = edge.next;
		}
		return false;
	}

	public Vec2 getDimensions(){
		return dimensions;
	}
	
	public void setDimensions(Vec2 dim){
		dimensions=dim;
	}
	
	public Image getImage() {
		return sprite;
	}
	
	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public String getBodyId() {
		return bodyId;
	}
	
	private void setBodyId(String bodyId) {
		GameObject.bodyId = bodyId;
	}
}
