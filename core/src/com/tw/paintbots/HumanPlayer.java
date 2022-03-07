package com.tw.paintbots;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.GameManager.SecretKey;

// =============================================================== //
public class HumanPlayer extends Player {
  //@formatter:off
  /** The key_map_ maps the keyboard controls to the player ID. */
  private int[][] key_map = {{Keys.LEFT,     Keys.RIGHT},      // player 0
                             {Keys.A,        Keys.D},          // player 1
                             {Keys.N,        Keys.M},          // player 2
                             {Keys.NUMPAD_4, Keys.NUMPAD_6}};  // player 3
  //@formatter:on

  /** Defines the rotation/direction in which the player moves. */
  private float rot_degree = 0.0f;
  private Vector2 dir = new Vector2(0.0f, 0.0f);

  /**
   * For the human controlled player we distinguish between 720 different move
   * directions, that are defined by sine and cosine values. We precompute a
   * lookUp table for the direction vectors.
   */
  private static final int dir_count = 720;
  private static final double[][] dir_look_up = new double[dir_count][2];
  private static final double delta_dir = dir_count / 360.0;
  private static boolean is_initialized = false;

  // ===================== HumanPlayer methods ===================== //
  public HumanPlayer(String name) throws PlayerException {
    super(name);
    // ---
    if (!is_initialized)
      initDirectionArray();
  }

  // --------------------------------------------------------------- //
  /** Precompute the possible move directions. */
  private static void initDirectionArray() {
    is_initialized = true;
    for (int i = 0; i < dir_count; ++i) {
      double rad = Math.toRadians((double) i * delta_dir);
      dir_look_up[i][0] = Math.cos(rad);
      dir_look_up[i][1] = Math.sin(rad);
    }
  }

  // --------------------------------------------------------------- //
  @Override
  public PlayerType getType() {
    return PlayerType.HUMAN;
  }

  // --------------------------------------------------------------- //
  @Override
  public void setInitialDirection(Vector2 dir, GameManager.SecretKey key) {
    Objects.requireNonNull(key);
    setDirection(dir);
  }

  // --------------------------------------------------------------- //
  /** Map the direction to the current rotation. */
  private void setDirection(Vector2 dir) {
    dir.setLength(1.0f);
    this.dir = dir;
    rot_degree = dirVectorToRotDegree(dir);
  }

  // --------------------------------------------------------------- //
  @Override
  public Vector2 getDirection() {
    return dir.cpy();
  }

  // --------------------------------------------------------------- //
  /** Read the keyboard input and adapt the move direction accordingly. */
  @Override
  public void update(SecretKey secret) {
    Objects.requireNonNull(secret);
    // ---
    mapKeyToRotation();
    // ---
    Vector2 dir_tmp = mapRotationToDirectionVector();
    try {
      setDirection(dir_tmp);
    } catch (Exception e) {
      System.out.print(e.getMessage());
    }
    super.update(secret);
  }

  // --------------------------------------------------------------- //
  /** Maps a pressed key to the player rotation. */
  private void mapKeyToRotation() {
    int id = getPlayerID();
    if (Gdx.input.isKeyPressed(key_map[id][0]))
      rot_degree += 5.0;
    if (Gdx.input.isKeyPressed(key_map[id][1]))
      rot_degree -= 5.0;

    // --- avoid negative values due to dirction look up id.
    if (rot_degree < 0.0)
      rot_degree += 360.0;
  }

  // --------------------------------------------------------------- //
  /**
   * Map the continuous rotation value to a discrete direction vector from the
   * look up table.
   */
  private Vector2 mapRotationToDirectionVector() {
    int dir_id = (int) (rot_degree / delta_dir) % dir_count;
    float x = (float) dir_look_up[dir_id][0];
    float y = (float) dir_look_up[dir_id][1];
    return new Vector2(x, y);
  }
}
