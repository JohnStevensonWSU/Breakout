package bounce;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * Paddle is an entity with a velocity.
 */
public class Paddle extends Entity {
    private Vector velocity; // velocity of paddle
    private Vector initialPosition; // initial position of paddle
    private Vector initialVelocity; // initial position of paddle

    public Paddle(final float x, final float y) {
        super(x, y);
        addImageWithBoundingBox(ResourceManager
                .getImage(BounceGame.PADDLE_RSC));
        velocity = new Vector(0f, 0f);
        initialPosition = new Vector(x,y);
        initialVelocity = velocity;
        setScale(0.66f);
    }

    /**
     * setter for velocity
     */
    public void setVelocity(final Vector v) {
        velocity = v;
    }

    /**
     * updates the paddle
     */
    public void update(final int delta) {
        translate(velocity.scale(delta));
    }

    public void reset() {
        setVelocity(initialVelocity);
        setPosition(initialPosition);
    }
}
