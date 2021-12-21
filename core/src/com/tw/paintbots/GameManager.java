package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.Renderables.Canvas;
import com.tw.paintbots.Renderables.Renderable;
import com.tw.paintbots.Renderables.SimpleRenderable;
import com.tw.paintbots.Renderables.RepeatedRenderable;
import com.tw.paintbots.Renderables.UITimer;
import com.tw.paintbots.Renderables.UIPlayerBoard;

import com.tw.paintbots.Items.PaintBooth;

/**
 * The GameManager is the core class of the game. It creates all Entities and
 * manages their behavior. It also calls the update routine and is responsible
 * for rendering. The GameManager implements the singleton pattern.
 */
// =============================================================== //
public class GameManager {
  // ======================= SecretKey class ======================= //
  //@formatter:off
  /** The GameManager mimics a friend class behavior, i.e. every method that
   * should be accessed only by the GameManager asked for the SecretKey. Only
   * the Game Manager can deliver this SecretKey */
  public static final class SecretKey { private SecretKey() {} }
  private static final SecretKey secret_key = new SecretKey();
  //@formatter:on

  // ====================== GameManager class ====================== //
  // GameManager uses singleton pattern
  private static GameManager instance = null;
  private double elapsed_time = 0.0;

  private HashMap<Integer, List<Renderable>> render_layers = new HashMap<>();
  private ArrayList<Entity> entities = new ArrayList<>();
  private GameSettings game_settings = null;

