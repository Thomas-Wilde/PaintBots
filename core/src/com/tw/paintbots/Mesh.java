package com.tw.paintbots;

import com.badlogic.gdx.math.Vector2;

public class Mesh {
  // --------------------------------------------------------------- //
  public enum Shape {
    CIRCLE, RECTANGLE
  }

  // --------------------------------------------------------------- //
  private Shape shape_ = null;
  private double[] dim_ = null; // dimension of the mesh object
  private Vector2 pos_ = new Vector2(0.0f, 0.0f);

  // --------------------------------------------------------------- //
  /** Create a rectangle mesh width the specified width and height. */
  Mesh(double width, double height) {
    shape_ = Shape.RECTANGLE;
    dim_ = new double[] {width, height};
  }

  // --------------------------------------------------------------- //
  /** Create a circle mesh width the specified radius. */
  Mesh(double radius) {
    shape_ = Shape.CIRCLE;
    dim_ = new double[] {radius};
  }

  // --------------------------------------------------------------- //
  public Shape getShape() {
    return shape_;
  }

  /**
   * Returns size information. If the mesh has the shape of a circle a 1D array
   * is returned, that contains the [radius] of the circle. If the mesh has the
   * shape of a rectangle a 2D array is returned, that contains [width, height]
   */
  public double[] getDimensions() {
    return dim_;
  }

  // --------------------------------------------------------------- //
  /// Set the center position of the mesh.
  public void setPosition(Vector2 pos) {
    pos_ = pos;
  }

  // --------------------------------------------------------------- //
  /// Get the center position of the mesh.
  public Vector2 getPosition() {
    return pos_;
  }

  // --------------------------------------------------------------- //
  /// Return true if the mesh hits the mesh of the other object.
  public boolean hits(Mesh mesh) throws MeshException {
    if (shape_ == Shape.CIRCLE && mesh.shape_ == Shape.CIRCLE)
      return circleHitsCircle(this, mesh);

    if (shape_ == Shape.RECTANGLE && mesh.shape_ == Shape.RECTANGLE)
      return rectangleHitsRectangle(this, mesh);

    if (shape_ == Shape.CIRCLE && mesh.shape_ == Shape.RECTANGLE)
      return circleHitsRectangle(this, mesh);

    if (shape_ == Shape.RECTANGLE && mesh.shape_ == Shape.CIRCLE)
      return circleHitsRectangle(mesh, this);

    return false;
  }

  // --------------------------------------------------------------- //
  public static boolean circleHitsRectangle(Mesh circle, Mesh rectangle)
      throws MeshException {
    // ---
    if (circle.getShape() != Shape.CIRCLE
        || rectangle.getShape() != Shape.RECTANGLE)
      throw new MeshException("wrong mesh types for hit test");

    // ---
    throw new MeshException("Hit test not implemented");
  }

  // --------------------------------------------------------------- //
  public static boolean rectangleHitsRectangle(Mesh rect0, Mesh rect1)
      throws MeshException {
    // ---
    if (rect0.getShape() != Shape.RECTANGLE
        || rect1.getShape() != Shape.RECTANGLE)
      throw new MeshException("wrong mesh types for hit test");

    // ---
    throw new MeshException("Hit test not implemented");
  }

  // --------------------------------------------------------------- //
  /// Checks if the two given mesh circles collide.
  public static boolean circleHitsCircle(Mesh circle0, Mesh circle1)
      throws MeshException {
    // ---
    if (circle0.getShape() != Shape.CIRCLE
        || circle1.getShape() != Shape.CIRCLE)
      throw new MeshException("wrong mesh types for hit test");

    // ---
    Vector2 pos0 = circle0.pos_;
    Vector2 pos1 = circle1.pos_;
    double r0 = circle0.dim_[0];
    double r1 = circle1.dim_[0];

    // --- we used squared values; no need for roots here
    double dx = pos0.x - pos1.x;
    double dy = pos0.y - pos1.y;
    double dist_sqrd = dx * dx + dy * dy;
    double rs_sqrd = r0 * r0 + r1 * r1;

    // if distance is smaller than the sum of circle radii, we get a hit
    return (dist_sqrd < rs_sqrd);
  }
}
