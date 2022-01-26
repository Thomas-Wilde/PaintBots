package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
/** The FenceH is an simple obstacle. The suffix H stands for horizontal */
public class FenceH extends Item {
  // ===================== PaintBooth methods ====================== //
  public FenceH() {
    super("FenceH", "fence_h.png", "fence_h_area.png", Array.of(0.40f, 0.40f),
        200);
  }
}
