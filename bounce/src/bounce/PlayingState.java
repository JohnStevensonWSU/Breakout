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
			bg.paddle.setVelocity(new Vector(-.15f, 0f));
		} else if (!input.isKeyDown(Input.KEY_RIGHT)) {
				//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(+.001f, 0f)));
				bg.paddle.setVelocity(new Vector(0f, 0f));
			}

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			//bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(+.001f, 0f)));
			bg.paddle.setVelocity(new Vector(.15f, 0f));
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

		float bmaxX = bg.ball.getCoarseGrainedMaxX();
		float bminX = bg.ball.getCoarseGrainedMinX();
		float bmaxY = bg.ball.getCoarseGrainedMaxY();
		float bminY = bg.ball.getCoarseGrainedMinY();
		float omaxX = bg.paddle.getCoarseGrainedMaxX();
		float ominX = bg.paddle.getCoarseGrainedMinX();
		float omaxY = bg.paddle.getCoarseGrainedMaxY();
		float ominY = bg.paddle.getCoarseGrainedMinY();

		float diffs[];
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
			if (yCheck1 == 2) {
				System.out.println("Bottom is stuck");
			}
			if (yCheck2 == 2) {
				System.out.println("Top is stuck");
			}
			if (xCheck1 == 2) {
				System.out.println("Right is stuck");
			}
			if (xCheck2 == 2) {
				System.out.println("Left is stuck");
			}
			float velx = bg.ball.getVelocity().getX();
			float vely = bg.ball.getVelocity().getY();
			float posx = bg.ball.getPosition().getX();
			float posy = bg.ball.getPosition().getY();
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

			bg.ball.setPosition(posx, posy);
			bmaxX = bg.ball.getCoarseGrainedMaxX();
			bminX = bg.ball.getCoarseGrainedMinX();
			bmaxY = bg.ball.getCoarseGrainedMaxY();
			bminY = bg.ball.getCoarseGrainedMinY();

		}

		if (bmaxY == ominY || bminY == omaxY) {
			if (bmaxX == ominX || bminX == omaxX) {
				bg.ball.bounce(180);
			} else if (ominX < bmaxX && bmaxX < omaxX || ominX < bminX && bminX < omaxX) {
				bg.ball.bounce(0);
			}
		} else if (bmaxX == ominX || bminX == omaxX) {
			if (ominY < bminY && bminY < omaxY || ominY < bmaxY && bmaxY < omaxY) {
				bg.ball.bounce(90);
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