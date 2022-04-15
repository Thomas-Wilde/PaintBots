package com.tw.paintbots;

import java.util.List;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import com.tw.paintbots.Items.Item;
import com.tw.paintbots.Items.RefillPlaceBase;
import com.tw.paintbots.GameManager.SecretKey;
import com.tw.paintbots.Items.ItemProperties;

// --------------------------------------------------------------- //
public class Level {
  private static HashMap<String, ItemProperties> item_dict = null;
  private String file;
  private List<Item> items = null;
  private SecretKey secret_key = null;
  private GameSettings settings = null;

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
    item_dict.put("RB", new ItemProperties("RB", "RefillBlue",   "refill_blue.png",   "refill_blue_area.png", 200));
    item_dict.put("RG", new ItemProperties("RG", "RefillGreen",  "refill_green.png",  "refill_green_area.png", 200));
    item_dict.put("RO", new ItemProperties("RO", "RefillOrange", "refill_orange.png", "refill_orange_area.png", 200));
    item_dict.put("RP", new ItemProperties("RP", "RefillPurple", "refill_purple.png", "refill_purple_area.png", 200));

    item_dict.put("PB", new ItemProperties("PB", "PaintBooth", "paint_booth.png", "paint_booth_area.png", 120));

    item_dict.put("FH", new ItemProperties("FH", "FenceH", "fence_h.png", "fence_h_area.png", 200));
    item_dict.put("FV", new ItemProperties("FV", "FenceV", "fence_v.png", "fence_v_area.png", 100));
    item_dict.put("TS", new ItemProperties("TS", "TreeS",  "tree_s.png", "tree_s_area.png", 150));
    item_dict.put("TM", new ItemProperties("TM", "TreeM",  "tree_m.png", "tree_m_area.png", 150));
    item_dict.put("TL", new ItemProperties("TL", "TreeL",  "tree_l.png", "tree_l_area.png", 150));

    item_dict.put("BSH", new ItemProperties("BSH", "BarrelStack",  "barrel_stack_h.png", "barrel_stack_h_area.png", 100));
    //@formatter:on
  }

  // --------------------------------------------------------------- //
  public void loadLevel(List<Item> items, GameSettings settings) {
    this.items = items;
    this.settings = settings;
    // --- try to load file from settings
    FileHandle file_handle = new FileHandle(file);
    // --- load default level if level file was not opened
    if (!file_handle.exists()) {
      if (file.length() == 0)
        System.out.println("No level selected.");
      else
        System.out.println("Level file '" + file + "'does not exists.");
      System.out.println("Load default level.");
      file_handle = Gdx.files.internal("level.lvl");
      if (!file_handle.exists()) {
        System.out.println("Default level could not be loaded.");
        return;
      }
    }
    String content = file_handle.readString();
    String[] data = content.split("\\r?\\n");
    for (String line : data)
      processLine(line);
  }

  // --------------------------------------------------------------- //
  private void processLine(String line) {
    if (line.length() == 0 || line.charAt(0) == '#')
      return;
    String[] content = line.split(",");
    // ---
    if (content.length != 5)
      return;
    // ---
    String token = content[0];
    //@formatter:off
    // --- load player info
    if (token.equals("P0") || token.equals("P1") ||
        token.equals("P2") || token.equals("P3")) //@formatter:on
      loadPlayer(content);

    // --- load item
    ItemProperties props = item_dict.get(token);
    if (props == null)
      return;
    loadItem(content, props);

    //@formatter:off
    // --- refill places consist of two Renderables
    if (token.equals("RB") || token.equals("RG") ||
        token.equals("RO") || token.equals("RP"))  //@formatter:on
      loadRefillBase(content);
    //@formatter:off
  }

  // --------------------------------------------------------------- //
  private void loadItem(String[] data, ItemProperties props) {
    int x = Integer.parseInt(data[1]);
    int y = Integer.parseInt(data[2]);
    float sx = Float.parseFloat(data[3]);
    float sy = Float.parseFloat(data[4]);
    // ---
    Item item = new Item(props.name, props.tex_file, props.area_file,
        Array.of(sx, sy), props.occ_depth);
    item.setPosition(new Vector2(x, y), secret_key);
    item.setRenderPosition(Array.of(x, y));
    item.init();
    items.add(item);
  }

  // --------------------------------------------------------------- //
  private void loadRefillBase(String[] data) {
    int x = Integer.parseInt(data[1]);
    int y = Integer.parseInt(data[2]);
    float sx = Float.parseFloat(data[3]);
    float sy = Float.parseFloat(data[4]);
    // ---
    Item item = new RefillPlaceBase(Array.of(sx,sy));
    item.setPosition(new Vector2(x, y), secret_key);
    item.setRenderPosition(Array.of(x, y));
    item.init();
    items.add(item);
  }

  // --------------------------------------------------------------- //
  private void loadPlayer(String[] data) {
    String token = data[0];
    int x = Integer.parseInt(data[1]);
    int y = Integer.parseInt(data[2]);
    float dx = Float.parseFloat(data[3]);
    float dy = Float.parseFloat(data[4]);
    int idx = 0;
    if (token.equals("P1")) idx = 1;
    if (token.equals("P2")) idx = 2;
    if (token.equals("P3")) idx = 3;
    settings.start_positions[idx] = new Vector2(x,y);
    settings.start_directions[idx] = new Vector2(dx,dy);
  }
}
