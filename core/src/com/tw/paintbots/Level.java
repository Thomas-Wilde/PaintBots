package com.tw.paintbots;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.Items.Item;
import com.tw.paintbots.Items.RefillPlace;
import com.tw.paintbots.Items.RefillPlaceBase;
import com.tw.paintbots.Items.PaintBooth;
import com.tw.paintbots.Items.FenceH;
import com.tw.paintbots.Items.FenceV;
import com.tw.paintbots.Items.TreeS;
import com.tw.paintbots.Items.TreeM;
import com.tw.paintbots.Items.TreeL;
import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Items.ItemProperties;

// --------------------------------------------------------------- //
public class Level {
  private static HashMap<String, ItemProperties> item_dict = null;
  private String file;
  private List<Item> items = null;
  private SecretKey secret_key = null;

  // --------------------------------------------------------------- //
  public Level(String file, SecretKey secret_key) {
    this.file = file;
    this.secret_key = secret_key;

    if (item_dict == null)
      initItemDictionary();
  }

  // --------------------------------------------------------------- //
  private static void initItemDictionary() {
    item_dict = new HashMap<>();
    //@formatter:off
    item_dict.put("RB", new ItemProperties("RB", "RefillBlue",   "refill_blue.png",   "refill_blue_area.png",   Array.of(0.35f, 0.35f), 200));
    item_dict.put("RG", new ItemProperties("RG", "RefillGreen",  "refill_green.png",  "refill_green_area.png",  Array.of(0.35f, 0.35f), 200));
    item_dict.put("RO", new ItemProperties("RO", "RefillOrange", "refill_orange.png", "refill_orange_area.png", Array.of(0.35f, 0.35f), 200));
    item_dict.put("RP", new ItemProperties("RP", "RefillPurple", "refill_purple.png", "refill_purple_area.png", Array.of(0.35f, 0.35f), 200));

    item_dict.put("PB", new ItemProperties("PB", "PaintBooth", "paint_booth.png", "paint_booth_area.png", Array.of(0.65f, 0.65f), 120));
    item_dict.put("FH", new ItemProperties("FH", "FenceH", "fence_h.png", "fence_h_area.png", Array.of(0.40f, 0.40f), 200));
    item_dict.put("FV", new ItemProperties("FV", "FenceV", "fence_v.png", "fence_v_area.png", Array.of(0.40f, 0.40f), 100));
    item_dict.put("TS", new ItemProperties("TS", "TreeS",  "tree_s.png", "tree_s_area.png", Array.of(0.35f, 0.35f), 150));
    item_dict.put("TM", new ItemProperties("TM", "TreeM",  "tree_m.png", "tree_m_area.png", Array.of(0.75f, 0.75f), 150));
    item_dict.put("TL", new ItemProperties("TL", "TreeL",  "tree_l.png", "tree_l_area.png", Array.of(1.00f, 1.00f), 150));
    //@formatter:on
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
    String token = content[0];
    ItemProperties props = item_dict.get(token);
    if (props == null)
      return;
    // ---
    Item item = new Item(props.name, props.tex_file, props.area_file,
        props.scale, props.occ_depth);
    item.setPosition(new Vector2(x, y), secret_key);
    item.setRenderPosition(Array.of(x, y));
    item.init();
    // ---
    items.add(item);
    // ---
    //@formatter:off
    // --- refill places consist of two Renderables
    if (token.equals("RB") || token.equals("RG") || token.equals("RO") || token.equals("RP")) { //@formatter:on
      item = new RefillPlaceBase();
      item.setPosition(new Vector2(x, y), secret_key);
      item.setRenderPosition(Array.of(x, y));
      item.init();
      items.add(item);
    }
  }
}
