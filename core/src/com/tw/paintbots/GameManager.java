package com.tw.paintbots;

import java.lang.reflect.Constructor;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.PaintBotsGame.GameKey;
import com.tw.paintbots.Renderables.Canvas;
import com.tw.paintbots.Renderables.RenderDepthComparator;
import com.tw.paintbots.Renderables.Renderable;
import com.tw.paintbots.Renderables.SimpleRenderable;
import com.tw.paintbots.Renderables.RepeatedRenderable;
import com.tw.paintbots.Renderables.UITimer;
import com.tw.paintbots.Renderables.StartTimer;
import com.tw.paintbots.Renderables.UIPlayerBoard;
import com.tw.paintbots.Renderables.UIMenuItem;
import com.tw.paintbots.LevelLoader;
import com.tw.paintbots.Items.Item;
import com.tw.paintbots.Items.ItemArea;
import com.tw.paintbots.Items.ItemType;
import com.tw.paintbots.NonePlayer;

/**
 * The GameManager is the core class of the game. It creates all Entities and
 * manages their behavior. It also calls the update routine and is responsible
 * for rendering. The GameManager implements the singleton pattern.
 */
// =============================================================== //
public class GameManager {
  // ======================= SecretKey class ======================= //
  //@formatter:off
  /** 
   * The GameManager mimics a friend class behavior, i.e. every method that
   * should be accessed only by the GameManager asks for the SecretKey.
   * Only the GameManager can deliver this SecretKey 
   */
  public static final class SecretKey { private SecretKey() {} }
  private static final SecretKey secret_key = new SecretKey();
  //@formatter:on

  // ======================== GameState enum ======================= //
  /**
   * The GameState effectively controls what is shown an what happens in the
   * update() routine.
   */
  //@formatter:off
  private enum GameState {
    MENU,        ///< Show the menu for bot and level selection.
    STARTTIMER,  ///< Show the countdown at the start of the game.
    GAME,        ///< The normal state during the game.
    RESULT       ///< Show the result at the end of the game.
  }
  //@formatter:on

  // ====================== GameManager class ====================== //
  // GameManager uses singleton pattern
  private static GameManager instance = null;
  private double elapsed_time = 0.0;
  private double delta_time = 0.0;
  private GameState game_state = GameState.MENU;

  // --- RenderLayer 20 is reserved for game board elements, e.g. objects.
  private HashMap<Integer, List<Renderable>> render_layers = new HashMap<>();
  private List<Renderable> player_layer = new ArrayList<>();
  private ArrayList<Entity> entities = new ArrayList<>();
  private GameSettings game_settings = null;

  // --- stuff for the menu
  private ArrayList<UIMenuItem> menu_item = new ArrayList<>();
  private ArrayList<String> bot_names = new ArrayList<>();
  private int menu_select = 0;
  private boolean read_menu_key = true;
  private ArrayList<String> level_files = new ArrayList<>();

  // --- List of Entities needed to create other ones
  private SimpleRenderable floor = null;
  private UITimer timer = null;
  private StartTimer start_timer = null;
  private ArrayList<Player> players = new ArrayList<>();
  private ArrayList<PlayerState> player_states = new ArrayList<>();
  private Canvas canvas = null;
  private Board board = null;
  private HashMap<String, Class<?>> bots = null;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the time that passed since the start of the round in seconds. */
  public double getElapsedTime() { return elapsed_time; }
  /** Get the time between the current and the last update step. */
  public double getDeltaTime() { return delta_time; }
  //@formatter:on

  // ===================== GameManager methods ===================== //
  /**
   * The GameManager implements the singleton pattern. With this method you can
   * access the only existing instance of the GameManager.
   */
  public static GameManager get() {
    if (instance == null)
      instance = new GameManager();
    return instance;
  }

  // --------------------------------------------------------------- //
  /**
   * Constructor is private due to the sigleton pattern.
   */
  private GameManager() {
    loadBots();
    loadLevels();
  }

  // --------------------------------------------------------------- //
  /**
   * Read the bots from the bots directory and collect them in a HashMap (name
   * and class) and an ArrayList (name). Furthermore dummy bots for human
   * controlled and inactive players are added.
   */
  private void loadBots() {
    // --- load some bots
    BotLoader bot_loader = new BotLoader();
    bots = bot_loader.loadBots();
    // ---
    bot_names.add("Human");
    bot_names.add("---");
    // ---
    Set<String> loaded_names = bots.keySet();
    for (String name : loaded_names)
      bot_names.add(name);
    // ---
    bots.put("Human", HumanPlayer.class);
    bots.put("---", null);
  }

