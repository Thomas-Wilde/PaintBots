package com.tw.paintbots;

import java.util.Objects;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Items.ItemType;

/**
 * The Board represents the area on which the players move around, i.e. the game
 * board. It contains different areas and gives information, e.g. about
 * obstacles. The Board has the same dimensions as the Canvas. It is split into
 * different cells, e.g. the pixels that can be painted.
 */
// =============================================================== //
public class Board {
  // --------------------------------------------------------------- //
  private ItemType[] cells = null;
  private final int width;
  private final int height;
  private int paintable_area = -1;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the width of the board, i.e. the number of cells in x-direction. */
  public int getWidth() { return width; }
  /** Get the height of the board, i.e. the number of cells in y-direction. */
  public int getHeight() { return height; }
  //@formatter:on

  // ======================= Board methods ======================== //
  public Board(int width, int height) {
    this.width = width;
    this.height = height;
    initBoard();
  }

  // --------------------------------------------------------------- //
  private void initBoard() {
    cells = new ItemType[width * height];
    // ---
    int size = width * height;
    for (int i = 0; i < size; ++i)
      cells[i] = ItemType.NONE;
  }

  // --------------------------------------------------------------- //
  /** Access the type of the board cell with the given coordinate. */
  public ItemType getType(int x, int y) {
    int idx = x + width * y;
    return cells[idx];
  }

  // --------------------------------------------------------------- //
  /**
   * The paintable area depends on the items placed at the board. Some items
   * prevent the player from painting at their location.
   *
   * @return The total number of pixels that can be painted.
   */
  public int getPaintableArea() {
    // compute the value at the first call
    if (paintable_area < 0)
      computaPaintableArea();
    return paintable_area;
  }

  // --------------------------------------------------------------- //
  /**
   * Compute the number of paintbale pixels the board contains. This method
   * changes the value of 'paintable_area'.
   */
  private void computaPaintableArea() {
    paintable_area = 0;
    for (int x = 0; x < getWidth(); ++x)
      for (int y = 0; y < getHeight(); ++y)
        paintable_area += getType(x, y).isPaintable() ? 1 : 0;
  }
  public void setType(int x, int y, ItemType type, SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    if (type == ItemType.NONE)
      return;
    // ---
    if (x < 0 || x >= width || y < 0 || y >= height)
      return;
    // ---
    int idx = x + width * y;
    if (cells[idx] == ItemType.NONE) // do not overwrite earlier entries
      cells[idx] = type;
  }
}
