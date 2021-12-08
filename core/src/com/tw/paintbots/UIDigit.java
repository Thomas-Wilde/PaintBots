package com.tw.paintbots;

// =============================================================== //
public class UIDigit extends TextureGrid {
  // --------------------------------------------------------------- //
  private PaintColor color = PaintColor.BLACK;
  private int digit_value = 0;

  // ======================= UIDigit methods ======================= //
  public UIDigit() {
    super("ui_numbers.png", 6, 11, 5);
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  public PaintColor getColor() { return color; }
  public int getDigitValue() { return digit_value; }
  //@formatter:on

  // --------------------------------------------------------------- //
  public void setColor(PaintColor color) {
    this.color = color;
    setTextureIndex(digit_value, color.getColorID());
  }

  // --------------------------------------------------------------- //
  public void setDigitValue(int value) {
    digit_value = Math.max(0, value);
    digit_value = Math.min(10, digit_value);
    setTextureIndex(digit_value, color.getColorID());
  }
}
