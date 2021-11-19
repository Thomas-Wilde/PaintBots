package com.tw.paintbots;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DirectionIndicator extends AnimatedObject {
  // --------------------------------------------------------------- //
  public DirectionIndicator(String texture_file) {
    super(texture_file, 1, 2, 0.25f);
  }

  // --------------------------------------------------------------- //
  public TextureRegion getFrame(Vector2 move_dir, float time) {
    CardinalDirection dir = vectorToCardinalDirection(move_dir);
    return getFrame(dir, time);
  }

  // --------------------------------------------------------------- //
  public CardinalDirection vectorToCardinalDirection(Vector2 dir_vec) {
    // ---
    float x = dir_vec.x;
    float y = dir_vec.y;
    // --- x and y correspond to sine and cosine values of the orientation angle
    if (x > 0.9237)
      return CardinalDirection.E;
    if (x < -0.9237)
      return CardinalDirection.W;
    if (y > 0.9237)
      return CardinalDirection.N;
    if (y < -0.9237)
      return CardinalDirection.S;
    if (x > 0.0 && y > 0.387)
      return CardinalDirection.NE;
    if (x > 0.0 && y < 0.387)
      return CardinalDirection.SE;
    if (x < 0.0 && y > 0.387)
      return CardinalDirection.NW;
    return CardinalDirection.SW;
  }

  // --------------------------------------------------------------- //
  public TextureRegion getFrame(CardinalDirection dir, float time) {
    return animation_.get(dir.get()).getKeyFrame(time, true);
  }
}
