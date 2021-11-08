package com.tw.paintbots;

// Basic class of entities/objects, that belong to the game.
// Basically everything is an entity.

public abstract class Entity {
  private static int id_counter_ = 0;
  private int id_ = -1;
  private String name_ = "entity";

  /// Create an Entity with a unique ID.
  protected Entity() {
    id_ = id_counter_++;
  }

  /// Create an Entity with a unique ID and the given name.
  protected Entity(String name) {
    name_ = name;
    id_ = id_counter_++;
  }

  /// Get the descriptive name of the Entity.
  public String getName() {
    return name_;
  }

  /// Get the ID of the Entity, which us unique.
  public int getID() {
    return id_;
  }

  /** Manually called at the end of the lifetime, e.g. to clean up resources. */
  public void destroy() {}

  /// This method is called once per loop and can be used to update the state of
  /// an entity.
  public abstract void update();
}
