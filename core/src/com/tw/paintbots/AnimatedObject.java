package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedObject {
  // --------------------------------------------------------------- //
  private final String texture_file;
  private final int rows;
  private final int cols;
  private final float duration;
  private Texture texture;

  protected List<Animation<TextureRegion>> animation = new ArrayList<>();
  protected List<TextureRegion[]> frames = new ArrayList<>();

  // =============================================================== //
  public AnimatedObject(String texture_file, int rows, int cols,
      float duration) {
    // --- load attributes
    this.texture_file = texture_file;
    this.rows = rows;
    this.cols = cols;
    this.duration = duration;
    // ---
    loadAnimatedTexture();
  }

  // --------------------------------------------------------------- //
  public void destroy() {
    texture.dispose();
  }

  // --------------------------------------------------------------- //
  /** Load the texture file that contains the animation. */
  private void loadAnimatedTexture() {
    texture = new Texture(Gdx.files.internal(texture_file));
    TextureRegion[][] frame_grid = loadFrameGrid();
    extractAnimationsFromFrameGrid(frame_grid);
  }

  // --------------------------------------------------------------- //
  /**
   * Extract the individual animation frames from the image grid to a 2D array.
   */
  private TextureRegion[][] loadFrameGrid() {
    int tile_width = texture.getWidth() / cols;
    int tile_height = texture.getHeight() / rows;
    TextureRegion[][] frame_grid =
        TextureRegion.split(texture, tile_width, tile_height);
    return frame_grid;
  }

  // --------------------------------------------------------------- //
  /** Transfer the frames from the grid to the animation list. */
  private void extractAnimationsFromFrameGrid(TextureRegion[][] frame_grid) {
    float frame_time = duration / frame_grid[0].length;
    for (int row = 0; row < rows; ++row) {
      Animation<TextureRegion> anim =
          new Animation<>(frame_time, frame_grid[row]);
      animation.add(anim);
    }
  }

  // --------------------------------------------------------------- //
  /**
   * Extract the correct frame from the animation with the given id and the
   * given time.
   */
  public TextureRegion getFrame(int animation_id, float time) {
    return animation.get(animation_id).getKeyFrame(time, true);
  }

  // --------------------------------------------------------------- //
  /**
   * Same as getFrame(0, time)
   */
  public TextureRegion getFrame(float time) {
    return animation.get(0).getKeyFrame(time, true);
  }
}
