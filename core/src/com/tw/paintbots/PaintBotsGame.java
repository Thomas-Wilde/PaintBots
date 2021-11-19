package com.tw.paintbots;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PaintBotsGame extends ApplicationAdapter {
  private Music music_;
  private SpriteBatch batch;
  private OrthographicCamera camera;

  private GameManager game_mgr = new GameManager();

  @Override
  public void create() {
    // load the background "music"
    music_ = Gdx.audio.newMusic(Gdx.files.internal("look_around.mp3"));

    // start the playback of the background music immediately
    music_.setLooping(true);
    music_.play();

    // create the camera and the SpriteBatch
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 1000, 1000);
    batch = new SpriteBatch();

    // load game settings
    GameSettings settings = new GameSettings();
    try {
      game_mgr.loadMap(settings);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void render() {
    // clear the screen with a dark blue color. The
    // arguments to clear are the red, green
    // blue and alpha component in the range [0,1]
    // of the color to be used to clear the screen.
    ScreenUtils.clear(0, 0, 0.2f, 1);
    // tell the camera to update its matrices.
    camera.update();
    // tell the SpriteBatch to render in the
    // coordinate system specified by the camera.
    batch.setProjectionMatrix(camera.combined);
    game_mgr.update();
    // --- draw the graphics
    batch.begin();
    game_mgr.render(batch);
    batch.end();
  }

  @Override
  public void dispose() {
    // dispose of all the native resources
    music_.dispose();
    batch.dispose();
  }
}
