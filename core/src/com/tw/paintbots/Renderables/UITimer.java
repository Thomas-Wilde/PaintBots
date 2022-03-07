package com.tw.paintbots.Renderables;

import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tw.paintbots.Array;
import com.tw.paintbots.GameManager;
import com.tw.paintbots.GameManager.SecretKey;

// =============================================================== //
public class UITimer extends SimpleRenderable {
  // --------------------------------------------------------------- //
  enum Type {
    SECONDS, CLOCK;
  }

  // --------------------------------------------------------------- //
  private UIDigit[] digits = new UIDigit[4]; // use 4 digits to show the time
  private Type type = Type.CLOCK;
  private int countdown = 0;
  private int time = 0;
  private float border_offset = 0.15f; // down scaling for numbers

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  public int getTime() { return time; }
  //@formatter:on

  // ===================== UITimer constructor ===================== //
  public UITimer(int seconds) {
    super("Timer", 1, "plank_15.png");
    for (int i = 0; i < digits.length; ++i) {
      digits[i] = new UIDigit(getLayers()[0] + 1);
      digits[i].setAnker(this);
    }
    countdown = seconds;
    time = seconds;
  }

  // ======================= UITimer methods ======================= //
  private void adjustDigitPosition() {
    int offset_x = (int) (getRenderSize()[0] * border_offset);
    int digit_width = digits[0].getRenderSize()[0];
    int digit_height = digits[0].getRenderSize()[1];
    int offset_y = (getRenderSize()[1] - digit_height) / 2;
    for (int i = digits.length - 1; i >= 0; --i) {
      int pos_x = offset_x + i * digit_width;
      int pos_y = offset_y;
      digits[i].setRenderPosition(Array.of(pos_x, pos_y));
    }
  }

  // --------------------------------------------------------------- //
  private void adjustDigitSize() {
    int width = getRenderSize()[0];
    int remaining = (int) (width * (1.0 - 2.0 * border_offset));
    int digit_width = remaining / digits.length;
    float scale = ((float) digit_width / digits[0].getRenderSize()[0]);
    for (UIDigit digit : digits)
      digit.setScale(Array.of(scale, scale));
    adjustDigitPosition();
  }

  // --------------------------------------------------------------- //
  private void renderSeconds(SpriteBatch batch, int layer) {
    int showtime = time;
    for (int i = digits.length - 1; i >= 0; --i) {
      int value = showtime % 10;
      showtime /= 10;
      digits[i].setDigitValue(value);
      digits[i].render(batch, layer);
    }
  }

  // --------------------------------------------------------------- //
  private void renderClock(SpriteBatch batch, int layer) {
    int min = time / 60;
    int sec = (time - min * 60);
    digits[0].setDigitValue(min);
    digits[1].setDigitValue(10); // render ':' sign
    digits[2].setDigitValue(sec / 10);
    digits[3].setDigitValue(sec % 10);
    for (UIDigit digit : digits)
      digit.render(batch, layer);
  }

  // ====================== Renderable methods ====================== //
  @Override
  public void setRenderSize(int width, int height) {
    super.setRenderSize(width, height);
    adjustDigitSize();
  }

  // --------------------------------------------------------------- //
  @Override
  public void setRenderWidth(int width) {
    super.setRenderWidth(width);
    adjustDigitSize();
  }

  // --------------------------------------------------------------- //
  @Override
  public void setRenderHeight(int height) {
    super.setRenderHeight(height);
    adjustDigitSize();
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch, int layer) {
    // --- render the plank
    super.render(batch, layer);
    // --- render the digits
    if (type == Type.SECONDS)
      renderSeconds(batch, layer);
    else
      renderClock(batch, layer);
  }

  // ======================== Entity methods ======================= //
  @Override
  public void destroy(SecretKey key) {
    Objects.requireNonNull(key);
    for (UIDigit digit : digits)
      digit.destroy(key);
    super.destroy(key);
  }

  // --------------------------------------------------------------- //
  @Override
  public void update(SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    int elapsed_time = (int) GameManager.get().getElapsedTime();
    time = countdown - elapsed_time;
  }
}
