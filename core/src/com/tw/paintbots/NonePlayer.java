package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;

// =============================================================== //
public class NonePlayer extends Player {
  // ===================== NonePlayer methods ==================== //
  /** Standard constructor used for bot loading. */
  public NonePlayer() {}

  // --------------------------------------------------------------- //
  public NonePlayer(String name) throws PlayerException {
    super(name);
  }

  // --------------------------------------------------------------- //
  @Override
  public PlayerType getType() {
    return PlayerType.NONE;
  }

  // ====================== Player methods ====================== //
  @Override
  public Vector2 getDirection() {
    return new Vector2(1.0f, 0.0f);
  }

  // --------------------------------------------------------------- //
  @Override
  public void setInitialDirection(Vector2 dir, GameManager.SecretKey key) {
    // --- nothing to do for NonePlayer
  }
}
