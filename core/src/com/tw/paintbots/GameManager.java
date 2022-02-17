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
import com.tw.paintbots.Renderables.RenderDepthComparator;
import com.tw.paintbots.Renderables.Renderable;
import com.tw.paintbots.Renderables.SimpleRenderable;
import com.tw.paintbots.Renderables.RepeatedRenderable;
import com.tw.paintbots.Renderables.UITimer;
import com.tw.paintbots.Renderables.UIPlayerBoard;
import com.tw.paintbots.Items.Item;
import com.tw.paintbots.Items.PaintBooth;
import com.tw.paintbots.Items.FenceH;
import com.tw.paintbots.Items.ItemArea;
import com.tw.paintbots.Items.ItemType;
import com.tw.paintbots.Level;

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
  private double delta_time = 0.0;

  // RenderLayer 20 is reserved for elements on the game board, e.g. objects.
  private HashMap<Integer, List<Renderable>> render_layers = new HashMap<>();
  private List<Renderable> player_layer = new ArrayList<>();
  private ArrayList<Entity> entities = new ArrayList<>();
  private GameSettings game_settings = null;

  // --- List of Entities needed to create other ones
  private SimpleRenderable floor = null;
  private UITimer timer = null;
  private Player[] players = null;
  private PlayerState[] player_states = null;
  private Canvas canvas = null;
  private PaintBooth booth = null;
  private Board board = null;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the time that passed since the start of the round in milliseconds. */
  public double getElapsedTime() { return elapsed_time; }
  /** Get the between the current and the last update step. */
  public double getDeltaTime() { return delta_time; }
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
    delta_time = Gdx.graphics.getDeltaTime();
    elapsed_time += delta_time;
    // ---
    preUpdate();
    // --- update all entities
    for (Entity entity : entities)
      entity.update(secret_key);
    // ---
    moveAllPlayers();
    if (timer.getTime() > 0)
      paintOnCanvas();
    adjustScores();
    // ---
    interactWithBoard();
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
      // ---
      if (id == 20) {
        renderItemLayer(batch);
        continue;
      }
      // ---
      List<Renderable> renderables = render_layers.get(id);
      for (Renderable item : renderables)
        item.render(batch, id.intValue());
    }
  }

  // --------------------------------------------------------------- //
  public void renderItemLayer(SpriteBatch batch) {
    // ---
    List<Renderable> items = render_layers.get(20);
    RenderDepthComparator comparator = new RenderDepthComparator();
    player_layer.sort(comparator);
    // ---
    int idx_item = 0;
    int idx_player = 0;
    Renderable item = null;
    Renderable player = null;
    while (true) {
      // ---
      item = null;
      player = null;
      if (idx_item < items.size())
        item = items.get(idx_item);
      if (idx_player < player_layer.size())
        player = player_layer.get(idx_player);
      // ---
      if (item == null && player == null)
        return;
      // ---
      if (comparator.compare(item, player) < 0) {
        item.render(batch, 20);
        ++idx_item;
      } else {
        player.render(batch, 20);
        ++idx_player;
      }
    }
  }

  // --------------------------------------------------------------- //
  /**
   * Add the Renderable item to all render layers the item demands. If a
   * specific layer does not exist yet, it gets created.
   */
  private void addRenderable(Renderable renderable) {
    int[] renderable_layers = renderable.getLayers();
    for (int layer_idx : renderable_layers) {
      // --- check if the layer exists
      if (render_layers.get(layer_idx) == null)
        render_layers.put(layer_idx, new ArrayList<Renderable>());
      // --- access the layer
      List<Renderable> layer = render_layers.get(layer_idx);
      layer.add(renderable);
    }
  }

  // --------------------------------------------------------------- //
  private void addEntity(Entity entity) {
    entities.add(entity);
  }

  // --------------------------------------------------------------- //
  private void addItem(Item item) {
    ItemArea area = item.getItemArea(secret_key);
    int[] origin = area.getOrigin();
    int width = area.getWidth();
    int height = area.getHeight();
    for (int x = 0; x < width; ++x)
      for (int y = 0; y < height; ++y) {
        int pos_x = origin[0] + x;
        int pos_y = origin[1] + y;
        ItemType type = area.getType(x, y);
        board.setType(pos_x, pos_y, type, secret_key);
      }
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
    createBoard();
    // ---
    loadLevelContent();
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
    int sec = game_settings.game_length;
    timer = new UITimer(sec);
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
        player_layer.add(player.getAnimation());
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
    int max_paint = game_settings.max_paint_amount;
    int start_paint = game_settings.start_paint_amount;
    int paint_radius = game_settings.paint_radius;
    int refill_speed = game_settings.refill_speed;
    player.setPosition(pos, secret_key);
    player.setDirection(dir, secret_key);
    player.setAnker(floor, secret_key);
    player.setMaximumPaintAmount(max_paint, secret_key);
    player.setPaintAmount(start_paint, secret_key);
    player.setPaintRadius(paint_radius, secret_key);
    player.setRefillSpeed(refill_speed, secret_key);
  }

  // --------------------------------------------------------------- //
  private void createPlayerUI(Player player) {
    UIPlayerBoard info_board = new UIPlayerBoard(player);
    int player_id = player.getPaintColor().getColorID();
    // --- define position and size
    int[] anker = timer.getRenderPosition();
    int ui_width = game_settings.ui_width;
    int width = (int) (ui_width * 0.45);
    info_board.setRenderWidth(width);
    int height = info_board.getRenderSize()[1];
    int offset_x = (int) (ui_width * 0.1 / 3);
    int offset_y = height + (int) (ui_width * 0.05);
    int pos_x = (player_id % 2 == 1) ? offset_x : width + 2 * offset_x;
    int pos_y = (player_id > 1) ? anker[1] - offset_y : anker[1] - 2 * offset_y;
    info_board.setRenderPosition(Array.of(pos_x, pos_y));
    // ---
    addRenderable(info_board);
    entities.add(info_board);
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
    clampPositionToBorder(new_pos);
    clampPositionToObstacles(new_pos, old_pos);
    // ---
    player.setPosition(new_pos, secret_key);
    player_states[player_idx].new_pos = new_pos;
  }

  // --------------------------------------------------------------- //
  private void interactWithBoard() {
    for (int player_idx = 0; player_idx < players.length; ++player_idx)
      interactWithBoard(player_idx);
  }

  // --------------------------------------------------------------- //
  private void interactWithBoard(int player_idx) {
    Player player = players[player_idx];
    Vector2 pos = player_states[player_idx].new_pos;
    int x = (int) pos.x;
    int y = (int) pos.y;
    ItemType cell_type = board.getType(x, y);

    if (cell_type == ItemType.NONE)
      return;

    // --- refill all colors
    if (cell_type == ItemType.REFILL) {
      int refill_speed = player.getRefillSpeed();
      int increase = (int) (refill_speed * delta_time);
      player.increasePaintAmount(increase, secret_key);
      return;
    }

    // --- refill green
    //@formatter:off
    if ((cell_type == ItemType.REFILL_GREEN  && player.getPaintColor() == PaintColor.GREEN) ||
        (cell_type == ItemType.REFILL_PURPLE && player.getPaintColor() == PaintColor.PURPLE) ||
        (cell_type == ItemType.REFILL_ORANGE && player.getPaintColor() == PaintColor.ORANGE) ||
        (cell_type == ItemType.REFILL_BLUE   && player.getPaintColor() == PaintColor.BLUE)) {
    //@formatter:on
      int refill_speed = player.getRefillSpeed();
      int increase = (int) (refill_speed * delta_time);
      player.increasePaintAmount(increase, secret_key);
      return;
    }
  }

  // --------------------------------------------------------------- //
  /** calls 'clampPosition(pos, 0.0f);' */
  private void clampPositionToBorder(Vector2 pos) {
    clampPositionToBorder(pos, 0.01f);
  }

  // --------------------------------------------------------------- //
  /**
   * Checks if the given position is inside of the game board. If not, change
   * the corresponding coordinates to be insider.
   */
  private void clampPositionToBorder(Vector2 pos, float offset) {
    int board_width = game_settings.board_dimensions[0];
    int board_height = game_settings.board_dimensions[1];
    pos.x = Math.max(pos.x, offset);
    pos.x = Math.min(pos.x, board_width - offset);
    pos.y = Math.max(pos.y, offset);
    pos.y = Math.min(pos.y, board_height - offset);
  }

  // --------------------------------------------------------------- //
  private void clampPositionToObstacles(Vector2 pos, Vector2 old_pos) {
    int pos_x = (int) pos.x;
    int pos_y = (int) pos.y;
    if (board.getType(pos_x, pos_y) != ItemType.OBSTACLE)
      return;
    // --- clamp x-coordinate
    int old_x = (int) old_pos.x;
    if (board.getType(old_x, pos_y) != ItemType.OBSTACLE) {
      pos.x = old_pos.x;
      return;
    }
    // --- clamp y-coordinate
    int old_y = (int) old_pos.y;
    if (board.getType(pos_x, old_y) != ItemType.OBSTACLE) {
      pos.y = old_pos.y;
      return;
    }
    // --- clamp x- and y-cooridnates
    if (board.getType(old_x, old_y) != ItemType.OBSTACLE) {
      pos.x = old_pos.x;
      pos.y = old_pos.y;
      return;
    }
    // If we reached this point an neither condition is true, we might have
    // started in an obstacle. So just go on and give a hint.
    System.out.println("ERROR: obstacle collision " + pos_x + " " + pos_y);
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
      int radius = player.getPaintRadius();
      int used_paint = canvas.paint(position, player.getPaintColor(), radius,
          board, secret_key);
      player.decreasePaintAmount(used_paint, secret_key);
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
  private void createPaintBooth() {
    booth = new PaintBooth();
    booth.setAnker(floor);
    int[] pos = new int[] {500, 500};
    booth.setPosition(new Vector2(pos[0], pos[1]), secret_key);
    booth.setRenderPosition(pos);
    booth.init();
    // ---
    addRenderable(booth);
    addEntity(booth);
    addItem(booth);
  }

  // --------------------------------------------------------------- //
  private void createFence() {
    // ---
    FenceH fence_1 = new FenceH();
    fence_1.setAnker(floor);
    int[] pos = new int[] {200, 750};
    fence_1.setPosition(new Vector2(pos[0], pos[1]), secret_key);
    fence_1.setRenderPosition(pos);
    fence_1.init();
    // ---
    addRenderable(fence_1);
    addEntity(fence_1);
    addItem(fence_1);
    // ---
    FenceH fence_2 = new FenceH();
    fence_2.setAnker(floor);
    pos = new int[] {800, 250};
    fence_2.setPosition(new Vector2(pos[0], pos[1]), secret_key);
    fence_2.setRenderPosition(pos);
    fence_2.init();
    // ---
    addRenderable(fence_2);
    addEntity(fence_2);
    addItem(fence_2);
  }

  // --------------------------------------------------------------- //
  private void createBoard() {
    int width = game_settings.board_dimensions[0];
    int height = game_settings.board_dimensions[1];
    board = new Board(width, height);
  }

  // --------------------------------------------------------------- //
  private void loadLevelContent() {
    // --- make sure there is an item layer
    if (render_layers.get(20) == null)
      render_layers.put(20, new ArrayList<Renderable>());
    // ---
    List<Item> level_items = new ArrayList<>();
    Level level = new Level("level1.lvl", secret_key);
    level.loadLevel(level_items);
    // ---
    for (Item item : level_items) {
      addEntity(item);
      item.setAnker(floor);
      addRenderable(item);
      addItem(item);
    }
    // ---
    List<Renderable> items_list = render_layers.get(20);
    items_list.sort(new RenderDepthComparator());
  }
}
