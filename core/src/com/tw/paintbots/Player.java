package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Player extends Entity implements Renderable {
  /** Maximum number of allowed players. */
  public static final int max_count_ = 4;
  private static int id_counter_ = 0;
  private int player_id_ = -1;
  private PaintColor paint_colors_[] =
      {PaintColor.GREEN, PaintColor.PURPLE, PaintColor.BLUE, PaintColor.ORANGE};

  private Vector2 pos_ = new Vector2(0.0f, 0.0f);
  private Vector2 dir_ = new Vector2(1.0f, 0.0f);
  private int sprite_width_ = 64;
  private final Mesh mesh_;
  private PlayerAnimation animation_ = null;
  private float anim_time_ = 0.0f;

  // --------------------------------------------------------------- //
  Player(String name) throws PlayerException {
    super(name);
    // ---
    player_id_ = id_counter_++;
    if (player_id_ >= max_count_)
      throw new PlayerException("Tried to create too many players.");
    // ---
    animation_ = new PlayerAnimation("chief_walk.png");
    mesh_ = new Mesh(sprite_width_);
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
  /** Access the unique painting color of the current player. */
  public PaintColor getPainColor() {
    return paint_colors_[player_id_];
  }

  // --------------------------------------------------------------- //
  public PlayerState getState() {
    PlayerState state = new PlayerState();
    state.old_pos = this.pos_.cpy();
    state.dir = this.dir_.cpy();
    state.type = this.getType();
    return state;
  }

  // --------------------------------------------------------------- //
  /**
   * Get the current position of the player. \return A copy of the position
   * vector.
   */
  public Vector2 getPosition() {
    return pos_.cpy();
  }

  // --------------------------------------------------------------- //
  public void setPosition(Vector2 position) {
    pos_ = position.cpy();
  }

  // --------------------------------------------------------------- //
  /**
   * Get the current direction, in which the player will move in the next
   * update-step.
   *
   * @return A copy to the direction vector.
   */
  public Vector2 getDirection() {
    return dir_.cpy();
  }

  // --------------------------------------------------------------- //
  /**
   * Set the direction, in which the player will move in the next update-step.
   * The direction vector needs to be normalized, i.e. it has an euclidean
   * length of 1.0. Setting the direction vector is the main option to define
   * the movement of your player. Throws an PlayerException if the 'dir' does
   * not have lenght 1.0.
   */
  public void setDirection(Vector2 dir) throws PlayerException {
    if (Math.abs(dir.len() - 1.0) > 1.0e-5)
      throw (new PlayerException("Length of direction vector must be 1.0."));
    dir_ = dir;
  }

  // --------------------------------------------------------------- //
  /** Get the mesh of the Player object, that is used for collision. */
  public Mesh getMesh() {
    return mesh_;
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    anim_time_ += Gdx.graphics.getDeltaTime();
    renderCharacter(batch);
  }

  // --------------------------------------------------------------- //
  private void renderCharacter(SpriteBatch batch) {
    TextureRegion frame = animation_.getFrame(getDirection(), anim_time_);
    int offset = frame.getRegionWidth() / 2;
    float pos_x = pos_.x - offset;
    float pos_y = pos_.y - offset;
    batch.draw(frame, pos_x, pos_y);
  }

  // --------------------------------------------------------------- //
  @Override
  public int getRenderLayer() {
    return 2;
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    animation_.destroy();
  }

  // --------------------------------------------------------------- //
  /**
   * Map the direction vector to a rotation degree. The direction vector needs
   * to be normalized.
   */
  protected float mapDirectionVectorToRotation(Vector2 dir) {
    float tmp_deg = (float) (Math.asin(dir.x) * 180.0 / Math.PI);
    if (dir.y < 0.0)
      tmp_deg = 360.0f - tmp_deg;
    return tmp_deg;
  }

  // --------------------------------------------------------------- //
  @Override
  public abstract void update();

  // --------------------------------------------------------------- //
  public abstract PlayerType getType();

}
