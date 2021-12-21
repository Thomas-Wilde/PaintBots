package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;

// =============================================================== //
public class PlayerState {
  public Vector2 old_pos = new Vector2(0.0f, 0.0f);
  public Vector2 new_pos = new Vector2(0.0f, 0.0f);
  public Vector2 dir = new Vector2(0.0f, 0.0f);
  public PlayerType type = PlayerType.NONE;
}
