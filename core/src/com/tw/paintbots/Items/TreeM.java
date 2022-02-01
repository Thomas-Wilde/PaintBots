package com.tw.paintbots.Items;

import com.tw.paintbots.Array;

// =============================================================== //
/** The TreeM is an simple obstacle. The suffix M stands for middle */
public class TreeM extends Item {
  // ===================== PaintBooth methods ====================== //
  public TreeM() {
    super("TreeM", "tree_m.png", "tree_m_area.png", Array.of(0.75f, 0.75f),
        150);
  }
}
