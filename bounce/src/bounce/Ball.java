package bounce;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

 class Ball extends Entity {
	private Vector initialPosition;
	private Vector initialVelocity;
	private Vector velocity;
	private int countdown;

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
		removeImage(ResourceManager.getImage(BounceGame.BALL_BALLIMG_RSC));
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BALL_BROKENIMG_RSC));
		countdown = 500;
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
