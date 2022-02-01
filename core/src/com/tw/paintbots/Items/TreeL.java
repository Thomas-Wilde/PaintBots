package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
/** The TreeL is an simple obstacle. The suffix L stands for large */
public class TreeL extends Item {
  // ===================== PaintBooth methods ====================== //
  public TreeL() {
    super("TreeL", "tree_l.png", "tree_l_area.png", Array.of(1.0f, 1.0f), 150);
  }
}
