package bounce;

import jig.Entity;
import jig.ResourceManager;

/**
 * Block is an entity that can be broken.
 */
public class Block extends Entity {
    private boolean isBroken = false;

    /**
     * Block constructor creates a block at coordinates (x,y).
     */
    public Block(final float x, final float y) {
        super(x, y);
        addImageWithBoundingBox(ResourceManager
                .getImage(BounceGame.BALL_BALLIMG_RSC));
    }

    /**
     * getIsBroken is a getter for isBroken.
     */
    public boolean getIsBroken() {
        return isBroken;
    }

    /**
     * setIsBroken is a setter for isBroken.
     */
    private void setIsBroken() {
        isBroken = true;
    }

    /**
     * breakBlock breaks a block
     */
    public void breakBlock() {
        setIsBroken();
    }
}
