package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;
import com.tw.paintbots.PaintColor;

// =============================================================== //
public class PlayerState {
  public int player_id = -1;
  public PaintColor paint_color = PaintColor.BLACK;
  public PlayerType type = PlayerType.NONE;

  public Vector2 pos = new Vector2(0.0f, 0.0f);
  public Vector2 dir = new Vector2(0.0f, 0.0f);

  public int score = -1;
  public int paint_radius = -1;
  public int paint_amount = -1;
  public int max_paint_amount = -1;
  public int refill_speed = -1;
  public boolean is_active = true;
}
