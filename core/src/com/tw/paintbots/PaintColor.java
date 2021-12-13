package com.tw.paintbots;

import com.badlogic.gdx.graphics.Color;

public enum PaintColor {
  GREEN(0), PURPLE(1), BLUE(2), ORANGE(3), BLACK(4);

  private final int color_id;
  private final Color color;

  //@formatter:off
  // --------------------------------------------------------------- //
  private PaintColor(int color_id) {
    this.color_id = color_id;
    switch (color_id) {
      case 0:  color = new Color(0.360f, 0.560f, 0.280f, 0.750f); break; //green  [ 92, 143,  71]
      case 1:  color = new Color(0.560f, 0.450f, 0.690f, 0.750f); break; //purple [143, 115, 176]
      case 2:  color = new Color(0.400f, 0.600f, 0.810f, 0.750f); break; //blue   [102, 153, 207]
      case 3:  color = new Color(0.830f, 0.440f, 0.370f, 0.750f); break; //orange [212, 112,  94]
      default: color = new Color(0.078f, 0.059f, 0.043f, 0.750f); break; //black  [ 20,  15,  11]
    }
  }

  // --------------------------------------------------------------- //
  public int   getColorID() { return color_id; }
  public Color getColor()   { return color.cpy(); }
  //@formatter:on
}
