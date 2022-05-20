package com.tw.paintbots.Items;

import com.tw.paintbots.Array;
import com.tw.paintbots.GameSettings;
import com.tw.paintbots.Renderables.TextureGrid;

public class PowerUp extends TextureGrid {
  // ---
  final PowerUpType type;
  static int layer = 19;
  final int spawn_time;
  final int death_time;

  // ======================== PowerUp methods ========================= //
  public PowerUp(PowerUpType type, int spawn_time, int life_time) {
    super("powerup_texture.png", layer, 7, 1);
    this.type = type;
    this.spawn_time = spawn_time;
    this.death_time = spawn_time + life_time;
    // ---
    if (GameSettings.headless)
      return;
    // ---
    setTextureIndex(type.getTypeID(), 0);
    setScale(Array.of(0.25f, 0.25f));
  }

  // --------------------------------------------------------------- //
  public class Info {
    public PowerUpType type = null;
    public int[] position = null;
    public int spawn_time = 0;
    public int death_time = 0;

    public String toString() {
      String info = type.toString() + " pos:[" + position[0] + ", "
          + position[1] + "] spawn: " + spawn_time + " death: " + death_time;
      return info;
    }
  }

  // --------------------------------------------------------------- //
  public Info getInfo() {
    Info info = new Info();
    info.type = this.type;
    info.position =
        new int[] {(int) this.getPosition().x, (int) this.getPosition().y};
    info.spawn_time = this.spawn_time;
    info.death_time = this.death_time;
    return info;
  }

  // --------------------------------------------------------------- //
  public PowerUpType getType() {
    return type;
  }

  // --------------------------------------------------------------- //
  public int getSpawnTime() {
    return spawn_time;
  }

  // --------------------------------------------------------------- //
  public int getDeathTime() {
    return death_time;
  }

  // ===================== TextureGrid methods ===================== //
  //@formatter:off
  private static boolean is_initialized = false;
  private static int grid_index = -1;
  @Override protected void setInitialized() { is_initialized = true; }
  @Override protected boolean isInitialized() { return is_initialized; }
  @Override protected void setGridIndex(int index) { PowerUp.grid_index = index; }
  @Override protected int getGridIndex() { return grid_index; }

}