  // --- List of Entities needed to create other ones
  private SimpleRenderable floor = null;
  private UITimer timer = null;
  private Player[] players = null;
  private PlayerState[] player_states = null;
  private Canvas canvas = null;

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
    preUpdate();
    // --- update all entities
    for (Entity entity : entities)
      entity.update(secret_key);
    // ---
    moveAllPlayers();
    paintOnCanvas();
    adjustPaintAmounts();
    adjustScores();
  }

  // --------------------------------------------------------------- //
  private void preUpdate() {
    for (int idx = 0; idx < players.length; ++idx)
      savePlayerState(idx);
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
    game_settings = settings;
    sanityCheckPlayerSettings(); // throws an exception if something is wrong
    createBackground();
    createFloor();
    createUITimer();
    createPlayers();
    createCanvas();
    createPaintBooth();
  }

  // --------------------------------------------------------------- //
  /**
   * Check the game settings for correctness. If something is wrong, throw an
   * exceptions. Things that may be wrong are: - the number of players is not in
   * [1,4] - not each player gets a start position - not each player gets a
   * start direction
   */
  private void sanityCheckPlayerSettings() throws GameMangerException {
    // --- sanity check
    int player_count = game_settings.player_types.length;
    if (player_count < 1 || player_count > 4)
      throw new GameMangerException("Too less/many players: " + player_count);
    // ---
    if (game_settings.start_positions.length != player_count)
      throw new GameMangerException("mismatch: player count and positions");
    // ---
    if (game_settings.start_directions.length != player_count)
      throw new GameMangerException("mismatch: player count and directions");
  }

  // --------------------------------------------------------------- //
  private void createBackground() {
    String tex_file = game_settings.back_texture;
    int[] repeat_xy = {6, 6};
    Renderable background =
        new RepeatedRenderable("background", 0, tex_file, repeat_xy);
    addRenderable(background);
    addEntity(background);
  }

  // --------------------------------------------------------------- //
  private void createFloor() {
    String floor_texture = game_settings.floor_texture;
    floor = new SimpleRenderable("floor", 1, floor_texture);
    // --- position
    int[] pos = game_settings.board_border.clone();
    pos[0] += game_settings.ui_width;
    floor.setRenderPosition(pos);
    // --- size
    int width = game_settings.board_dimensions[0];
    int height = game_settings.board_dimensions[1];
    floor.setRenderSize(width, height);
    Entity.setBoardDimensions(Array.of(width, height), secret_key);
    // ---
    addRenderable(floor);
    addEntity(floor);
  }

  // --------------------------------------------------------------- //
  private void createUITimer() {
    timer = new UITimer(180);
    // --- define position and size
    int ui_width = game_settings.ui_width;
    int cam_res_y = game_settings.cam_resolution[1];
    int width = (int) (ui_width * 0.75);
    timer.setRenderWidth(width);
    int height = timer.getRenderSize()[1];
    int offset = (int) (ui_width * 0.125);
    int pos_x = offset;
    int pos_y = cam_res_y - height - offset;
    timer.setRenderPosition(Array.of(pos_x, pos_y));
    // ---
    addRenderable(timer);
    addEntity(timer);
  }

  // --------------------------------------------------------------- //
  private void createPlayers() throws GameMangerException {
    // --- create players
    int count = game_settings.player_types.length;
    players = new Player[count];
    player_states = new PlayerState[count];
    // ---
    for (int i = 0; i < count; ++i) {
      if (game_settings.player_types[i] == PlayerType.AI)
        throw new GameMangerException("No AI players implemented.");
      try {
        Player player = new HumanPlayer("Player" + i);
        initPlayer(player);
        addRenderable(player.getAnimation());
        addRenderable(player.getIndicator());
        addEntity(player);
        createPlayerUI(player);
        players[i] = player;
        savePlayerState(i);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  // --------------------------------------------------------------- //
  /**
   * Transfer player properties from the game settings to the player with the
   * given index .
   */
  private void initPlayer(Player player) throws PlayerException {
    int idx = player.getPlayerID();
    Vector2 pos = game_settings.start_positions[idx];
    Vector2 dir = game_settings.start_directions[idx].setLength(1.0f);
    player.setPosition(pos, secret_key);
    player.setDirection(dir, secret_key);
    player.setAnker(floor, secret_key);
  }

  // --------------------------------------------------------------- //
  private void createPlayerUI(Player player) {
    UIPlayerBoard board = new UIPlayerBoard(player);
    int player_id = player.getPaintColor().getColorID();
    // --- define position and size
    int[] anker = timer.getRenderPosition();
    int ui_width = game_settings.ui_width;
    int width = (int) (ui_width * 0.45);
    board.setRenderWidth(width);
    int height = board.getRenderSize()[1];
    int offset_x = (int) (ui_width * 0.1 / 3);
    int offset_y = height + (int) (ui_width * 0.05);
    int pos_x = (player_id % 2 == 1) ? offset_x : width + 2 * offset_x;
    int pos_y = (player_id > 1) ? anker[1] - offset_y : anker[1] - 2 * offset_y;
    board.setRenderPosition(Array.of(pos_x, pos_y));
    // ---
    addRenderable(board);
    entities.add(board);
  }

  // --------------------------------------------------------------- //
  /**
   * Write the current state of the player idx into the player_state array. This
   * is done to prevent cheatinge attempts, i.e. only the GameManager can change
   * the state of the player.
   */
  private void savePlayerState(int idx) {
    player_states[idx] = players[idx].getState();
  }

  // --------------------------------------------------------------- //
  /** Calls 'movePlayer(int idx)' for each player */
  private void moveAllPlayers() {
    for (int player_idx = 0; player_idx < players.length; ++player_idx)
      movePlayer(player_idx);
  }

  // --------------------------------------------------------------- //
  /**
   * Performs a move step in the current move direction if possible. The
   * translation vector depends on the move speed. The movement is clamped at
   * the borders of the board.
   */
  private void movePlayer(int player_idx) {
    Vector2 old_pos = player_states[player_idx].old_pos;
    Player player = players[player_idx];
    Vector2 move_dir = player.getDirection();
    // ---
    Vector2 new_pos = old_pos.cpy();
    new_pos.add(move_dir.scl(200.0f * Gdx.graphics.getDeltaTime()));
    clampPositionToBoard(new_pos);
    // ---
    player.setPosition(new_pos, secret_key);
    player_states[player_idx].new_pos = new_pos;
  }

  // --------------------------------------------------------------- //
  /**
   * Checks if the given position is inside of the game board. If not, change
   * the corresponding coordinates to be insider.
   */
  private void clampPositionToBoard(Vector2 pos, float offset) {
    int board_width = game_settings.board_dimensions[0];
    int board_height = game_settings.board_dimensions[1];
    pos.x = Math.max(pos.x, offset);
    pos.x = Math.min(pos.x, board_width - offset);
    pos.y = Math.max(pos.y, offset);
    pos.y = Math.min(pos.y, board_height - offset);
  }

  // --------------------------------------------------------------- //
  /** calls 'clampPosition(pos, 0.0f);' */
  private void clampPositionToBoard(Vector2 pos) {
    clampPositionToBoard(pos, 0.01f);
  }

  // --------------------------------------------------------------- //
  private void createCanvas() {
    int width = game_settings.board_dimensions[0];
    int height = game_settings.board_dimensions[1];
    canvas = new Canvas(width, height);
    canvas.setAnker(floor);
    addRenderable(canvas);
    addEntity(canvas);
  }

  // --------------------------------------------------------------- //
  private void paintOnCanvas() {
    for (int idx = 0; idx < players.length; ++idx) {
      Player player = players[idx];
      if ((player.getPaintAmount() <= 0.0))
        continue;
      Vector2 position = player_states[idx].new_pos;
      canvas.paint(position, player.getPaintColor(), 40, secret_key);
    }
    canvas.sendPixmapToTexture();
  }

  // --------------------------------------------------------------- //
  private void adjustPaintAmounts() {
    for (int player_idx = 0; player_idx < players.length; ++player_idx)
      adjustPaintAmount(player_idx);
  }

  // --------------------------------------------------------------- //
  private void adjustPaintAmount(int player_idx) {
    Vector2 old_pos = player_states[player_idx].old_pos;
    Vector2 new_pos = player_states[player_idx].new_pos;
    float move_dist = new_pos.dst(old_pos) / 4000.0f;
    Player player = players[player_idx];
    player.decreasePaintAmount(move_dist, secret_key);
  }

  // --------------------------------------------------------------- //
  private void adjustScores() {
    for (int player_idx = 0; player_idx < players.length; ++player_idx)
      adjustScore(player_idx);
  }

  // --------------------------------------------------------------- //
  private void adjustScore(int player_idx) {
    Player player = players[player_idx];
    long pixels = canvas.getPaintCount()[player_idx];
    long score = pixels * 100 / canvas.getTotalArea();
    player.setScore((int) score, secret_key);
  }

  // --------------------------------------------------------------- //
  private void createPaintBooth() {
    PaintBooth booth = new PaintBooth();
    booth.setAnker(floor);
    int[] pos = new int[] {300, 300};
    booth.setRenderPosition(pos);
    booth.setScale(Array.of(0.57f, 0.57f));
    // ---
    addRenderable(booth);
    addEntity(booth);
  }
}
