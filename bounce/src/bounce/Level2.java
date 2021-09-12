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

class Level2 extends Level {

    @Override
    public void init(GameContainer container, StateBasedGame game)
    throws SlickException{
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        super.enter(container, game);
        nextState = (BasicGameState) bg.getState(bg.GAMEOVERSTATE);
        blocks = new Block[10];

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
