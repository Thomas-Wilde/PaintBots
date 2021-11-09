package com.tw.paintbots;

public enum PlayerType {
  NONE(0), HUMAN(1), AI(2);

  private final int type;

  PlayerType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }
}
