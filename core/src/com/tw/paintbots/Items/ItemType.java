package com.tw.paintbots.Items;

public enum ItemType {
  //@formatter:off
  NONE(0), OBSTACLE(1),
  REFILL(2),
  REFILL_GREEN(3), REFILL_PURPLE(4), REFILL_BLUE(5), REFILL_ORANGE(6);
  //@formatter:on
  private final int type_id;

  // --------------------------------------------------------------- //
  private ItemType(int type_id) {
    this.type_id = type_id;
  }

  // --------------------------------------------------------------- //
  public int getTypeID() {
    return type_id;
  }
}
