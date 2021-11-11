package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** This class shows the bottom with some kind of cobblestone texture  */
public class Floor extends Entity implements Renderable {
  private Texture texture_ = null;
  private TextureRegion tex_region_ = null;

  // --------------------------------------------------------------- //
  Floor(String texture_file, int width, int height) {
    super("floor");
    texture_ = new Texture(Gdx.files.internal(texture_file));
    texture_.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    tex_region_ = new TextureRegion(texture_);
    tex_region_.setRegion(0, 0, width, height);
  }

  // --------------------------------------------------------------- //
  @Override
  public void update() {}

  // --------------------------------------------------------------- //
  @Override
  public void render(SpriteBatch batch) {
    batch.draw(tex_region_, 0, 0);
  }

  // --------------------------------------------------------------- //
  @Override
  public int getRenderLayer() {
    return 0;
  }

  // --------------------------------------------------------------- //
  @Override
  public void destroy() {
    texture_.dispose();
  }

}
