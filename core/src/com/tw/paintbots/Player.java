package com.tw.paintbots;

import java.util.Objects;

import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Renderables.DirectionIndicator;
import com.tw.paintbots.Renderables.PlayerAnimation;
import com.tw.paintbots.Renderables.Renderable;

// =============================================================== //
public abstract class Player extends Entity {
  /** Maximum number of allowed players. */
  public static final int MAX_COUNT = 4;
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

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the Renderable that draws the character. */
  public PlayerAnimation getAnimation() { return animation; }
  /** Get the Renderable that draws the direction indicator around the character. */
  public DirectionIndicator getIndicator() { return dir_indicator; }
  /** Get the ID of the player. It is an element in [0,3], i.e. we allow at most
   *  4 players. The ID is set at the beginning of the game and it is immutable. */
  public int getPlayerID() { return player_id; }
  /** Access the unique painting color of the player. */
  public PaintColor getPaintColor() { return paint_colors[player_id]; }
  /** Get the current paint amount. It reflects the number of canvas pixels that still can get painted. */
  public int getPaintAmount() { return paint_amount; }
  /** The maximum paint the player can carry. */
  public int getMaximumPaintAmount() { return max_paint_amount; }
  /** Get the current score of the player. */
  public int getScore() { return score; }
  /** The radius of a single paint stroke/circle. */
  public int getPaintRadius() { return paint_radius; }
  /** The amount of paint that is refilled per second at a paint store. */
   public int getRefillSpeed() { return refill_speed; }
  //@formatter:on

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
  public void initRenderables() {
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
  public PlayerState getState() {
    PlayerState state = new PlayerState();
    state.old_pos = this.getPosition();
    state.type = this.getType();
    state.dir = this.getDirection();
    state.dir.setLength(1.0f);
    return state;
  }

  // --------------------------------------------------------------- //
  /** Set the anker for the Renderable; this should be the floor. */
  public void setAnker(Renderable anker, SecretKey key) {
    Objects.requireNonNull(key);
    animation.setAnker(anker);
  }

  // --------------------------------------------------------------- //
  public void setMaximumPaintAmount(int amount, SecretKey key) {
    Objects.requireNonNull(key);
    max_paint_amount = Math.max(0, amount);
  }

  // --------------------------------------------------------------- //
  public void setPaintAmount(int amount, SecretKey key) {
    Objects.requireNonNull(key);
    paint_amount = Math.max(0, amount);
    paint_amount = Math.min(amount, max_paint_amount);
  }

  // --------------------------------------------------------------- //
  public void increasePaintAmount(int plus, SecretKey key) {
    Objects.requireNonNull(key);
    setPaintAmount(paint_amount + plus, key);
  }

  // --------------------------------------------------------------- //
  public void decreasePaintAmount(int minus, SecretKey key) {
    Objects.requireNonNull(key);
    setPaintAmount(paint_amount - minus, key);
  }

  // --------------------------------------------------------------- //
  public void setPaintRadius(int radius, SecretKey key) {
    Objects.requireNonNull(key);
    this.paint_radius = radius;
  }

  // --------------------------------------------------------------- //
  public void setRefillSpeed(int paint_per_second, SecretKey key) {
    Objects.requireNonNull(key);
    this.refill_speed = paint_per_second;
  }

  // --------------------------------------------------------------- //
  public void setScore(int points, SecretKey key) {
    Objects.requireNonNull(key);
    score = Math.max(0, points);
    score = Math.min(points, 100);
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

  // ====================== Entity methods ====================== //
  @Override
  public void destroy(SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    animation.destroy(key);
    dir_indicator.destroy(key);
  }

  // --------------------------------------------------------------- //
  @Override
  public void update(SecretKey key) {
    Objects.requireNonNull(key);
    if (animation == null)
      return;
    // --- update animation if graphics are loaded
    Vector2 pos = getPosition();
    animation.setRenderPosition(Array.of((int) pos.x, (int) pos.y));
    // --- add magic value to get different animation steps for each player
    float time = (float) GameManager.get().getElapsedTime() + 0.25f * player_id;
    animation.setAnimationTime(time);
    dir_indicator.setAnimationTime(time);
    // ---
    Vector2 move_dir = getDirection();
    move_dir.setLength(1.0f);
    animation.setMoveDirection(move_dir);
    animation.updateFrameTexture();
    dir_indicator.updateFrameTexture();
    // ---
    float deg = dirVectorToRotDegree(move_dir);
    dir_indicator.setRotation(deg);
  }

  // ====================== abstract methods ====================== //
  /**
   * Currently two types are supported: HUMAN and AI. A HUMAN player is
   * controlled by the keyboard. The AI is controlled by your program.
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
