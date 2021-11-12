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
  private short[] canvas_ = null;
  private Pixmap pixmap_ = null;
  private Texture texture_ = null;
  private int[] dimension_ = new int[2];

  // --------------------------------------------------------------- //
  Canvas(int width, int height) {
    super("canvas");
    dimension_[0] = width;
    dimension_[1] = height;
    canvas_ = new short[width * height];
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
    int x = (int) position.x;
    int y = (int) position.y;
    for (int i = -radius; i < radius; ++i)
      for (int j = -radius; j < radius; ++j)
        if ((i * i + j * j) <= (radius * radius))
          pixmap_.drawPixel(x + i, dimension_[1] - y + j);
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
