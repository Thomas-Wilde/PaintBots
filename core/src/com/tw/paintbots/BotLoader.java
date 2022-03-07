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

/** Class to load AI bots from the folder ./bots. */
public class BotLoader extends ClassLoader {
  private String run_dir = "";
  private String bot_dir = "";

  // --------------------------------------------------------------- //
  public BotLoader() {
    // ---
    run_dir = System.getProperty("user.dir");
    bot_dir = run_dir + "/bots";
    // ---
  }

  // --------------------------------------------------------------- //
  public HashMap<String, Class<?>> loadBots() {
    // ---
    checkBotDirectory();
    List<String> filenames = readBotFilenames();
    // ---
    HashMap<String, Class<?>> bots = new HashMap<>();
    try {
      bots = loadBotClasses(filenames);
    } catch (Exception e) {
      System.out.println(
          "Could not load bots from './bots' directory: " + e.getMessage());
    }
    System.out.println("loaded bots:");
    for (String bot_name : bots.keySet())
      System.out.println(bot_name);

    return bots;
  }

  // --------------------------------------------------------------- //
  /**
   * @return A list with all class files int the ./bot subdirectory.
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
    File file = new File(run_dir);
    URL url = file.toURI().toURL();
    URL[] urls = new URL[] {url};
    URLClassLoader cl = new URLClassLoader(urls);
    // ---
    for (String filename : filenames) {
      try {
        Class<?> bot_class = cl.loadClass("bots." + filename);
        AIPlayer bot_obj = (AIPlayer) bot_class.getConstructor().newInstance();
        bots.put(bot_obj.getBotName(), bot_class);
      } catch (Exception e) {
        System.out.println("Could not load bot from 'bots." + filename + ": "
            + e.getMessage());
      }
    }
    try {
      cl.close();
    } catch (Exception e) {
      System.out.println("Could not close ClassLoader. " + e.getMessage());
    }
    return bots;
  }

  // ----------------------------------------------------//
  private void checkBotDirectory() {
    Path bot_path = Paths.get(bot_dir);
    if (!Files.exists(bot_path) || !Files.isDirectory(bot_path)) {
      try {
        System.out
            .println(bot_dir + " directory not found - try to create it.");
        new File(bot_dir).mkdirs();
      } catch (Exception e) {
        System.out.println("Could not create bot directory.");
      }
    }
  }
}
