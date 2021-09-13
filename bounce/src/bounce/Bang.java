package bounce;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.Animation;

class Bang extends Entity {
	private Animation explosion;

	public Bang(final float x, final float y) {
		super(x, y);
		explosion = new Animation(ResourceManager.getSpriteSheet(
				BounceGame.BANG_EXPLOSIONIMG_RSC, 64, 64), 0, 0, 22, 0, true, 50,
				true);
		addAnimation(explosion);
		explosion.setLooping(false);
		ResourceManager.getSound(BounceGame.BANG_EXPLOSIONSND_RSC).play();
	}

	public boolean isActive() {
		return !explosion.isStopped();
	}
}