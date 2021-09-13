package bounce;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Level3 is the third, and final, level in Breakout
 */
class Level3 extends Level {

    @Override
    public void init(GameContainer container, StateBasedGame game)
    throws SlickException{
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        super.enter(container, game);

        nextState = (BasicGameState) bg.getState(bg.GAMEOVERSTATE);
        blocks = new Block[19];

        // sets the blocks in two rows
        for (int i = 0; i < blocks.length; i++) {
            float posx;
            float posy;
            if (i < 10) {
                posx = bg.ScreenWidth * (i + 1) / 11;
            } else {
                posx = bg.ScreenWidth * (i - 9) / 10;
            }
            if (i < 10) {
                posy = bg.ScreenHeight * 3 / 8;
            } else {
                posy = bg.ScreenHeight * 1 / 4;
            }
            blocks[i] = new Block(posx, posy);
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
    public int getID() { return BounceGame.LEVEL_3; }
}
