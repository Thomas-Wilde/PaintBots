package com.tw.paintbots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// =============================================================== //
public class TextureGrid extends Renderable {
  // --------------------------------------------------------------- //
  private static Texture texture;
  private static TextureRegion[][] texture_grid = null;
  private static boolean initialized = false;
  private static int column_count;
  private static int row_count;
  private int column_idx = 0;
  private int row_idx = 0;

  // ===================== TextureGrid methods ===================== //
  public TextureGrid(String filename, int layer, int columns, int rows) {
    super("UITextureGrid", layer);
    super.resolution = new int[] {0, 0};
    // ---
    TextureGrid.column_count = columns;
    TextureGrid.row_count = rows;
    // ---
    if (!initialized)
      loadGrid(filename);
    // ---
    computeResolution();
  }

  // --------------------------------------------------------------- //
  private static void loadGrid(String filename) {
    texture = new Texture(Gdx.files.internal(filename));
    int region_width = texture.getWidth() / column_count;
    int region_height = texture.getHeight() / row_count;
    TextureGrid.texture_grid =
        TextureRegion.split(texture, region_width, region_height);
    TextureGrid.initialized = true;

  }

  // --------------------------------------------------------------- //
  public void setTextureIndex(int column_idx, int row_idx) {
    this.column_idx = column_idx;
    this.row_idx = row_idx;
    updateTextureRegion();
  }

  // --------------------------------------------------------------- //
  private void updateTextureRegion() {
    texture_region = texture_grid[row_idx][column_idx];
  }

  // ====================== Renderable methods ====================== //
  @Override
  protected void computeResolution() {
    int region_width = texture.getWidth() / column_count;
    int region_height = texture.getHeight() / row_count;
    resolution[0] = region_width;
    resolution[1] = region_height;
  }

  // ======================== Entity methods ======================= //
  @Override
  public void destroy() {
    texture.dispose();
    super.destroy();
  }
}
