package com.tw.paintbots;

import java.util.Objects;

import com.tw.paintbots.GameManager.SecretLock;
import com.tw.paintbots.Items.ItemType;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;

/**
 * The Board represents the area on which the players move around, i.e. the game
 * board. It contains different areas and gives information, e.g. about
 * obstacles, paintable areas, and possible interactions. The Board has the same
 * dimensions as the Canvas. It is split into different cells, e.g. the pixels
 * that can be painted.
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
  /** @return The width of the board, i.e. the number of cells in x-direction. */
  public int getWidth() { return width; }
  /** @return The height of the board, i.e. the number of cells in y-direction. */
  public int getHeight() { return height; }
  //@formatter:on

  // ======================= Board methods ======================== //
  /**
   * Constrcutor, that also initalizes the board. The game board consists of
   * cells/pixels, that allow possible interactions at their location. The size
   * of the game board is defined at the start of the game.
   *
   * @param width number of cells/pixels in x-direction
   * @param height number of cells/pixels in y-direction
   */
  public Board(int width, int height) {
    this.width = width;
    this.height = height;
    initBoard();
  }

  // --------------------------------------------------------------- //
  /**
   * Initialzies an ItemType-array with correct size, that is filled with the
   * information about possible interactions. The possible interaction types are
   * set after the level is loaded.
   */
  private void initBoard() {
    cells = new ItemType[width * height];
    // ---
    int size = width * height;
    for (int i = 0; i < size; ++i)
      cells[i] = ItemType.NONE;
  }

  // --------------------------------------------------------------- //
  /**
   * Access the type of the board cell with the given coordinate.
   *
   * @param x coordinate of the location
   * @param y coordinate of the location
   * @return The type of interaction that can be performed at this location.
   */
  public ItemType getType(int x, int y) {
    int idx = x + width * y;
    return cells[idx];
  }

  // --------------------------------------------------------------- //
  /**
   * The paintable area depends on the items placed at the board. Some items
   * prevent the player from painting at their location.
   *
   * @return The total number of pixels/cells that can be painted.
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

  // --------------------------------------------------------------- //
  /**
   * Set the interaction type of a specific location at the game board. The type
   * defines which interactions can be performed at this location, e.g. refill
   * paint, move across, paint the location. This method is only available to
   * the GameManager.
   *
   * @param x coordinate of the location
   * @param y coordinate of the location
   * @param type which the cell should be
   * @param lock SecretLock only available to the GameManager
   */
  public void setType(int x, int y, ItemType type, SecretLock lock) {
    Objects.requireNonNull(lock);
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

  // --------------------------------------------------------------- //
  public void saveToFile(SecretLock lock) {
    Objects.requireNonNull(lock);
    Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
    pixmap.setColor(1.0f, 1.0f, 1.0f, 0.0f);

    for (int x = 0; x < width; ++x)
      for (int y = 0; y < height; ++y) {
        pixmap.setColor(typeToColor(getType(x, y)));
        pixmap.drawPixel(x, height - y);
      }
    FileHandle fh = new FileHandle("board.png");
    PixmapIO.writePNG(fh, pixmap);
    pixmap.dispose();
  }

  // --------------------------------------------------------------- //
  private Color typeToColor(ItemType type) {
    //@formatter:off
    switch (type) {
      case NONE:          return Color.DARK_GRAY;
      case OBSTACLE:      return Color.CORAL;
      case BLOCKED:       return Color.RED;
      case REFILL:        return Color.MAROON;
      case REFILL_GREEN:  return Color.GREEN;
      case REFILL_PURPLE: return Color.PURPLE;
      case REFILL_BLUE:   return Color.BLUE;
      case REFILL_ORANGE: return Color.ORANGE;
      case POLE_GREEN:    return Color.FOREST;
      case POLE_PURPLE:   return Color.PINK;
      case POLE_BLUE:     return Color.CYAN;
      case POLE_ORANGE:   return Color.FIREBRICK;
      default: return Color.BLACK;
    }
    //@formatter:on
  }
}
