package com.tw.paintbots;

import com.badlogic.gdx.graphics.Color;

public enum PaintColor {
  GREEN(0), PURPLE(1), BLUE(2), ORANGE(3), BLACK(4);

  private final int color_id;
  private final Color color;

  // --------------------------------------------------------------- //
  PaintColor(int color_id) {
    this.color_id = color_id;
    switch (color_id) {
      // ---
      case 0: // green
        color = new Color(0.360f, 0.560f, 0.280f, 0.750f); // [92, 143. 71]
        break;
      // ---
      case 1: // purple
        color = new Color(0.560f, 0.450f, 0.690f, 0.750f); // [143, 115, 176]
        break;
      // ---
      case 2: // blue
        color = new Color(0.400f, 0.600f, 0.810f, 0.750f); // [102, 153, 207]
        break;
      // ---
      case 3: // orange
        color = new Color(0.830f, 0.440f, 0.370f, 0.750f); // [212, 112, 94]
        break;
      // ---
      default: // black
        color = new Color(0.078f, 0.059f, 0.043f, 0.750f); // [20, 15, 11]
        break;
    }
  }

  // --------------------------------------------------------------- //
  public int getColorID() {
    return color_id;
  }

  // --------------------------------------------------------------- //
  public Color getColor() {
    return color;
  }
}
