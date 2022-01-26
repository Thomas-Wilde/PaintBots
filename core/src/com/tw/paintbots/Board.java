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
  public void setType(int x, int y, ItemType type, SecretKey key) {
    Objects.requireNonNull(key);
    if (x < 0 || x >= width || y < 0 || y >= height)
      return;
    int idx = x + width * y;
    cells[idx] = type;
  }
}
