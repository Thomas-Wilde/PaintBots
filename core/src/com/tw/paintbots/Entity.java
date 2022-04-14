package com.tw.paintbots;

import java.util.Objects;
import com.badlogic.gdx.math.Vector2;
import com.tw.paintbots.GameManager.SecretKey;

/**
 * Basic class of entities/objects, that belong to the game. Basically
 * everything that the GameManager manages is an Entity.
 */
// =============================================================== //
public abstract class Entity {
  // --------------------------------------------------------------- //
  private static int id_counter = 0;
  private static int[] board_dimensions = null;
  private int id = -1;
  private String name = "entity";
  private Vector2 position = new Vector2(-1.0f, -1.0f);
  private boolean is_active = true;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the descriptive name of the Entity. */
  public String getName() { return name; }
  /** Get the unique ID of the Entity. */
  public int getID() { return id; }
  /** Get value of is_active flag. */
  public boolean isActive() { return is_active; }
  /** Set the entity in-/active. An entity is only updated, it it is active. */
  public void setActive(boolean active) { is_active = active; }

  //@formatter:on

  // ======================= Entity methods ======================= //
  /** Create an Entity with a unique ID. */
  protected Entity() {
    id = id_counter++;
  }

  // --------------------------------------------------------------- //
  /** Create an Entity with a unique ID and the given name. */
  protected Entity(String name) {
    id = id_counter++;
    this.name = id + "_" + name;
  }

  // --------------------------------------------------------------- //
  /**
   * Set the position of the Entity. The position has to be on the gaming board,
   * i.e. in the interval [x,y] = [0, width) x [0,height), were width and height
   * are the board dimensions in game coordinates. Only the GameManager can call
   * this method.
   *
   * @return false if the position is outside of the board, true otherwise.
   */
  public boolean setPosition(Vector2 position, SecretKey key) {
    // ---
    Objects.requireNonNull(key);
    if (board_dimensions == null)
      System.out.println("board dimensions not set in Entity");
    Objects.requireNonNull(board_dimensions);
    // ---
    if ((position.x < 0.0f || position.x >= board_dimensions[0])
        || (position.y < 0.0f || position.y >= board_dimensions[1]))
      return false;
    // ---
    this.position = position.cpy();
    return true;
  }

  // --------------------------------------------------------------- //
  /**
   * Set the board dimensions. The board has the origin [0, 0], the dimensions
   * define the extent and therefore the maximum allowed coordinates of Entity
   * positions. Only the GameManager can set this value.
   */
  public static void setBoardDimensions(int[] dimensions, SecretKey key) {
    Objects.requireNonNull(key);
    board_dimensions = dimensions.clone();
  }

  // --------------------------------------------------------------- //
  /**
   * Returns the position of the Entity related to the origin (0,0). If the
   * position was not set (-1.0f, -1.0f) is returned.
   *
   * @return Copy of the Vector2 that contains the current position.
   */
  public Vector2 getPosition() {
    return position.cpy();
  }

  // --------------------------------------------------------------- //
  /**
   * Remove this entity from the board by settings its position to null, i.e.
   * [-1, -1]. Only the GameManager can call this method.
   */
  public void removeFromBoard(SecretKey key) {
    Objects.requireNonNull(key);
    position = new Vector2(-1.0f, -1.0f);
  }

  // ======================= Object methods ======================= //
  @Override
  public String toString() {
    return name;
  }

  // ====================== abstract methods ====================== //
  /**
   * This method is called by the GameManager at the end of the lifetime, e.g.
   * to clean up resources. Only the GameManager can call this method.
   */
  public abstract void destroy(SecretKey key);

  // --------------------------------------------------------------- //
  /**
   * This method is called once per loop and can be used to update the state of
   * an Entity. Only the GameManager can call this method.
   */
  public abstract void update(SecretKey key);
}
