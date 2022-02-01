package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.Items.Item;
import com.tw.paintbots.Items.PaintBooth;
import com.tw.paintbots.Items.PaintGreen;
import com.tw.paintbots.Items.FenceH;
import com.tw.paintbots.Items.FenceV;
import com.tw.paintbots.Items.TreeS;
import com.tw.paintbots.Items.TreeM;
import com.tw.paintbots.Items.TreeL;
import com.tw.paintbots.GameManager.SecretKey;

// --------------------------------------------------------------- //
public class Level {
  private String file;
  private List<Item> items = null;
  private SecretKey secret_key = null;

  // --------------------------------------------------------------- //
  public Level(String file, SecretKey secret_key) {
    this.file = file;
    this.secret_key = secret_key;
  }

  // --------------------------------------------------------------- //
  public void loadLevel(List<Item> items) {
    // ---
    this.items = items;
    // ---
    // FileHandle file_handle = new FileHandle(file);
    FileHandle file_handle = Gdx.files.internal(file);
    if (!file_handle.exists()) {
      System.out.println("Level file does not exists");
      return;
    }
    String content = file_handle.readString();
    String[] data = content.split("\\r?\\n");
    for (String line : data)
      processLine(line);
  }

  // --------------------------------------------------------------- //
  private void processLine(String line) {
    if (line.charAt(0) == '#')
      return;
    String[] content = line.split(",");
    // ---
    if (content.length == 0)
      return;
    // ---
    int x = Integer.parseInt(content[1]);
    int y = Integer.parseInt(content[2]);
    // ---
    //@formatter:off
    String token  = content[0];
    Item item = null;
    if (token.equals("PB")) item = new PaintBooth();
    if (token.equals("PG")) item = new PaintGreen();
    if (token.equals("FH")) item = new FenceH();
    if (token.equals("FV")) item = new FenceV();
    if (token.equals("TS")) item = new TreeS();
    if (token.equals("TM")) item = new TreeM();
    if (token.equals("TL")) item = new TreeL();
    //@formatter:on
    // ---
    if (item == null)
      return;
    item.setPosition(new Vector2(x, y), secret_key);
    item.setRenderPosition(Array.of(x, y));
    item.init();
    // ---
    items.add(item);
  }
}
