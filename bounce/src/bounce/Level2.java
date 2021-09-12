package bounce;

import jig.Entity;
import jig.ResourceManager;

import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

class Level2 extends BasicGameState {
    int bounces;
    int numLives = 3;

    @Override
    public void init(GameContainer container, StateBasedGame game)
    throws SlickException{
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        BounceGame bg = (BounceGame) game;

        container.setSoundOn(false);
        bg.ball.reset();

        for (int i = 0; i < bg.blocks.length; i++) {
            int posx = bg.ScreenWidth * (i + 1) / 11;
            int posy;
            if (i <= 4) {
                posy = bg.ScreenHeight * (6 - i) / 11;
            } else {
                posy = bg.ScreenHeight * (i - 3) / 11;
            }
            bg.blocks[i].setPosition(posx, posy);
            bg.blocks[i].unsetIsBroken();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        BounceGame bg = (BounceGame) game;

        bg.ball.render(g);
        bg.paddle.render(g);
        for (Block b : bg.blocks) {
            if (!b.getIsBroken()) {
                b.render(g);
            }
        }
        g.drawString("Bounces: " + bounces, 10, 30);
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
        BounceGame bg = (BounceGame) game;
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

        boolean bounced = false;
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
            numLives = numLives - 1;
            bg.ball.reset();
            if (numLives > 0) {
                bg.hearts[numLives - 1].killHeart();
            }
        }

        ballCollisionDetection(bg.ball, bg.paddle);

        for (Block b : bg.blocks) {
            if (!b.getIsBroken()) {
                bounced = ballCollisionDetection(bg.ball, b);
                if (bounced) {
                    b.breakBlock();
                }
            }
        }

        for (Block b : bg.blocks) {
            if (!b.getIsBroken()) {
                blockExists = true;
            }
        }

        bg.ball.update(delta);
        bg.paddle.update(delta);

        if (numLives <= 0 || !blockExists) {
            ((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
            game.enterState(BounceGame.GAMEOVERSTATE);
        }
    }

    @Override
    public int getID() { return BounceGame.LEVEL_2; }

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
