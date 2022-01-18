package com.tw.paintbots.Renderables;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Player;
import com.tw.paintbots.Array;

// =============================================================== //
public class UIPlayerBoard extends SimpleRenderable {
  private Player player = null;
  private UIColorBar paintbar = null;
  private UIDigit[] score = null;
  private SimpleRenderable portrait = null;

  // ==================== UIPlayerBoard methods ==================== //
  public UIPlayerBoard(Player player) {
    super("UIPlayerBoard", 2, "player_board.png");
    this.player = player;
    createPaintbar();
    createScoreDigits();
    createPortrait();
  }

  // --------------------------------------------------------------- //
  void createPaintbar() {
    paintbar = new UIColorBar(2);
    paintbar.setColor(player.getPaintColor());
    paintbar.setAnker(this);
  }

  // --------------------------------------------------------------- //
  void createScoreDigits() {
    // ---
    score = new UIDigit[3];
    for (int i = 0; i < 3; ++i) {
      score[i] = new UIDigit(2);
      score[i].setColor(player.getPaintColor());
      score[i].setDigitValue(0);
      score[i].setAnker(this);
    }
    score[2].setDigitValue(12);
  }

  // --------------------------------------------------------------- //
  void createPortrait() {
    if (portrait != null)
      return;
    String portrait_file = "";
    //@formatter:off
    switch (player.getPlayerID()) {
      case 0:  portrait_file = "portrait_green.png"; break;
      case 1:  portrait_file = "portrait_purple.png"; break;
      case 2:  portrait_file = "portrait_blue.png"; break;
      default: portrait_file = "portrait_orange.png"; break;
    }
    //@formatter:on
    portrait = new SimpleRenderable("portrait", 2, portrait_file);
    portrait.setAnker(this);
  }

  // --------------------------------------------------------------- //
  private void updatePaintbarPosition() {
    // colorbar position depends on the textures resolution; place the colorbar
    // at relative position [0.239, 0.236] = [150, 660]
    int h = paintbar.getRenderSize()[1];
    int[] size = getRenderSize();
    int pos_x = (int) (size[0] * 0.239); // value depends on texture
    int pos_y = (int) (size[1] * 0.236 - h);
    paintbar.setRenderPosition(Array.of(pos_x, pos_y));
  }

  // --------------------------------------------------------------- //
  private void updateScoreDigitsPosition() {
    // score position depends on the textures resolution
    int w = score[0].getRenderSize()[0];
    int[] size = getRenderSize();
    int pos_x0 = (int) (size[0] * 0.5 - 1.3 * w);
    int pos_x1 = (int) (size[0] * 0.5 - 0.3 * w);
    int pos_x2 = (int) (size[0] * 0.5 + 0.5 * w);
    int pos_y = (int) (size[1] * 0.5);
    score[0].setRenderPosition(Array.of(pos_x0, pos_y));
    score[1].setRenderPosition(Array.of(pos_x1, pos_y));
    score[2].setRenderPosition(Array.of(pos_x2, pos_y));
  }

  // --------------------------------------------------------------- //
  private void updatePortraitPosition() {
    // score position depends on the textures resolution
    int w = portrait.getRenderSize()[0];
    int[] size = getRenderSize();
    int pos_x0 = (int) ((size[0] - w) * 0.5);
    int pos_y = (int) (size[1] * 0.8);
    portrait.setRenderPosition(Array.of(pos_x0, pos_y));
  }

  // ====================== Renderable methods ====================== //
  @Override
  public void setRenderPosition(int[] position) {
    super.setRenderPosition(position);
    updatePaintbarPosition();
    updatePortraitPosition();
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
    portrait.setScale(Array.of(scale[0] * 1.5f, scale[1] * 1.5f));
    updatePortraitPosition();
    // ---
    float[] digit_scale = {scale[0] * 0.8f, scale[1] * 0.8f};
    for (UIDigit digit : score)
      digit.setScale(digit_scale);
    updateScoreDigitsPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch, int layer) {
    // ---
    super.render(batch, layer);
    if (paintbar != null)
      paintbar.render(batch, layer);
    if (portrait != null)
      portrait.render(batch, layer);
    for (UIDigit digit : score) {
      digit.render(batch, layer);
    }
  }

  // ======================== Entity methods ======================= //
  @Override
  public void update(SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    super.update(key);
    int paint = player.getPaintAmount();
    int max_paint = player.getMaximumPaintAmount();
    paintbar.setAmount((float) paint / max_paint);
    // ---
    int points = player.getScore();
    score[1].setDigitValue(points % 10);
    score[0].setDigitValue(points / 10);
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy(SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    paintbar.destroy(key);
    for (UIDigit digit : score)
      digit.destroy(key);
    portrait.destroy(key);
    super.destroy(key);
  }
}
