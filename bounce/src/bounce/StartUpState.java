package bounce;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * StartUpState is the initial state of a game. It transitions to the first level of the game
 */
class StartUpState extends BasicGameState {
	private BasicGameState nextState; // next state
	private BounceGame bg; // game associated with start up state

	/**
	 * Signals the StartUpState has been called. This is where all the resources are loaded.
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	/**
	 * Signals the start of this game state
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bg = (BounceGame) game;
		// turn on the sound
		container.setSoundOn(true);
		// set the next level
		nextState = (Level) bg.getState(bg.LEVEL_1);
		// play the opening music
		ResourceManager.getSound(BounceGame.TITLE_MUSIC_RSC).play();
	}


	/**
	 * renders all contents of state
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		// draw splash screen
		g.drawString("Breakout", 365, 100);
		g.drawString("By John Stevenson", 322, 150);
		g.drawString("Press Enter...", 345, 300);
	}

	/**
	 * Updates the condition and contents of StartUpState
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		BounceGame bg = (BounceGame) game;
		checkInput(container, game);
	}

	/**
	 * getter for state id
	 */
	@Override
	public int getID() {
		return BounceGame.STARTUPSTATE;
	}

	/**
	 * checks the input from the user
	 */
	private void checkInput(GameContainer container, StateBasedGame game) {
		Input input = container.getInput();

		// start the game if the user presses enter
		if (input.isKeyDown(Input.KEY_ENTER) || input.isKeyDown(Input.KEY_NUMPADENTER)) {
			game.enterState(nextState.getID());
		}
	}
}