package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;
import com.tw.paintbots.GameManager.SecretKey;

// =============================================================== //
public abstract class AIPlayer extends Player {
  // ===================== AIPlayer methods ===================== //
  /** Standard constructor used for bot loading. */
  public AIPlayer() {}

  // --------------------------------------------------------------- //
  /** Constructor to create the actual bots. */
  public AIPlayer(String name) throws PlayerException {
    super("AI-" + name);
  }

  // --------------------------------------------------------------- //
  @Override
  final public PlayerType getType() {
    return PlayerType.AI;
  }

  // ====================== abstract methods ====================== //
  //@formatter:off
  public abstract String getBotName();
  public abstract String getStudent();
  public abstract int getMatrikel();
  public abstract Vector2 getDirection();
  public abstract void setInitialDirection(Vector2 dir, SecretKey key);
  public abstract void initBot();
  //@formatter:on
}
