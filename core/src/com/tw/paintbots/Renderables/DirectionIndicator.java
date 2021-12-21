package com.tw.paintbots.Renderables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// =============================================================== //
public class DirectionIndicator extends AnimatedRenderable {
  private float degree = 0.0f;

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  public void setRotation(float degree) { this.degree = degree; }
  public float getRotation() { return this.degree; }
  //@formatter:on

  // =================== PlayerAnimation methods =================== //
  public DirectionIndicator() {
    super("DirectionIndicator", 4, "dir_indicator.png", 1, 4, 0.5f);
  }

  // ====================== Renderable methods ===================== //
  /**
   * Draw the DirectionIndicator at the player position and rotate it.
   */
  @Override
  public void render(SpriteBatch batch, int layer) {
    TextureRegion frame = texture_region;
    int width = frame.getRegionWidth();
    int height = frame.getRegionHeight();
    int fig_width = anker.getRenderSize()[0];
    int fig_height = anker.getRenderSize()[1];
    int diff_width = width - fig_width;
    int diff_height = height - fig_height;
    int[] rnd_pos = getRenderPosition();
    float x = rnd_pos[0] - diff_width / 2.0f;
    float y = rnd_pos[1] - diff_height / 2.0f;
    float org_x = width / 2.0f;
    float org_y = height / 2.0f;
    float[] scale = getScale();
    float sx = scale[0];
    float sy = scale[1];
    batch.draw(frame, x, y, org_x, org_y, width, height, sx, sy, degree);
  }
}
