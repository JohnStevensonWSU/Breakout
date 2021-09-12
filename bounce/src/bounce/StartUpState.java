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
	private int numLives = 3;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(false);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		
		bg.ball.render(g);
		bg.paddle.render(g);
		g.drawString("Bounces: ?", 10, 30);
		for (Bang b : bg.explosions)
			b.render(g);
		g.drawImage(ResourceManager.getImage(BounceGame.STARTUP_BANNER_RSC),
				225, 270);		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;

		if (input.isKeyDown(Input.KEY_SPACE)) {
			bg.enterState(BounceGame.LEVEL_1);
		} else if (input.isKeyDown(Input.KEY_2)) {
			bg.enterState((BounceGame.LEVEL_2));
		}

		// bounce the ball...
		float posx = bg.ball.getX();
		float posy = bg.ball.getY();
		float diff;

		if (bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth) {
			diff = bg.ball.getCoarseGrainedMaxX() - bg.ScreenWidth;
			posx = posx - diff;
			if (bg.ball.getVelocity().getY() > 0) {
				posy = posy - diff;
			} else {
				posy = posy + diff;
			}
			bg.ball.setPosition(posx, posy);
			bg.ball.bounce(90);
		} else if (bg.ball.getCoarseGrainedMinX() < 0) {
			diff = -bg.ball.getCoarseGrainedMinX();
			posx = posx + diff;
			if (bg.ball.getVelocity().getY() > 0) {
				posy = posy - diff;
			} else {
				posy = posy + diff;
			}
			bg.ball.setPosition(posx, posy);
			bg.ball.bounce(90);
		} else if (bg.ball.getCoarseGrainedMinY() < 0) {
			diff = -bg.ball.getCoarseGrainedMinY();
			posy = posy + diff;
			if (bg.ball.getVelocity().getX() > 0) {
				posx = posx - diff;
			} else {
				posx = posx + diff;
			}
			bg.ball.setPosition(posx, posy);
			bg.ball.bounce(180);
		} else if(bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight) {
			diff = bg.ball.getCoarseGrainedMaxY() - bg.ScreenHeight;
			posy = posy - diff;
			if (bg.ball.getVelocity().getX() > 0) {
				posx = posx - diff;
			} else {
				posx = posx + diff;
			}
			bg.ball.setPosition(posx, posy);
			bg.ball.bounce(180);
		}

		ballCollisionDetection(bg.ball, bg.paddle);

		bg.ball.update(delta);

		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}

	}

	@Override
	public int getID() {
		return BounceGame.STARTUPSTATE;
	}

	private boolean ballCollisionDetection(Entity x, Entity o) {
		Ball b = (Ball) x;

		float bmaxX = b.getCoarseGrainedMaxX();
		float bminX = b.getCoarseGrainedMinX();
		float bmaxY = b.getCoarseGrainedMaxY();
		float bminY = b.getCoarseGrainedMinY();
		float omaxX = o.getCoarseGrainedMaxX();
		float ominX = o.getCoarseGrainedMinX();
		float omaxY = o.getCoarseGrainedMaxY();
		float ominY = o.getCoarseGrainedMinY();

		float[] diffs;
		float diff = 1000;

		diffs = new float[8];
		// bottom of block is within paddle
		diffs[0] = bmaxY - ominY; // bottom of block below top of paddle
		diffs[1] = omaxY - bmaxY; // bottom of block above bottom of paddle
		// top of block is within paddle
		diffs[2] = omaxY - bminY; // top of block above bottom of paddle
		diffs[3] = bminY - ominY; // top of block below top of paddle
		//right of block is within paddle
		diffs[4] = omaxX - bmaxX; // right of block is left of right of paddle
		diffs[5] = bmaxX - ominX; // right of block is right of left of paddle
		//left of block is within paddle
		diffs[6] = bminX - ominX; // left of block is right of left of paddle
		diffs[7] = omaxX - bminX; // left of block is left of right of paddle

		int yCheck1 = 0;
		int yCheck2 = 0;
		int xCheck1 = 0;
		int xCheck2 = 0;

		for (int i = 0; i < diffs.length; i++) {
			if (diffs[i] >= 0) {
				if (i == 0 || i == 1) {
					yCheck1++;
				} else if (i == 2 || i == 3) {
					yCheck2++;
				} else if (i == 4 || i == 5) {
					xCheck1++;
				} else if (i == 6 || i == 7){
					xCheck2++;
				}

				if (i % 2 == 0 && (diffs[i + 1] >= 0 && diffs[i] < diff)) {
					diff = diffs[i];
				} else if (i % 2 == 1 && (diffs[i - 1] >= 0 && diffs[i] < diff)) {
					diff = diffs[i];
				}
			}
		}

		if ((yCheck1 == 2 && (xCheck1 == 2 || xCheck2 == 2)) || (yCheck2 == 2 && (xCheck1 == 2 || xCheck2 == 2))) {
			float velx = b.getVelocity().getX();
			float vely = b.getVelocity().getY();
			float posx = b.getPosition().getX();
			float posy = b.getPosition().getY();
			if (xCheck1 == 2) {
				if (yCheck1 == 2) {
					posx = posx - diff;
					posy = posy - diff;
				} else if (yCheck2 == 2) {
					posx = posx - diff;
					posy = posy + diff;
				}
			} else if (xCheck2 == 2) {
				if (yCheck1 == 2) {
					posx = posx + diff;
					posy = posy - diff;
				} else if (yCheck2 == 2) {
					posx = posx + diff;
					posy = posy + diff;
				}
			}

			b.setPosition(posx, posy);
			bmaxX = b.getCoarseGrainedMaxX();
			bminX = b.getCoarseGrainedMinX();
			bmaxY = b.getCoarseGrainedMaxY();
			bminY = b.getCoarseGrainedMinY();

		}

		if (bmaxY == ominY || bminY == omaxY) {
			if (bmaxX == ominX || bminX == omaxX) {
				b.bounce(180);
				return true;
			} else if (ominX < bmaxX && bmaxX < omaxX || ominX < bminX && bminX < omaxX) {
				b.bounce(0);
				return true;
			}
		} else if (bmaxX == ominX || bminX == omaxX) {
			if (ominY < bminY && bminY < omaxY || ominY < bmaxY && bmaxY < omaxY) {
				b.bounce(90);
				return true;
			}
		}
		return false;
	}
}