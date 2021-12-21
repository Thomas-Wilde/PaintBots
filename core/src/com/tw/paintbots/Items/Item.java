package com.tw.paintbots.Items;

import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.Renderables.MultiLayerRenderable;

public abstract class Item extends MultiLayerRenderable {
  private ItemType type = ItemType.NONE;

  // ======================== Item methods ========================= //
  protected Item(String name, int[] layers, String[] texture_files,
      ItemType type) {
    super(name, layers, texture_files);
    this.type = type;
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  public ItemType getType()              { return type; }
  public void     setType(ItemType type) { this.type = type; }
  //@formatter:on

  // --------------------------------------------------------------- //
  //@formatter:off
  public abstract ItemType[] getInteractionArea();
  public abstract Vector2 getInteractionAreaOrigin();
  //@formatter:on
}
