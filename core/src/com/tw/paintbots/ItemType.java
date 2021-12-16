package com.tw.paintbots;

public enum ItemType {
  NONE(0), OBSTACLE(1), REFILL(2);

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
