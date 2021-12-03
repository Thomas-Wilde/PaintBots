package com.tw.paintbots;

// =============================================================== //
public class UITimer extends Renderable {
  // --------------------------------------------------------------- //
  enum Type {
    SECONDS, CLOCK;
  }

  // --------------------------------------------------------------- //
  private int countdown = 0;
  private UIDigit[] digits = new UIDigit[5];
  private PaintColor color = PaintColor.BLACK;
  private Type type = Type.CLOCK;

  // ======================== Entity methods ======================= //
  @Override
  public void destroy() {
    for (UIDigit digit : digits)
      digit.destroy();
    super.destroy();
  }

  // ======================= UITimer methods ======================= //
  public UITimer(int seconds) {
    super("Timer", "plank_15.png", 2);
    countdown = seconds;
  }

  // --------------------------------------------------------------- //
}
