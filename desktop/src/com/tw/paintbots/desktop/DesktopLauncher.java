package com.tw.paintbots.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tw.paintbots.GameManager;
import com.tw.paintbots.GameSettings;
import com.tw.paintbots.PaintBotsGame;
import com.tw.paintbots.PlayerState;
import com.tw.paintbots.PlayerType;
import com.tw.paintbots.LevelLoader.LevelInfo;

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

    // --- check for game time seed
    if (argContains(arg, "-time")) {
      int idx = getArgIndex(arg, "-time");
      int time_idx = idx + 1;
      if (arg.length <= time_idx)
        System.out.println("parameter -time was found but no value");
      else {
        int time = 0;
        try {
          time = Integer.valueOf(arg[time_idx]).intValue();
          GameSettings.game_length = time;
          System.out.println("game length set to: " + GameSettings.game_length);
        } catch (Exception e) {
          System.out.println("game length could not be extracted");
        }
      }
    }
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
    int fps = GameSettings.fps;
    // ---
    if (argContains(arg, "-admission")) {
      runAdmissionMode(arg);
    } else {
      Lwjgl3ApplicationConfiguration config =
          new Lwjgl3ApplicationConfiguration();
      config.setWindowedMode(1500, 1000);
      config.setIdleFPS(fps);
      config.setForegroundFPS(fps);
      new Lwjgl3Application(new PaintBotsGame(), config);
    }
  }

  // --------------------------------------------------------------- //
  public static void runAdmissionMode(String[] arg) {
    System.out.println("run admission mode");
    String bot_name = extractBotName(arg);
    // ---
    GameManager mgr = GameManager.get();
    GameSettings settings = new GameSettings();
    // --- we run in admission mode
    settings.headless = true;
    // --- we only use bots
    for (int i = 0; i < settings.player_types.length; ++i) {
      settings.player_types[i] = PlayerType.AI;
      settings.bot_names[i] = "RandomBot";
    }
    settings.bot_names[3] = bot_name;
    // --- we load a specific admission level
    settings.level = new LevelInfo(null, "admission", false);
    if (!mgr.initAdmissionMode(settings, true)) {
      System.out.println("Admission FAILED");
      System.out.println("FAIL");
      return;
    }
    // ---
    int runs = 0;
    int wins = 0;
    for (int j = 0; j < 3; ++j) {
      for (int i = 0; i < 4; ++i) {
        System.out.print("\nseed: " + settings.random_seed);
        System.out.println(" start pos: " + (3 + i) % 4);
        mgr.resetAdmissionMode(i);
        mgr.runAdmissionMode();
        wins += checkForWin(settings) ? 1 : 0;
        ++runs;
      }
      settings.random_seed += 1337;
    }

    // ---
    System.out.println("========================");
    String bot = settings.bot_names[3];
    System.out.println(bot + " won " + wins + " out of " + runs + " rounds");
    float rate = (float) (wins) / (float) (runs);
    System.out.println("Win rate: " + rate);
    if (rate > 0.625)
      System.out.println("PASS");
    else
      System.out.println("FAIL");
  }

  // --------------------------------------------------------------- //
  private static String extractBotName(String[] arg) {
    if (!argContains(arg, "-bot"))
      return null;
    int idx = getArgIndex(arg, "-bot") + 1;
    if (arg.length <= idx) {
      System.out.println("parameter -bot was found but no value");
      return null;
    }
    return arg[idx];
  }

  // --------------------------------------------------------------- //
  private static boolean checkForWin(GameSettings settings) {
    GameManager mgr = GameManager.get();
    PlayerState[] states = new PlayerState[4];
    for (int i = 0; i < 4; ++i)
      states[i] = mgr.getPlayerState(i);
    // --- inactive
    String bot = settings.bot_names[3];
    if (!states[3].is_active) {
      System.out.println(bot + " is inactive/disqualified and lost");
      return false;
    }
    // --- not the highest score
    int score0 = states[0].score;
    int score1 = states[1].score;
    int score2 = states[2].score;
    int score3 = states[3].score;
    if (score3 < score0 || score3 < score1 || score3 < score2) {
      System.out.println(bot + " lost");
      return false;
    }
    // --- highest score
    System.out.println(bot + " won");
    return true;
  }
}
