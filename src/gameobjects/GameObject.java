package gameobjects;

import gamestates.PhysUtils;

import java.awt.geom.Point2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import org.newdawn.slick.GameContainer;

public class GameObject {
	/** Position as a point (Double) **/
	private Point2D.Double position;
	/** Whether or not this unit blocks the passage of other units **/
	private boolean blocked;
	/** Render positions **/
	private double xRender, yRender;	
	/** The object's image **/
	private Image object;
	/** The left and right facing images for the players **/
	private Image object_right;
	private Image object_left; 
	/** Whether or not the object is currently visible **/
	private boolean visible;
	/** The objects name **/
	private String name;
	Body body;
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
		System.out.printf ("(w,h) = (%4.2f,%4.2f)\n", dimensions.x, dimensions.y);
	}
	
	private void createBody(Vec2 location, World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(location);
		body = world.createBody(bodyDef);
		
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(dimensions.x, dimensions.y);
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
	
	public void draw(GameContainer gc) {
		Vec2 slickPos = PhysUtils.JBoxToSlickVec(body.getPosition());
		object.draw(slickPos.x, gc.getHeight()-slickPos.y);
	}

	
	public void setPosition(final Point2D.Double position) {
		this.position = position;
	}
	public Point2D.Double getPosition() {
		return position;
	}
	public float getX() {
		return (float)getPosition().x;
	}
	public float getY() {
		return (float)getPosition().y;
	}
	public void setX(Double X) {
		this.position.x=X;
	}
	public void setY(Double Y) {
		this.position.y=Y;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public Image getObject_left() {
		return object_left;
	}
	public void setObject_left(Image object_left) {
		this.object_left = object_left;
	}
	public Image getObject() {
		return object;
	}
	public void setObject(Image object) {
		this.object = object;
	}
	public double getxRender() {
		return xRender;
	}
	public void setxRender(double xRender) {
		this.xRender = xRender;
	}
	public double getyRender() {
		return yRender;
	}
	public void setyRender(double yRender) {
		this.yRender = yRender;
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public Image getObject_right() {
		return object_right;
	}
	public void setObject_right(Image object_right) {
		this.object_right = object_right;
	}


}
