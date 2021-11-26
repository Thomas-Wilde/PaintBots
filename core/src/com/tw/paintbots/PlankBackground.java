package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlankBackground extends Entity implements Renderable {
  private Texture texture = null;
  private TextureRegion tex_region = null;
  private int[] render_offset = {0, 0};
  private static final int tex_repeat = 6;
  private int tex_width = 0;
  private int tex_height = 0;
  private float scale_x = 1.0f;
  private float scale_y = 1.0f;

  public PlankBackground(int[] render_resolution) {
    super("plank_background");
    texture = new Texture(Gdx.files.internal("plank_background.png"));
    texture.setWrap(Texture.TextureWrap.MirroredRepeat,
        Texture.TextureWrap.Repeat);
    // ---
    tex_width = texture.getWidth() * tex_repeat;
    tex_height = texture.getHeight() * tex_repeat;
    tex_region = new TextureRegion(texture);
    tex_region.setRegion(0, 0, tex_width, tex_height);
    // ---
    scale_x = render_resolution[0] / (float) tex_width;
    scale_y = render_resolution[1] / (float) tex_height;
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    render(batch, render_offset);
  }

  @Override
  public void render(SpriteBatch batch, int[] position) {
    batch.draw(tex_region, 0, 0, position[0], position[1], tex_width,
        tex_height, scale_x, scale_y, 0.0f);
  }

  // --------------------------------------------------------------- //
  @Override
  public int getRenderLayer() {
    return 0;
  }

  // --------------------------------------------------------------- //
  @Override
  public void setRenderOffset(int[] offset) {
    render_offset = offset;
  }

  // --------------------------------------------------------------- //
  @Override
  public int[] getRenderOffset() {
    return render_offset;
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    texture.dispose();

  }

  // --------------------------------------------------------------- //
  @Override
  public void update() {}
}
