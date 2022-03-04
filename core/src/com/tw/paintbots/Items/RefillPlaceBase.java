package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
public class RefillPlaceBase extends Item {
  public RefillPlaceBase(float[] scale) {
    super("RefillBase", "refill_base.png", "empty_area.png", scale, 0);
    setLayers(Array.of(19));
  }
}
