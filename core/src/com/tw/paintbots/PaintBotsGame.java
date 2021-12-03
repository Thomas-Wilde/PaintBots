package com.tw.paintbots;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PaintBotsGame extends ApplicationAdapter {
  private Music music;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private int cam_width = 0;
  private int cam_height = 0;

  private GameManager game_mgr = GameManager.get();

  @Override
  public void create() {
    // --- load game settings
    GameSettings settings = new GameSettings();

    // ---
    music = Gdx.audio.newMusic(Gdx.files.internal("look_around.mp3"));
    music.setLooping(true);
    // music_.play();

    // --- create the camera and the SpriteBatch
    int[] border = settings.board_border;
    int[] board_dim = settings.board_dimensions;
    int ui_width = settings.ui_width;

    cam_width = board_dim[0] + 2 * border[0] + ui_width;
    cam_height = board_dim[1] + 2 * border[1];
    camera = new OrthographicCamera();
    camera.setToOrtho(false, cam_width, cam_height);
    batch = new SpriteBatch();

    // --- load the map
    game_mgr.setCameraResolution(new int[] {cam_width, cam_height});
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
    ScreenUtils.clear(0.078f, 0.059f, 0.043f, 1.0f);
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
    music.dispose();
    batch.dispose();
  }
}
