package com.tw.paintbots;

import java.util.Objects;

import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Renderables.DirectionIndicator;
import com.tw.paintbots.Renderables.PlayerAnimation;
import com.tw.paintbots.Renderables.Renderable;

// =============================================================== //
public abstract class Player extends Entity {
  public static final int MAX_COUNT = 4; /// < The game allows at most 4 Players
  private static int id_counter = 0;
  //@formatter:off
  private static PaintColor[] paint_colors = {PaintColor.GREEN, PaintColor.PURPLE,
                                              PaintColor.BLUE,  PaintColor.ORANGE};
  //@formatter:on
  private int player_id = -1;
  private PlayerAnimation animation = null;
  private DirectionIndicator dir_indicator = null;
  private int paint_amount = 250000;
  private int max_paint_amount = 250000;
  private int score = 0;
  private int paint_radius = 40;
  private int refill_speed = 50000;
  private Vector2 old_position = new Vector2(-1.0f, -1.0f);

  // ======================== Getter/Setter ======================== //

  // --------------------------------------------------------------- //
  /**
   * Get the Renderable that draws the character. This method is only available
   * to the GameManager. *
   *
   * @param key The SecretKey owned by the GameManager.
   * @return The PlayerAnimation with the updated texture.
   */
  public PlayerAnimation getAnimation(SecretKey key) {
    Objects.requireNonNull(key);
    return animation;
  }

  // --------------------------------------------------------------- //
  /**
   * Get the Renderable that draws the direction indicator around the character.
   * This method is only available to the GameManager. *
   *
   * @param key The SecretKey owned by the GameManager.
   * @return A reference to the DirectionInidicator of this player.
   */
  public DirectionIndicator getIndicator(SecretKey key) {
    Objects.requireNonNull(key);
    return dir_indicator;
  }

  // --------------------------------------------------------------- //
  /**
   * Get the ID of the player. It is an element in [0,3], i.e. we allow at most
   * 4 players. The ID is set at the beginning of the game and it is immutable.
   *
   * @return The ID of player.
   */
  public int getPlayerID() {
    return player_id;
  }

  // --------------------------------------------------------------- //
  /**
   * @return The unique painting color of the player.
   * @see PaintColor
   */
  public PaintColor getPaintColor() {
    return paint_colors[player_id];
  }

  // --------------------------------------------------------------- //
  /** @return The number of canvas pixels that still can get painted. */
  public int getPaintAmount() {
    return paint_amount;
  }

  // --------------------------------------------------------------- //
  /** @return The maximum paint the player can carry. */
  public int getMaximumPaintAmount() {
    return max_paint_amount;
  }

  // --------------------------------------------------------------- //
  /** @return The current score of the player as a value in [0, 99]. */
  public int getScore() {
    return score;
  }

  // --------------------------------------------------------------- //
  /** @return The radius of a single paint stroke/circle. */
  public int getPaintRadius() {
    return paint_radius;
  }

  // --------------------------------------------------------------- //
  /** @return The amount of paint refilled per second at a paint store. */
  public int getRefillSpeed() {
    return refill_speed;
  }

  // ======================= Player methods ======================== //
  /** The standard constructor is only called during bot load process. */
  protected Player() {}

  // --------------------------------------------------------------- //
  protected Player(String name) throws PlayerException {
    player_id = id_counter++;
    if (player_id >= MAX_COUNT)
      throw new PlayerException("Tried to create too many players.");
  }

  // --------------------------------------------------------------- //
  /**
   * Load and initialize the animation and the direction indicator. This method
   * is only availbale to the GameManager.
   *
   * @param key The SecretKey that is only available to the GameManager.
   */
  public void initRenderables(SecretKey key) {
    Objects.requireNonNull(key);
    loadAnimation();
    dir_indicator = new DirectionIndicator();
    dir_indicator.setAnker(animation);
  }

