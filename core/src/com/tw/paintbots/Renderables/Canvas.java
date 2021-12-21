package com.tw.paintbots.Renderables;

import java.util.Objects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.PaintColor;

// =============================================================== //
/** Canvas is the represents the area that gets painted. */
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
    createPixmap();
    texture = new Texture(pixmap);
    initTextureRegion();
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
  public void paint(Vector2 position, PaintColor color, int radius,
      SecretKey key) {
    // ---
    Objects.requireNonNull(key);
    // ---
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
        int paint_y = height - ctr_y + j;
        if (paint_x < 0 || paint_x >= width || paint_y < 0 || paint_y >= height)
          continue;
        // ---
        updatePixmap(paint_x, paint_y);
        updatePaintCount(paint_x, paint_y, color);
      }
  }

  // --------------------------------------------------------------- //
  private void updatePixmap(int x, int y) {
    pixmap.drawPixel(x, y);
  }

  // --------------------------------------------------------------- //
  /**
   * Paint the specified pixel with the given color and count how many cells of
   * the canvas belong to which color.
   */
  private void updatePaintCount(int x, int y, PaintColor color) {
    int idx = x + y * width;
    int color_id = color.getColorID();
    // --- canvas is blank increase count for current player
    if (picture[idx] == -1) {
      picture[idx] = (byte) color_id;
      ++paint_count[color_id];
      return;
    }
    // --- canvas is not blank switch ownership of pixel
    short old_color_id = picture[idx];
    picture[idx] = (byte) color_id;
    ++paint_count[color_id];
    --paint_count[old_color_id];
  }

  // --------------------------------------------------------------- //
  public void sendPixmapToTexture() {
    texture.draw(pixmap, 0, 0);
  }

  // --------------------------------------------------------------- //
  /** @return An array with the areas/pixels each player has painted. */
  public long[] getPaintCount() {
    return paint_count.clone();
  }

  // --------------------------------------------------------------- //
  /** @return Return the total number of pixels that the canvas contains. */
  public int getTotalArea() {
    return resolution[0] * resolution[1];
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
  public void destroy(SecretKey key) {
    Objects.requireNonNull(key);
    texture.dispose();
  }

  // --------------------------------------------------------------- //
  /** The Canvas does nothing special in the update method. */
  @Override
  public void update(SecretKey key) {
    Objects.requireNonNull(key);
  }
}
