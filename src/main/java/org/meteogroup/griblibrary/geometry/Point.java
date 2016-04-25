package org.meteogroup.griblibrary.geometry;

public final class Point implements Geometry {

    private final Rectangle mbr;

    public Point(double x, double y) {
        this((float) x, (float) y);
    }

    public Point(float x, float y) {
        this.mbr = new Rectangle(x, y, x, y);
    }

    @Override
    public Rectangle mbr() {
        return mbr;
    }

    @Override
    public double distance(Rectangle r) {
        return mbr.distance(r);
    }

    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    public double distanceSquared(Point p) {
        float dx = mbr().x1() - p.mbr().x1();
        float dy = mbr().y1() - p.mbr().y1();
        return dx * dx + dy * dy;
    }

    @Override
    public boolean intersects(Rectangle r) {
        return mbr.intersects(r);
    }

    public float x() {
        return mbr.x1();
    }

    public float y() {
        return mbr.y1();
    }

    @Override
    public String toString() {
        return "Point [x=" + x() + ", y=" + y() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return mbr.equals(point.mbr);

    }

    @Override
    public int hashCode() {
        return mbr.hashCode();
    }
}