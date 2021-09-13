package bounce;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 *  Level1 is the first Level in Breakout. See Level for method descriptions
 */
class Level1 extends Level {

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		super.enter(container, game);
		// sets next state to level 2
		nextState = (BasicGameState) bg.getState(bg.LEVEL_2);
		// creates ten blocks for level 1
		blocks = new Block[10];
		// the blocks are set equally apart 1/4th down the screen
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