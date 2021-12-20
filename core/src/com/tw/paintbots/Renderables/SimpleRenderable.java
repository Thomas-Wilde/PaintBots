package com.tw.paintbots.Renderables;

import java.util.Objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tw.paintbots.GameManager.SecretKey;

/**
 * ToDo: explain SimpleRenderable
 */
// =============================================================== //
public class SimpleRenderable extends Renderable {
  // --------------------------------------------------------------- //
  private final String texture_file;
  private Texture texture;

  // ================== SimpleRenderable methods ================== //
  public SimpleRenderable(String name, int layer, String texture_file) {
    super(name, layer);
    this.texture_file = texture_file;
    // --- init graphics
    loadTexture();
    initResolution();
    initTextureRegion();
  }

  // --------------------------------------------------------------- //
  protected void loadTexture() {
    // ---
    texture = new Texture(Gdx.files.internal(texture_file));
    // ---
    Texture.TextureWrap wrap_method = Texture.TextureWrap.MirroredRepeat;
    texture.setWrap(wrap_method, wrap_method);
  }

  // --------------------------------------------------------------- //
  @Override
  protected void initResolution() {
    int width = texture.getWidth();
    int height = texture.getHeight();
    resolution = new int[] {width, height};
  }

  // --------------------------------------------------------------- //
  @Override
  protected void initTextureRegion() {
    int width = resolution[0];
    int height = resolution[1];
    texture_region = new TextureRegion(texture);
    texture_region.setRegion(0, 0, width, height);
  }

  // --------------------------------------------------------------- //
  /**
   * Define the size of the rendered image in pixels. Depending on the
   * resolution of the texture, the scale factors in x- and y-direction are
   * adjusted to achieve the desired size.
   *
   * @param width in pixels
   * @param height in pixels
   */
  public void setRenderSize(int width, int height) {
    float sx = ((float) width / resolution[0]);
    float sy = ((float) height / resolution[1]);
    setScale(new float[] {sx, sy});
  }

  // --------------------------------------------------------------- //
  /**
   * Define the width of the rendered image in pixels. Depending on the
   * resolution of the texture, the scale factor is adjusted uniformly to
   * achieve the desired size.
   *
   * @param width in pixels
   */
  public void setRenderWidth(int width) {
    float s = ((float) width / resolution[0]);
    setScale(new float[] {s, s});
  }

  // --------------------------------------------------------------- //
  /**
   * Define the height of the rendered image in pixels. Depending on the
   * resolution of the texture, the scale factor is adjusted uniformly to
   * achieve the desired size.
   *
   * @param height in pixels
   */
  public void setRenderHeight(int height) {
    float s = ((float) height / resolution[1]);
    setScale(new float[] {s, s});
  }

  // --------------------------------------------------------------- //
  /**
   * Get the render size that is computed depending on the texture resolution,
   * and the scale factor.
   *
   * @return An int[2] array with width and height in pixels.
   */
  public int[] getRenderSize() {
    float[] scale = getScale();
    int[] rnd_size = new int[2];
    rnd_size[0] = (int) (resolution[0] * scale[0]);
    rnd_size[1] = (int) (resolution[1] * scale[1]);
    return rnd_size;
  }

  // --------------------------------------------------------------- //
  /**
   * Draw the SimpleRenderable to the current image layer.
   */
  @Override
  public void render(SpriteBatch batch, int layer) {
    // --- SimpleRenderable belongs to only one layer
    if (layer != getLayers()[0])
      return;
    super.render(batch, layer);
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
  /** The SimpleRenderable does nothing special in the update method. */
  @Override
  public void update(SecretKey key) {
    /* implemented by sub-class */
    Objects.requireNonNull(key);
  }
}
