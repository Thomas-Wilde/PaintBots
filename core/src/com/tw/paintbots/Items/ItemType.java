package com.tw.paintbots.Items;

public enum ItemType {
  //@formatter:off
  NONE(0), OBSTACLE(1), BLOCKED(2),
  REFILL(10), REFILL_GREEN(11), REFILL_PURPLE(12), REFILL_BLUE(13), REFILL_ORANGE(14);
  //@formatter:on
  private final int type_id;

  // --------------------------------------------------------------- //
  /**
   * constructor
   *
   * @param type_id The type defines what the user can do at the location of
   *        this item
   */
  private ItemType(int type_id) {
    this.type_id = type_id;
  }

  // --------------------------------------------------------------- //
  /** @return The integer that refers to the unique id of this ItemType. */
  public int getTypeID() {
    return type_id;
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  /**
   * Some items prevent the player from painting at a location. With this method
   * you can check if the location is paintable.
   * @return
   * - true if the player can paint at this location
   * - false otherwise
   */
  //@formatter:on
  public boolean isPaintable() {
    //@formatter:off
    switch (this) {
      case NONE:     return true;
      case OBSTACLE: return true;
      case REFILL:   return true;
      default:       return false;
    }
    //@formatter:on
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  /**
   * Some items prevent the player from walking across this location. With this
   * method you can check if the location is passable.
   * @return
   * - true if the player can walk at this location
   * - false otherwise
   */
  //@formatter:on
  public boolean isPassable() {
    //@formatter:off
    switch (this) {
      case OBSTACLE: return false;
      case BLOCKED:  return false;
      default:       return true;
    }
    //@formatter:on
  }
}
