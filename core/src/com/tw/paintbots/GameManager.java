package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameManager {
  //@formatter:off
  // mimic a friend class behavior → every method that should be
  // accessed only by the GameManager has to deliver a secret key
  public static final class SecretKey { private SecretKey() {} }
  private static final SecretKey secret_key = new SecretKey();
  //@formatter:on

  // GameManager uses SingletonPattern
  private static GameManager instance = null;

  private GameSettings map_settings = null;
  private Player[] players = null;
  private PlayerState[] player_states = null;
  private Canvas canvas = null;
  private int[] cam_resolution = {0, 0};
  private double elapsed_time = 0.0;

  private UITimer timer = null;

  private ArrayList<Entity> entities = new ArrayList<>();
  private List<List<Renderable>> render_layers_ = null;
  private final int layers_count = 10;

  // --------------------------------------------------------------- //
  public static GameManager get() {
    if (instance == null)
      instance = new GameManager();
    return instance;
  }

  // --------------------------------------------------------------- //
  public double getElapsedTime() {
    return elapsed_time;
  }

  // --------------------------------------------------------------- //
  private GameManager() {
    render_layers_ = new ArrayList<List<Renderable>>();
    for (int i = 0; i < layers_count; ++i)
      render_layers_.add(new ArrayList<Renderable>());
  }

  // --------------------------------------------------------------- //
  public void setCameraResolution(int[] resolution) {
    cam_resolution = resolution;
  }

  // =============================================================== //
  public void loadMap(GameSettings settings) throws GameMangerException {
    map_settings = settings;
    sanityCheckPlayerSettings(); // throws an exception if something is wrong
    createBackground();
    createFloor();
    createUITimer();
    createPlayers();
    createCanvas();
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
    int player_count = map_settings.player_types.length;
    if (player_count < 1 || player_count > 4)
      throw new GameMangerException("Too less/many players: " + player_count);
    // ---
    if (map_settings.start_positions.length != player_count)
      throw new GameMangerException("mismatch: player count and positions");
    // ---
    if (map_settings.start_directions.length != player_count)
      throw new GameMangerException("mismatch: player count and directions");
  }

  // --------------------------------------------------------------- //
  private void createBackground() {
    String background_texture = map_settings.back_texture;
    int[] repeat_xy = {6, 6};
    Renderable background = new Renderable("background", background_texture,
        Array.of(0), repeat_xy, cam_resolution);
    addRenderable(background);
    entities.add(background);
  }

  // --------------------------------------------------------------- //
  private void createFloor() {
    String floor_texture = map_settings.floor_texture;
    Renderable floor = new Renderable("floor", floor_texture, Array.of(1));
    // --- position
    int[] pos = map_settings.board_border.clone();
    pos[0] += map_settings.ui_width;
    floor.setRenderPosition(pos);
    // --- size
    int width = map_settings.board_dimensions[0];
    int height = map_settings.board_dimensions[1];
    floor.setRenderSize(width, height);
    // ---
    addRenderable(floor);
    entities.add(floor);
  }

  // --------------------------------------------------------------- //
  /**
   * Add a renderable item to the render layers. Layers with a lower index are
   * rendered before layers with a higher index.
   */
  private void addRenderable(Renderable item) {
    for (int layer_idx : item.getLayers()) {
      List<Renderable> layer = render_layers_.get(layer_idx);
      layer.add(item);
    }
  }

  // --------------------------------------------------------------- //
  private void createPlayers() throws GameMangerException {
    // --- create players
    int count = map_settings.player_types.length;
    players = new Player[count];
    player_states = new PlayerState[count];
    // ---
    for (int i = 0; i < count; ++i) {
      if (map_settings.player_types[i] == PlayerType.AI)
        throw new GameMangerException("No AI players implemented.");
      try {
        players[i] = new HumanPlayer("Player" + i);
        entities.add(players[i]);
        addRenderable(players[i]);
        initPlayer(i);
        savePlayerState(i);
        createPlayerUI(players[i]);
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
  private void initPlayer(int idx) throws PlayerException {
    Player player = players[idx];
    Vector2 pos = map_settings.start_positions[idx];
    Vector2 dir = map_settings.start_directions[idx].setLength(1.0f);
    int[] render_pos = map_settings.board_border.clone();
    render_pos[0] += map_settings.ui_width;

    player.setPosition(pos, new SecretKey());
    player.setDirection(dir, new SecretKey());
    player.setRenderPosition(render_pos);
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
  private void createCanvas() {
    int width = map_settings.board_dimensions[0];
    int height = map_settings.board_dimensions[1];
    int[] pos = map_settings.board_border.clone();
    pos[0] += map_settings.ui_width;

    canvas = new Canvas(width, height);
    canvas.setRenderPosition(pos);
    addRenderable(canvas);
    entities.add(canvas);
  }

  // --------------------------------------------------------------- //
  private void createUITimer() {
    timer = new UITimer(180);
    // --- define position and size
    int ui_width = map_settings.ui_width;
    int width = (int) (ui_width * 0.75);
    timer.setRenderWidth(width);
    int height = timer.getRenderSize()[1];
    int offset = (int) (ui_width * 0.125);
    int pos_x = offset;
    int pos_y = cam_resolution[1] - height - offset;
    timer.setRenderPosition(Array.of(pos_x, pos_y));
    // ---
    addRenderable(timer);
    entities.add(timer);
  }

  // --------------------------------------------------------------- //
  private void createPlayerUI(Player player) {
    UIPlayerBoard board = new UIPlayerBoard(player);
    int player_id = player.getPaintColor().getColorID();
    // --- define position and size
    int[] anker = timer.getRenderPosition();
    int ui_width = map_settings.ui_width;
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
  public void update() {
    elapsed_time += Gdx.graphics.getDeltaTime();
    // ---
    preUpdate();
    // --- update all entities
    for (Entity entity : entities)
      entity.update();
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
  private void clampPositionToBoard(Vector2 pos, double offset) {
    int board_width = map_settings.board_dimensions[0];
    int board_height = map_settings.board_dimensions[1];
    pos.x = Math.max(pos.x, 0);
    pos.x = Math.min(pos.x, board_width - (float) offset);
    pos.y = Math.max(pos.y, 0);
    pos.y = Math.min(pos.y, board_height - (float) offset);
  }

  // --------------------------------------------------------------- //
  /** calls 'clampPosition(pos, 0.0f);' */
  private void clampPositionToBoard(Vector2 pos) {
    clampPositionToBoard(pos, 0.0);
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
  public void render(SpriteBatch batch) {
    // --- render all layers
    int layer_idx = 0;
    for (List<Renderable> layer_items : render_layers_) {
      for (Renderable renderable : layer_items)
        renderable.render(batch, layer_idx);
      ++layer_idx;
    }
  }
}
