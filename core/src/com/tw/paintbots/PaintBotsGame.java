package com.tw.paintbots;

import com.badlogic.gdx.ApplicationAdapter;
// import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PaintBotsGame extends ApplicationAdapter {
  // ======================= GameKey class ======================= //
  //@formatter:off
  /** The PaintBotsGame mimics a friend class behavior, i.e. every method that
   * should be accessed only by the GamPaintBotsGame asks for the GameKey.
   * Only the PaintBotsGame can deliver this GameKey */
  public static final class GameKey { private GameKey() {} }
  private static final GameKey game_key = new GameKey();
  //@formatter:on

  // --------------------------------------------------------------- //
  // private Music music;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private GameManager game_mgr = GameManager.get();

  // ==================== PaintBotGame methods ==================== //
  public void initGameManager() {
    GameSettings settings = new GameSettings();
    try {
      game_mgr.loadMenu(settings, game_key);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // --------------------------------------------------------------- //
  public void initGameManagerHeadless() {
    GameSettings settings = new GameSettings();
    try {
      game_mgr.loadMapHeadless(settings, game_key);
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
    game_mgr.update(game_key);
    // draw the graphics
    batch.begin();
    game_mgr.render(batch, game_key);
    batch.end();
  }

  @Override
  public void dispose() {
    // dispose of all the native resources
    // music.dispose();
    game_mgr.destroy(game_key);
    batch.dispose();
  }
}
