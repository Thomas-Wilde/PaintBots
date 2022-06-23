package com.tw.paintbots.desktop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.tw.paintbots.GameManager;
import com.tw.paintbots.GameSettings;
import com.tw.paintbots.PaintBotsGame;
import com.tw.paintbots.Player;
import com.tw.paintbots.PlayerState;
import com.tw.paintbots.PlayerType;
import com.tw.paintbots.LevelLoader.LevelInfo;

public class DesktopLauncher {
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
  private static int parseRandomSeed(String[] arg) {
    // --- check for random seed
    int random_seed = (int) (Math.random() * 1337);
    if (argContains(arg, "-seed")) {
      int idx = getArgIndex(arg, "-seed");
      int seed_idx = idx + 1;
      if (arg.length <= seed_idx)
        System.out.println("parameter -seed was found but no value");
      else {
        int seed = 0;
        try {
          seed = Integer.valueOf(arg[seed_idx]).intValue();
          random_seed = seed;
        } catch (Exception e) {
          System.out.println("seed could not be extracted");
        }
      }
    }
    System.out.println("random seed: " + random_seed);
    return random_seed;
  }

  // --------------------------------------------------------------- //
  private static int parseHighFPS(String[] arg) {
    // --- check for 60fps
    if (argContains(arg, "-highFPS")) {
      System.out.println("run with 60FPS");
      return 60;
    }
    return 30;
  }

  // --------------------------------------------------------------- //
  private static int parseTime(String[] arg) {
    int time = 150;
    // --- check for game time seed
    if (argContains(arg, "-time")) {
      int idx = getArgIndex(arg, "-time");
      int time_idx = idx + 1;
      if (arg.length <= time_idx)
        System.out.println("parameter -time was found but no value");
      else {
        try {
          time = Integer.valueOf(arg[time_idx]).intValue();
          System.out.println("game length set to: " + time);
        } catch (Exception e) {
          System.out.println("game length could not be extracted");
        }
      }
    }
    return time;
  }

  // --------------------------------------------------------------- //
  private static String parseContestLevel(String[] arg) {
    String level_file = "contestI.bin";
    // --- check for game time seed
    if (argContains(arg, "-level")) {
      int idx = getArgIndex(arg, "-level");
      int level_idx = idx + 1;
      if (arg.length <= level_idx)
        System.out.println("parameter -level was found but no value");
      else {
        try {
          level_file = arg[level_idx];
          System.out.println("level: " + level_file);
        } catch (Exception e) {
          System.out.println("level could not be extracted");
        }
      }
    }
    return level_file;
  }

  // --------------------------------------------------------------- //
  public static void main(String[] arg) {
    // ===================== //
    String version = "0.06.18";
    // ===================== //
    // ---
    if (argContains(arg, "-version")) {
      System.out.println("PaintBots version: " + version);
    }
    // ---
    if (argContains(arg, "-contest")) {
      // config.setWindowedMode(1500, 1000);
      // config.setIdleFPS(settings.fps);
      // config.setForegroundFPS(settings.fps);
      // new Lwjgl3Application(new PaintBotsGame(settings), config);
      runContestMode(arg);
      return;
    }
    // ---
    if (argContains(arg, "-admission")) {
      runAdmissionMode(arg);
    } else {
      // ---
      GameSettings settings = new GameSettings();
      settings.fps = parseHighFPS(arg);
      settings.game_length = parseTime(arg);
      settings.random_seed = parseRandomSeed(arg);// ---
      if (argContains(arg, "-hidecountdown"))
        settings.countdown = 0.0f;
      if (argContains(arg, "-loadsettings"))
        settings.load_settings = true;
      // ---
      Lwjgl3ApplicationConfiguration config =
          new Lwjgl3ApplicationConfiguration();
      config.setWindowedMode(1500, 1000);
      config.setIdleFPS(settings.fps);
      config.setForegroundFPS(settings.fps);
      new Lwjgl3Application(new PaintBotsGame(settings), config);
    }
  }

