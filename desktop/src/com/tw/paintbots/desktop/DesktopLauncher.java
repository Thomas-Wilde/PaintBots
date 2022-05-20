package com.tw.paintbots.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tw.paintbots.GameSettings;
import com.tw.paintbots.PaintBotsGame;

public class DesktopLauncher {
  // --------------------------------------------------------------- //
  private static void parseArguments(String[] arg) {
    // --- check for 60fps
    if (argContains(arg, "-highFPS")) {
      GameSettings.fps = 60;
      System.out.println("run with 60FPS");
    }

    // --- check for random seed
    GameSettings.random_seed = (int) (Math.random() * 1337);
    if (argContains(arg, "-seed")) {
      int idx = getArgIndex(arg, "-seed");
      int seed_idx = idx + 1;
      if (arg.length <= seed_idx)
        System.out.println("parameter -seed was found but no value");
      else {
        int seed = 0;
        try {
          seed = Integer.valueOf(arg[seed_idx]).intValue();
          GameSettings.random_seed = seed;
        } catch (Exception e) {
          System.out.println("seed could not be extracted");
        }
      }
    }
    System.out.println("random seed: " + GameSettings.random_seed);
  }

  // --------------------------------------------------------------- //
  /** Search an entry in the args array */
  private static boolean argContains(String[] arg, String id) {
    String compare = id.toLowerCase();
    for (String entry : arg) {
      entry = entry.toLowerCase();
      if (entry.equals(compare))
        return true;
    }
    return false;
  }

  // --------------------------------------------------------------- //
  /** Get the index of a specific argument or -1 if not found */
  private static int getArgIndex(String[] arg, String id) {
    String compare = id.toLowerCase();
    int i = 0;
    for (String entry : arg) {
      entry = entry.toLowerCase();
      if (entry.equals(compare))
        return i;
      ++i;
    }
    return -1;
  }

  // --------------------------------------------------------------- //
  public static void main(String[] arg) {
    // ---
    parseArguments(arg);
    // ---
    int fps = GameSettings.fps;
    // ---
    Lwjgl3ApplicationConfiguration config =
        new Lwjgl3ApplicationConfiguration();
    config.setWindowedMode(1500, 1000);
    config.setIdleFPS(fps);
    config.setForegroundFPS(fps);
    // config.setInitialVisible(false);
    new Lwjgl3Application(new PaintBotsGame(), config);
  }
}
