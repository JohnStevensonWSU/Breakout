package bounce;

import jig.Entity;
import jig.ResourceManager;

public class Heart extends Entity {
    private boolean isAlive = true;

    public Heart(final float x, final float y) {
        super(x, y);
        addImage(ResourceManager.getImage(BounceGame.HEART_RSC));
    }

    public boolean getStatus() {
        return isAlive;
    }

    public void killHeart() {
        isAlive = false;
    }
}
