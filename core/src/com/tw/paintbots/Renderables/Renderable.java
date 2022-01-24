package com.tw.paintbots.Renderables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tw.paintbots.Entity;

/**
 * Renderables can be rendered to the camera view.
 */
// =============================================================== //
public abstract class Renderable extends Entity {
  // --------------------------------------------------------------- //
  private int[] render_position = {0, 0};
  private int[] render_offset = {0, 0};
  private int render_depth = -1;
  private float[] scale = {1.0f, 1.0f};
  private int[] layers = null;
  protected Renderable anker = null;
  protected TextureRegion texture_region = null;
  protected int[] resolution = null;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  /** Get the layers in which this Renderable is rendered. */
  public int[] getLayers() { return layers.clone(); }
  /** Set the render position in camera/pixel coordinates - lower left corner. */
  public void setRenderPosition(int[] position) { this.render_position = position.clone(); }
  /** Set the scale factor that is used for rendering. */
  public void setScale(float[] scale) { this.scale = scale.clone(); }
  /** return A copy of the array that contains the scaling. */
  public float[] getScale() { return scale.clone(); }
  /** Set the render offset in camera/pixel coordinates. */
  public void setRenderOffset(int[] offset) { this.render_offset = offset.clone(); }
  /** Get the render offset in camera/pixel coordinates. */
  public int[] getRenderOffset() { return this.render_offset.clone(); }
  // ToDo: comment
  public void setRenderDepth(int depth) { render_depth = depth; }
  // ToDo: comment
  public int getRenderDepth() { return render_depth; }
  //@formatter:on

  // ====================== Renderable methods ====================== //
  protected Renderable(String name, int layer) {
    super(name);
    this.layers = new int[] {layer};
  }

  // --------------------------------------------------------------- //
  protected Renderable(String name, int[] layers) {
    super(name);
    this.layers = layers.clone();
  }

  // --------------------------------------------------------------- //
  /**
   * Set the anker object of this Renderable. The Renderable is then a child of
   * the anker, i.e. if the anker is moved, the Renderable is also moved.
   */
  public void setAnker(Renderable anker) {
    this.anker = anker;
  }

  // --------------------------------------------------------------- //
  /** Set the anker object to null. */
  public void clearAnker() {
    this.anker = null;
  }

  // --------------------------------------------------------------- //
  /**
   * Get the (hierachical) render position of the Renderable. If it has an anker
   * the position of the anker serves as the origin of this object.
   */
  public int[] getRenderPosition() {
    int[] rnd_pos = render_position.clone();
    if (anker != null) {
      rnd_pos[0] += anker.getRenderPosition()[0] + render_offset[0] * scale[0];
      rnd_pos[1] += anker.getRenderPosition()[1] + render_offset[1] * scale[1];
    }
    return rnd_pos;
  }

  // --------------------------------------------------------------- //
  /**
   * Get the render size that is computed depending on the texture resolution,
   * and the scale factor.
   *
   * @return An int[2] array with width and height in pixels.
   */
  public int[] getRenderSize() {
    int[] rnd_size = new int[2];
    rnd_size[0] = (int) (resolution[0] * scale[0]);
    rnd_size[1] = (int) (resolution[1] * scale[1]);
    return rnd_size;
  }

  // --------------------------------------------------------------- //
  /**
   * Draw the Renderable at/with its current position/scale/resolution.
   */
  public void render(SpriteBatch batch, int layer) {
    int[] rnd_pos = getRenderPosition();
    int x = rnd_pos[0];
    int y = rnd_pos[1];
    float sx = scale[0];
    float sy = scale[1];
    int width = resolution[0];
    int height = resolution[1];
    batch.draw(texture_region, x, y, 0, 0, width, height, sx, sy, 0.0f);
  }

  // ======================== abstract methods ======================== //
  protected abstract void initTextureRegion();

  protected abstract void initResolution();
}
