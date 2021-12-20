package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.Renderables.Renderable;
import com.tw.paintbots.Renderables.RepeatedRenderable;

/**
 * The GameManager is the core class of the game. It creates all Entities and
 * manages their behavior. It also calls the update routine and is responsible
 * for rendering. The GameManager implements the singleton pattern.
 */
// =============================================================== //
public class GameManager {
  // ======================= SecretKey class ======================= //
  //@formatter:off
  /** The GameManager mimics a friend class behavior, i.e. every method that should be accessed only by the GameManager asked for the SecretKey. Only the Game Manager can deliver this SecretKey */
  public static final class SecretKey { private SecretKey() {} }
  private static final SecretKey secret_key = new SecretKey();
  //@formatter:on

  // ====================== GameManager class ====================== //
  // GameManager uses singleton pattern
  private static GameManager instance = null;
  private double elapsed_time = 0.0;

  private HashMap<Integer, List<Renderable>> render_layers = new HashMap<>();
  private ArrayList<Entity> entities = new ArrayList<>();
  private GameSettings map_settings = null;
  // private Player[] players = null;
  // private PlayerState[] player_states = null;
  // private Canvas canvas = null;
  // private int[] cam_resolution = {0, 0};

  // private UITimer timer = null;

  // private List<List<Renderable>> render_layers_ = null;
  // private final int layers_count = 10;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the time that passed since the start of the round in milliseconds. */
  public double getElapsedTime() { return elapsed_time; }
  //@formatter:on

  // ===================== GameManager methods ===================== //
  /** Access the single instance of the GameManager. */
  public static GameManager get() {
    if (instance == null)
      instance = new GameManager();
    return instance;
  }

  // --------------------------------------------------------------- //
  /**
   * Constructor is private due to the sigleton pattern.
   */
  private GameManager() {}

  // --------------------------------------------------------------- //
  /** Dispose all entities. */
  public void destroy() {
    for (Entity entity : entities)
      entity.destroy(secret_key);
    entities.clear();
  }

  // --------------------------------------------------------------- //
  public void update() {
    elapsed_time += Gdx.graphics.getDeltaTime();
    // ---
    // preUpdate();
    // --- update all entities
    for (Entity entity : entities)
      entity.update(secret_key);
    // ---
    // moveAllPlayers();
    // paintOnCanvas();
    // adjustPaintAmounts();
    // adjustScores();
  }

  // --------------------------------------------------------------- //
  /** Draw the Renderables of each layer. */
  public void render(SpriteBatch batch) {
    Set<Integer> layer_ids = render_layers.keySet();
    for (Integer id : layer_ids) {
      List<Renderable> renderables = render_layers.get(id);
      for (Renderable item : renderables)
        item.render(batch, id.intValue());
    }
  }

  // --------------------------------------------------------------- //
  /**
   * Add the Renderable item to all render layers item demands. If a specific
   * layer does not exist yet, it gets created.
   */
  private void addRenderable(Renderable item) {
    int[] item_layers = item.getLayers();
    for (int layer_idx : item_layers) {
      // --- check if the layer exists
      if (render_layers.get(layer_idx) == null)
        render_layers.put(layer_idx, new ArrayList<Renderable>());
      // --- access the layer
      List<Renderable> layer = render_layers.get(layer_idx);
      layer.add(item);
    }
  }

  // --------------------------------------------------------------- //
  private void addEntity(Entity item) {
    entities.add(item);
  }

  // --------------------------------------------------------------- //
  public void loadMap(GameSettings settings) throws GameMangerException {
    map_settings = settings;
    // sanityCheckPlayerSettings(); // throws an exception if something is wrong
    createBackground();
    // createFloor();
    // createUITimer();
    // createPlayers();
    // createCanvas();
    // createPaintBooth();
  }

  // --------------------------------------------------------------- //
  private void createBackground() {
    String tex_file = map_settings.back_texture;
    int[] repeat_xy = {6, 6};
    RepeatedRenderable background =
        new RepeatedRenderable("background", 0, tex_file, repeat_xy);
    addRenderable(background);
    addEntity(background);
  }
}
