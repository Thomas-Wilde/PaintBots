package com.tw.paintbots;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tw.paintbots.GameManager.SecretKey;

public abstract class Player extends Renderable {
  /** Maximum number of allowed players. */
  public static final int MAX_COUNT = 4;
  private static int id_counter = 0;
  private int player_id = -1;
  private PaintColor[] paint_colors =
      {PaintColor.GREEN, PaintColor.PURPLE, PaintColor.BLUE, PaintColor.ORANGE};

  private Vector2 pos_ = new Vector2(0.0f, 0.0f);
  // private Vector2 dir_ = new Vector2(1.0f, 0.0f);
  private int character_width_ = 64;
  private final Mesh mesh_;
  private PlayerAnimation animation_ = null;
  private AnimatedObject dir_indicator_ = null;
  private float anim_time_ = 0.0f;
  private float color_amount = 1.0f;

  // --------------------------------------------------------------- //
  Player(String name) throws PlayerException {
    super(name, 5);
    // ---
    player_id = id_counter++;
    if (player_id >= MAX_COUNT)
      throw new PlayerException("Tried to create too many players.");
    // ---
    mesh_ = new Mesh(character_width_);
    animation_ = new PlayerAnimation("chief_walk.png");
    dir_indicator_ = new AnimatedObject("dir_indicator.png", 1, 4, 0.5f);
  }

  // --------------------------------------------------------------- //
  /**
   * Get the ID of the player. It is an element in [0,3], i.e. we allow at most
   * 4 players. The ID is set at the beginning of the game and it is immutable.
   */
  public int getPlayerID() {
    return player_id;
  }

  // --------------------------------------------------------------- //
  /** Access the unique painting color of the current player. */
  public PaintColor getPaintColor() {
    return paint_colors[player_id];
  }

  // --------------------------------------------------------------- //
  public PlayerState getState() {
    PlayerState state = new PlayerState();
    state.old_pos = this.pos_.cpy();
    state.type = this.getType();
    state.dir = this.getDirection();
    state.dir.setLength(1.0f);
    return state;
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
  /** Set the position of the player. Only GameManager can call this method. */
  public void setPosition(Vector2 position, SecretKey key) {
    Objects.requireNonNull(key);
    pos_ = position.cpy();
  }

  // --------------------------------------------------------------- //
  /**
   * Get the current direction, in which the player will move in the next
   * update-step. The direction vector needs to be normalized, i.e., it has a
   * Euclidean length of 1.0. If it has not length 1.0, it will be normalized
   * anyway but this maybe modify the intented direction.
   *
   * @return A copy to the (normalized) direction vector.
   */
  abstract public Vector2 getDirection();

  // --------------------------------------------------------------- //
  /**
   * Set the direction in which the player will move in the next update step.
   * The GameManager is the only guy, that can access this method. This
   * restriction is made to avoid cheating, i.e. if someone tries to modify the
   * behavior of other players. This method is usually only used once at the
   * start of the battle to set the initial move direction.
   */
  abstract public void setDirection(Vector2 dir, SecretKey key);

  // --------------------------------------------------------------- //
  /** Get the mesh of the Player object, that is used for collision. */
  public Mesh getMesh() {
    return mesh_;
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    anim_time_ += Gdx.graphics.getDeltaTime();
    renderDirectionIndicator(batch);
    renderCharacter(batch);
  }

  // --------------------------------------------------------------- //
  private void renderCharacter(SpriteBatch batch) {
    // get different animation frames for each player
    float anim_shift = anim_time_ / 4 * player_id;
    Vector2 dir_tmp = getDirection();
    dir_tmp.setLength(1.0f);
    TextureRegion frame = animation_.getFrame(dir_tmp, anim_time_ + anim_shift);
    int offset = frame.getRegionWidth() / 2;
    float pos_x = pos_.x - offset + render_position[0];
    float pos_y = pos_.y - offset + render_position[1];
    batch.draw(frame, pos_x, pos_y);
  }

  // --------------------------------------------------------------- //
  private void renderDirectionIndicator(SpriteBatch batch) {
    TextureRegion frame = dir_indicator_.getFrame(anim_time_);
    // --- position
    int width = frame.getRegionWidth();
    int height = frame.getRegionHeight();
    float pos_x = pos_.x - width / 2.0f + render_position[0];
    float pos_y = pos_.y - height / 2.0f + render_position[1];
    // --- rotation
    Vector2 dir_tmp = getDirection();
    dir_tmp.setLength(1.0f);
    float deg = dirVectorToRotDegree(dir_tmp);
    batch.draw(frame, pos_x, pos_y, width / 2.0f, height / 2.0f, width, height,
        1.0f, 1.0f, deg);
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    animation_.destroy();
    dir_indicator_.destroy();
  }

  // --------------------------------------------------------------- //
  /**
   * Map the direction vector to a rotation degree. The direction vector needs
   * to be normalized.
   */
  protected float dirVectorToRotDegree(Vector2 dir) {
    float tmp_deg = (float) (Math.asin(dir.y) * 180.0 / Math.PI);
    // --- include 360 instead of 180 degrees
    if (dir.x < 0.0)
      tmp_deg = 180.0f - tmp_deg;
    // --- shift to positive values
    if (tmp_deg < 0.0)
      tmp_deg += 360.f;
    return tmp_deg;
  }

  // --------------------------------------------------------------- //
  public abstract PlayerType getType();

}
