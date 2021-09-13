package bounce;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * BounceGame is a StateBasedGame of Breakout. It has a startup state,
 * three levels and a game over state. It contains all images and
 * sounds of the game.
 */
public class BounceGame extends StateBasedGame {
	/**
	 * The BounceGame state ids
	 */
	public static final int STARTUPSTATE = 0;
	public static final int LEVEL_1 = 1;
	public static final int LEVEL_2 = 2;
	public static final int LEVEL_3 = 3;
	public static final int GAMEOVERSTATE = 4;

	/**
	 * BounceGame resource aliases
	 */
	public static final String BALL_BALLIMG_RSC = "bounce/resource/ball.png";
	public static final String BALL_BROKENIMG_RSC = "bounce/resource/brokenball.png";
	public static final String GAMEOVER_BANNER_RSC = "bounce/resource/GameOver.png";
	public static final String STARTUP_BANNER_RSC = "bounce/resource/PressSpace.png";
	public static final String BANG_EXPLOSIONIMG_RSC = "bounce/resource/explosion.png";
	public static final String BANG_EXPLOSIONSND_RSC = "bounce/resource/explosion.wav";
	public static final String PADDLE_RSC = "bounce/resource/Paddle.png";
	public static final String HEART_RSC = "bounce/resource/Heart.png";
	public static final String TITLE_MUSIC_RSC = "bounce/resource/titleMusic.wav";

	/**
	 * Screen dimensions
	 */
	public final int ScreenWidth;
	public final int ScreenHeight;

	private final int score = 0; // score variable

	/**
	 * constructor creates a BounceGame titled title with screen height
	 * height and screen width width.
	 */
	public BounceGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}

	/**
	 * initStatesList adds states to a BounceGame and initializes all resources.
	 */
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		/**
		 * add states to BounceGame
		 */
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new Level1());
		addState(new Level2());
		addState(new Level3());

		/**
		 * load sounds
		 */
		ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);
		ResourceManager.loadSound(TITLE_MUSIC_RSC);

		/**
		 * load images
		 */
		ResourceManager.loadImage(BALL_BALLIMG_RSC);
		ResourceManager.loadImage(BALL_BROKENIMG_RSC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(STARTUP_BANNER_RSC);
		ResourceManager.loadImage(BANG_EXPLOSIONIMG_RSC);
		ResourceManager.loadImage(PADDLE_RSC);
		ResourceManager.loadImage(HEART_RSC);
	}

	/**
	 * main method of BounceGame creates a BounceGame window and
	 * starts game.
	 */
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new BounceGame("Bounce!", 800, 600));
			app.setDisplayMode(800, 600, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getScore is a getter for score
	 */
	public int getScore() {
		return score;
	}
}
