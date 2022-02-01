package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
/** The TreeS is an simple obstacle. The suffix S stands for small */
public class TreeS extends Item {
  // ===================== PaintBooth methods ====================== //
  public TreeS() {
    super("TreeS", "tree_s.png", "tree_s_area.png", Array.of(0.35f, 0.35f),
        150);
  }
}
