package com.tw.paintbots; // !! Do NOT change this package name !!

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.PlayerException;
import com.tw.paintbots.AIPlayer;
import com.tw.paintbots.GameManager;
import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Items.PowerUp;
import com.tw.paintbots.Items.PowerUpType;

// =============================================================== //
class RandomBot extends AIPlayer {
  // --------------------------------------------------------------- //
  private Vector2 dir = new Vector2(-1.0f, 0.0f);
  private double angle = 0.0;
  private double add = 1.0;
  private double next_switch = 0.0;
  private Random random = null;
  private long seed = 0;

  // ======================= Player methods ===================== //
  /**
   * This method is required, so that the GameManager can set your initial
   * direction.
   *
   * @param dir - the direction in which the player should look
   * @param key - the SecretKey only available to the GameManager
   */
  @Override
  public void setInitialDirection(Vector2 dir, GameManager.SecretKey key) {
    Objects.requireNonNull(key);
    this.dir = dir;
  }

  // ======================= AIPlayer methods ====================== //
  //@formatter:off
  // ToDo: fill in your details
  @Override public String  getBotName()   { return "RandomBot"; }
  @Override public String  getStudent()   { return "Thomas Wilde"; }
  @Override public int     getMatrikel()  { return 123456; }
  @Override public Vector2 getDirection() { return dir; }
  //@formatter:on

  // ======================= RenameMeBot methods ==================== //
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

  // --------------------------------------------------------------- //
  /**
   * This method is called by the GameManager in each update loop. The
   * GameManager is the only class that can call this method.
   *
   * @param key - the SecretKey only available to the GameManager
   */
  //@formatter:off
  @Override
  public void update(GameManager.SecretKey secret) {
    Objects.requireNonNull(secret); // <= keep this line to avoid cheating
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
    angle = 0.0;
    add = 1.0;
    next_switch = 0.0;
    Vector2 pos = getPosition();
    seed = (long) (GameManager.get().randomSeed() + 13 * pos.x + 17 * pos.y);
    random = new Random(seed);
  }

  // --------------------------------------------------------------- //
  /** This is a helper method called in the update method. */
  public void myUpdate() {
    GameManager mgr = GameManager.get();
    // --- switch direction every two seconds
    if (mgr.getElapsedTime() > next_switch) {
      double rnd_time = random.nextDouble() * 2.0 + 0.5;
      next_switch += rnd_time;
      add *= -1.0;
    }
    // --- scale the rotation speed randomly
    double speed_scale = random.nextDouble() * 0.5 + 0.75;
    // --- increase/decrease the orientation
    angle += 360.0 * mgr.getDeltaTime() * add * speed_scale / 2.5;
    double angle_rad = angle * Math.PI / 180.0;
    double x = Math.cos(angle_rad);
    double y = Math.sin(angle_rad);
    dir = new Vector2((float) x, (float) y);
  }
}
