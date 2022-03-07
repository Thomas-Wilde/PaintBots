package com.tw.paintbots;

import com.badlogic.gdx.ApplicationAdapter;
// import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PaintBotsGame extends ApplicationAdapter {
  // --------------------------------------------------------------- //
  // private Music music;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private GameManager game_mgr = GameManager.get();

  // ==================== PaintBotsGame methods ==================== //
  public void initGameManager() {
    GameSettings settings = new GameSettings();
    try {
      game_mgr.loadGame(settings);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // --------------------------------------------------------------- //
  public void initGameManagerHeadless() {
    GameSettings settings = new GameSettings();
    try {
      game_mgr.loadMapHeadless(settings);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // --------------------------------------------------------------- //
  @Override
  public void create() {
    // loadAIBotClasses();

    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    // --- load game settings
    GameSettings settings = new GameSettings();
    int[] resolution = settings.cam_resolution;
    camera.setToOrtho(false, resolution[0], resolution[1]);
    // ---
    // music = Gdx.audio.newMusic(Gdx.files.internal("look_around.mp3"));
    // music.setLooping(true);
    // music.play();
    initGameManager();
  }

  @Override
  public void render() {
    // clear the screen with a dark blue color
    ScreenUtils.clear(0.078f, 0.059f, 0.043f, 1.0f);
    // tell the camera to update its matrices
    camera.update();
    // tell the SpriteBatch to render in the
    // coordinate system specified by the camera.
    batch.setProjectionMatrix(camera.combined);
    // tell the GameManager to perform an update step
    game_mgr.update();
    // draw the graphics
    batch.begin();
    game_mgr.render(batch);
    batch.end();
  }

  @Override
  public void dispose() {
    // dispose of all the native resources
    // music.dispose();
    game_mgr.destroy();
    batch.dispose();
  }
}
