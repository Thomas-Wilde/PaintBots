package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameManager {
  private GameSettings map_settings = null;
  private Player[] players = null;
  private PlayerState[] player_states = null;
  private Renderable background = null;
  private Renderable floor = null;
  private Canvas canvas_ = null;
  private int[] cam_resolution = {0, 0};

  private ArrayList<Entity> entities = new ArrayList<>();
  private List<List<Renderable>> render_layers_ = null;

  // --------------------------------------------------------------- //
  public GameManager() {
    render_layers_ = new ArrayList<List<Renderable>>();
    for (int i = 0; i < 8; ++i)
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
    background = new Renderable("background", background_texture, 0, repeat_xy,
        cam_resolution);
    addRenderableToLayer(background, background.getLayer());
    entities.add(background);
  }

  // --------------------------------------------------------------- //
  private void createFloor() {
    String floor_texture = map_settings.floor_texture;
    floor = new Renderable("floor", floor_texture, 1);
    int[] pos = map_settings.board_border;
    floor.setRenderPosition(pos);
    addRenderableToLayer(floor, floor.getLayer());
    entities.add(floor);
  }

  // --------------------------------------------------------------- //
  /**
   * Add a renderable item to the render layers. Layers with a lower index are
   * rendered before layers with a higher index.
   */
  private void addRenderableToLayer(Renderable item, int layer_idx) {
    List layer = render_layers_.get(layer_idx);
    layer.add(item);
  }

  // --------------------------------------------------------------- //
  private void createPlayers() throws GameMangerException {
    // // --- create players
    // int count = map_settings.player_types.length;
    // players = new Player[count];
    // player_states = new PlayerState[count];
    // // ---
    // for (int i = 0; i < count; ++i) {
    // if (map_settings.player_types[i] == PlayerType.AI)
    // throw new GameMangerException("No AI players implemented.");
    // try {
    // players[i] = new HumanPlayer("Player" + i);
    // entities.add(players[i]);
    // addRenderableToLayer(players[i], players[i].getRenderLayer());
    // initPlayer(i);
    // savePlayerState(i);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }
    // }
  }

  // --------------------------------------------------------------- //
  /**
   * Transfer player properties from the game settings to the player with the
   * given index .
   */
  private void initPlayer(int idx) throws PlayerException {
    // Player player = players[idx];
    // Vector2 pos = map_settings.start_positions[idx];
    // Vector2 dir = map_settings.start_directions[idx];
    // int[] offset = map_settings.board_border;

    // player.setPosition(pos);
    // player.setDirection(dir);
    // player.setRenderOffset(offset);
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
    // int width = map_settings.board_dimensions[0];
    // int height = map_settings.board_dimensions[1];
    // int[] offset = map_settings.board_border;
    // canvas_ = new Canvas(width, height);
    // canvas_.setRenderOffset(offset);
    // addRenderableToLayer(canvas_, canvas_.getRenderLayer());
    // entities.add(canvas_);
  }

  // --------------------------------------------------------------- //
  public void update() {
    preUpdate();
    // update all entities
    for (Entity entity : entities)
      entity.update();
    // ---
    moveAllPlayers();
    paintOnCanvas();
  }

  // --------------------------------------------------------------- //
  private void preUpdate() {
    // for (int idx = 0; idx < players.length; ++idx)
    // savePlayerState(idx);
  }

  // --------------------------------------------------------------- //
  /** Calls 'movePlayer(int idx)' for each player */
  private void moveAllPlayers() {
    // for (int idx = 0; idx < players.length; ++idx)
    // movePlayer(idx);
  }

  // --------------------------------------------------------------- //
  /**
   * Performs a move step in the current move direction if possible. The
   * translation vector depends on the move speed. The movement is clamped at
   * the borders of the board.
   */
  private void movePlayer(int idx) {
    // Vector2 old_pos = player_states[idx].old_pos;
    // Player player = players[idx];
    // Vector2 move_dir = player.getDirection();
    // // ---
    // Vector2 new_pos = old_pos.cpy();
    // new_pos.add(move_dir.scl(200.0f * Gdx.graphics.getDeltaTime()));
    // clampPositionToBoard(new_pos);
    // // ---
    // player.setPosition(new_pos);
    // player_states[idx].new_pos = new_pos;
  }

  // --------------------------------------------------------------- //
  /**
   * Checks if the given position is inside of the game board. If not, change
   * the corresponding coordinates to be insider.
   */
  private void clampPositionToBoard(Vector2 pos, double offset) {
    // int board_width = map_settings.board_dimensions[0];
    // int board_height = map_settings.board_dimensions[1];
    // pos.x = Math.max(pos.x, 0);
    // pos.x = Math.min(pos.x, board_width - (float) offset);
    // pos.y = Math.max(pos.y, 0);
    // pos.y = Math.min(pos.y, board_height - (float) offset);
  }

  // --------------------------------------------------------------- //
  /** calls 'clampPosition(pos, 0.0f);' */
  private void clampPositionToBoard(Vector2 pos) {
    // clampPositionToBoard(pos, 0.0);
  }

  // --------------------------------------------------------------- //
  private void paintOnCanvas() {
    // for (int idx = 0; idx < players.length; ++idx) {
    // Player player = players[idx];
    // Vector2 position = player_states[idx].new_pos;
    // canvas_.paint(position, player.getPainColor(), 40);
    // }
    // canvas_.sendPixmapToTexture();
  }

  // --------------------------------------------------------------- //
  public void render(SpriteBatch batch) {
    // --- render all layers
    for (List<Renderable> layer_items : render_layers_)
      for (Renderable renderable : layer_items)
        renderable.render(batch);
  }
}
