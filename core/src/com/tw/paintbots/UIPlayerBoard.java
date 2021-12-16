package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// =============================================================== //
public class UIPlayerBoard extends Renderable {
  private Player player = null;
  private UIColorBar paintbar = null;
  private UIDigit[] score = null;

  // ==================== UIPlayerBoard methods ==================== //
  public UIPlayerBoard(Player player) {
    super("UIPlayerBoard", "player_board.png", Array.of(2));
    this.player = player;
    createPaintbar();
    createScoreDigits();
  }

  // --------------------------------------------------------------- //
  void createPaintbar() {
    paintbar = new UIColorBar();
    paintbar.setColor(player.getPaintColor());
  }

  // --------------------------------------------------------------- //
  void createScoreDigits() {
    // ---
    score = new UIDigit[3];
    for (int i = 0; i < 3; ++i) {
      score[i] = new UIDigit();
      score[i].setColor(player.getPaintColor());
      score[i].setDigitValue(0);
    }
    score[2].setDigitValue(12);
  }

  // --------------------------------------------------------------- //
  private void updatePaintbarPosition() {
    // colorbar position depends on the textures resolution; place the colorbar
    // at relative position [0.239, 0.236] = [150, 660]
    int h = paintbar.getRenderSize()[1];
    int[] size = getRenderSize();
    int[] pos0 = getRenderPosition();
    int pos_x = (int) (pos0[0] + size[0] * 0.239); // value depends on texture
    int pos_y = (int) (pos0[1] + size[1] * 0.236 - h);
    paintbar.setRenderPosition(Array.of(pos_x, pos_y));
  }

  // --------------------------------------------------------------- //
  private void updateScoreDigitsPosition() {
    // score position depends on the textures resolution
    int w = score[0].getRenderSize()[0];
    int[] size = getRenderSize();
    int[] pos0 = getRenderPosition();
    int pos_x0 = (int) (pos0[0] + size[0] * 0.5 - 1.3 * w);
    int pos_x1 = (int) (pos0[0] + size[0] * 0.5 - 0.3 * w);
    int pos_x2 = (int) (pos0[0] + size[0] * 0.5 + 0.5 * w);
    int pos_y = (int) (pos0[1] + size[1] * 0.5);
    score[0].setRenderPosition(Array.of(pos_x0, pos_y));
    score[1].setRenderPosition(Array.of(pos_x1, pos_y));
    score[2].setRenderPosition(Array.of(pos_x2, pos_y));
  }

  // ====================== Renderable methods ====================== //
  @Override
  public void setRenderPosition(int[] position) {
    super.setRenderPosition(position);
    updatePaintbarPosition();
    updateScoreDigitsPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void setScale(float[] scale) {
    super.setScale(scale);
    // ---
    paintbar.setScale(scale);
    updatePaintbarPosition();
    // ---
    float[] digit_scale = {scale[0] * 0.8f, scale[1] * 0.8f};
    for (UIDigit digit : score)
      digit.setScale(digit_scale);
    updateScoreDigitsPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    super.render(batch);
    if (paintbar != null)
      paintbar.render(batch);
    for (UIDigit digit : score) {
      digit.render(batch);
    }
  }

  // ======================== Entity methods ======================= //
  @Override
  public void update() {
    super.update();
    paintbar.setAmount(player.getPaintAmount());
    // ---
    int points = player.getScore();
    score[1].setDigitValue(points % 10);
    score[0].setDigitValue(points / 10);
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    paintbar.destroy();
    for (UIDigit digit : score)
      digit.destroy();
    super.destroy();
  }
}
