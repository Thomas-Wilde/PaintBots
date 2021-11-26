package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// --------------------------------------------------------------- //
/** This class shows the floor with some kind of cobblestone texture */
public class Floor extends Entity implements Renderable {
  private Texture texture = null;
  private TextureRegion tex_region = null;
  private int[] render_offset = {0, 0};

  // --------------------------------------------------------------- //
  public Floor(String texture_file, int width, int height) {
    super("floor");
    texture = new Texture(Gdx.files.internal(texture_file));
    texture.setWrap(Texture.TextureWrap.MirroredRepeat,
        Texture.TextureWrap.MirroredRepeat);
    tex_region = new TextureRegion(texture);
    tex_region.setRegion(0, 0, width, height);
  }

  // --------------------------------------------------------------- //
  @Override
  public void update() {}

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    render(batch, render_offset);
  }

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch, int[] position) {
    batch.draw(tex_region, position[0], position[1]);
  }

  // --------------------------------------------------------------- //
  @Override
  public int getRenderLayer() {
    return 1;
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

}
