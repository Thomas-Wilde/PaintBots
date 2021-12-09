package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;

public class GameSettings {
  public PlayerType[] player_types =
      {PlayerType.HUMAN, PlayerType.HUMAN, PlayerType.HUMAN, PlayerType.HUMAN};
  public Vector2[] start_positions =
      {new Vector2(250.0f, 500.0f), new Vector2(750.0f, 500.0f),
          new Vector2(500.0f, 750.0f), new Vector2(500.0f, 250.0f)};
  public Vector2[] start_directions =
      {new Vector2(-1.0f, 0.0f), new Vector2(1.0f, 0.0f),
          new Vector2(0.0f, 1.0f), new Vector2(0.0f, -1.0f),};

  public String back_texture = "plank_background.png";
  public String floor_texture = "floor_stone_medium.png";
  public int[] board_dimensions = {1000, 1000};
  public int[] board_border = {100, 100};
  public int ui_width = 600;
}
