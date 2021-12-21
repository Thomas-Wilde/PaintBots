package com.tw.paintbots.Renderables;

import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.CardinalDirection;

// =============================================================== //
public class PlayerAnimation extends AnimatedRenderable {
  private Vector2 move_dir = new Vector2(1.0f, 0.0f);

  // ======================== Getter/Setter ======================== //
  //@formatter:off
  public Vector2 getMoveDirection() { return this.move_dir.cpy(); }
  //@formatter:on

  // =================== PlayerAnimation methods =================== //
  public PlayerAnimation(String texture_file) {
    super("PlayerAnimation", 5, texture_file, 8, 8, 1.0f);
  }

  // --------------------------------------------------------------- //
  public void setMoveDirection(Vector2 move) {
    this.move_dir = move.cpy();
    CardinalDirection dir = vectorToCardinalDirection(move_dir);
    setAnimationID(dir.getID());
  }

  // --------------------------------------------------------------- //
  public CardinalDirection vectorToCardinalDirection(Vector2 dir_vec) {
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
}
