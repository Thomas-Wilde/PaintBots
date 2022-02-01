package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
/**
 * The PaintGreen is an obstacle with a border to refill the green paint.
 */
public class PaintGreen extends Item {
  // ===================== PaintBooth methods ====================== //
  public PaintGreen() {
    super("PaintGreen", "paint_green.png", "paint_green_area.png",
        Array.of(0.35f, 0.35f), 120);
  }
}