  // --------------------------------------------------------------- //
  private void loadAnimation() {
    String animation_file = "";
    //@formatter:off
    switch (player_id) {
      case 0:  animation_file = "walk_green.png"; break;
      case 1:  animation_file = "walk_purple.png"; break;
      case 2:  animation_file = "walk_blue.png"; break;
      default: animation_file = "walk_orange.png"; break;
    }
    //@formatter:on
    animation = new PlayerAnimation(animation_file);
  }

  // --------------------------------------------------------------- //
  /**
   * @return An instance of PlayerState that collects all information about the
   *         player.
   * @see PlayerState
   */
  public PlayerState getState() {
    PlayerState state = new PlayerState();
    state.player_id = this.player_id;
    state.paint_color = this.getPaintColor();
    state.type = this.getType();

    state.old_pos = this.getPosition();
    state.new_pos = this.getPosition();
    state.dir = this.getDirection().cpy();

    state.score = this.getScore();
    state.paint_radius = this.getPaintRadius();
    state.paint_amount = this.getPaintAmount();
    state.max_paint_amount = this.getMaximumPaintAmount();
    state.refill_speed = this.getRefillSpeed();

    return state;
  }

  // --------------------------------------------------------------- //
  /**
   * Set the anker for the Renderable; this should be the floor. This method is
   * only availbale to the GameManager.
   *
   * @param anker - The Renderable used for relative positioning.
   * @key The SecretKey that is only available to the GameManager.
   */
  public void setAnker(Renderable anker, SecretKey key) {
    Objects.requireNonNull(key);
    animation.setAnker(anker);
  }

  // --------------------------------------------------------------- //
  /**
   * This method is only availbale to the GameManager.
   *
   * @param amount - The maximum paint the player can hold.
   * @key The SecretKey that is only available to the GameManager.
   */
  public void setMaximumPaintAmount(int amount, SecretKey key) {
    Objects.requireNonNull(key);
    max_paint_amount = Math.max(0, amount);
  }

  // --------------------------------------------------------------- //
  /**
   * This method is only availbale to the GameManager.
   *
   * @param amount - The current paint the player holds.
   * @key The SecretKey that is only available to the GameManager.
   */
  public void setPaintAmount(int amount, SecretKey key) {
    Objects.requireNonNull(key);
    paint_amount = Math.max(0, amount);
    paint_amount = Math.min(amount, max_paint_amount);
  }

  // --------------------------------------------------------------- //
  /**
   * This method is only availbale to the GameManager.
   *
   * @param plus - Increase the paint amount of the player by this value.
   * @key The SecretKey that is only available to the GameManager.
   */
  public void increasePaintAmount(int plus, SecretKey key) {
    Objects.requireNonNull(key);
    setPaintAmount(paint_amount + plus, key);
  }

  // --------------------------------------------------------------- //
  /**
   * This method is only availbale to the GameManager.
   *
   * @param minus - Decrease the paint amount of the player by this value.
   * @key The SecretKey that is only available to the GameManager.
   */
  public void decreasePaintAmount(int minus, SecretKey key) {
    Objects.requireNonNull(key);
    setPaintAmount(paint_amount - minus, key);
  }

  // --------------------------------------------------------------- //
  /**
   * This method is only availbale to the GameManager.
   *
   * @param radius - The radius of this players paint circle.
   * @key The SecretKey that is only available to the GameManager.
   */
  public void setPaintRadius(int radius, SecretKey key) {
    Objects.requireNonNull(key);
    this.paint_radius = radius;
  }

  // --------------------------------------------------------------- //
  /**
   * This method is only availbale to the GameManager.
   *
   * @param paint_per_second - The paint amount this player refills in one
   *        second if he is next to a paint store of its color.
   * @key The SecretKey that is only available to the GameManager.
   */
  public void setRefillSpeed(int paint_per_second, SecretKey key) {
    Objects.requireNonNull(key);
    this.refill_speed = paint_per_second;
  }

