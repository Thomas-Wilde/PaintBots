package com.tw.paintbots.Renderables;

/**
 * ToDo: explain RepeatedRenderable
 */
// =============================================================== //
public class RepeatedRenderable extends SimpleRenderable {
  // --------------------------------------------------------------- //
  private int[] repeat_xy = null;

  // ================== RepeatedRenderable methods ================= //
  public RepeatedRenderable(String name, int layer, String texture_file,
      int[] repeat_xy) {
    super(name, layer, texture_file);
    this.repeat_xy = repeat_xy.clone();
    // --- init graphics
    repeatResolution();
    initTextureRegion();
  }

  // --------------------------------------------------------------- //
  private void repeatResolution() {
    int width = resolution[0] * repeat_xy[0];
    int height = resolution[1] * repeat_xy[1];
    resolution = new int[] {width, height};
  }
}
