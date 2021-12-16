package com.tw.paintbots;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// =============================================================== //
public abstract class TextureGrid extends Renderable {
  // --------------------------------------------------------------- //
  private static int index_counter = 0;
  private static ArrayList<Texture> loaded_textures = new ArrayList<>();
  private static ArrayList<TextureRegion[][]> loaded_grids = new ArrayList<>();

  protected int columns = 0;
  protected int rows = 0;
  private int column_idx = 0;
  private int row_idx = 0;

  //@formatter:off
  protected abstract void setInitialized();
  protected abstract boolean isInitialized();
  protected abstract void setGridIndex(int index);
  protected abstract int getGridIndex();
  //@formatter:on

  // ===================== TextureGrid methods ===================== //
  public TextureGrid(String filename, int[] layers, int columns, int rows) {
    super("UITextureGrid", layers);
    super.resolution = new int[] {0, 0};
    // ---
    this.columns = columns;
    this.rows = rows;
    // ---
    if (!isInitialized()) {
      setGridIndex(index_counter++);
      setInitialized();
      loadGrid(filename);
    }
    // ---
    computeResolution();
  }

  // --------------------------------------------------------------- //
  private void loadGrid(String filename) {
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
  protected void computeResolution() {
    int idx = getGridIndex();
    int region_width = loaded_textures.get(idx).getWidth() / columns;
    int region_height = loaded_textures.get(idx).getHeight() / rows;
    resolution[0] = region_width;
    resolution[1] = region_height;
  }

  // ======================== Entity methods ======================= //
  @Override
  public void destroy() {
    for (Texture tex : loaded_textures)
      tex.dispose();
    super.destroy();
  }
}