  // --------------------------------------------------------------- //
  /**
   * This method is only availbale to the GameManager.
   *
   * @param points - Set the score of this player (cut to [0,100])
   * @key The SecretKey that is only available to the GameManager.
   */
  public void setScore(int points, SecretKey key) {
    Objects.requireNonNull(key);
    score = Math.max(0, points);
    score = Math.min(points, 100);
  }

  // --------------------------------------------------------------- //
  /**
   * Map the direction vector to a rotation degree. The direction vector needs
   * to be normalized. This method is used to turn the direction indicator and
   * the players avatar.
   *
   * @param dir - The normalized direction vector
   * @return The angle of the corresponding rotation in [0,360]
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

  // ====================== Entity methods ====================== //
  @Override
  public boolean setPosition(Vector2 position, SecretKey key) {
    old_position = this.getPosition();
    return super.setPosition(position, key);
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy(SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    if (animation != null)
      animation.destroy(key);
    if (dir_indicator != null)
      dir_indicator.destroy(key);
  }

  // --------------------------------------------------------------- //
  /**
   * The update routine for the Player object. In the update step the player
   * image is moved to its current position, the animation is continued and the
   * direction indicator is updated. This method is only available to the
   * GameManager.
   *
   * @param key SecretKey only available to the GameManager
   */
  @Override
  public void update(SecretKey key) {
    Objects.requireNonNull(key);
    if (animation == null)
      return;
    // --- update animation if graphics are loaded
    Vector2 pos = getPosition();
    animation.setRenderPosition(Array.of((int) pos.x, (int) pos.y));
    //
    // --- scale the animation time by walk distance
    // compute how far we have walked ...
    Vector2 temp = old_position;
    temp.sub(getPosition());
    // ... use the walk distance to scale the animation speed
    float dist = (float) temp.len();
    float old_time = animation.getAnimationTime();
    float delta_time = (float) GameManager.get().getDeltaTime();
    float new_time = old_time + (delta_time) * dist / 3.0f;
    animation.setAnimationTime(new_time);
    animation.updateFrameTexture();
    //
    // --- direction indicator just blinks
    float time = (float) GameManager.get().getElapsedTime();
    dir_indicator.setAnimationTime(time);
    dir_indicator.updateFrameTexture();
    //
    // --- rotate the player and the direction indicator
    Vector2 move_dir = getDirection();
    move_dir.setLength(1.0f);
    animation.setMoveDirection(move_dir);
    // ---
    float deg = dirVectorToRotDegree(move_dir);
    dir_indicator.setRotation(deg);
  }

  // ====================== abstract methods ====================== //
  /**
   * Currently three types are supported: HUMAN, AI and NONE. A HUMAN player is
   * controlled by the keyboard. The AI is controlled by your program. NONE is
   * the type for inactive players - these are created by the GameManager but
   * they do nothing and area also not rendered.
   *
   * @return The type of the Player [Human, AI, NONE]
   * @see PlayerType
   */
  public abstract PlayerType getType();

  // --------------------------------------------------------------- //
  /**
   * Get the current direction, in which the player will move in the next
   * update-step. The direction vector needs to be normalized, i.e., it has a
   * Euclidean length of 1.0. If it has not length 1.0, it will be normalized
   * anyway but this maybe modify the intented direction.
   *
   * @return A copy to the (normalized) direction vector.
   */
  public abstract Vector2 getDirection();

  // --------------------------------------------------------------- //
  /**
   * Set the direction in which the player will move in the next update step.
   * The GameManager is the only guy, that can access this method. This
   * restriction is made to avoid cheating, i.e. if someone tries to modify the
   * behavior of other players. This method is usually only used once at the
   * start of the battle to set the initial move direction.
   *
   * @param dir The (normalized) direction in which the player is oriented.
   * @param key A secret key only the GameManager can access.
   */
  public abstract void setInitialDirection(Vector2 dir, SecretKey key);
}
