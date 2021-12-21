package com.tw.paintbots;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShapeValue;
import com.badlogic.gdx.utils.ScreenUtils;

import java.net.URLClassLoader;
import java.net.URL;

public class PaintBotsGame extends ApplicationAdapter {
  // --------------------------------------------------------------- //
  private Music music;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private GameManager game_mgr = GameManager.get();

  // ==================== PaintBostGame methods ==================== //
  private void loadAIBotClasses() {
    List<String> files = readClassFilesFromRunDir();
    for (String class_name : files) {
      // String qualified_name = "com.tw.paintbots." + class_name;
      String qualified_name = class_name;
      loadBotFile(qualified_name);
    }
  }

  // --------------------------------------------------------------- //
  private List<String> readClassFilesFromRunDir() {
    String run_dir = System.getProperty("user.dir");
    File tmp = new File(run_dir);
    String[] files = tmp.list();
    List<String> class_files = new ArrayList<>();
    // ---
    for (String file : files) {
      if (file.endsWith(".class")) {
        int idx = file.lastIndexOf(".class");
        class_files.add(file.substring(0, idx));
      }
    }
    return class_files;
  }

  // --------------------------------------------------------------- //
  private void loadBotFile(String fullyQualifiedClassName) {
    try {
      String run_dir = System.getProperty("user.dir");
      File tmp = new File(run_dir);
      URL url = tmp.toURI().toURL();
      URL[] urls = new URL[] {url};
      ClassLoader cl = new URLClassLoader(urls);
      Class cls = cl.loadClass(fullyQualifiedClassName);
    } catch (Exception e) {
      System.out.println("Could not load " + fullyQualifiedClassName);
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
    music = Gdx.audio.newMusic(Gdx.files.internal("look_around.mp3"));
    music.setLooping(true);
    music.play();

    try {
      game_mgr.loadMap(settings);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
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
    music.dispose();
    game_mgr.destroy();
    batch.dispose();
  }
}
