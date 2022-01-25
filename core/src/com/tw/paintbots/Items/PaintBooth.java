package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
/** The PaintBooth is an obstacle with a border to refill the paint. */
public class PaintBooth extends Item {
  // ===================== PaintBooth methods ====================== //
  public PaintBooth() {
    super("PaintBooth", "paint_booth.png", "paint_booth_area.png",
        Array.of(0.65f, 0.65f), 120);
  }
}
