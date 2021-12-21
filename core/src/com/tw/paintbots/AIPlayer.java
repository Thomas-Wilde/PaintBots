package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;
import com.tw.paintbots.GameManager.SecretKey;

// =============================================================== //
public abstract class AIPlayer extends Player {
  // ===================== AIPlayer methods ===================== //
  protected AIPlayer() throws PlayerException {
    super("AI-Player");
  }

  // --------------------------------------------------------------- //
  @Override
  public PlayerType getType() {
    return PlayerType.AI;
  }

  // ====================== abstract methods ====================== //
  //@formatter:off
  public abstract String getBotName();
  public abstract String getStudent();
  public abstract int getMatrikel();
  public abstract Vector2 getDirection();
  public abstract void setDirection(Vector2 dir, SecretKey key);
  //@formatter:on
}
