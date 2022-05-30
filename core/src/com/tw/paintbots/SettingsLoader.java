package com.tw.paintbots;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import com.badlogic.gdx.files.FileHandle;

/** Class to load a settings file */
public class SettingsLoader extends ClassLoader {
  private String run_dir = "";
  private String settings_file = "";

  // --------------------------------------------------------------- //
  public SettingsLoader() {
    run_dir = System.getProperty("user.dir");
    settings_file = run_dir + "/settings.cfg";
    // ---
  }

  // --------------------------------------------------------------- //
  public boolean loadSettings(GameSettings settings) {
    // ---
    if (!settings.load_settings)
      return false;
    // ---
    if (!checkSettingsFile())
      createSettingsFile();
    // ---
    return readSettingsFile(settings);
  }

  // --------------------------------------------------------------- //
  /**
   * Checks if the file 'settings.cfg' exists in the base directory.
   *
   * @return true, if the file exists, false otherwise
   */
  private boolean checkSettingsFile() {
    Path file_path = Paths.get(settings_file);
    if (!Files.exists(file_path) || Files.isDirectory(file_path)) {
      System.out.println(file_path + " not found.");
      return false;
    }
    return true;
  }

  // --------------------------------------------------------------- //
  /**
   * Creates the file 'settings.cfg' in the working folder.
   *
   * @return true if the directory was created, false otherwise.
   */
  private boolean createSettingsFile() {
    String file_path = System.getProperty("user.dir") + "/" + settings_file;
    String content =
        "time 150\nmax_paint 250000\nstart_paint 250000\nradius 40\nrefill 100000\nwalk 200\nseed 1337\ncountdown 5.0\nbot0 Human\nbot1 RandomBot\nbot2 ---\nbot3 ---\nlevel 0\ninit_time 1000\nupdate_time 5";
    Path path = Paths.get(settings_file);
    try {
      Files.write(path, content.getBytes(), StandardOpenOption.CREATE);
    } catch (Exception e) {
      System.out.println("could not create settings file");
      return false;
    }
    System.out.println("created settings.cfg");
    return true;
  }

  // --------------------------------------------------------------- //
  private boolean readSettingsFile(GameSettings settings) {
    // ---
    String file_path = System.getProperty("user.dir") + "/" + settings_file;
    Path path = Paths.get(settings_file);
    // ---
    List<String> data = null;
    try {
      data = Files.readAllLines(path);
    } catch (Exception e) {
      System.out.println("could not load settings file");
      return false;
    }
    // ---
    String[] expect = {"time", "max_paint", "start_paint", "radius", "refill",
        "walk", "seed", "countdown", "bot0", "bot1", "bot2", "bot3", "level",
        "init_time", "update_time"};
    for (int i = 0; i < expect.length; ++i)
      if (!checkLine(data.get(i), expect[i])) {
        System.out.println("expected " + expect[i] + " got " + data.get(i));
        return false;
      }
    // ---
    //@formatter:off
    settings.game_length        = Integer.valueOf(data.get(0).split(" ")[1]);
    settings.max_paint_amount   = Integer.valueOf(data.get(1).split(" ")[1]);
    settings.start_paint_amount = Integer.valueOf(data.get(2).split(" ")[1]);
    settings.paint_radius       = Integer.valueOf(data.get(3).split(" ")[1]);
    settings.refill_speed       = Integer.valueOf(data.get(4).split(" ")[1]);
    settings.walk_speed         = Integer.valueOf(data.get(5).split(" ")[1]);
    settings.random_seed        = Integer.valueOf(data.get(6).split(" ")[1]);
    settings.countdown          =   Float.valueOf(data.get(7).split(" ")[1]);
    settings.bot_names[0]       =                 data.get(8).split(" ")[1];
    settings.bot_names[1]       =                 data.get(9).split(" ")[1];
    settings.bot_names[2]       =                 data.get(10).split(" ")[1];
    settings.bot_names[3]       =                 data.get(11).split(" ")[1];
    settings.level_index        = Integer.valueOf(data.get(12).split(" ")[1]);
    settings.init_time          = Integer.valueOf(data.get(13).split(" ")[1]);
    settings.update_time        = Integer.valueOf(data.get(14).split(" ")[1]);
    //@formatter:on

    for (int i = 0; i < 4; ++i) {
      settings.player_types[i] = PlayerType.AI;
      if (settings.bot_names[i].equals("Human"))
        settings.player_types[i] = PlayerType.HUMAN;
      if (settings.bot_names[i].equals("---"))
        settings.player_types[i] = PlayerType.NONE;
    }

    return true;
  }

  // --------------------------------------------------------------- //
  private boolean checkLine(String data, String expected) {
    String[] split = data.split(" ");
    return (split[0].equals(expected));
  }
}
