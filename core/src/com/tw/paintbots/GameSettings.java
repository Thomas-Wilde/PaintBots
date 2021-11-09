package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;

public class GameSettings {
  public PlayerType[] player_types = {PlayerType.HUMAN, PlayerType.HUMAN};
  public Vector2[] start_positions =
      {new Vector2(250.0f, 500.0f), new Vector2(500.0f, 500.0f),};
  public Vector2[] start_directions =
      {new Vector2(-1.0f, 0.0f), new Vector2(1.0f, 0.0f),};

  public String floor_texture = "";
  public int[] floor_dimension = {1000, 1000};

}
