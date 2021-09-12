package bounce;

import java.util.Iterator;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the bounce counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {
	private BasicGameState nextState;
	private BounceGame bg;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bg = (BounceGame) game;
		container.setSoundOn(true);
		nextState = (Level) bg.getState(bg.LEVEL_1);
		ResourceManager.getSound(BounceGame.TITLE_MUSIC_RSC).play();
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		g.drawString("Breakout", 365, 100);
		g.drawString("By John Stevenson", 322, 150);
		g.drawString("Press Enter...", 345, 300);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		BounceGame bg = (BounceGame) game;
		checkInput(container, game);
	}

	@Override
	public int getID() {
		return BounceGame.STARTUPSTATE;
	}

	private void checkInput(GameContainer container, StateBasedGame game) {
		Input input = container.getInput();

		if (input.isKeyDown(Input.KEY_ENTER) || input.isKeyDown(Input.KEY_NUMPADENTER)) {
			game.enterState(nextState.getID());
		}
	}
}