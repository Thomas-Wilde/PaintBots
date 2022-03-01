package com.tw.paintbots.Items;

public enum ItemType {
  //@formatter:off
  NONE(0), OBSTACLE(1), BLOCKED(2),
  REFILL(10), REFILL_GREEN(11), REFILL_PURPLE(12), REFILL_BLUE(13), REFILL_ORANGE(14);
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
