package com.tw.paintbots.Items;

import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.Array;

// =============================================================== //
public class PaintBooth extends Item {
  /** Magic values specific for this item. */
  private int[] offset = {-10, -20};
  private int[] dim = {484, 268};

  // ===================== PaintBooth methods ====================== //
  public PaintBooth() {
    super("PaintBooth", Array.of(6, 4),
        Array.of("paint_booth_a.png", "paint_booth_b.png"), ItemType.REFILL);
  }

  // --------------------------------------------------------------- //
  @Override
  public ItemType[] getInteractionArea() {

    return null;
  }

  // --------------------------------------------------------------- //
  @Override
  public Vector2 getInteractionAreaOrigin() {
    Vector2 origin = getPosition();
    float[] scale = getScale();
    origin.x += scale[0] * offset[0];
    origin.y += scale[1] * offset[1];
    return origin;
  }

}
