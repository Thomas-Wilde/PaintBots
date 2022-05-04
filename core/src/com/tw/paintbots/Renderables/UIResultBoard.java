package com.tw.paintbots.Renderables;

import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Player;
import com.tw.paintbots.Array;

// =============================================================== //
public class UIResultBoard extends SimpleRenderable {
  private UIDigit[] score = null;
  private SimpleRenderable portrait = null;
  private Player player = null;

  // ==================== UIResultBoard methods ==================== //
  public UIResultBoard(Player player) {
    super("UIResultBoard", 31, "player_result.png");
    this.player = player;
    // ---
    createPortrait();
    createScoreDigits();
  }

  // --------------------------------------------------------------- //
  private void createPortrait() {
    if (portrait != null)
      return;
    String portrait_file = "";
    //@formatter:off
    switch (player.getPlayerID()) {
      case 0:  portrait_file = "portrait_green.png"; break;
      case 1:  portrait_file = "portrait_purple.png"; break;
      case 2:  portrait_file = "portrait_blue.png"; break;
      case 3:  portrait_file = "portrait_orange.png"; break;
      default: break;
    }
    //@formatter:on
    portrait = new SimpleRenderable("portrait", 31, portrait_file);
    portrait.setAnker(this);
  }

  // --------------------------------------------------------------- //
  void createScoreDigits() {
    // ---
    score = new UIDigit[3];
    for (int i = 0; i < 3; ++i) {
      score[i] = new UIDigit(31);
      score[i].setColor(player.getPaintColor());
      score[i].setAnker(this);
      score[i].setScale(Array.of(0.4f, 0.4f));
    }
    int points = player.getScore();
    score[0].setDigitValue(points / 10);
    score[1].setDigitValue(points % 10);
    score[2].setDigitValue(12); // that is the % sign
  }

  // --------------------------------------------------------------- //
  private void updatePortraitPosition() {
    if (portrait == null)
      return;
    // position depends on the textures resolution
    int w = portrait.getRenderSize()[0];
    int[] size = getRenderSize();
    int pos_x0 = (int) ((size[0] - w) * 0.5);
    int pos_y = (int) (size[1] * 0.7);
    portrait.setRenderPosition(Array.of(pos_x0, pos_y));
  }

  // --------------------------------------------------------------- //
  private void updateScoreDigitsPosition() {
    // score position depends on the textures resolution
    int w = score[0].getRenderSize()[0];
    int[] size = getRenderSize();
    int pos_x0 = (int) (size[0] * 0.5 - 1.3 * w);
    int pos_x1 = (int) (size[0] * 0.5 - 0.3 * w);
    int pos_x2 = (int) (size[0] * 0.5 + 0.5 * w);
    int pos_y = (int) (size[1] * 0.2);
    score[0].setRenderPosition(Array.of(pos_x0, pos_y));
    score[1].setRenderPosition(Array.of(pos_x1, pos_y));
    score[2].setRenderPosition(Array.of(pos_x2, pos_y));
  }

  // ====================== Renderable methods ====================== //
  @Override
  public void setRenderPosition(int[] position) {
    super.setRenderPosition(position);
    updatePortraitPosition();
    updateScoreDigitsPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void setScale(float[] scale) {
    super.setScale(scale);
    // ---
    if (portrait != null)
      portrait.setScale(Array.of(scale[0], scale[1]));
    updatePortraitPosition();
    updateScoreDigitsPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch, int layer) {
    // ---
    super.render(batch, layer);
    if (portrait != null)
      portrait.render(batch, layer);
    for (UIDigit digit : score)
      digit.render(batch, layer);
  }

  // ======================== Entity methods ======================= //
  @Override
  public void update(SecretKey key) {
    Objects.requireNonNull(key);
    super.update(key);
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy(SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    if (portrait != null)
      portrait.destroy(key);
    super.destroy(key);
  }
}
