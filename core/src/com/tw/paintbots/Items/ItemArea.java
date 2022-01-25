package com.tw.paintbots.Items;

// =============================================================== //
public class ItemArea {
  // --------------------------------------------------------------- //
  private final int width;
  private final int height;
  private final int size;
  private int[] origin;

  private ItemType[] area = null;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  public int[] getOrigin() { return origin.clone(); }
  public void setOrigin(int[] origin) { this.origin = origin.clone(); }
  //@formatter:on

  // ====================== ItemArea methods ====================== //
  public ItemArea(int width, int height) {
    this.width = width;
    this.height = height;
    this.size = width * height;
    // ---
    initArea();
  }

  // --------------------------------------------------------------- //
  private void initArea() {
    area = new ItemType[size];
    for (int i = 0; i < size; ++i)
      area[i] = ItemType.NONE;
  }

  // --------------------------------------------------------------- //
  public ItemType getType(int x, int y) {
    return area[x + width * y];
  }

  // --------------------------------------------------------------- //
  public void setType(int x, int y, ItemType type) {
    area[x + width * y] = type;
  }

  // --------------------------------------------------------------- //
  public void setType(int x, int y, int type_id) {
    ItemType type = ItemType.NONE;
    //@formatter:off
    switch (type_id) {
      case 1: type = ItemType.OBSTACLE; break;
      case 2: type = ItemType.REFILL; break;
      default: type = ItemType.NONE; break;
    }
    //@formatter:on
    area[x + width * y] = type;
  }
}
