package com.tw.paintbots.Items;

import com.tw.paintbots.Player;
import com.tw.paintbots.PaintColor;

// =============================================================== //
/**
 * The ItemType defines which interactions a player can perform on a specific
 * location at the game board. \see Board, Canvas, PaintColor
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
  REFILL_ORANGE(14), ///< Only orange can refill here.
  POLE_GREEN(21),    ///< Only green can pass.
  POLE_PURPLE(22),   ///< Only purple can pass.
  POLE_BLUE(23),     ///< Only blue can pass.
  POLE_ORANGE(24);   ///< Only orange can pass.
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
      case NONE:        return true;
      case OBSTACLE:    return true;
      case REFILL:      return true;
      case POLE_GREEN:  return true;
      case POLE_PURPLE: return true;
      case POLE_BLUE:   return true;
      case POLE_ORANGE: return true;
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
  public boolean isPassable(Player player) {
    PaintColor player_color = player.getPaintColor();
    //@formatter:off
    switch (this) {
      case OBSTACLE: return false;
      case BLOCKED:  return false;
      case POLE_GREEN:  return player_color == PaintColor.GREEN;
      case POLE_PURPLE: return player_color == PaintColor.PURPLE;
      case POLE_BLUE:   return player_color == PaintColor.BLUE;
      case POLE_ORANGE: return player_color == PaintColor.ORANGE;
      default:       return true;
    }
    //@formatter:on
  }
}