  // --------------------------------------------------------------- //
  /**
   * Collect the level files from the levels directory in an ArrayList.
   */
  private void loadLevels() {
    LevelLoader level_loader = new LevelLoader();
    level_files = level_loader.loadLevelFiles();
    level_files.add(0, "default");
  }

  // --------------------------------------------------------------- //
  /**
   * Dispose all entities. This method is called when the program closes to
   * clean up the memory. This method can only be called by the PaintBotsGame
   * class.
   */
  public void destroy(GameKey key) {
    Objects.requireNonNull(key);
    for (Entity entity : entities)
      entity.destroy(secret_key);
    entities.clear();
  }

  // --------------------------------------------------------------- //
 //@formatter:off
 /**
  * The update routine that is called in the loop. This method can only be
  * called by the PaintBotsGame class. Depending on the GameState different
  * things happen:* 
  * - MENU: The keyboard controls are used to select bots and levels
  * - STARTTIMER: Count down 5 seconds at the start of the game.
  * - GANE: Update all entities, especially the players/bots.
  */
  //@formatter:on
  public void update(GameKey key) {
    Objects.requireNonNull(key);
    // delta_time = Gdx.graphics.getDeltaTime();
    delta_time = 1.0 / 60.0;
    elapsed_time += delta_time;
    // ---
    switch (game_state) {
      // ---
      case MENU: {
        updateMenu();
      }
        break;
      // ---
      case STARTTIMER: {
        updateStartTime();
      }
        break;
      // ---
      case GAME:
        updateGame();
        break;
      default:
        break;
    }
  }

