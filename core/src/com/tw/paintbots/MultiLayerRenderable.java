package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// =============================================================== //
public class MultiLayerRenderable extends Renderable {
  // --------------------------------------------------------------- //
  protected final String[] texture_files;
  protected Texture[] textures;
  protected TextureRegion[] texture_regions;
  protected String[] filenames;

  // ================ MultiLayerRenderable methods ================= //
  MultiLayerRenderable(String name, String[] texture_files, int[] layers) {
    super(name, layers);
    this.texture_files = texture_files;
    loadTextures();
    computeResolution();
    initTextureRegions();
  }

  // --------------------------------------------------------------- //
  private void loadTextures() {
    int num = texture_files.length;
    textures = new Texture[num];
    // ---
    for (int i = 0; i < num; ++i)
      textures[i] = new Texture(Gdx.files.internal(texture_files[i]));
    texture = textures[0];
  }

  // --------------------------------------------------------------- //
  protected void initTextureRegions() {
    int num = texture_files.length;
    texture_regions = new TextureRegion[num];
    // ---
    int width = resolution[0];
    int height = resolution[1];
    for (int i = 0; i < num; ++i) {
      Texture texture = textures[i];
      texture_regions[i] = new TextureRegion(texture);
      texture_regions[i].setRegion(0, 0, width, height);
    }
  }

  // --------------------------------------------------------------- //
  private void selectTexture(int layer) {
    for (int i = 0; i < layers.length; ++i)
      if (layers[i] == layer) {
        texture_region = texture_regions[i];
        return;
      }
  }

  // ===================== Renderable methods ====================== //
  @Override
  public void render(SpriteBatch batch, int layer) {
    selectTexture(layer);
    render(batch);
  }

  // ======================== Entity methods ======================== //
  @Override
  public void destroy() {
    for (Texture texture : textures)
      texture.dispose();
    super.destroy();
  }
}
