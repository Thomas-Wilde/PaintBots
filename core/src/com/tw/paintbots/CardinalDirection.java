package com.tw.paintbots;

public enum CardinalDirection {
  E(0), NE(1), N(2), NW(3), W(4), SW(5), S(6), SE(7);

  private final int id_;

  CardinalDirection(int id) {
    id_ = id;
  }

  public int get() {
    return id_;
  }
}
