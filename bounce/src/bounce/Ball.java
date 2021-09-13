package bounce;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import java.util.Random;

/**
 *  Ball is an Entity with a velocity. A ball is initialized with
 *  an x coordinate, y coordinate, x speed and y speed respectively.
 *  A ball can bounce off a surface tangent, update its position
 *  based on a time difference delta, and reset its position and
 *  velocity to their original values.
 */
class Ball extends Entity {
	private Vector initialPosition; // initial value of the position
	private Vector initialVelocity; // initial value of the velocity
	private Vector velocity; // current velocity
	private int countdown; // countdown for image change
	private final float velocityScale = 0.15f; // velocity scale reference

	/**
	 * Ball constructor creates an instance of a ball with initial
	 * position (x,y) and initial velocity (vx,vy).
	 */
	public Ball(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		initialPosition = new Vector(x, y);
		initialVelocity = new Vector(vx, vy);
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BALL_BALLIMG_RSC));
		velocity = new Vector(vx, vy);
		countdown = 0;
	}

	/**
	 * setVelocity is a setter for the velocity of a ball
	 */
	public void setVelocity(final Vector v) {
		velocity = v;
	}

	/**
	 * getVelocity is a getter for the velocity of a ball
	 */
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * bounce is a method of ball that reflects the ball off a surface tangent.
	 * The velocity of the ball is randomly generated between 2/3 and 4/3 of
	 * velocityScale variable. The ball's image is changed to the broken image.
	 */
	public void bounce(float surfaceTangent) {
		float velX = (velocityScale * 2 / 3) + (new Random().nextFloat() * (velocityScale * 2 / 3));
		float velY = (velocityScale * 2 / 3) + (new Random().nextFloat() * (velocityScale * 2 / 3));
		removeImage(ResourceManager.getImage(BounceGame.BALL_BALLIMG_RSC));
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BALL_BROKENIMG_RSC));
		countdown = 500;
		if (getVelocity().getX() < 0) {
			velX = -velX;
		}
		if (getVelocity().getY() < 0) {
			velY = -velY;
		}
		velocity = new Vector(velX, velY);
		velocity = velocity.bounce(surfaceTangent);
	}

	/**
	 * update updates the condition of a ball. It translates the velocity
	 * based on the amount of time passed (delta). It also updates the
	 * countdown to change the ball image back to the standard ball after
	 * a collision has been detected.
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
			if (countdown <= 0) {
				addImageWithBoundingBox(ResourceManager
						.getImage(BounceGame.BALL_BALLIMG_RSC));
				removeImage(ResourceManager
						.getImage(BounceGame.BALL_BROKENIMG_RSC));
			}
		}
	}

	/**
	 * reset resets the state of the ball to its initial conditions.
	 */
	public void reset() {
		setPosition(initialPosition);
		setVelocity(initialVelocity);
		countdown = 0;
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BALL_BALLIMG_RSC));
	}
}
