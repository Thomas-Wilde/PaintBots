package com.tw.paintbots;

import com.badlogic.gdx.graphics.Color;

public enum PaintColor {
  GREEN(0), PURPLE(1), BLUE(2), ORANGE(3);

  private final int color_id_;
  private final Color color_;

  // --------------------------------------------------------------- //
  PaintColor(int color_id_) {
    this.color_id_ = color_id_;
    switch (color_id_) {
      // ---
      case 0: // green
        color_ = new Color(0.36f, 0.56f, 0.28f, 0.75f);
        break;
      // ---
      case 1: // purple
        color_ = new Color(0.56f, 0.45f, 0.69f, 0.75f);
        break;
      // ---
      case 2: // blue
        color_ = new Color(0.40f, 0.60f, 0.81f, 0.75f);
        break;
      // ---
      default:
        color_ = new Color(0.83f, 0.44f, 0.37f, 0.75f);
        break;
    }
  }

  // --------------------------------------------------------------- //
  public int getColorID() {
    return color_id_;
  }

  // --------------------------------------------------------------- //
  public Color getColor() {
    return color_;
  }
}
