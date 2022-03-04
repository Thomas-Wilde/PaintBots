package com.tw.paintbots.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tw.paintbots.PaintBotsGame;

public class DesktopLauncher {
  public static void main(String[] arg) {
    // ---
    Lwjgl3ApplicationConfiguration config =
        new Lwjgl3ApplicationConfiguration();
    config.setWindowedMode(1500, 1000);
    config.setIdleFPS(60);
    config.setForegroundFPS(60);
    // config.setInitialVisible(false);
    new Lwjgl3Application(new PaintBotsGame(), config);
  }
}
