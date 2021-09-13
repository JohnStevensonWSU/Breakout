package bounce;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

/**
 * GameOverState is a BasicGameState that signals the end of a StateBased Game
 */
class GameOverState extends BasicGameState {
	private int timer; // timer to reset game
	private int lastKnownBounces; // final score

	/**
	 * init loads resources needed for state
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	/**
	 * enter is an indication that this game state has been entered
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timer = 4000;
	}

	/**
	 * setUserScore is a setter for lastKnownBounces
	 */
	public void setUserScore(int bounces) {
		lastKnownBounces = bounces;
	}

	/**
	 * render renders every object in the game state
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		g.drawString("Bounces: " + lastKnownBounces, 10, 30);
		g.drawImage(ResourceManager.getImage(BounceGame.GAMEOVER_BANNER_RSC), 225,
				270);
	}

	/**
	 * update updates the state and its components
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		timer -= delta;
		if (timer <= 0)
			game.enterState(BounceGame.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition() );
	}

	/**
	 * getId is a getter for the states' id
	 */
	@Override
	public int getID() {
		return BounceGame.GAMEOVERSTATE;
	}
	
}