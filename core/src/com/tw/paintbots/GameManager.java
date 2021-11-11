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
  private Floor floor_ = null;

  private ArrayList<Entity> entities = new ArrayList<>();
  private List<List<Renderable>> render_layers_ = null;

  // --------------------------------------------------------------- //
  public GameManager() {
    render_layers_ = new ArrayList<List<Renderable>>();
    for (int i = 0; i < 8; ++i)
      render_layers_.add(new ArrayList<Renderable>());
  }

  // =============================================================== //
  public void loadMap(GameSettings settings) throws GameMangerException {
    map_settings = settings;
    sanityCheckPlayerSettings(); // throws an exception if something is wrong
    createPlayers();
    createFloor();
  }

  // --------------------------------------------------------------- //
  /** Check the game settings for correctness. If something is wrong, throw an
   * exceptions. Things that may be wrong are:
   * - the number of players is not in [1,4]
   * - not each player gets a start position
   * - not each player gets a start direction */
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
  private void createPlayers() throws GameMangerException {

    // --- create players
    int count = map_settings.player_types.length;
    players = new Player[count];
    player_states = new PlayerState[count];
    try {
      for (int i = 0; i < count; ++i) {
        if (map_settings.player_types[i] == PlayerType.AI)
          throw new GameMangerException("No AI players implemented.");
        players[i] = new HumanPlayer("Player" + i);
        entities.add(players[i]);
        initPlayer(i);
        savePlayerState(i);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new GameMangerException("Creating players failed.");
    }
  }

  // --------------------------------------------------------------- //
  /** Read the properties of player idx from the current game settings. */
  private void initPlayer(int idx) {
    players[idx].setPosition(map_settings.start_positions[idx]);
    try {
      players[idx].setDirection(map_settings.start_directions[idx]);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // --------------------------------------------------------------- //
  /** Write the current state of the player idx into the player_state array.
   * This is done to prevent cheatinge attempts, i.e. only the GameManager can
   * change the state of the player.
   */
  private void savePlayerState(int idx) {
    player_states[idx] = players[idx].getState();
  }

  // --------------------------------------------------------------- //
  /** Add a renderable item to the render layers. Layers with a lower index
  *  are rendered before layers with a higher index.
  */
  private void addRenderableToLayer(Renderable item, int layer_idx) {
    List layer = render_layers_.get(layer_idx);
    layer.add(item);
  }

  // --------------------------------------------------------------- //
  private void createFloor() {
    String floor_texture = map_settings.floor_texture;
    int width = map_settings.board_dimensions[0];
    int height = map_settings.board_dimensions[1];
    floor_ = new Floor(floor_texture, width, height);
    addRenderableToLayer(floor_, floor_.getRenderLayer());
  }

  // --------------------------------------------------------------- //
  public void update() {
    preUpdate();
    // update all entities
    for (Entity entity : entities)
      entity.update();
    // ---
    moveAllPlayers();
  }

  // --------------------------------------------------------------- //
  private void preUpdate() {
    for (int idx = 0; idx < players.length; ++idx)
      savePlayerState(idx);
  }

  // --------------------------------------------------------------- //
  /** Calls 'movePlayer(int idx)' for each player */
  private void moveAllPlayers() {
    for (int idx = 0; idx < players.length; ++idx)
      movePlayer(idx);
  }

  // --------------------------------------------------------------- //
  /** Performs a move step in the current move direction if possible. The
   *  translation vector depends on the move speed. The movement is clamped at
   *  the borders of the board. */
  private void movePlayer(int idx) {
    Vector2 old_pos = player_states[idx].pos;
    Player player = players[idx];
    Vector2 move_dir = player.getDirection();
    double player_radius = player.getMesh().getDimensions()[0];
    // ---
    Vector2 new_pos = old_pos.cpy();
    new_pos.add(move_dir.scl(200.0f * Gdx.graphics.getDeltaTime()));
    clampPositionToBoard(new_pos, player_radius * 2.0);
    // ---
    player.setPosition(new_pos);
  }

  // --------------------------------------------------------------- //
  /** Checks if the given position is inside of the game board. If not, fix
   * the corresponding coordinate.*/
  private void clampPositionToBoard(Vector2 pos, double offset) {
    pos.x = Math.max(pos.x, 0);
    pos.x = Math.min(pos.x, map_settings.board_dimensions[0] - (float) offset);
    pos.y = Math.max(pos.y, 0);
    pos.y = Math.min(pos.y, map_settings.board_dimensions[1] - (float) offset);
  }

  // --------------------------------------------------------------- //
  /** calls 'clampPosition(pos, 0.0f);' */
  private void clampPositionToBoard(Vector2 pos) {
    clampPositionToBoard(pos, 0.0);
  }

  // --------------------------------------------------------------- //
  public void render(SpriteBatch batch) {
    // --- render all layers
    for (List<Renderable> layer_items : render_layers_)
      for (Renderable renderable : layer_items)
        renderable.render(batch);
  }
}
