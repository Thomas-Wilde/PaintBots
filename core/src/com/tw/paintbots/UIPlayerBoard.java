package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// =============================================================== //
public class UIPlayerBoard extends Renderable {
  private Player player = null;
  private UIColorBar paintbar = null;

  // ==================== UIPlayerBoard methods ==================== //
  public UIPlayerBoard(Player player) {
    super("UIPlayerBoard", "player_board.png", 2);
    this.player = player;
    createPaintbar();
  }

  // --------------------------------------------------------------- //
  void createPaintbar() {
    paintbar = new UIColorBar();
    paintbar.setColor(player.getPaintColor());
  }

  // --------------------------------------------------------------- //
  private void updatePaintbarPosition() {
    // colorbar position depends on the textures resolution; place the colorbar
    // at relative position [0.2272, 0.2361] = [150, 660]
    int h = paintbar.getRenderSize()[1];
    int[] size = getRenderSize();
    int[] pos0 = getRenderPosition();
    int pos_x = (int) (pos0[0] + size[0] * 0.239); // value depends on texture
    int pos_y = (int) (pos0[1] + size[1] * 0.236 - h);
    paintbar.setRenderPosition(new int[] {pos_x, pos_y});
  }

  // ====================== Renderable methods ====================== //
  @Override
  public void setRenderPosition(int[] position) {
    super.setRenderPosition(position);
    updatePaintbarPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void setScale(float[] scale) {
    super.setScale(scale);
    paintbar.setScale(scale);
    updatePaintbarPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    super.render(batch);
    if (paintbar != null)
      paintbar.render(batch);
  }

  // ======================== Entity methods ======================= //
  @Override
  public void update() {
    super.update();
    paintbar.setAmount(player.getPaintAmount());
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    super.destroy();
    paintbar.destroy();
  }
}
