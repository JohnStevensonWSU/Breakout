package bounce;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Level2 is the second level in Breakout. See Level for method descriptions
 */
class Level2 extends Level {

    @Override
    public void init(GameContainer container, StateBasedGame game)
    throws SlickException{
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        super.enter(container, game);
        // sets next state to level 3
        nextState = (BasicGameState) bg.getState(bg.LEVEL_3);
        blocks = new Block[10];

        // sets the blocks in a triangle formation
        for (int i = 0; i < blocks.length; i++) {
            int posx = bg.ScreenWidth * (i + 1) / 11;
            int posy;
            if (i <= 4) {
                posy = bg.ScreenHeight * (6 - i) / 11;
            } else {
                posy = bg.ScreenHeight * (i - 3) / 11;
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
    public int getID() { return BounceGame.LEVEL_2; }
}
