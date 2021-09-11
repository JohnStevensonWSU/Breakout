package bounce;

import java.util.Iterator;

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
class PlayingState extends BasicGameState {
	int bounces;
	int numLives = 3;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		
		bg.ball.render(g);
		bg.paddle.render(g);
		for (Block b : bg.blocks) {
			if (!b.getIsBroken()) {
				b.render(g);
			}
		}
		g.drawString("Bounces: " + bounces, 10, 30);
		for (Bang b : bg.explosions)
			b.render(g);
		for (Heart h : bg.hearts) {
			if (h.getStatus()) {
				h.render(g);
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
		boolean blockExists = false;

		if (input.isKeyDown(Input.KEY_UP)) {
			//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, -.001f)));
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, +.001f)));
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(-.001f, 0)));
			bg.paddle.setVelocity(new Vector(-.25f, 0f));
		} else if (!input.isKeyDown(Input.KEY_RIGHT)) {
				//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(+.001f, 0f)));
				bg.paddle.setVelocity(new Vector(0f, 0f));
			}

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(+.001f, 0f)));
			bg.paddle.setVelocity(new Vector(.25f, 0f));
		} else if (!input.isKeyDown(Input.KEY_LEFT)) {
			//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(-.001f, 0)));
			bg.paddle.setVelocity(new Vector(-0f, 0f));
		}

		// bounce the ball...
		boolean bounced = false;
		if (bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.ball.getCoarseGrainedMinX() < 0) {
			bg.ball.bounce(90);
			bounced = true;
		} else if (bg.ball.getCoarseGrainedMinY() < 0) {
			bg.ball.bounce(0);
			bounced = true;
		} else if(bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight) {
			numLives = numLives - 1;
			bg.ball.reset();
			if (numLives > 0) {
				bg.hearts[numLives - 1].killHeart();
			}
		}

		if (bounced) {
			bg.explosions.add(new Bang(bg.ball.getX(), bg.ball.getY()));
			bounces++;
		}

		if (bg.ball.getCoarseGrainedMaxY() > bg.paddle.getCoarseGrainedMinY() && bg.ball.getCoarseGrainedMaxY() < bg.paddle.getCoarseGrainedMaxY()) {
			if (bg.ball.getCoarseGrainedMinX() > bg.paddle.getCoarseGrainedMinX()  && bg.ball.getCoarseGrainedMinX() < bg.paddle.getCoarseGrainedMaxX()
					|| bg.ball.getCoarseGrainedMaxX() < bg.paddle.getCoarseGrainedMaxX() && bg.ball.getCoarseGrainedMaxX() > bg.paddle.getCoarseGrainedMinX()) {
				bg.ball.bounce(0);
				bounced = true;
			}
		}
		for (Block b : bg.blocks) {
			if (!b.getIsBroken()) {
				if (bg.ball.getCoarseGrainedMinY() < b.getCoarseGrainedMaxY() && bg.ball.getCoarseGrainedMinY() > b.getCoarseGrainedMinY()) {
					if (bg.ball.getCoarseGrainedMinX() > b.getCoarseGrainedMinX() && bg.ball.getCoarseGrainedMinX() < b.getCoarseGrainedMaxX()
							|| bg.ball.getCoarseGrainedMaxX() < b.getCoarseGrainedMaxX() && bg.ball.getCoarseGrainedMaxX() > b.getCoarseGrainedMinX()) {
						bg.ball.bounce(0);
						bounced = true;
						b.breakBlock();
					}
				}
			}
		}

		bg.ball.update(delta);
		bg.paddle.update(delta);

		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}

		for (Block b : bg.blocks) {
			if (!b.getIsBroken()) {
				blockExists = true;
			}
		}

		if (numLives <= 0 || !blockExists) {
			((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
			game.enterState(BounceGame.GAMEOVERSTATE);
		}

	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}
	
}