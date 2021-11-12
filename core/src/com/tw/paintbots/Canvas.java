package com.tw.paintbots;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/** Canvas is the represents the area that gets painted. */
public class Canvas extends Entity implements Renderable {
  /** Contains the painting information. */
  private byte[] canvas_ = null;
  private long[] paint_count_ = {0, 0, 0, 0};
  private Pixmap pixmap_ = null;
  private Texture texture_ = null;
  private int[] dimension_ = new int[2];

  // --------------------------------------------------------------- //
  Canvas(int width, int height) {
    super("canvas");
    dimension_[0] = width;
    dimension_[1] = height;
    // ---
    canvas_ = new byte[width * height];
    for (int i = 0; i < width * height; ++i)
      canvas_[i] = -1;
    // ---
    createPixmap();
    texture_ = new Texture(pixmap_);
  }

  // --------------------------------------------------------------- //
  private void createPixmap() {
    pixmap_ = new Pixmap(dimension_[0], dimension_[0], Format.RGBA8888);
    pixmap_.setColor(1.0f, 1.0f, 1.0f, 0.0f);
    pixmap_.fill();
    pixmap_.setBlending(Blending.None);
  }

  // --------------------------------------------------------------- //
  public void paint(Vector2 position, PaintColor color, int radius) {
    pixmap_.setColor(color.getColor());
    int ctr_x = (int) position.x;
    int ctr_y = (int) position.y;
    int width = dimension_[0];
    int height = dimension_[1];

    for (int i = -radius; i < radius; ++i)
      for (int j = -radius; j < radius; ++j) {
        // --- paint circle inside
        if ((i * i + j * j) >= (radius * radius))
          continue;
        // --- check if we leave the board
        int paint_x = ctr_x + i;
        int paint_y = dimension_[1] - ctr_y + j;
        if (paint_x < 0 || paint_x >= width || paint_y < 0 || paint_y >= height)
          continue;
        // ---
        updatePixmap(paint_x, paint_y);
        updatePaintCount(paint_x, paint_y, color);
      }
  }

  // --------------------------------------------------------------- //
  private void updatePixmap(int x, int y) {
    pixmap_.drawPixel(x, y);
  }

  // --------------------------------------------------------------- //
  /**
   * Paint the specified pixel with the given color and count how many cells of
   * the canvas belong to which color.
   */
  private void updatePaintCount(int x, int y, PaintColor color) {
    int width = dimension_[0];
    int idx = x + y * width;
    int color_id = color.getColorID();
    // --- canvas is blank increase count for current player
    if (canvas_[idx] == -1) {
      canvas_[idx] = (byte) color_id;
      ++paint_count_[color_id];
      return;
    }
    // --- canvas is not blank switch ownership of pixel
    short old_color_id = canvas_[idx];
    canvas_[idx] = (byte) color_id;
    ++paint_count_[color_id];
    --paint_count_[old_color_id];
  }

  // --------------------------------------------------------------- //
  @Override
  public void update() {}

  // --------------------------------------------------------------- //
  public void sendPixmapToTexture() {
    texture_.draw(pixmap_, 0, 0);
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    batch.draw(texture_, 0, 0);
  }

  // --------------------------------------------------------------- //
  @Override
  public int getRenderLayer() {
    return 1;
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    texture_.dispose();
  }

}