  // --------------------------------------------------------------- //
  /** Read the keyboard input to control the main menu. */
  private void updateMenu() {
    if (ignoreMenuInteraction())
      return;
    // --- handle up/down key
    if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)) {
      changeMenuFocus();
      read_menu_key = false;
    }
    // --- handle left/right key
    if (Gdx.input.isKeyPressed(Keys.LEFT)
        || Gdx.input.isKeyPressed(Keys.RIGHT)) {
      changeMenuValue();
      read_menu_key = false;
    }
    // ---
    if (Gdx.input.isKeyPressed(Keys.ENTER) && menu_select == 5)
      startGame();
  }

  // --------------------------------------------------------------- //
  /**
   * The interaction with the menu needs discrete key presses, i.e. the key has
   * to be released.
   */
  private boolean ignoreMenuInteraction() {
    // --- user must release keys after each press
    if (!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN)
        && !Gdx.input.isKeyPressed(Keys.LEFT)
        && !Gdx.input.isKeyPressed(Keys.RIGHT))
      read_menu_key = true;
    return !read_menu_key;
  }

  // --------------------------------------------------------------- //
  /** With up/down cursors the focus for the menu entry can be set. */
  private void changeMenuFocus() {
    int old_menu_select = menu_select;
    if (Gdx.input.isKeyPressed(Keys.UP))
      menu_select -= 1;
    if (Gdx.input.isKeyPressed(Keys.DOWN))
      menu_select += 1;
    // --- clamp to entry count
    if (menu_select < 0)
      menu_select = menu_item.size() - 1;
    if (menu_select >= menu_item.size())
      menu_select = 0;
    // ---
    menu_item.get(old_menu_select).setFocus(false);
    menu_item.get(menu_select).setFocus(true);
  }

  // --------------------------------------------------------------- //
  /** Depending on the menu entry bots or levels can be swutched. */
  private void changeMenuValue() {
    if (menu_select >= 0 && menu_select <= 3)
      changeSelectedBot();
    if (menu_select == 4)
      changeSelectedLevel();
  }

  // --------------------------------------------------------------- //
  /**
   * With left/right cursor the selected bot at the current menu entry can be
   * changed.
   */
  private void changeSelectedBot() {
    UIMenuItem bot_select = menu_item.get(menu_select);
    int bot_index = bot_select.getItemIndex(secret_key);
    // ---
    if (Gdx.input.isKeyPressed(Keys.LEFT))
      bot_index -= 1;
    if (Gdx.input.isKeyPressed(Keys.RIGHT))
      bot_index += 1;
    // --- clamp to entry count
    if (bot_index < 0)
      bot_index = bot_names.size() - 1;
    if (bot_index >= bot_names.size())
      bot_index = 0;
    // ---
    bot_select.setItem(bot_names.get(bot_index), bot_index, secret_key);
  }

  // --------------------------------------------------------------- //
  /**
   * With left/right cursor the selected level at the level menu entry can be
   * changed.
   */
  private void changeSelectedLevel() {
    UIMenuItem level_select = menu_item.get(menu_select);
    int level_index = level_select.getItemIndex(secret_key);
    // ---
    if (Gdx.input.isKeyPressed(Keys.LEFT))
      level_index -= 1;
    if (Gdx.input.isKeyPressed(Keys.RIGHT))
      level_index += 1;
    // --- clamp to entry count
    if (level_index < 0)
      level_index = level_files.size() - 1;
    if (level_index >= level_files.size())
      level_index = 0;
    // ---
    level_select.setItem(level_files.get(level_index), level_index, secret_key);
  }

  // --------------------------------------------------------------- //
  /**
   * Read the selection from the main menu, deactivate the main menu and load
   * the map.
   */
  private void startGame() {
    // --- read bot settings from menu
    for (int i = 0; i < 4; ++i) {
      UIMenuItem bot_select = menu_item.get(i);
      int bot_idx = bot_select.getItemIndex(secret_key);
      if (bot_idx == 0) {
        game_settings.player_types[i] = PlayerType.HUMAN;
        game_settings.bot_names[i] = "Human";
      } else if (bot_idx == 1) {
        game_settings.player_types[i] = PlayerType.NONE;
        game_settings.bot_names[i] = "---";
      } else {
        game_settings.player_types[i] = PlayerType.AI;
        game_settings.bot_names[i] = bot_select.getItemName(secret_key);
      }
    }
    // --- read level settings from menu
    UIMenuItem level_select = menu_item.get(4);
    String level_file = "";
    if (level_select.getItemIndex(secret_key) != 0)
      level_file = System.getProperty("user.dir") + "/levels/"
          + level_select.getItemName(secret_key);
    game_settings.level_file = level_file;
    // ---
    game_state = GameState.STARTTIMER;
    elapsed_time = 0.0;
    hideMenu();
    try {
      loadMap();
    } catch (GameMangerException e) {
      e.printStackTrace();
    }
  }

  // --------------------------------------------------------------- //
  /** Deactivate and hide the main menu. */
  private void hideMenu() {
    for (UIMenuItem item : menu_item) {
      item.setActive(false);
      item.setVisible(false);
    }
  }

  // --------------------------------------------------------------- //
  /**
   * Update the StartTimer that is shown at the start of the game. The
   * StartTimer gets the elapsed time. If the elapsed time reaches the countdown
   * the timer deactivates itself. If so the GameState is changed to 'GAME'.
   */
  private void updateStartTime() {
    start_timer.setElapsed((float) elapsed_time);
    // ---
    if (!start_timer.isActive()) {
      game_state = GameState.GAME;
      elapsed_time = 0.0;
      start_timer.setVisible(false);
    }
  }

  // --------------------------------------------------------------- //
 //@formatter:off
 /**
  * This method effectively runs the game and is called in each update loop.
  * The following things happen:
  * - save the player states
  * - update all active entities
  * - move all players
  * - paint on the canvas
  * - adjust the score
  * - let the players interact with the board
  */
  //@formatter:on
  private void updateGame() {
    preUpdate();
    // --- update all entities
    for (Entity entity : entities)
      if (entity.isActive())
        entity.update(secret_key);
    // ---
    moveAllPlayers();
    if (timer.getTime() > 0) {
      paintOnCanvas();
      adjustScores();
    }
    // ---
    interactWithBoard();
  }

  // --------------------------------------------------------------- //
  private void preUpdate() {
    for (int idx = 0; idx < players.size(); ++idx) {
      if (players.get(idx).getType() == PlayerType.NONE)
        continue;
      savePlayerState(idx);
    }
  }

  // --------------------------------------------------------------- //
  /** Draw the Renderables of each layer. */
  public void render(SpriteBatch batch, GameKey key) {
    Objects.requireNonNull(key);
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
        if (item.isVisible())
          item.render(batch, id.intValue());
    }
  }

  // --------------------------------------------------------------- //
  private void renderItemLayer(SpriteBatch batch) {
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
        if (item.isVisible())
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
  /**
   * This method is the entry point from the main class into the actual game.
   * This method can only be called the PaintBotsGame class
   * 
   * @param settings The initial GameSettings that can be changed in the menu
   * @param key The GameKey that is only available to the PaintBotsGame class
   */
  public void loadMenu(GameSettings settings, GameKey key) {
    Objects.requireNonNull(key);
    game_settings = settings;
    createBackground();
    createMenu();
  }

  // --------------------------------------------------------------- //
  private void createMenu() {
    for (int i = 0; i < 6; ++i) {
      UIMenuItem select = new UIMenuItem(i);
      menu_item.add(select);
      select.setScale(Array.of(0.75f, 0.75f));
      // --- define position and size
      int res_x = game_settings.cam_resolution[0];
      int res_y = game_settings.cam_resolution[1];
      int width = select.getRenderSize()[0];
      int height = select.getRenderSize()[1];
      int pos_x = (res_x - width) / 2;
      int pos_y = res_y - (height * (i + 2));
      select.setRenderPosition(Array.of(pos_x, pos_y));

      if (i == 4)
        select.setItemName("default", secret_key);

      addRenderable(select);
      addEntity(select);
    }
  }

  // --------------------------------------------------------------- //
  private void loadMap() throws GameMangerException {
    createFloor();
    createUITimer();
    createStartTimer();
    // ---
    createCanvas();
    initCanvasRenderables();
    createBoard();
    loadLevelContent();
    // --- players have to be loaded after the level
    sanityCheckPlayerSettings(); // throws an exception if something is wrong
    createPlayers();
    initPlayerRenderables();
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
  private void createStartTimer() {
    start_timer = new StartTimer(5.0f);
    start_timer.setAnker(floor);
    start_timer.setElapsed(0.0f);
    // --- define position and size
    int[] floor_size = floor.getRenderSize();
    int[] timer_size = start_timer.getRenderSize();
    int pos_x = floor_size[0] / 2 - timer_size[0] / 2;
    int pos_y = floor_size[1] / 2 - timer_size[1] / 2;
    start_timer.setRenderPosition(Array.of(pos_x, pos_y));
    // ---
    addRenderable(start_timer);
    addEntity(start_timer);
  }

  // --------------------------------------------------------------- //
  private void createPlayers() throws GameMangerException {
    // --- create players
    int count = game_settings.player_types.length;
    // ---
    for (int i = 0; i < count; ++i) {
      Player player = null;
      try {
        // --- load bot
        if (game_settings.player_types[i] == PlayerType.AI) {
          player = createBot(i);
          if (player == null)
            continue;
        }
        // --- load human player
        if (game_settings.player_types[i] == PlayerType.HUMAN)
          player = new HumanPlayer("Player" + i);

        if (game_settings.player_types[i] == PlayerType.NONE) {
          player = new NonePlayer("Player" + i);
          player.setActive(false);
        }
        // ---
        initPlayer(player);
        addEntity(player);
        players.add(player);
        player_states.add(new PlayerState());
        savePlayerState(i);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  // --------------------------------------------------------------- //
  private Player createBot(int player_idx) {
    Player player = null;
    String bot_name = game_settings.bot_names[player_idx];
    Class<?> bot_class = bots.get(bot_name);
    // --- check if a bot with the name exists
    if (bot_class == null) {
      System.out
          .println("Could not load " + bot_name + " - check class and name!");
      return null;
    }
    // --- try to access the constructor
    try {
      Constructor<?> cons = bot_class.getConstructor(String.class);
      player = (AIPlayer) cons.newInstance("AI_" + bot_name + player_idx);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
    // ---
    return player;
  }

  // --------------------------------------------------------------- //
  private void initPlayerRenderables() {
    for (Player player : players) {
      if (player.getType() == PlayerType.NONE)
        continue;
      player.initRenderables(secret_key);
      player.setAnker(floor, secret_key);
      player_layer.add(player.getAnimation(secret_key));
      addRenderable(player.getIndicator(secret_key));
      createPlayerUI(player);
      // --- place renderable at correct location
      Vector2 pos = player.getPosition();
      player.getAnimation(secret_key)
          .setRenderPosition(Array.of((int) pos.x, (int) pos.y));
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
    player.setInitialDirection(dir, secret_key);
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
    addEntity(info_board);
  }

  // --------------------------------------------------------------- //
  /**
   * Write the current state of the player idx into the player_state array. This
   * is done to prevent cheatinge attempts, i.e. only the GameManager can change
   * the state of the player.
   */
  private void savePlayerState(int idx) {
    PlayerState state = players.get(idx).getState();
    player_states.set(idx, state);
  }

  // --------------------------------------------------------------- //
  /** Calls 'movePlayer(int idx)' for each player */
  private void moveAllPlayers() {
    for (int player_idx = 0; player_idx < players.size(); ++player_idx) {
      if (players.get(player_idx).getType() == PlayerType.NONE)
        continue;
      movePlayer(player_idx);
    }
  }

  // --------------------------------------------------------------- //
  /**
   * Performs a move step in the current move direction if possible. The
   * translation vector depends on the move speed. The movement is clamped at
   * the borders of the board.
   */
  private void movePlayer(int player_idx) {
    Vector2 old_pos = player_states.get(player_idx).old_pos;
    Player player = players.get(player_idx);
    Vector2 move_dir = player.getDirection();
    // ---
    Vector2 new_pos = old_pos.cpy();
    new_pos.add(move_dir.scl(200.0f * (float) delta_time)); // scale
    clampPositionToBorder(new_pos);
    clampPositionToObstacles(new_pos, old_pos);
    // ---
    player.setPosition(new_pos, secret_key);
    player_states.get(player_idx).new_pos = new_pos;
  }

  // --------------------------------------------------------------- //
  private void interactWithBoard() {
    for (int player_idx = 0; player_idx < players.size(); ++player_idx) {
      if (players.get(player_idx).getType() == PlayerType.NONE)
        continue;
      interactWithBoard(player_idx);
    }
  }

  // --------------------------------------------------------------- //
  private void interactWithBoard(int player_idx) {
    Player player = players.get(player_idx);
    Vector2 pos = player_states.get(player_idx).new_pos;
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
    if (board.getType(pos_x, pos_y).isPassable())
      return;
    // --- clamp x-coordinate
    int old_x = (int) old_pos.x;
    if (board.getType(old_x, pos_y).isPassable()) {
      pos.x = old_pos.x;
      return;
    }
    // --- clamp y-coordinate
    int old_y = (int) old_pos.y;
    if (board.getType(pos_x, old_y).isPassable()) {
      pos.y = old_pos.y;
      return;
    }
    // --- clamp x- and y-cooridnates
    if (board.getType(old_x, old_y).isPassable()) {
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
    addEntity(canvas);
  }

  // --------------------------------------------------------------- //
  private void initCanvasRenderables() {
    canvas.initCanvasRenderables();
    canvas.setAnker(floor);
    addRenderable(canvas);
  }

  // --------------------------------------------------------------- //
  private void paintOnCanvas() {
    for (int idx = 0; idx < players.size(); ++idx) {
      Player player = players.get(idx);
      if (player.getType() == PlayerType.NONE)
        continue;
      if ((player.getPaintAmount() <= 0.0))
        continue;
      Vector2 position = player_states.get(idx).new_pos;
      int radius = player.getPaintRadius();
      int used_paint = canvas.paint(position, player.getPaintColor(), radius,
          board, secret_key);
      player.decreasePaintAmount(used_paint, secret_key);
    }
    canvas.sendPixmapToTexture();
  }

  // --------------------------------------------------------------- //
  private void adjustScores() {
    for (int player_idx = 0; player_idx < players.size(); ++player_idx)
      adjustScore(player_idx);
  }

  // --------------------------------------------------------------- //
  private void adjustScore(int player_idx) {
    Player player = players.get(player_idx);
    long pixels = canvas.getPaintCount()[player_idx];
    long score = pixels * 100 / (board.getPaintableArea() + 1); // max 99%
    player.setScore((int) score, secret_key);
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
    String level_file = game_settings.level_file;
    List<Item> level_items = new ArrayList<>();
    Level level = new Level(level_file, secret_key);
    level.loadLevel(level_items, game_settings);
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
