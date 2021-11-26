package com.tw.paintbots;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

interface Renderable {
  public void render(SpriteBatch batch);

  public void render(SpriteBatch batch, int[] position);

  public int getRenderLayer();

  public void setRenderOffset(int[] offset);

  public int[] getRenderOffset();
}
