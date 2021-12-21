package com.tw.paintbots.Renderables;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tw.paintbots.GameManager.SecretKey;

// =============================================================== //
public class MultiLayerRenderable extends Renderable {
  // --------------------------------------------------------------- //
  protected final String[] texture_files;
  protected Texture[] textures;
  protected TextureRegion[] texture_regions;
  protected String[] filenames;

  // ================ MultiLayerRenderable methods ================= //
  public MultiLayerRenderable(String name, int[] layers,
      String[] texture_files) {
    super(name, layers);
    this.texture_files = texture_files;
    loadTextures();
    initResolution();
    initTextureRegions();
    initTextureRegion();
  }

  // --------------------------------------------------------------- //
  private void loadTextures() {
    int num = texture_files.length;
    textures = new Texture[num];
    // ---
    for (int i = 0; i < num; ++i)
      textures[i] = new Texture(Gdx.files.internal(texture_files[i]));
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
    int[] layers = getLayers();
    for (int i = 0; i < layers.length; ++i)
      if (layers[i] == layer) {
        texture_region = texture_regions[i];
        return;
      }
  }

  // ===================== Renderable methods ===================== //
  @Override
  protected void initResolution() {
    int width = textures[0].getWidth();
    int height = textures[0].getHeight();
    resolution = new int[] {width, height};
  }

  // --------------------------------------------------------------- //
  @Override
  protected void initTextureRegion() {
    selectTexture(getLayers()[0]);
  }

  // ===================== Renderable methods ====================== //
  @Override
  public void render(SpriteBatch batch, int layer) {
    // ---
    selectTexture(layer);
    super.render(batch, layer);
  }

  // ======================== Entity methods ======================== //
  /**
   * Free the memory of the textures. Only the GameManager can call this method.
   */
  @Override
  public void destroy(SecretKey key) {
    Objects.requireNonNull(key);
    // ---
    for (Texture texture : textures)
      texture.dispose();
  }

  // --------------------------------------------------------------- //
  /** The MultiLayerRenderable does nothing special in the update method. */
  @Override
  public void update(SecretKey key) {
    /* implemented by sub-class */
    Objects.requireNonNull(key);
  }
}
