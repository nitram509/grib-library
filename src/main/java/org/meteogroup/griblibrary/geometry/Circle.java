package org.meteogroup.griblibrary.geometry;

import static org.meteogroup.griblibrary.geometry.Geometries.point;

public final class Circle implements Geometry {

    private final float x, y, radius;
    private final Rectangle mbr;

    protected Circle(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mbr = new Rectangle(x - radius, y - radius, x + radius, y + radius);
    }

    static Circle create(double x, double y, double radius) {
        return new Circle((float) x, (float) y, (float) radius);
    }

    static Circle create(float x, float y, float radius) {
        return new Circle(x, y, radius);
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float radius() {
        return radius;
    }

    @Override
    public Rectangle mbr() {
        return mbr;
    }

    @Override
    public double distance(Rectangle r) {
        return Math.max(0, point(x, y).distance(r) - radius);
    }

    @Override
    public boolean intersects(Rectangle r) {
        return distance(r) == 0;
    }

    public boolean intersects(Circle c) {
        double total = radius + c.radius;
        return point(x, y).distanceSquared(point(c.x, c.y)) <= total * total;
    }


    public boolean intersects(Point point) {
        return Math.sqrt(sqr(x - point.x()) + sqr(y - point.y())) <= radius;
    }

    private static final float sqr(final float x) {
        return x * x;
    }

    public boolean intersects(Line line) {
        return line.intersects(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Circle circle = (Circle) o;

        if (Float.compare(circle.x, x) != 0) return false;
        if (Float.compare(circle.y, y) != 0) return false;
        if (Float.compare(circle.radius, radius) != 0) return false;
        return mbr != null ? mbr.equals(circle.mbr) : circle.mbr == null;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        result = 31 * result + (mbr != null ? mbr.hashCode() : 0);
        return result;
    }
}
