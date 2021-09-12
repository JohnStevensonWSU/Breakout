package bounce;

import java.util.Iterator;

import jig.Entity;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class Level1 extends Level {

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		super.enter(container, game);
		nextState = (BasicGameState) bg.getState(bg.LEVEL_2);
		blocks = new Block[10];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new Block(bg.ScreenWidth * (i + 1) / 11, bg.ScreenHeight * 1 / 4);
		}
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		super.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		super.update(container, game, delta);
	}

	@Override
	public int getID() {
		return BounceGame.LEVEL_1;
	}
}