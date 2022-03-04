package com.tw.paintbots.Renderables;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * ToDo: explain SimpleRenderable
 */
// =============================================================== //
public class AnimatedRenderable extends SimpleRenderable {
  // --------------------------------------------------------------- //
  private final int rows;
  private final int columns;
  private final float duration;
  private float time = 0.0f;
  private int animation_id = 0;

  protected List<Animation<TextureRegion>> animation = new ArrayList<>();
  protected List<TextureRegion[]> frames = new ArrayList<>();

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  public void setAnimationID(int id) { animation_id = id; }
  public int getAnimationID() { return animation_id; }
  public void setAnimationTime(float time) { this.time = time; }
  public float getAnimationTime() { return time; }

  //@formatter:on

  // ================== AnimatedRenderable methods ================== //
  public AnimatedRenderable(String name, int layer, String texture_file,
      int rows, int cols, float duration) {
    super(name, layer, texture_file);
    // --- load attributes
    this.rows = rows;
    this.columns = cols;
    this.duration = duration;
    // ---
    adjustResolution();
    initAnimations();
    updateFrameTexture();
  }

  // --------------------------------------------------------------- //
  private void adjustResolution() {
    resolution[0] = resolution[0] / columns;
    resolution[1] = resolution[1] / rows;
  }

  // --------------------------------------------------------------- //
  /** Load the texture file that contains the animation. */
  private void initAnimations() {
    TextureRegion[][] frame_grid = loadFrameGrid();
    extractAnimationsFromFrameGrid(frame_grid);
  }

  // --------------------------------------------------------------- //
  /**
   * Extract the individual animation frames from the image grid to a 2D array.
   */
  private TextureRegion[][] loadFrameGrid() {
    int tile_width = resolution[0];
    int tile_height = resolution[1];
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
  protected TextureRegion getFrame(int animation_id, float time) {
    return animation.get(animation_id).getKeyFrame(time, true);
  }

  // --------------------------------------------------------------- //
  public void updateFrameTexture() {
    texture_region = getFrame(animation_id, time);
  }
}
