package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;
import com.tw.paintbots.LevelLoader;
import com.tw.paintbots.LevelLoader.LevelInfo;

public class GameSettings {
  public PlayerType[] player_types =
      {PlayerType.HUMAN, PlayerType.HUMAN, PlayerType.HUMAN, PlayerType.HUMAN};
  public String[] bot_names = {"---", "---", "---", "---"};
  public Vector2[] start_positions =
      {new Vector2(500.0f, 500.0f), new Vector2(500.0f, 500.0f),
          new Vector2(500.0f, 500.0f), new Vector2(500.0f, 500.0f)};
  public Vector2[] start_directions =
      {new Vector2(-1.0f, 0.0f), new Vector2(1.0f, 0.0f),
          new Vector2(0.0f, 1.0f), new Vector2(0.0f, -1.0f),};

  public String back_texture = "plank_background.png";
  public String floor_texture = "floor_stone_medium.png";
  public LevelInfo level = new LevelInfo("level.lvl", "Nothing Special", true);
  public int[] board_dimensions = {1000, 1000};
  public int[] board_border = {100, 100};
  public int ui_width = 600;
  public int[] cam_resolution =
      {(board_dimensions[0] + 2 * board_border[0] + ui_width),
          (board_dimensions[1] + 2 * board_border[1])};
  public int game_length = 150;
  public int max_paint_amount = 250000;
  public int start_paint_amount = 250000;
  public int paint_radius = 40;
  public int refill_speed = 100000;
  public int walk_speed = 200;
}
