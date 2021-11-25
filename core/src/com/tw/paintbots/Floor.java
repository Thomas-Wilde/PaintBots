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
  private int[] offset = {0, 0};

  // --------------------------------------------------------------- //
  Floor(String texture_file, int width, int height) {
    this(texture_file, width, height, 0, 0);
  }

  // --------------------------------------------------------------- //
  Floor(String texture_file, int width, int height, int offset_x,
      int offset_y) {
    super("floor");
    texture = new Texture(Gdx.files.internal(texture_file));
    texture.setWrap(Texture.TextureWrap.MirroredRepeat,
        Texture.TextureWrap.MirroredRepeat);
    tex_region = new TextureRegion(texture);
    tex_region.setRegion(0, 0, width, height);
    offset = new int[] {offset_x, offset_y};
  }

  // --------------------------------------------------------------- //
  @Override
  public void update() {}

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    batch.draw(tex_region, offset[0], offset[1]);
  }

  // --------------------------------------------------------------- //
  @Override
  public int getRenderLayer() {
    return 0;
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    texture.dispose();
  }

}
