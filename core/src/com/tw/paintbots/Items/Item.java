package com.tw.paintbots.Items;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Color;

import com.tw.paintbots.Renderables.SimpleRenderable;
import com.tw.paintbots.GameManager.SecretKey;

import com.tw.paintbots.Array;

public class Item extends SimpleRenderable {
  private String area_file = "";
  private ItemArea area;

  // ======================== Item methods ========================= //
  //@formatter:off
  public Item(String name,
                 String texture_file, String area_file,
                 float[] scale, int occlusion_depth) { //@formatter:on
    super(name, 20, texture_file);
    this.area_file = area_file;
    setScale(scale);
    // --- occlusion depth
    setOcclusionDepth((int) (occlusion_depth * scale[1]));
    int width = texture.getWidth();
    int height = texture.getHeight();
    setRenderOffset(Array.of(-width / 2, -height / 2));
  }

  // --------------------------------------------------------------- //
  public void init() {
    initItemArea();
  }

  // --------------------------------------------------------------- //
  protected void initItemArea() {
    // --- prepare values
    Vector2 pos = getPosition();
    float[] scale = getScale();
    int[] offset = getRenderOffset();
    int[] origin = new int[] {(int) (pos.x + (offset[0] * scale[0])),
        (int) (pos.y + (offset[1] * scale[1]))};
    int width = (int) (texture.getWidth() * scale[0]);
    int height = (int) (texture.getHeight() * scale[1]);
    // --- create area
    area = new ItemArea(width, height);
    area.setOrigin(origin);
    Pixmap area_data = loadItemAreaData();
    transferDataToArea(area_data);
  }

  // --------------------------------------------------------------- //
  private Pixmap loadItemAreaData() {
    Texture area_texture = new Texture(Gdx.files.internal(area_file));
    if (!area_texture.getTextureData().isPrepared())
      area_texture.getTextureData().prepare();
    // --- transfer texture to area
    Pixmap pixmap = area_texture.getTextureData().consumePixmap();
    area_texture.dispose();
    return pixmap;
  }

  // --------------------------------------------------------------- //
  private void transferDataToArea(Pixmap area_data) {
    int width = area.getWidth();
    int height = area.getHeight();
    float[] scale = getScale();
    // --- load interaction texture
    for (int i = 0; i < width; ++i)
      for (int j = 0; j < height; ++j) {
        int x = (int) (i / scale[0]);
        int y = (int) (j / scale[1]);
        Color col = new Color(area_data.getPixel(x, y));
        int type_id = (int) (col.r * 255.0f); // red channel encodes item type
        area.setType(i, height - 1 - j, type_id); // image is upside-down
      }
  }

  // --------------------------------------------------------------- //
  public ItemArea getItemArea(SecretKey key) {
    Objects.requireNonNull(key);
    return area;
  }
}
