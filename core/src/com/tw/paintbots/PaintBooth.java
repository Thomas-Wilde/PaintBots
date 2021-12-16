package com.tw.paintbots;

public class PaintBooth extends MultiLayerRenderable implements Item {
  private ItemType type = ItemType.REFILL;

  // ======================== PaintBooth methods ========================= //
  PaintBooth() {
    super("PaintBooth", Array.of("paint_booth_a.png", "paint_booth_b.png"),
        Array.of(6, 4));
  }

  // ======================== Item methods ========================= //
  //@formatter:off
  @Override public ItemType getType() { return type; }
  //@formatter:on

}
