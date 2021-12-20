package com.tw.paintbots;

import java.util.Objects;
import com.tw.paintbots.GameManager.SecretKey;

/**
 * Basic class of entities/objects, that belong to the game. Basically
 * everything that the GameManager manages is an Entity.
 */
// =============================================================== //
public abstract class Entity {
  // --------------------------------------------------------------- //
  private static int id_counter = 0;
  private int id = -1;
  private String name = "entity";
  private int[] position = {-1, -1};
  private static int[] board_dimension = {1000, 1000};

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the descriptive name of the Entity. */
  public String getName() { return name; }
  /** Get the ID of the Entity, which us unique. */
  public int getID() { return id; }
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
    name = id + "_" + name;
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
  public boolean setPosition(int[] position, SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    if ((position[0] < 0 || position[0] >= board_dimension[0])
        || (position[1] < 0 || position[1] >= board_dimension[1]))
      return false;
    // ---
    this.position = position.clone();
    return true;
  }

  // --------------------------------------------------------------- //
  /**
   * Set the bard dimensions. The board has the origin [0, 0], the dimensions
   * define the extent and therefore the maximum allowed coordinates of Entity
   * positions. Only the GameManager can set this value.
   */
  public static void setBoardDimensions(int[] dimensions, SecretKey key) {
    Objects.requireNonNull(key);
    board_dimension = dimensions.clone();
  }

  // --------------------------------------------------------------- //
  /**
   * Returns the position of the Entity related to the origin (0,0). If the
   * position was not set [-1, -1] is returned.
   *
   * @return Copy of the int[] array that contains the current position.
   */
  public int[] getPosition() {
    return position.clone();
  }

  // --------------------------------------------------------------- //
  /**
   * Remove this entity from the board by settings its position to null, i.e.
   * [-1, -1]. Only the GameManager can call this method.
   */
  public void removeFromBoard(SecretKey key) {
    Objects.requireNonNull(key);
    position = Array.of(-1, -1);
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
