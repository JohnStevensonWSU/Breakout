package bounce;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import java.util.Random;

class Ball extends Entity {
	private Vector initialPosition;
	private Vector initialVelocity;
	private Vector velocity;
	private int countdown;
	private final float velocityScale = 0.15f;

	public Ball(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		initialPosition = new Vector(x, y);
		initialVelocity = new Vector(vx, vy);
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BALL_BALLIMG_RSC));
		velocity = new Vector(vx, vy);
		countdown = 0;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

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

	public void reset() {
		setPosition(initialPosition);
		setVelocity(initialVelocity);
		countdown = 0;
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BALL_BALLIMG_RSC));
	}
}
