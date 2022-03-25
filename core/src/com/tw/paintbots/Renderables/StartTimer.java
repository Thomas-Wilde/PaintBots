package com.tw.paintbots.Renderables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tw.paintbots.PaintColor;
import com.tw.paintbots.Array;

// =============================================================== //
public class StartTimer extends UIDigit {
  // --------------------------------------------------------------- //
  private float elapsed = 0.0f;
  private float countdown = 0.0f;
  private int[] initial_size = {0, 0};

  // ==================== StartCountdown methods ==================== //
  public StartTimer(float countdown) {
    super(30);
    this.countdown = countdown;
    this.initial_size = getRenderSize();
  }

  // --------------------------------------------------------------- //
  public void setElapsed(float elapsed) {
    // --- change value
    this.elapsed = elapsed;
    float remain = countdown - elapsed;
    setDigitValue((int) remain + 1);
    // --- compute scale
    float scale = 1.5f - (remain - (float) Math.floor(remain));
    setScale(Array.of(scale, scale));
    // --- define position and size
    int[] size = getRenderSize();
    int[] floor_size = anker.getRenderSize();
    int pos_x = floor_size[0] / 2 - size[0] / 2;
    int pos_y = floor_size[1] / 2 - size[1] / 2;
    setRenderPosition(Array.of(pos_x, pos_y));

    // --- change color
    //@formatter:off
    switch ((int) remain) {
      case 0: setColor(PaintColor.GREEN); break;
      case 1: setColor(PaintColor.BLUE); break;
      case 2: setColor(PaintColor.ORANGE); break;
      case 3: setColor(PaintColor.PURPLE); break;
      default: setColor(PaintColor.BLACK); break;
    }
    //@formatter:on
  }

  // --------------------------------------------------------------- //
  public boolean isActive() {
    return elapsed < countdown;
  }
}
