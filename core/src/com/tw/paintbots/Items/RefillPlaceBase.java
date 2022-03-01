package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
public class RefillPlaceBase extends Item {
  public RefillPlaceBase() {
    super("RefillBase", "refill_base.png", "empty_area.png",
        Array.of(0.35f, 0.35f), 0);
    setLayers(Array.of(19));
  }
}
