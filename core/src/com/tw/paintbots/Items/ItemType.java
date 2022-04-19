package com.tw.paintbots.Items;

// =============================================================== //
/**
 * The ItemType defines which interactions a player can perform on a specific
 * location at the game board.
 *
 * \see Board, Canvas, PaintColor
 */
// =============================================================== //
public enum ItemType {
  //@formatter:off
  NONE(0),           ///< Inital type - nothing special here.
  OBSTACLE(1),       ///< You can paint but not walk here.
  BLOCKED(2),        ///< You can neither paint nor walk here.
  REFILL(10),        ///< Walk here to refill your paint.
  REFILL_GREEN(11),  ///< Only green can refill here.
  REFILL_PURPLE(12), ///< Only purple can refill here.
  REFILL_BLUE(13),   ///< Only blue can refill here.
  REFILL_ORANGE(14); ///< Only orange can refill here.
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
