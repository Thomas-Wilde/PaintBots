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
  private byte[] canvas = null;
  private long[] paint_count = {0, 0, 0, 0};
  private Pixmap pixmap = null;
  private Texture texture = null;
  private int[] dimension = new int[2];

  // --------------------------------------------------------------- //
  Canvas(int width, int height) {
    super("canvas");
    dimension[0] = width;
    dimension[1] = height;
    // ---
    canvas = new byte[width * height];
    for (int i = 0; i < width * height; ++i)
      canvas[i] = -1;
    // ---
    createPixmap();
    texture = new Texture(pixmap);
  }

  // --------------------------------------------------------------- //
  private void createPixmap() {
    pixmap = new Pixmap(dimension[0], dimension[0], Format.RGBA8888);
    pixmap.setColor(1.0f, 1.0f, 1.0f, 0.0f);
    pixmap.fill();
    pixmap.setBlending(Blending.None);
  }

  // --------------------------------------------------------------- //
  public void paint(Vector2 position, PaintColor color, int radius) {
    pixmap.setColor(color.getColor());
    int ctr_x = (int) position.x;
    int ctr_y = (int) position.y;
    int width = dimension[0];
    int height = dimension[1];

    for (int i = -radius; i < radius; ++i)
      for (int j = -radius; j < radius; ++j) {
        // --- paint circle inside
        if ((i * i + j * j) >= (radius * radius))
          continue;
        // --- check if we leave the board
        int paint_x = ctr_x + i;
        int paint_y = dimension[1] - ctr_y + j;
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
    int width = dimension[0];
    int idx = x + y * width;
    int color_id = color.getColorID();
    // --- canvas is blank increase count for current player
    if (canvas[idx] == -1) {
      canvas[idx] = (byte) color_id;
      ++paint_count[color_id];
      return;
    }
    // --- canvas is not blank switch ownership of pixel
    short old_color_id = canvas[idx];
    canvas[idx] = (byte) color_id;
    ++paint_count[color_id];
    --paint_count[old_color_id];
  }

  // --------------------------------------------------------------- //
  @Override
  public void update() {}

  // --------------------------------------------------------------- //
  public void sendPixmapToTexture() {
    texture.draw(pixmap, 0, 0);
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    batch.draw(texture, 0, 0);
  }

  // --------------------------------------------------------------- //
  @Override
  public int getRenderLayer() {
    return 1;
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    texture.dispose();
  }

}
