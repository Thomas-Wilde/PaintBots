package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// =============================================================== //
public class UIDigit extends Renderable {
  // --------------------------------------------------------------- //
  private static Texture digit_texture;
  private static TextureRegion[][] digits_grid = null;
  private static boolean initialized = false;
  private PaintColor color = PaintColor.BLACK;
  private int digit_value = 0;

  // ======================== Entity methods ======================== //
  @Override
  public void destroy() {
    digit_texture.dispose();
    super.destroy();
  }

  // =============================================================== //
  public UIDigit() {
    super("digit", 6);
    // ---
    int rows = 5;
    int columns = 11;
    // ---
    if (!initialized)
      loadDigitsGrid(rows, columns);
    // ---
    resolution = new int[] {0, 0};
    resolution[0] = digit_texture.getWidth() / columns;
    resolution[1] = digit_texture.getHeight() / rows;
    texture_region = digits_grid[0][0];
  }

  // --------------------------------------------------------------- //
  private static void loadDigitsGrid(int rows, int columns) {
    digit_texture = new Texture(Gdx.files.internal("ui_numbers.png"));
    int digit_height = digit_texture.getHeight() / rows;
    int digit_width = digit_texture.getWidth() / columns;
    UIDigit.digits_grid =
        TextureRegion.split(digit_texture, digit_width, digit_height);
    UIDigit.initialized = true;
  }

  // --------------------------------------------------------------- //
  //@formatter:off
  public PaintColor getColor() { return color; }
  public int getDigitValue() { return digit_value; }
  //@formatter:on

  // --------------------------------------------------------------- //
  public void setColor(PaintColor color) {
    this.color = color;
    updateTextureRegion();
  }

  // --------------------------------------------------------------- //
  public void setDigitValue(int value) {
    digit_value = Math.max(0, value);
    digit_value = Math.min(10, digit_value);
    updateTextureRegion();
  }

  // --------------------------------------------------------------- //
  private void updateTextureRegion() {
    int color_id = getColor().getColorID();
    int value = getDigitValue();
    texture_region = digits_grid[color_id][value];
  }
}
