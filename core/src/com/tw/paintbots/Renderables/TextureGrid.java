package com.tw.paintbots.Renderables;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tw.paintbots.GameSettings;
import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.GameManager.SecretLock;

// =============================================================== //
public abstract class TextureGrid extends Renderable {
  // --------------------------------------------------------------- //
  private static int texture_grid_counter = 0;
  private static List<Texture> loaded_textures = new ArrayList<>();
  private static List<TextureRegion[][]> loaded_grids = new ArrayList<>();

  protected int columns = 0;
  protected int rows = 0;
  private int column_idx = 0;
  private int row_idx = 0;

  // ===================== TextureGrid methods ===================== //
  public TextureGrid(String filename, int layer, int columns, int rows) {
    super("UITextureGrid", layer);
    super.resolution = new int[] {0, 0};
    // ---
    this.columns = columns;
    this.rows = rows;
    // ---
    if (!isInitialized()) {
      setGridIndex(texture_grid_counter++);
      setInitialized();
      loadTextureGrid(filename);
    }
    // ---
    initResolution();
  }

  // --------------------------------------------------------------- //
  private void loadTextureGrid(String filename) {
    Texture texture = new Texture(Gdx.files.internal(filename));
    int region_width = texture.getWidth() / columns;
    int region_height = texture.getHeight() / rows;
    TextureRegion[][] texture_grid =
        TextureRegion.split(texture, region_width, region_height);
    // ---
    loaded_textures.add(texture);
    loaded_grids.add(texture_grid);
  }

  // --------------------------------------------------------------- //
  public void setTextureIndex(int column_idx, int row_idx) {
    this.column_idx = column_idx;
    this.row_idx = row_idx;
    updateTextureRegion();
  }

  // --------------------------------------------------------------- //
  private void updateTextureRegion() {
    int idx = getGridIndex();
    texture_region = loaded_grids.get(idx)[row_idx][column_idx];
  }

  // ====================== Renderable methods ====================== //
  @Override
  protected void initResolution() {
    int idx = getGridIndex();
    int width = loaded_textures.get(idx).getWidth() / columns;
    int height = loaded_textures.get(idx).getHeight() / rows;
    resolution = new int[] {width, height};
  }

  // --------------------------------------------------------------- //
  @Override
  protected void initTextureRegion() {
    updateTextureRegion();
  }

  // ======================== Entity methods ======================= //
  @Override
  public void destroy(SecretLock lock) {
    Objects.requireNonNull(lock);
    for (Texture texture : loaded_textures)
      texture.dispose();
  }

  // --------------------------------------------------------------- //
  /** A TextureGrid does nothing special in the update method. */
  @Override
  public void update(SecretKey key) {
    /* implemented by sub-class */
    Objects.requireNonNull(key);
  }

  // ======================== abstract methods ======================== //
  //@formatter:off
  protected abstract void setInitialized();
  protected abstract boolean isInitialized();
  protected abstract void setGridIndex(int index);
  protected abstract int getGridIndex();
  //@formatter:on
}
