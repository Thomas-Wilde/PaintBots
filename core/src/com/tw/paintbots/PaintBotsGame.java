package com.tw.paintbots;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
// import com.badlogic.gdx.graphics.GL20;

public class PaintBotsGame extends ApplicationAdapter {
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private long lastDropTime;

	private Pixmap canvas_pixmap;
	private Texture canvas_texture;
	private final Color clearColor = new Color(0, 0, 0, 0);
	private final Color drawColor = new Color(1, 1, 1, 1);

	@Override
	public void create() {
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("dummy_item.png"));
		bucketImage = new Texture(Gdx.files.internal("dummy_bot.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("look_around.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
		bucket.width = 64;
		bucket.height = 64;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		// canvas_pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Format.RGBA8888);
		canvas_pixmap = new Pixmap(1000, 1000, Format.RGBA8888);
		canvas_pixmap.setColor(clearColor);
		canvas_pixmap.fill();
		canvas_pixmap.setColor(drawColor);
		canvas_pixmap.fillCircle(500, 500, 10);
		canvas_texture = new Texture(canvas_pixmap);
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		ScreenUtils.clear(0, 0, 0.2f, 1);

		// Gdx.gl.glClearColor(0, 0, 0, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		canvas_pixmap.setColor(Color.RED);
		// canvas_pixmap.fillCircle(50, 50, 50);
		canvas_texture.draw(canvas_pixmap, 0, 0);

		// begin a new batch and draw the bucket and
		// all drops
		batch.begin();

		batch.draw(canvas_texture, 0, 0);
		batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.UP))
			bucket.y += 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			bucket.y -= 400 * Gdx.graphics.getDeltaTime();

		canvas_pixmap.fillCircle((int) bucket.x, 1000 - (int) bucket.y, 50);

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 1000 - 64)
			bucket.x = 1000 - 64;
		if (bucket.y < 0)
			bucket.y = 0;
		if (bucket.y > 1000 - 64)
			bucket.y = 1000 - 64;

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the latter case we play back
		// a sound effect as well.
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0)
				iter.remove();
			if (raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}
