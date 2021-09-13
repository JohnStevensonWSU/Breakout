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

/**
 * Level is a BasicGameState that the player will actually play the game in.
 * It has a collection of blocks, hearts, a paddle, and a ball. When finished,
 * it will go on to the next Gamestate. However, when no more hearts are alive,
 * it will end the game.
 */
class Level extends BasicGameState {
    private boolean levelStart = false; // boolean representing whether the level has begun
    public Block[] blocks; // collection of blocks
    private Heart[] hearts; // collection of hearts
    private Ball ball; // ball
    private Paddle paddle; // paddle
    public BasicGameState nextState; // nextState reference
    public BounceGame bg; // game associated with this level

    /**
     * getter for level id
     */
    @Override
    public int getID() {
        return 0;
    }

    /**
     * loads up all resources for the level
     */
    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    }

    /**
     * signals the level has been entered
     */
    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        bg = (BounceGame) game;
        levelStart = false; // the level has not started yet
        hearts = new Heart[3]; // initialize lives

        // initialize all objects in level
        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new Heart(20 * (i + 1), 60);
        }
        ball = new Ball(bg.ScreenWidth / 2, bg.ScreenHeight / 2, .15f, .15f);
        paddle = new Paddle(bg.ScreenWidth / 2, bg.ScreenHeight * 3 / 4);

        // turn sound on
        container.setSoundOn(true);
    }

    /**
     * draws all objects in level
     */
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // draw score
        g.drawString("Score: " + bg.getScore(), 10, 30);
        // draw hearts
        for (Heart h : hearts) {
            if (h.getStatus()) {
                h.render(g);
            }
        }
        //draw blocks
        for (Block b : blocks) {
            if (!b.getIsBroken()) {
                b.render(g);
            }
        }
        // draw ball
        ball.render(g);
        //draw paddle
        paddle.render(g);
        // prompt user to press space if the level has not yet been started
        if (!levelStart) {
            g.drawImage(ResourceManager.getImage(BounceGame.STARTUP_BANNER_RSC),
                    225, 270);
        }
    }

    /**
     * updates the state of the level and its components
     */
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // check for input from the user
        if (!checkInput(container)) {
            return;
        }

        // check all the blocks in the game
        if (!checkBlocks()) {
            game.enterState(nextState.getID());
        }

        // check the paddle
        checkPaddle();
        if (!checkScreen()) {
            ((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bg.getScore());
            game.enterState(BounceGame.GAMEOVERSTATE);
        }

        // update the ball
        ball.update(delta);
        // update the paddle
        paddle.update(delta);
    }

    /**
     * Checks to see if the ball has collided with an object.
     * If it has, it returns true. If not, it returns false.
     */
    private boolean ballCollisionDetection(Ball b, Entity o) {
        // get the corners of the ball and the object
        float bmaxX = b.getCoarseGrainedMaxX();
        float bminX = b.getCoarseGrainedMinX();
        float bmaxY = b.getCoarseGrainedMaxY();
        float bminY = b.getCoarseGrainedMinY();
        float omaxX = o.getCoarseGrainedMaxX();
        float ominX = o.getCoarseGrainedMinX();
        float omaxY = o.getCoarseGrainedMaxY();
        float ominY = o.getCoarseGrainedMinY();

        float[] diffs; // array of distances
        float diff = 1000; // minimum distance between edge of ball and edge of object when ball is within object

        diffs = new float[8];
        // if a value pair below is positive, the ball is within the object in some way
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

        // values below are 2 if corresponding value pair above is positive
        int yCheck1 = 0;
        int yCheck2 = 0;
        int xCheck1 = 0;
        int xCheck2 = 0;

        // find the shortest distance from an edge of the ball within the object to the object edge
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

        // if two adjacent sides are within the object, the ball is within the object
        if ((yCheck1 == 2 && (xCheck1 == 2 || xCheck2 == 2)) || (yCheck2 == 2 && (xCheck1 == 2 || xCheck2 == 2))) {
            float velx = b.getVelocity().getX();
            float vely = b.getVelocity().getY();
            float posx = b.getPosition().getX();
            float posy = b.getPosition().getY();
            // move the ball from within the object to the nearest wall
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

        // reflect the ball off an object if it is touching
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

    /**
     * Checks the user input. Returns false when the level has not started yet.
     */
    private boolean checkInput(GameContainer container) {
        Input input = container.getInput();
        // cheat codes for level selection
        if (input.isKeyDown(Input.KEY_1)) {
            bg.enterState(bg.LEVEL_1);
        } else if (input.isKeyDown(Input.KEY_2)) {
            bg.enterState(bg.LEVEL_2);
        } else if (input.isKeyDown(Input.KEY_3)) {
            bg.enterState(bg.LEVEL_3);
        }
        // level startup key
        if (!levelStart) {
            if (!input.isKeyDown(Input.KEY_SPACE)) {
                return false;
            } else {
                levelStart = true;
            }
        }
        // cheat code for breaking all blocks
        if (input.isKeyDown(Input.KEY_X)) {
            for (Block b : blocks) {
                b.breakBlock();
            }
        }
        // paddle movement
        if (input.isKeyDown(Input.KEY_LEFT)) {
            paddle.setVelocity(new Vector(-.15f, 0f));
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            paddle.setVelocity(new Vector(.15f, 0f));
        } else {
            paddle.setVelocity(new Vector(-0f, 0f));
        }
        return true;
    }

    /**
     *  Checks every block in the level. Returns false if no blocks are unbroken.
     */
    private boolean checkBlocks() {
        boolean bounced; // stores info on if the block has been broken
        boolean blockExists = false; // stores info on if a block still exists in the level
        // loop through the blocks in the level
        for (Block b : blocks) {
            // if this block is not broken, check to see if the ball and the block have collided
            if (!b.getIsBroken()) {
                bounced = ballCollisionDetection(ball, b);
                blockExists = true;
                // if the ball bounced off the block, break the block
                if (bounced) {
                    b.breakBlock();
                }
            }
        }
        return blockExists;
    }

    /**
     * Checks the paddle in the level.
     */
    private void checkPaddle() {
        float posx = paddle.getX();
        // see if the ball collided with the ball
        ballCollisionDetection(ball, paddle);

        // correct paddle if it tries to go outside the screen
        if (paddle.getCoarseGrainedMinX() < 0) {
            posx = posx - paddle.getCoarseGrainedMinX();
            paddle.setX(posx);
        } else if (paddle.getCoarseGrainedMaxX() > bg.ScreenWidth) {
            posx = posx - paddle.getCoarseGrainedMaxX() + bg.ScreenWidth;
            paddle.setX(posx);
        }
    }

    /**
     * Check to make sure the ball isn't trying to go outside the screen.
     * Returns false if no more hearts are alive.
     */
    private boolean checkScreen() {
        boolean heartExists = false; // value to store whether a heart is alive
        float posx = ball.getX(); // x coord of the ball
        float posy = ball.getY(); // y coord of the ball
        float diff; // distance from the balls outermost-coord to the edge of the screen

        //correct the ball's course if it is trying to go outside the screen
        if (ball.getCoarseGrainedMaxX() > bg.ScreenWidth) {
            diff = ball.getCoarseGrainedMaxX() - bg.ScreenWidth;
            posx = posx - diff;
            if (ball.getVelocity().getY() > 0) {
                posy = posy - diff;
            } else {
                posy = posy + diff;
            }
            ball.setPosition(posx, posy);
            ball.bounce(90);
        } else if (ball.getCoarseGrainedMinX() < 0) {
            diff = -ball.getCoarseGrainedMinX();
            posx = posx + diff;
            if (ball.getVelocity().getY() > 0) {
                posy = posy - diff;
            } else {
                posy = posy + diff;
            }
            ball.setPosition(posx, posy);
            ball.bounce(90);
        } else if (ball.getCoarseGrainedMinY() < 0) {
            diff = -ball.getCoarseGrainedMinY();
            posy = posy + diff;
            if (ball.getVelocity().getX() > 0) {
                posx = posx - diff;
            } else {
                posx = posx + diff;
            }
            ball.setPosition(posx, posy);
            ball.bounce(180);
        } else if(ball.getCoarseGrainedMaxY() > bg.ScreenHeight) {
            // if the ball goes below the screen, decrement one life and reset the ball
            for (int i = hearts.length - 1; i >= 0 ; i--) {
                if (hearts[i].getStatus()) {
                    hearts[i].killHeart();
                    break;
                }
            }
            ball.reset();
        }
        for (Heart h : hearts) {
            if (h.getStatus()) {
                heartExists = true;
            }
        }
       return heartExists;
    }
}
