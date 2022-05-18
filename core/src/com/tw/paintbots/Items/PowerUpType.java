package com.tw.paintbots.Items;

// =============================================================== //
/**
 * The PowerType defines which buff the player gets.
 */
// =============================================================== //
public enum PowerUpType {
  //@formatter:off
  SPEED(0),              ///< walk speed increased by 15%
  PAINT_RADIUS(1),       ///< paint radius increased by 50%
  PAINT_AMOUNT(2),       ///< maximum paint amount increased by 20%
  REFILL_SPEED(3),       ///< the refill speed is increased by 50%
  INSTANT_REFILL(4),     ///< the paint is instantly refilled
  PAINT_EXPLOSION_I(5),  ///< you paint a big circle at your current location
  PAINT_EXPLOSION_II(6); ///< you paint 10 small circle at random locations
  //@formatter:on

  private final int type_id;

  // --------------------------------------------------------------- //
  /** @return The number of different power up types. */
  public static int getTypeCount() {
    return PAINT_EXPLOSION_II.type_id;
  }

  // --------------------------------------------------------------- //
  /**
   * Convert an index to the corresponding PowerUp.
   *
   * @return The PowerUpType that belongs to the index.
   */
  public static PowerUpType idxToType(int idx) {
    //@formatter:off
    switch (idx) {
      case 0: return SPEED;
      case 1: return PAINT_RADIUS;
      case 2: return PAINT_AMOUNT;
      case 3: return REFILL_SPEED;
      case 4: return INSTANT_REFILL;
      case 5: return PAINT_EXPLOSION_I;
      case 6: return PAINT_EXPLOSION_II;
      default: return SPEED;
    }
    //@formatter:on
  }

  // --------------------------------------------------------------- //
  public String toString() {
    //@formatter:off
    switch (type_id) {
      case 0: return "SPEED";
      case 1: return "PAINT_RADIUS";
      case 2: return "PAINT_AMOUNT";
      case 3: return "REFILL_SPEED";
      case 4: return "INSTANT_REFILL";
      case 5: return "PAINT_EXPLOSION_I";
      case 6: return "PAINT_EXPLOSION_II";
      default: return "SPEED";
    }
    //@formatter:on
  }

  // --------------------------------------------------------------- //
  /**
   * constructor
   *
   * @param type_id The PowerUpType defines which buff the player gets.
   */
  private PowerUpType(int type_id) {
    this.type_id = type_id;
  }

  // --------------------------------------------------------------- //
  /** @return The integer that refers to the unique id of this PowerUpType. */
  public int getTypeID() {
    return type_id;
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  /**
   * The player can collect some power ups to gain a permanent buff. Some
   * power ups are used at the moment the player walks over them.
   * With this method you can check if the power up is collectible.
   * @return
   * - true if the power up is collectible and gives a permanent buff
   * - false otherwise
   */
  //@formatter:on
  public boolean isCollectible() {
    //@formatter:off
    switch (this) {
      case SPEED:              return true;
      case PAINT_RADIUS:       return true;
      case PAINT_AMOUNT:       return true;
      case REFILL_SPEED:       return true;
      case INSTANT_REFILL:     return false;
      case PAINT_EXPLOSION_I:  return false;
      case PAINT_EXPLOSION_II: return false;
      default:                 return false;
    }
    //@formatter:on
  }

}
