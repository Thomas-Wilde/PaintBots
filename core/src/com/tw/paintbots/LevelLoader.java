package com.tw.paintbots;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/** Class to list all levels from the folder ./levels. */
public class LevelLoader {
  private String run_dir = "";
  private String levels_dir = "";

  // --------------------------------------------------------------- //
  public LevelLoader() {
    run_dir = System.getProperty("user.dir");
    levels_dir = run_dir + "/levels";
    // ---
  }

  // --------------------------------------------------------------- //
  /** ToDo: comment */
  public ArrayList<String> loadLevelFiles() {
    ArrayList<String> levels = new ArrayList<>();
    // --- check if the level folder exists
    if (!checkLevelsDirectory() && !createLevelsDirectory())
      return levels;
    // ---
    levels = readLevelFilenames();
    System.out.println("level files:");
    for (String level_name : levels)
      System.out.println(level_name);
    return levels;
  }

  // --------------------------------------------------------------- //
  /**
   * Checks if the folder 'levels' exists in the base directory.
   *
   * @return true, if the directory exists, false otherwise
   */
  private boolean checkLevelsDirectory() {
    Path levels_path = Paths.get(levels_dir);
    if (!Files.exists(levels_path) || !Files.isDirectory(levels_path)) {
      System.out.println(levels_dir + " directory not found.");
      return false;
    }
    return true;
  }

  // --------------------------------------------------------------- //
  /**
   * Creates the subdirectory './levels' in the working folder.
   *
   * @return true if the directory was created, false otherwise.
   */
  private boolean createLevelsDirectory() {
    try {
      System.out.println("Create " + levels_dir + " directory.");
      new File(levels_dir).mkdirs();
    } catch (Exception e) {
      System.out.println("Could not create levels directory.");
      return false;
    }
    return true;
  }

  // --------------------------------------------------------------- //
  /**
   * @return A list with all 'lvl' files int the './levels' subdirectory.
   */
  private ArrayList<String> readLevelFilenames() {
    System.out.println("load level files from: " + levels_dir);
    // ---
    File tmp = new File(levels_dir);
    String[] files = tmp.list();
    ArrayList<String> level_files = new ArrayList<>();
    // ---
    for (String file : files) {
      if (file.endsWith(".lvl"))
        level_files.add(file);
    }
    return level_files;
  }
}
