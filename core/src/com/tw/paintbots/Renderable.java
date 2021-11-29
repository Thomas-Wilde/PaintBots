package com.tw.paintbots;

import java.util.Arrays;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// =============================================================== //
public class Renderable extends Entity {
  protected final Texture texture;
  protected final String texture_file;
  protected TextureRegion texture_region;
  protected int layer = 0;
  protected int[] position = {0, 0};
  protected int[] repeat_xy = {1, 1};
  protected int[] resolution = null;
  protected float[] scale = {1.0f, 1.0f};

  // ======================== Entity methods ======================== //
  //@formatter:off
  @Override public void destroy() { texture.dispose(); }
  @Override public void update()  { /* implemented by sub-class */ }
  //@formatter:on

  // ====================== Renderable methods ====================== //
  Renderable(String name, String texture_file, int layer) {
    this(name, texture_file, layer, new int[] {1, 1}, null);
  }

  Renderable(String name, String texture_file, int layer, int[] repeat_xy) {
    this(name, texture_file, layer, repeat_xy, null);
  }

  Renderable(String name, String texture_file, int layer, int[] repeat_xy,
      int[] render_resolution) {
    // --- set the attributes
    super(name);
    this.texture_file = texture_file;
    this.layer = layer;
    this.repeat_xy = Arrays.copyOf(repeat_xy, 2);
    // --- load the texture
    texture = new Texture(Gdx.files.internal(texture_file));
    Texture.TextureWrap wrap_method = Texture.TextureWrap.MirroredRepeat;
    texture.setWrap(wrap_method, wrap_method);
    // --- compute the resolution
    int width = texture.getWidth() * repeat_xy[0];
    int height = texture.getHeight() * repeat_xy[1];
    resolution = new int[] {width, height};
    // --- convert the texture into a texture region
    texture_region = new TextureRegion(texture);
    texture_region.setRegion(0, 0, width, height);
    // --- adjust the scaling if necessary
    if (render_resolution == null)
      return;
    scale[0] = (float) render_resolution[0] / width;
    scale[1] = (float) render_resolution[1] / height;
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  public int   getLayer()    { return layer; }
  public int[] getPosition() { return position; }
  //@formatter:on

  // --------------------------------------------------------------- //
  public void setPosition(int[] position) {
    this.position[0] = position[0];
    this.position[1] = position[1];
  }

  // --------------------------------------------------------------- //
  public void render(SpriteBatch batch) {
    int x = position[0];
    int y = position[1];
    int width = resolution[0];
    int height = resolution[1];
    batch.draw(texture_region, 0, 0, x, y, width, height, scale[0], scale[1],
        0.0f);
  }
}
