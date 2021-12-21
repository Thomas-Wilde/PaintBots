package com.tw.paintbots.Renderables;

import com.tw.paintbots.PaintColor;

// =============================================================== //
public class UIDigit extends TextureGrid {
  // --------------------------------------------------------------- //
  private PaintColor color = PaintColor.BLACK;
  private int digit_value = 0;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  public PaintColor getColor() { return color; }
  public int getDigitValue() { return digit_value; }
  //@formatter:on

  // ======================= UIDigit methods ======================= //
  public UIDigit(int layer) {
    super("ui_numbers.png", layer, 12, 5);
  }

  // --------------------------------------------------------------- //
  public void setColor(PaintColor color) {
    this.color = color;
    setTextureIndex(digit_value, color.getColorID());
  }

  // --------------------------------------------------------------- //
  public void setDigitValue(int value) {
    digit_value = Math.max(0, value);
    digit_value = Math.min(columns - 1, digit_value);
    setTextureIndex(digit_value, color.getColorID());
  }

  // ===================== TextureGrid methods ===================== //
  //@formatter:off
  private static boolean is_initialized = false;
  private static int grid_index = -1;
  @Override protected void setInitialized() { is_initialized = true; }
  @Override protected boolean isInitialized() { return is_initialized; }
  @Override protected void setGridIndex(int index) { UIDigit.grid_index = index; }
  @Override protected int getGridIndex() { return grid_index; }
  //@formatter:on
}
