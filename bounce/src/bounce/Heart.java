package bounce;

import jig.Entity;
import jig.ResourceManager;

/**
 * Heart is an entity that represents a life in a game.
 */
public class Heart extends Entity {
    private boolean isAlive = true; // signifies if a heart is alive or not

    /**
     * constructs a heart at (x,y).
     */
    public Heart(final float x, final float y) {
        super(x, y);
        addImage(ResourceManager.getImage(BounceGame.HEART_RSC));
    }

    /**
     * getter for isAlive
     */
    public boolean getStatus() {
        return isAlive;
    }

    /**
     * kills the heart
     */
    public void killHeart() {
        isAlive = false;
    }
}
