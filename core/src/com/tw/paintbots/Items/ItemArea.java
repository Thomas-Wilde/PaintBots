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
    if (x < 0 || x >= width || y < 0 || y >= height)
      return ItemType.NONE;
    return area[x + width * y];
  }

  // --------------------------------------------------------------- //
  public void setType(int x, int y, ItemType type) {
    if (x < 0 || x >= width || y < 0 || y >= height)
      return;
    area[x + width * y] = type;
  }

  // --------------------------------------------------------------- //
  public void setType(int x, int y, int type_id) {
    ItemType type = ItemType.NONE;
    //@formatter:off
    switch (type_id) {
      case 1:  type = ItemType.OBSTACLE; break;
      case 2:  type = ItemType.BLOCKED; break;
      case 10: type = ItemType.REFILL; break;
      case 11: type = ItemType.REFILL_GREEN; break;
      case 12: type = ItemType.REFILL_PURPLE; break;
      case 13: type = ItemType.REFILL_BLUE; break;
      case 14: type = ItemType.REFILL_ORANGE; break;
      default: type = ItemType.NONE; break;
    }
    //@formatter:on
    setType(x, y, type);
  }
}
