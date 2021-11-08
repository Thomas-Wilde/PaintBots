package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Player extends Entity {
  /** Maximum number of allowed players. */
  public static final int max_count_ = 4;
  private static int id_counter_ = 0;
  private int player_id_ = -1;

  private Vector2 pos_ = new Vector2(0.0f, 0.0f);
  private Vector2 dir_ = new Vector2(1.0f, 0.0f);
  private Mesh mesh_ = new Mesh(10.0);
  private Texture texture_ = null;

  // --------------------------------------------------------------- //
  Player(String name) throws PlayerException {
    super(name);
    player_id_ = id_counter_++;
    if (player_id_ >= max_count_)
      throw new PlayerException("Tried to create to many players.");
    // ---
    texture_ = new Texture(Gdx.files.internal("dummy_bot.png"));
  }

  // --------------------------------------------------------------- //
  /**
   * Get the current position of the player.
   *
   * @return A copy of the position vector.
   */
  public Vector2 getPosition() {
    return pos_.cpy();
  }

  // --------------------------------------------------------------- //
  /**
   * Get the current direction, in which the player will move in the next
   * update-step.
   *
   * @return A reference to the direction vector.
   */
  public Vector2 getDirection() {
    return dir_;
  }

  // --------------------------------------------------------------- //
  /**
   * Set the direction, in which the player will move in the next update-step.
   * Setting the direction vector is the main option to define the movement of
   * your player.
   */
  public void setDirection(Vector2 dir) {
    dir_ = dir;
  }

  // --------------------------------------------------------------- //
  /**
   * Get the ID of the player. It is an element in [0,3], i.e. we allow at most
   * 4 players. The ID is set at the beginning of the game and it is immutable.
   */
  public int getPlayerID() {
    return player_id_;
  }

  // --------------------------------------------------------------- //
  public void render(SpriteBatch batch) {
    batch.draw(texture_, pos_.x, pos_.y);
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    texture_.dispose();
  }

  // --------------------------------------------------------------- //
  @Override
  public abstract void update();
}
