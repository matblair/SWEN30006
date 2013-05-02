package gameworlds;

import gameengine.*;
import gameobjects.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;



public class Level {
	/** Our Player **/
	private Player player;
	private Image bg;
	private World world;
	private int velocityIterations = 6;
	private int positionIterations = 2;

	public Level(GameContainer gc) throws SlickException {
		//player=Player.getPlayer("/assets/sprites/chell.png", 400, 300);
		// TODO Auto-generated constructor stub

		setBg(new Image("assets/levels/bg.png"));
		// Static Body
		Vec2 gravity = new Vec2(0,-9.8f);
		world = new World(gravity);
		PhysUtils.addWall(world, 0, 0, 100, 1); //floor
		PhysUtils.addWall(world, 0, 0, 0.5f, 10); //left wall
		PhysUtils.addWall(world, 0, 8.823f, 100, 0); //top
		PhysUtils.addWall(world, 10, 2.5f, 4, 0.25f);
		PhysUtils.addWall(world, 14, 3.5f, 4, 0.25f);
		PhysUtils.addWall(world, 99.5f, 0, 0.5f, 10); //right wall

		// Dynamic Body
		player = new Player("/assets/sprites/chell.png",new Vec2(2, 5), world);
	}


	public void render(Graphics g, boolean debug,Camera cam, GameContainer gc) {
		RenderEngine.drawBG(bg, cam);
		RenderEngine.drawGameObject(player, cam);
	}

	public void update(int delta, StateBasedGame sbg) throws SlickException {
		float timeStep = (float)delta/1000;
		world.step(timeStep, velocityIterations, positionIterations);
		
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public Image getBg() {
		return bg;
	}

	public void setBg(Image bg) {
		this.bg = bg;
	}


	public void updateGameState(GameContainer gc) {
		// TODO Auto-generated method stub
		
	}

}
