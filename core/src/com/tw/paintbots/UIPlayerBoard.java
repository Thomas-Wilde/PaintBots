package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// =============================================================== //
public class UIPlayerBoard extends Renderable {
  private Player player = null;

  // ==================== UIPlayerBoard methods ==================== //
  public UIPlayerBoard(Player player) {
    super("UIPlayerBoard", "player_board.png", 2);
    this.player = player;
  }

  // ====================== Renderable methods ====================== //
  @Override
  public void render(SpriteBatch batch) {
    super.render(batch);
  }

  // ======================== Entity methods ======================= //
  @Override
  public void destroy() {
    super.destroy();
  }
}
