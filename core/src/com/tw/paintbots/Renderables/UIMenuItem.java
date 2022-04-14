package com.tw.paintbots.Renderables;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Array;

// =============================================================== //
public class UIMenuItem extends SimpleRenderable {
  private int item_idx = -1;
  private SimpleRenderable portrait = null;
  private SimpleRenderable sword = null;
  private static BitmapFont font = null;
  private static LabelStyle style = null;
  private static boolean init_font = true;
  private boolean has_focus = false;
  private Label label = null;
  private String item_name = "Human";
  private int item_index = 0;

  // ==================== UIPlayerBoard methods ==================== //
  public UIMenuItem(int item_idx) {
    super("UIMenuItem", 2,
        item_idx == 5 ? "menu_start.png" : "menu_select.png");
    this.item_idx = item_idx;
    // ---
    if (init_font == true) {
      init_font = false;
      has_focus = true;
      loadFont();
      createLabelStyle();
    }
    // ---
    createPortrait();
    createSword();
    createLabel();
  }

  // --------------------------------------------------------------- //
  private void loadFont() {
    FreeTypeFontGenerator generator =
        new FreeTypeFontGenerator(Gdx.files.internal("GothamBlackRegular.ttf"));
    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    parameter.size = 24;
    font = generator.generateFont(parameter); // font size 12 pixels
    generator.dispose(); // avoid memory leaks!
  }

  // --------------------------------------------------------------- //
  private void createLabelStyle() {
    style = new LabelStyle();
    style.font = font;
    style.fontColor = Color.GOLD;
  }

  // --------------------------------------------------------------- //
  private void createLabel() {
    label = new Label(item_name, style);
    int[] size = this.getRenderSize();
    label.setSize(size[0] * 0.7f, size[1] * 0.7f);
  }

  // --------------------------------------------------------------- //
  private void createPortrait() {
    if (portrait != null)
      return;
    String portrait_file = "";
    //@formatter:off
    switch (item_idx) {
      case 0:  portrait_file = "portrait_green.png"; break;
      case 1:  portrait_file = "portrait_purple.png"; break;
      case 2:  portrait_file = "portrait_blue.png"; break;
      case 3:  portrait_file = "portrait_orange.png"; break;
      case 4:  portrait_file = "map.png"; break;
      default: { item_name = ""; return; }
    }
    //@formatter:on
    portrait = new SimpleRenderable("portrait", 2, portrait_file);
    portrait.setAnker(this);
  }

  // --------------------------------------------------------------- //
  private void createSword() {
    if (sword != null)
      return;
    sword = new SimpleRenderable("menu_sword", 2, "sword_01.png");
    sword.setScale(Array.of(0.5f, 0.5f));
    sword.setAnker(this);
  }

  // --------------------------------------------------------------- //
  private void updatePortraitPosition() {
    if (portrait == null)
      return;
    // position depends on the textures resolution
    int w = portrait.getRenderSize()[0];
    int[] size = getRenderSize();
    int pos_x0 = (int) ((size[0] - w) * 0.05);
    int pos_y = (int) (size[1] * 0.2);
    portrait.setRenderPosition(Array.of(pos_x0, pos_y));
  }

  // --------------------------------------------------------------- //
  private void updateLabelPosition() {
    int[] size = getRenderSize();
    int[] pos = getRenderPosition();
    int pos_x = (int) (size[0] * 0.30) + pos[0];
    int pos_y = (int) (size[1] * 0.05) + pos[1];
    label.setPosition(pos_x, pos_y);
  }

  // --------------------------------------------------------------- //
  private void updateSwordPosition() {
    // anker is used for positioning
    int[] size = sword.getRenderSize();
    sword.setRenderPosition(
        Array.of((int) (-size[0] * 1.1), (int) (size[1] * 1.2)));
  }

  // --------------------------------------------------------------- //
  public void setItem(String name, int index, SecretKey key) {
    Objects.requireNonNull(key);
    item_name = name;
    item_index = index;
    label.setText(name);
  }

  // --------------------------------------------------------------- //
  public void setItemName(String text, SecretKey key) {
    Objects.requireNonNull(key);
    item_name = text;
    label.setText(text);
  }

  // --------------------------------------------------------------- //
  /** Get the name of the item. */
  public String getItemName(SecretKey key) {
    Objects.requireNonNull(key);
    return item_name;
  }

  // --------------------------------------------------------------- //
  /** Get the index of the bot in the list with the loaded bots. */
  public int getItemIndex(SecretKey key) {
    Objects.requireNonNull(key);
    return item_index;
  }

  // --------------------------------------------------------------- //
  public void setFocus(boolean focus) {
    this.has_focus = focus;
  }

  // ====================== Renderable methods ====================== //
  @Override
  public void setRenderPosition(int[] position) {
    super.setRenderPosition(position);
    updatePortraitPosition();
    updateLabelPosition();
    updateSwordPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void setScale(float[] scale) {
    super.setScale(scale);
    // ---
    if (portrait != null)
      portrait.setScale(Array.of(scale[0], scale[1]));
    updatePortraitPosition();
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch, int layer) {
    // ---
    super.render(batch, layer);
    if (portrait != null)
      portrait.render(batch, layer);
    if (sword != null && has_focus)
      sword.render(batch, layer);
    if (label != null)
      label.draw(batch, 1.0f);
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
    sword.destroy(key);
    super.destroy(key);
  }
}
