package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
/** The FenceH is an simple obstacle. The suffix H stands for horizontal */
public class FenceV extends Item {
  // ===================== PaintBooth methods ====================== //
  public FenceV() {
    super("FenceV", "fence_v.png", "fence_v_area.png", Array.of(0.40f, 0.40f),
        100);
  }
}
