package com.tw.paintbots;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

/** Class to load AI bots from the folder ./bots. */
public class BotLoader extends ClassLoader {
  private String run_dir = "";
  private String bot_dir = "";

  // --------------------------------------------------------------- //
  public BotLoader() {
    run_dir = System.getProperty("user.dir");
    bot_dir = run_dir + "/bots";
    // ---
  }

  // --------------------------------------------------------------- //
  /**
   * Loads bots from the subdirectory './bots'. If the subdirectory does not
   * exist, it is created. The loaded bots are listed at the at the console.
   *
   * @return A HashMap that contains the names of the bots and the corresponding
   *         class needed to create them.
   */
  public HashMap<String, Class<?>> loadBots() {
    HashMap<String, Class<?>> bots = new HashMap<>();
    // --- check if the bot folder exists
    if (!checkBotDirectory() && !createBotDirectory())
      return bots;
    // ---
    List<String> filenames = readBotFilenames();
    try {
      bots = loadBotClasses(filenames);
    } catch (Exception e) {
      System.out.println(
          "Could not load bots from './bots' directory: " + e.getMessage());
    }
    return bots;
  }

  // --------------------------------------------------------------- //
  /**
   * Checks if the folder 'bots' exists in the base directory.
   *
   * @return true, if the directory exists, false otherwise
   */
  private boolean checkBotDirectory() {
    Path bot_path = Paths.get(bot_dir);
    if (!Files.exists(bot_path) || !Files.isDirectory(bot_path)) {
      System.out.println(bot_dir + " directory not found.");
      return false;
    }
    return true;
  }

  // --------------------------------------------------------------- //
  /**
   * Creates the subdirectory './bots' in the working folder.
   *
   * @return true if the directory was created, false otherwise.
   */
  private boolean createBotDirectory() {
    try {
      System.out.println("Create " + bot_dir + " directory.");
      new File(bot_dir).mkdirs();
    } catch (Exception e) {
      System.out.println("Could not create bot directory.");
      return false;
    }
    return true;
  }

  // --------------------------------------------------------------- //
  /**
   * @return A list with all class files int the './bots' subdirectory.
   */
  private List<String> readBotFilenames() {
    System.out.println("load bots from: " + bot_dir);
    // ---
    File tmp = new File(bot_dir);
    String[] files = tmp.list();
    List<String> class_files = new ArrayList<>();
    // ---
    for (String file : files) {
      if (file.endsWith(".class")) {
        int idx = file.lastIndexOf(".class");
        class_files.add(file.substring(0, idx));
      }
    }
    return class_files;
  }

  // --------------------------------------------------------------- //
  private HashMap<String, Class<?>> loadBotClasses(List<String> filenames)
      throws MalformedURLException {
    // ---
    HashMap<String, Class<?>> bots = new HashMap<>();
    File file = new File(run_dir + "/");
    URL url = file.toURI().toURL();
    URL[] urls = new URL[] {url};
    URLClassLoader cl = new URLClassLoader(urls);
    // ---
    for (String filename : filenames) {
      try {
        Class<?> bot_class = cl.loadClass("bots." + filename);
        // --- set access to constructors
        Constructor<?> constructor = bot_class.getConstructor();
        constructor.setAccessible(true);
        AIPlayer bot_obj = (AIPlayer) constructor.newInstance();
        bots.put(bot_obj.getBotName(), bot_class);
      } catch (Exception e) {
        System.out.println(
            "Could not load bot '" + filename + "'': " + e.getMessage());
      }
    }
    try {
      cl.close();
    } catch (Exception e) {
      System.out.println("Could not close ClassLoader. " + e.getMessage());
    }
    return bots;
  }
}
