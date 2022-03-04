package com.tw.paintbots.Items;

public class ItemProperties {
  public String id = null;
  public String name = null;
  public String tex_file = null;
  public String area_file = null;
  public int occ_depth = -1;

  public ItemProperties(String id, String name, String tex_file,
      String area_file, int occ_depth) {
    this.id = id;
    this.name = name;
    this.tex_file = tex_file;
    this.area_file = area_file;
    this.occ_depth = occ_depth;
  }
}
