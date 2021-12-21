package com.tw.paintbots;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
  private float paint_amount = 1.0f;
  private int score = 0;

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
  /** Get the current paint amount as an value in [0.0, 1.0]. */
  public float getPaintAmount() { return paint_amount; }
  /** Get the current score of the player. */
  public int getScore() { return score; }
  //@formatter:on

  // ======================= Player methods ======================== //
  Player(String name) throws PlayerException {
    player_id = id_counter++;
    if (player_id >= MAX_COUNT)
      throw new PlayerException("Tried to create too many players.");
    // ---
    animation = new PlayerAnimation("chief_walk.png");
    dir_indicator = new DirectionIndicator();
    dir_indicator.setAnker(animation);
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
  public void setPaintAmount(float amount, SecretKey key) {
    Objects.requireNonNull(key);
    paint_amount = Math.max(0.0f, amount);
    paint_amount = Math.min(amount, 1.0f);
  }

  // --------------------------------------------------------------- //
  public void increasePaintAmount(float plus, SecretKey key) {
    Objects.requireNonNull(key);
    setPaintAmount(paint_amount + plus, key);
  }

  // --------------------------------------------------------------- //
  public void decreasePaintAmount(float minus, SecretKey key) {
    Objects.requireNonNull(key);
    setPaintAmount(paint_amount - minus, key);
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
    // ---
    int offset = (int) (0.5 * animation.getRenderSize()[0]); // width of avatar
    Vector2 pos = getPosition();
    animation.setRenderPosition(
        Array.of((int) pos.x - offset, (int) pos.y - offset));
    // ---
    float time = (float) GameManager.get().getElapsedTime() + 0.25f * player_id;
    animation.setAnimationTime(time);
    // ---
    Vector2 move_dir = getDirection();
    move_dir.setLength(1.0f);
    animation.setMoveDirection(move_dir);
    animation.updateFrameTexture();
    // ---
    float deg = dirVectorToRotDegree(move_dir);
    dir_indicator.setRotation(deg);
  }

  // ====================== abstract methods ====================== //
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
   */
  public abstract void setDirection(Vector2 dir, SecretKey key);

}
