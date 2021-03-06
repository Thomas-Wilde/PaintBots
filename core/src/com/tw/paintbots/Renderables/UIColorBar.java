package com.tw.paintbots.Renderables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tw.paintbots.PaintColor;

// =============================================================== //
public class UIColorBar extends TextureGrid {
  // --------------------------------------------------------------- //
  private PaintColor color = PaintColor.BLACK;
  private float amount = 1.0f;

  // ====================== UIColorBar methods ===================== //
  public UIColorBar(int layer) {
    super("color_bars.png", layer, 1, 5);
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  public PaintColor getColor() { return color; }
  public float getAmount() { return amount; }
  //@formatter:on

  // --------------------------------------------------------------- //
  public void setColor(PaintColor color) {
    this.color = color;
    setTextureIndex(0, color.getColorID());
  }

  // --------------------------------------------------------------- //
  public void setAmount(float value) {
    amount = value;
    amount = Math.max(0.0f, amount);
    amount = Math.min(1.0f, amount);
  }

  // ===================== TextureGrid methods ===================== //
  //@formatter:off
  private static boolean is_initialized = false;
  private static int grid_index = -1;
  @Override protected void setInitialized() { is_initialized = true; }
  @Override protected boolean isInitialized() { return is_initialized; }
  @Override protected void setGridIndex(int index) { UIColorBar.grid_index = index; }
  @Override protected int getGridIndex() { return grid_index; }
  //@formatter:on

  // ====================== Renderable methods ===================== //
  @Override
  public void render(SpriteBatch batch, int layer) {
    int[] rnd_pos = getRenderPosition();
    float[] scale = getScale();
    int x = rnd_pos[0];
    int y = rnd_pos[1];
    int width = resolution[0];
    int height = resolution[1];
    float scale_x = scale[0] * amount;
    batch.draw(texture_region, x, y, 0, 0, width, height, scale_x, scale[1],
        0.0f);
  }
}
