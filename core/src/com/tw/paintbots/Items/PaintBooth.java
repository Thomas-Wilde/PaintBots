package com.tw.paintbots.Items;

import java.util.Objects;
import com.badlogic.gdx.math.Vector2;
import com.tw.paintbots.Array;
import com.tw.paintbots.GameManager.SecretKey;

// =============================================================== //
public class PaintBooth extends Item {
  /** Magic values specific for this item. */
  private int[] area_offset = {40, 60};
  private int[] dim = {464, 260};
  private ItemArea area;

  // ===================== PaintBooth methods ====================== //
  public PaintBooth() {
    super("PaintBooth", Array.of(6, 4),
        Array.of("paint_booth_a.png", "paint_booth_b.png"), ItemType.REFILL);
    // ---
    setRenderOffset(Array.of(-dim[0] / 2, -dim[1] / 2));
    setScale(Array.of(0.57f, 0.57f));
  }

  // --------------------------------------------------------------- //
  /** The PaintBooth is an obstacle with a border to refill the paint. */
  @Override
  protected void initItemArea() {
    // ---
    float[] scale = getScale();
    int width = (int) (dim[0] * scale[0]) + 2 * area_offset[0];
    int height = (int) (dim[1] * scale[1]) + 2 * area_offset[1];
    area = new ItemArea(width, height);
    // ---
    Vector2 origin = getPosition();
    origin.x -= width / 2.0f;
    origin.y -= height / 2.0f;
    area.setOrigin(Array.of((int) origin.x, (int) origin.y));
    // ---
    for (int x = 0; x < area.getWidth(); ++x)
      for (int y = 0; y < area.getHeight(); ++y) {
        area.setType(x, y, ItemType.REFILL);
      }
    int off_x = area_offset[0];
    int off_y = area_offset[1];
    for (int x = off_x / 2; x < area.getWidth() - off_x / 2; ++x)
      for (int y = off_y / 2; y < area.getHeight() - off_y; ++y) {
        area.setType(x, y, ItemType.OBSTACLE);
      }
  }

  // ========================= Item methods ======================== //
  @Override
  public ItemArea getItemArea(SecretKey key) {
    Objects.requireNonNull(key);
    return area;
  }
}
