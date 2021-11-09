package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class HumanPlayer extends Player {
  /** The key_map_ maps the keyboard controls to the player ID. */
  private int[][] key_map_ = {{Keys.LEFT, Keys.RIGHT}, {Keys.A, Keys.D},
      {Keys.N, Keys.M}, {Keys.NUMPAD_4, Keys.NUMPAD_6}};
  /** Defines the rotation in which the player moves. */
  private float rot_degree_ = 0.0f;

  /**
   * We distinguish 720 different move directions, that are defined by sine and
   * cosine values. We precompute a lookUp table for the directions.
   */
  private static double[][] dir_look_up_ = null;
  /** Number of different move directions we distinguish. */
  private static final int dir_look_up_count_ = 720;
  private static final double delta_dir_ = dir_look_up_count_ / 360.0;

  // --------------------------------------------------------------- //
  public HumanPlayer(String name) throws PlayerException {
    super(name);
    // ---
    if (dir_look_up_ == null)
      initDirectionArray();
  }

  // --------------------------------------------------------------- //
  public PlayerType getType() {
    return PlayerType.HUMAN;
  }

  // --------------------------------------------------------------- //
  /** Precompute the possible move directions. */
  private static void initDirectionArray() {
    dir_look_up_ = new double[dir_look_up_count_][2];
    for (int i = 0; i < dir_look_up_count_; ++i) {
      double rad = Math.toRadians((double) i * delta_dir_);
      dir_look_up_[i][0] = Math.sin(rad);
      dir_look_up_[i][1] = Math.cos(rad);
    }
  }

  // --------------------------------------------------------------- //
  /** Read the keyboard input and adapt the move direction accordingly. */
  @Override
  public void update() {
    mapKeyToRotation();
    mapRotationToDirectionVector();
  }

  // --------------------------------------------------------------- //
  /** Maps a pressed key to the player rotation. */
  private void mapKeyToRotation() {
    int id = getPlayerID();
    if (Gdx.input.isKeyPressed(key_map_[id][0]))
      rot_degree_ -= 5.0;
    if (Gdx.input.isKeyPressed(key_map_[id][1]))
      rot_degree_ += 5.0;

    // avoid negative values due to dirction look up id.
    if (rot_degree_ < 0.0) {
      rot_degree_ += 360.0;
      System.out.println("clamp");
    }
  }

  // --------------------------------------------------------------- //
  /**
   * Map the continuous rotation value to a discrete direction value from the
   * look up table.
   */
  private void mapRotationToDirectionVector() {
    int dir_id = (int) (rot_degree_ / delta_dir_) % dir_look_up_count_;
    float x = (float) dir_look_up_[dir_id][0];
    float y = (float) dir_look_up_[dir_id][1];
    super.setDirection(new Vector2(x, y));
  }
}