  // =============================================================== //
  private static void runAdmissionMode(String[] arg) {
    System.out.println("run admission mode");
    String bot_name = extractBotName(arg);
    // ---
    GameManager mgr = GameManager.get();
    GameSettings settings = new GameSettings();
    // --- we run in admission mode
    settings.headless = true;
    settings.game_length = parseTime(arg);
    settings.random_seed = parseRandomSeed(arg);
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
      System.out.println("NICHT BESTANDEN");
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
    if (wins >= 8)
      System.out.println("BESTANDEN");
    else
      System.out.println("NICHT BESTANDEN");
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
  private static String[] extractBotNames(String[] arg) {
    String[] bot_names = new String[4];
    for (int i = 0; i < 4; ++i) {
      String find = "-bot";
      find += String.valueOf(i);
      // ---
      if (!argContains(arg, find)) {
        System.out.println(find + " argument missing");
        return null;
      }
      // ---
      int idx = getArgIndex(arg, find) + 1;
      if (arg.length <= idx) {
        System.out.println("parameter " + find + " was found but no value");
        return null;
      }
      // ---
      bot_names[i] = arg[idx];
    }
    return bot_names;
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

  // --------------------------------------------------------------- //

  private static void runContestMode(String[] args) {
    // ----
    PrintStream system_out = System.out;
    PrintStream dummy_stream = new PrintStream(new OutputStream() {
      public void write(int b) {}
    });
    System.setOut(dummy_stream);
    // ---
    System.out.println("run contest mode");
    String[] bot_names = extractBotNames(args);
    // ---
    GameManager mgr = GameManager.get();
    GameSettings settings = new GameSettings();
    // --- we run in admission mode
    settings.headless = true;
    settings.game_length = parseTime(args);
    settings.random_seed = (int) (Math.random() * 31415926);
    settings.update_time = 1000;
    settings.init_time = 2000;
    configureLevelSettings(args, settings);
    // --- we only use bots
    for (int i = 0; i < 4; ++i) {
      settings.player_types[i] = PlayerType.AI;
      settings.bot_names[i] = bot_names[i];
    }
    // ---
    // System.setOut(system_out);
    boolean init = mgr.initContestMode(settings);
    List<PlayerState> states = mgr.getPlayerStates();
    if (!init || states.size() < 4) {
      System.setOut(system_out);
      System.out.println("Contest initialization failed.");
      System.out.println("loaded bots:");
      for (PlayerState state : states)
        System.out.println(state.bot_name);
      return;
    }
    // ---
    String[] result = new String[4];
    for (int i = 0; i < 4; ++i)
      result[i] = new String("|");
    int[] points = new int[4];
    // --- play 4 rounds, each player starts at each position
    for (int round = 0; round < 4; ++round) {
      // --- compute round
      mgr.resetAdmissionMode(round);
      mgr.runAdmissionMode();
      // --- compute the points
      int[] round_points = computeContestPoints();
      // --- prepare result for each player
      for (int pl_id = 0; pl_id < 4; ++pl_id) {
        points[pl_id] += round_points[pl_id];
        PlayerState state = mgr.getPlayerState(pl_id);
        if (!state.is_active) {
          result[pl_id] += "  x = ";
        } else {
          int score = state.score;
          result[pl_id] += (score < 10 ? " " : "");
          result[pl_id] += " " + state.score + " = ";
        }
        result[pl_id] += round_points[pl_id] + "pts |";
      }
    }
    // -------------------------
    String table_info = "";
    String level = settings.level.level_name;
    // ---
    //@formatter:off
    table_info += "---\n\n";
    table_info += "`level: " + level + " seed: " + settings.random_seed + " time: " + settings.game_length + "`\n\n";
    table_info += "|  Round 1  |  Round 2  |  Round 3  |  Round 4  | Points  |   Bot   |\n";
    table_info += "| --------: | --------: | --------: | --------: | ------: | :------ |\n";
    for (int j = 0; j < 4; ++j) {
      result[j] += (points[j] < 10 ? "     " : "    ") + points[j] + " |";
      result[j] += " " + settings.bot_names[j] + " |\n";
      table_info += result[j];
    }
    table_info += "\n";

    // -------------------------
    String point_info = "";
    for (int j = 0; j < 4; ++j) {
      point_info += settings.bot_names[j] + ";";
      point_info += points[j] + "\n";
    }

    //   String level = settings.level.level_name;
  //   System.out.println("---\n");
  //   System.out.println("`level: " + level + " seed: " + settings.random_seed
  //       + " time: " + settings.game_length + "`\n");
  // //@formatter:off
  // System.out.println("|  Round 1  |  Round 2  |  Round 3  |  Round 4  | Points  |   Bot   |");
  //   System.out.println("| :-------: | :-------: | :-------: | :-------: | :-----: | :-----: |");
  //   //@formatter:on
    // for (int j = 0; j < 4; ++j) {
    // result[j] += (points[j] < 10 ? " " : " ") + points[j] + " |";
    // result[j] += " " + settings.bot_names[j] + " |";
    // System.out.println(result[j]);
    // }
    // System.out.println("");

    System.setOut(system_out);
    System.out.println(table_info);
    System.out.println(point_info);

    // -------------------------
    try {
      BufferedWriter writer =
          new BufferedWriter(new FileWriter("contest_results.md", true));
      writer.append(table_info);
      writer.close();
    } catch (Exception e) {
      System.out.println("could not write to contest_results.md");
    }

    // -------------------------
    try {
      BufferedWriter writer =
          new BufferedWriter(new FileWriter("contest_points.md", true));
      writer.append(point_info);
      writer.close();
    } catch (Exception e) {
      System.out.println("could not write to contest_points.md");
    }
  }

  // --------------------------------------------------------------- //
  private static int[] computeContestPoints() {
    int[] points = new int[4];
    GameManager mgr = GameManager.get();
    int[] scores = new int[4];
    PlayerState[] states = new PlayerState[4];
    // ---
    for (int i = 0; i < 4; ++i) {
      PlayerState state = mgr.getPlayerState(i);
      states[i] = state;
      scores[i] = state.score;
    }
    Arrays.sort(scores);
    for (int player_id = 0; player_id < 4; ++player_id) {
      // ---
      for (int score_id = 0; score_id < 4; ++score_id)
        if (states[player_id].score == scores[score_id])
          points[player_id] = score_id + 1;

      // --- disqualified players get 0 points
      if (!states[player_id].is_active)
        points[player_id] = 0;
    }
    return points;
  }

  // --------------------------------------------------------------- //
  private static boolean configureLevelSettings(String[] args,
      GameSettings settings) {
    String level_file = parseContestLevel(args);
    // ---
    if (level_file.equals("contestI.bin")) {
      settings.level = new LevelInfo("contestI.bin", "Contest I", true);
      settings.start_positions[0] = new Vector2(600f, 600f);
      settings.start_positions[1] = new Vector2(600f, 400f);
      settings.start_positions[2] = new Vector2(400f, 600f);
      settings.start_positions[3] = new Vector2(400f, 400f);
      settings.start_directions[0] = new Vector2(1.0f, 1.0f);
      settings.start_directions[1] = new Vector2(1.0f, -1.0f);
      settings.start_directions[2] = new Vector2(-1.0f, 1.0f);
      settings.start_directions[3] = new Vector2(-1.0f, -1.0f);
      return true;
    }
    // ---
    if (level_file.equals("contestII.bin")) {
      settings.level = new LevelInfo("contestII.bin", "Contest II", true);
      settings.start_positions[0] = new Vector2(100f, 100f);
      settings.start_positions[1] = new Vector2(100f, 900f);
      settings.start_positions[2] = new Vector2(900f, 100f);
      settings.start_positions[3] = new Vector2(900f, 900f);
      settings.start_directions[0] = new Vector2(1.0f, 1.0f);
      settings.start_directions[1] = new Vector2(1.0f, -1.0f);
      settings.start_directions[2] = new Vector2(-1.0f, 1.0f);
      settings.start_directions[3] = new Vector2(-1.0f, -1.0f);
      return true;
    }
    // ---
    if (level_file.equals("contestIII.bin")) {
      settings.level = new LevelInfo("contestIII.bin", "Contest III", true);
      settings.start_positions[0] = new Vector2(600f, 600f);
      settings.start_positions[1] = new Vector2(600f, 400f);
      settings.start_positions[2] = new Vector2(400f, 600f);
      settings.start_positions[3] = new Vector2(400f, 400f);
      settings.start_directions[0] = new Vector2(1.0f, 1.0f);
      settings.start_directions[1] = new Vector2(1.0f, -1.0f);
      settings.start_directions[2] = new Vector2(-1.0f, 1.0f);
      settings.start_directions[3] = new Vector2(-1.0f, -1.0f);
      return true;
    }

    // ---
    if (level_file.equals("contestIV.bin")) {
      settings.level = new LevelInfo("contestIV.bin", "Contest IV", true);
      settings.start_positions[0] = new Vector2(460, 460);
      settings.start_positions[1] = new Vector2(460, 540);
      settings.start_positions[2] = new Vector2(540, 460);
      settings.start_positions[3] = new Vector2(540, 540);
      settings.start_directions[0] = new Vector2(-1.0f, 0.0f);
      settings.start_directions[1] = new Vector2(-1.0f, 0.0f);
      settings.start_directions[2] = new Vector2(1.0f, 0.0f);
      settings.start_directions[3] = new Vector2(1.0f, 0.0f);
      return true;
    }
    // ---
    if (level_file.equals("contestV.bin")) {
      settings.level = new LevelInfo("contestV.bin", "Contest V", true);
      settings.start_positions[0] = new Vector2(460f, 460f);
      settings.start_positions[1] = new Vector2(460f, 540f);
      settings.start_positions[2] = new Vector2(540f, 460f);
      settings.start_positions[3] = new Vector2(540f, 540f);
      settings.start_directions[0] = new Vector2(-1.0f, 0.0f);
      settings.start_directions[1] = new Vector2(-1.0f, 0.0f);
      settings.start_directions[2] = new Vector2(1.0f, 0.0f);
      settings.start_directions[3] = new Vector2(1.0f, 0.0f);
      return true;
    }

    return false;
  }
}
