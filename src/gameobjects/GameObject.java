package gameobjects;

import gameengine.PhysUtils;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class GameObject {
	/** The object's image **/
	private Image object;
	/** The left and right facing images for the players **/
	private Image object_right;
	@SuppressWarnings("unused")
	private Image object_left; 

	/** The objects name **/
	private String name;
	private Body body;
	private Vec2 dimensions; // JBox dimensions

	public GameObject (String imgloc, Vec2 location, World world)
			throws SlickException {
		object_right = new Image(imgloc);
		setObject_left(object_right.getFlippedCopy(true,false));
		setObject(object_right);
		object = object_right;

		dimensions = PhysUtils.SlickToJBoxVec(new Vec2(object.getWidth(), object.getHeight()));
		createBody(location,world);		
		System.out.printf ("(x,y) = (%4.2f,%4.2f)\n", location.x, location.y);
		System.out.printf ("(w,h) = (%4.2f,%4.2f)\n", dimensions.x/2, dimensions.y/2);
	}
	
	private void createBody(Vec2 location, World world){
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(location);
		bd.fixedRotation = true;
		body = world.createBody(bd);
		
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(dimensions.x/2, dimensions.y/2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density=1;
		fixtureDef.friction=0.3f;
		body.createFixture(fixtureDef);
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vec2 getDimensions() {
		return dimensions;
	}
	
	public Vec2 getLocation() {
		return body.getPosition();
	}
	
	/** Returns the top left position of the player
	 * @return Top left position of player as a JBox position
	 */
	public Vec2 getTopLeftLoc() {
		Vec2 addVec = new Vec2(-dimensions.x/2, dimensions.y/2);
		return body.getPosition().add(addVec);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Image getImage() {
		return object;
	}
	public void setObject(Image object) {
		this.object = object;
	}
	
	public void setObject_left(Image object_left) {
		this.object_left = object_left;
	}
	public void setObject_right(Image object_right) {
		this.object_right = object_right;
	}


}
