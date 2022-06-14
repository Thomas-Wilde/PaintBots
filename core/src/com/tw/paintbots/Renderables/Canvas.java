package com.tw.paintbots.Renderables;

import java.util.Objects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.GameManager.SecretLock;
import com.tw.paintbots.Items.ItemType;
import com.tw.paintbots.PaintColor;
import com.tw.paintbots.Board;

// =============================================================== //
/**
 * The Canvas represents the area that gets painted by the players. It has the
 * same size as the board. The paint process is performed in circles with a
 * given radius. The Canvas holds a Pixmap that is used for the painting. The
 * Pixmap gets is send to a texture that is drawn to the screen. The canvas also
 * counts how many pixels/cells are covered with each color.
 */
// =============================================================== //
public class Canvas extends Renderable {
  // --------------------------------------------------------------- //
  /** Contains the painting information. */
  private byte[] picture = null;
  private long[] paint_count = {0, 0, 0, 0};
  private Pixmap pixmap = null;
  private Texture texture = null;
  private final int width;
  private final int height;

  // ======================= Canvas methods ======================== //
  public Canvas(int width, int height) {
    super("canvas", 2);
    this.width = width;
    this.height = height;

    // ---
    initResolution();
    initPicture();
  }

  // --------------------------------------------------------------- //
  private void initPicture() {
    picture = new byte[width * height];
    for (int i = 0; i < width * height; ++i)
      picture[i] = -1;
  }

  // --------------------------------------------------------------- //
  private void createPixmap() {
    pixmap = new Pixmap(resolution[0], resolution[1], Format.RGBA8888);
    pixmap.setColor(1.0f, 1.0f, 1.0f, 0.0f);
    pixmap.fill();
    pixmap.setBlending(Blending.None);
  }

  // --------------------------------------------------------------- //
  /**
   * The canvas also refers to a texture that shows which location contains
   * which color. Thie texture is initialized in this method. This method is
   * only available to the GameManager.
   *
   * @param lock SecretLock only available to the GameManager
   */
  public void initCanvasRenderables(SecretLock lock) {
    Objects.requireNonNull(lock);
    createPixmap();
    texture = new Texture(pixmap);
    initTextureRegion();
  }

  // --------------------------------------------------------------- //
  /**
   * Paint the canvas at the given position, with the given color, if it is
   * possible to paint there. The paint is applied in circles. This method is
   * only available to the GameManager.
   *
   * @param position The coordinates of the location that you want to paint.
   * @param color The color used for the coloring process.
   * @param radius The radius of the colored circle in pixels/cells.
   * @param board The board, to check if the location is paintable.
   * @param lock SecretKey only available to the GameManager
   * @return The number of pixels that were painted. This number corresponds to
   *         the amount of paint needed for the coloring.
   */
  public int paint(Vector2 position, PaintColor color, int radius, Board board,
      SecretLock lock) {
    // ---
    Objects.requireNonNull(lock);
    // ---
    int paint = 0;
    if (pixmap != null) // null in headless mode
      pixmap.setColor(color.getColor());
    int ctr_x = (int) position.x;
    int ctr_y = (int) position.y;

    for (int i = -radius; i < radius; ++i)
      for (int j = -radius; j < radius; ++j) {
        // --- paint only the inside of the circle
        if ((i * i + j * j) >= (radius * radius))
          continue;
        // --- check if we leave the board
        int paint_x = ctr_x + i;
        int paint_y = ctr_y + j;
        if (paint_x < 0 || paint_x >= width || paint_y < 0 || paint_y >= height)
          continue;
        // --- paint only at places with no items
        ItemType cell_type = board.getType(paint_x, paint_y);
        if (!cell_type.isPaintable())
          continue;
        // ---
        updatePixmap(paint_x, height - paint_y);
        paint += updatePaintCount(paint_x, paint_y, color);
      }
    return paint;
  }

  // --------------------------------------------------------------- //
  private void updatePixmap(int x, int y) {
    if (pixmap == null) // is null in headless mode
      return;
    pixmap.drawPixel(x, y);
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  /**
   * Paint the specified pixel with the given color and count how many cells of
   * the canvas belong to which color. If the canvas is blank at this position
   * we can simply paint the pixel. If there is another color from a different
   * player, we have to use the double amount of color. If the cell already
   * belongs to the Player, no color is needed.
   *
   * @return
   * - 0 if no coloring is needed, i.e. the pixel belongs the player already
   * - 1 if the cell gets colored for the first time
   * - 2 if the cell gets recolored.
   */
  //@formatter:on
  private int updatePaintCount(int x, int y, PaintColor color) {
    int idx = x + y * width;
    int color_id = color.getColorID();
    // --- canvas is blank increase count for current player
    if (picture[idx] == -1) {
      picture[idx] = (byte) color_id;
      ++paint_count[color_id];
      return 1;
    }
    // --- canvas is not blank
    short old_color_id = picture[idx];
    picture[idx] = (byte) color_id;
    // --- do nothing if the cell belongs to the current player
    if (color_id == old_color_id)
      return 0;
    // --- switch ownership of pixel
    ++paint_count[color_id];
    --paint_count[old_color_id];
    return 2;
  }

  // --------------------------------------------------------------- //
  /**
   * The players draw into an internal pixmap. This methods converts the Pixmap
   * to a texture. This method is only available to the GameManager.
   *
   * @param lock SecretLock only available to the GameManager
   */
  public void sendPixmapToTexture(SecretLock lock) {
    Objects.requireNonNull(lock);
    texture.draw(pixmap, 0, 0);
  }

  // --------------------------------------------------------------- //
  /**
   * @return A copy of an array with the number of cells/pixels each player has
   *         painted.
   */
  public long[] getPaintCount() {
    return paint_count.clone();
  }

  // --------------------------------------------------------------- //
  /** @return The total number of pixels that the canvas contains. */
  public int getTotalArea() {
    return resolution[0] * resolution[1];
  }

  // --------------------------------------------------------------- //
  /**
   * Return the information about the current color state at the asked location.
   * If the location was not painted yet or is out of the board dimensions
   * PaintColor.NONE is returned. The canvas has the same dimension as the
   * board.
   *
   * @param x - x-coordinate of the location of interest
   * @param y - y-coordinate of the location of interest
   * @return The color at the corresponding location or PaintColor.NONE.
   */
  public PaintColor getColor(int x, int y) {
    // ---
    if (x < 0 || x >= width || y < 0 || y >= height)
      return PaintColor.NONE;
    // ---
    int idx = x + y * width;
    byte color_id = picture[idx];
    //@formatter:off
    switch (color_id) {
      case 0: return PaintColor.GREEN;
      case 1: return PaintColor.PURPLE;
      case 2: return PaintColor.BLUE;
      case 3: return PaintColor.ORANGE;
      default: break;
    }
    //@formatter:on
    return PaintColor.NONE;
  }

  // ===================== Renderable methods ===================== //
  @Override
  protected void initResolution() {
    resolution = new int[] {width, height};
  }

  // --------------------------------------------------------------- //
  @Override
  protected void initTextureRegion() {
    texture_region = new TextureRegion(texture);
    texture_region.setRegion(0, 0, width, height);
  }

  // ======================== Entity methods ======================== //
  /**
   * Free the memory for the texture. Only the GameManager can call this method.
   */
  @Override
  public void destroy(SecretLock lock) {
    Objects.requireNonNull(lock);
    texture.dispose();
    pixmap.dispose();
  }

  // --------------------------------------------------------------- //
  /** The Canvas does nothing special in the update method. */
  @Override
  public void update(SecretKey key) {
    Objects.requireNonNull(key);
  }
}
