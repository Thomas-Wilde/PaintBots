package com.tw.paintbots.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tw.paintbots.PaintBotsGame;

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
  public static void main(String[] arg) {
    // ---
    int fps = 30;
    if (argContains(arg, "highFPS")) {
      fps = 60;
      System.out.println("run with 60FPS");
    }
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
