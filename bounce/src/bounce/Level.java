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

class Level extends BasicGameState {
    private boolean levelStart = false;
    public Block[] blocks;
    private Heart[] hearts;
    private Ball ball;
    private Paddle paddle;
    public BasicGameState nextState;
    public BounceGame bg;

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        bg = (BounceGame) game;
        levelStart = false;
        hearts = new Heart[3];

        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new Heart(20 * (i + 1), 60);
        }
        ball = new Ball(bg.ScreenWidth / 2, bg.ScreenHeight / 2, .15f, .15f);
        paddle = new Paddle(bg.ScreenWidth / 2, bg.ScreenHeight * 3 / 4);

        container.setSoundOn(true);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawString("Score: " + bg.getScore(), 10, 30);
        for (Heart h : hearts) {
            if (h.getStatus()) {
                h.render(g);
            }
        }
        for (Block b : blocks) {
            if (!b.getIsBroken()) {
                b.render(g);
            }
        }
        ball.render(g);
        paddle.render(g);
        if (!levelStart) {
            g.drawImage(ResourceManager.getImage(BounceGame.STARTUP_BANNER_RSC),
                    225, 270);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        boolean blockExists = false;

        if (!checkInput(container)) {
            return;
        }

        if (!checkBlocks()) {
            game.enterState(nextState.getID());
        }

        checkPaddle();
        if (!checkScreen()) {
            ((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bg.getScore());
            game.enterState(BounceGame.GAMEOVERSTATE);
        }

        ball.update(delta);
        paddle.update(delta);
    }

    private boolean ballCollisionDetection(Ball b, Entity o) {
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

    private boolean checkInput(GameContainer container) {
        Input input = container.getInput();
        if (!levelStart) {
            if (!input.isKeyDown(Input.KEY_SPACE)) {
                return false;
            } else {
                levelStart = true;
            }
        }

        if (input.isKeyDown(Input.KEY_X)) {
            for (Block b : blocks) {
                b.breakBlock();
            }
        }
        if (input.isKeyDown(Input.KEY_LEFT)) {
            paddle.setVelocity(new Vector(-.15f, 0f));
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            paddle.setVelocity(new Vector(.15f, 0f));
        } else {
            paddle.setVelocity(new Vector(-0f, 0f));
        }
        return true;
    }

    private boolean checkBlocks() {
        boolean bounced;
        boolean blockExists = false;
        for (Block b : blocks) {
            if (!b.getIsBroken()) {
                bounced = ballCollisionDetection(ball, b);
                blockExists = true;
                if (bounced) {
                    b.breakBlock();
                }
            }
        }
        return blockExists;
    }

    private void checkPaddle() {
        float posx = paddle.getX();
        ballCollisionDetection(ball, paddle);

        if (paddle.getCoarseGrainedMinX() < 0) {
            posx = posx - paddle.getCoarseGrainedMinX();
            paddle.setX(posx);
        } else if (paddle.getCoarseGrainedMaxX() > bg.ScreenWidth) {
            posx = posx - paddle.getCoarseGrainedMaxX() + bg.ScreenWidth;
            paddle.setX(posx);
        }
    }

    private boolean checkScreen() {
        boolean bounced = false;
        boolean heartExists = false;
        float posx = ball.getX();
        float posy = ball.getY();
        float diff;

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
