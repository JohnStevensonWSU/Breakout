package bounce;

import jig.Entity;
import jig.ResourceManager;

public class Block extends Entity {
    private boolean isBroken = false;

    public Block(final float x, final float y) {
        super(x, y);
        addImageWithBoundingBox(ResourceManager
                .getImage(BounceGame.BALL_BALLIMG_RSC));
    }

    public boolean getIsBroken() {
        return isBroken;
    }

    private void setIsBroken() {
        isBroken = true;
    }

    public void breakBlock() {
        setIsBroken();
    }
}
