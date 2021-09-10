package bounce;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Paddle extends Entity {
    private Vector velocity;

    public Paddle(final float x, final float y) {
        super(x, y);
        addImageWithBoundingBox(ResourceManager
                .getImage(BounceGame.PADDLE_RSC));
        velocity = new Vector(0f, 0f);
        setScale(0.66f);
    }
}
