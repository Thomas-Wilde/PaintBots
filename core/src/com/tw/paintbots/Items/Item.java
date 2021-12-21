package com.tw.paintbots.Items;

import com.tw.paintbots.Renderables.MultiLayerRenderable;
import com.tw.paintbots.GameManager.SecretKey;

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
  public ItemType getType() { return type; }
  //@formatter:on

  // --------------------------------------------------------------- //
  public void init() {
    initItemArea();
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  public abstract ItemArea getItemArea(SecretKey key);
  protected abstract void initItemArea();
  //@formatter:on
}
