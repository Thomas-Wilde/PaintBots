package com.tw.paintbots.Renderables;

import java.util.Comparator;

public class RenderDepthComparator implements Comparator<Renderable> {
  @Override
  public int compare(Renderable r1, Renderable r2) {
    // ---
    if (r1 == null && r2 == null)
      return 0;
    if (r1 == null)
      return 1;
    if (r2 == null)
      return -1;
    // ---
    int depth1 = r1.getOcclusionDepth() + r1.getRenderPosition()[1];
    int depth2 = r2.getOcclusionDepth() + r2.getRenderPosition()[1];
    return depth2 - depth1;
  }
}
