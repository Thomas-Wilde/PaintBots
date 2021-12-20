package com.tw.paintbots.Renderables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tw.paintbots.Entity;

/**
 * Renderables can be rendered to the camera view.
 */
// =============================================================== //
public abstract class Renderable extends Entity {
  // --------------------------------------------------------------- //
  private int[] layers = null;
  private int[] render_position = {0, 0};
  private float[] scale = {1.0f, 1.0f};
  private Renderable anker = null;

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
      rnd_pos[0] += anker.getRenderPosition()[0];
      rnd_pos[1] += anker.getRenderPosition()[1];
    }
    return rnd_pos;
  }

  // ======================== abstract methods ======================== //
  public abstract void render(SpriteBatch batch, int layer);
}
