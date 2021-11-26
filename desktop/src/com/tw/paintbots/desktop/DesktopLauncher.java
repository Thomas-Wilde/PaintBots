package com.tw.paintbots.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tw.paintbots.PaintBotsGame;

public class DesktopLauncher {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "Paint Bots";
    config.height = 1000;
    config.width = 1500;
    new LwjglApplication(new PaintBotsGame(), config);
  }
}
