package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimatedObject {
  // --------------------------------------------------------------- //
  private final String texture_file_;
  private final int rows_;
  private final int cols_;
  private final float duration_;
  private Texture texture_;

  protected List<Animation<TextureRegion>> animation_;
  protected List<TextureRegion[]> frames_;

  // --------------------------------------------------------------- //6
  public AnimatedObject(String texture_file, int rows, int cols,
      float duration) {
    // ---
    texture_file_ = texture_file;
    rows_ = rows;
    cols_ = cols;
    duration_ = duration;
    // ---
    animation_ = new ArrayList<>();
    frames_ = new ArrayList<>();
    loadAnimatedTexture();
  }

  // --------------------------------------------------------------- //
  public void destroy() {
    texture_.dispose();
  }

  // --------------------------------------------------------------- //
  private void loadAnimatedTexture() {
    texture_ = new Texture(Gdx.files.internal(texture_file_));
    TextureRegion[][] frame_grid = loadFrameGrid();
    extractAnimationsFromFrameGrid(frame_grid);
  }

  // --------------------------------------------------------------- //
  /**
   * Extract the individual animation frames from the image grid to an 2D array.
   */
  private TextureRegion[][] loadFrameGrid() {
    int tile_width = texture_.getWidth() / cols_;
    int tile_height = texture_.getHeight() / rows_;
    TextureRegion[][] frame_grid =
        TextureRegion.split(texture_, tile_width, tile_height);
    return frame_grid;
  }

  // --------------------------------------------------------------- //
  private void extractAnimationsFromFrameGrid(TextureRegion[][] frame_grid) {
    float frame_time = duration_ / frame_grid[0].length;
    for (int row = 0; row < rows_; ++row) {
      Animation<TextureRegion> anim =
          new Animation<>(frame_time, frame_grid[row]);
      animation_.add(anim);
    }
  }

  // --------------------------------------------------------------- //
  public TextureRegion getFrame(int animation_id, float time) {
    return animation_.get(animation_id).getKeyFrame(time, true);
  }

  // --------------------------------------------------------------- //
  public TextureRegion getFrame(float time) {
    return animation_.get(0).getKeyFrame(time, true);
  }
}
