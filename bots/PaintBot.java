package bots; // !! Do NOT change this package name !!

import java.util.Objects;

import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.PlayerException;
import com.tw.paintbots.AIPlayer;
import com.tw.paintbots.GameManager;
import com.tw.paintbots.GameManager.SecretKey;

// =============================================================== //
class RandomBot extends AIPlayer {
  // --------------------------------------------------------------- //
  private Vector2 dir = new Vector2(-1.0f, 0.0f);

  // --------------------------------------------------------------- //
  /**
   * This method is required, so that the GameManager can set your initial
   * direction.
   */
  @Override
  public void setInitialDirection(Vector2 dir, GameManager.SecretKey key) {
    Objects.requireNonNull(key);
    this.dir = dir;
  }

  // ======================= RandomBot methods ===================== //
  // !! Please provide this constructor but do NOT use it !! //
  // !! ....... Use the initBot() method instead ........ !! //
  public RandomBot() { /* !! leave this blank !! */ }

  // --------------------------------------------------------------- //
  // !! Please provide this constructor but do NOT use it !! /
  // !! ....... Use the initBot() method instead ........ !! /
  public RandomBot(String name) throws PlayerException {
    /* do not change this */
    super("AI-" + name);
  }

  // ======================= AIPlayer methods ====================== //
  //@formatter:off
  // ToDo: fill in your details
  @Override public String  getBotName()   { return "Random Bot"; }
  @Override public String  getStudent()   { return "Thomas Wilde"; }
  @Override public int     getMatrikel()  { return 123456; }
  @Override public Vector2 getDirection() { return dir; }
  //@formatter:on

  // --------------------------------------------------------------- //
  /**
   * This method is called by the GameManager in each update loop. The
   * GameManager is the only class that can call this method.
   */
  //@formatter:off
  @Override
  public void update(GameManager.SecretKey secret) {
    Objects.requireNonNull(secret); // <= keep this line
    myUpdate();                     // <= call your own method
    super.update(secret);           // <= keep this line for the animation
  }
  //@formatter:on

  // --------------------------------------------------------------- //
  /**
   * This method is called as soon as an instance of this bot for the actual
   * game is created. You can use this as a substitute for the constructor.
   */
  @Override
  public void initBot() {
    // ToDo: initialize some stuff if necessary
  }

  // --------------------------------------------------------------- //
  /** This is a helper method called in the update method. */
  public void myUpdate() {
    // ToDo: implement a method, called in each update loop
  }
}
