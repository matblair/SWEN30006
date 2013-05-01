package gameworlds;

import gameobjects.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;



public class TestWorld {
	/** Our Player **/
	private  Player player;

	public TestWorld(GameContainer gc) throws SlickException {
		//player=Player.getPlayer("/assets/sprites/chell.png", 400, 300);
		// TODO Auto-generated constructor stub
	}

	public void updateGameState(GameContainer container) {
		// TODO Auto-generated method stub
		
	}

	public void render(Graphics g, boolean debug) {
		// TODO Auto-generated method stub
		player.getObject().draw(player.getX(), player.getY());
		
	}

	public void update(double dir_x, double dir_y, int delta, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
		player.updatePos(dir_x, dir_y, delta);
		
	}

}
