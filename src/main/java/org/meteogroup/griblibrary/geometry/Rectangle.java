package org.meteogroup.griblibrary.geometry;

import static java.lang.Math.max;
import static java.lang.Math.min;

public final class Rectangle implements Geometry, HasGeometry {

    private final float x1, y1, x2, y2;

    public Rectangle(float x1, float y1, float x2, float y2) {
        this.x1 = min(x1, x2);
        this.y1 = min(y1, y2);
        this.x2 = max(x1, x2);
        this.y2 = max(y1, y2);
    }

    public Rectangle(double x1, double y1, double x2, double y2) {
        this((float) x1, (float) x2, (float) y1, (float) y2);
    }

    public float north() {
        return y2;
    }

    public float east() {
        return x2;
    }

    public float south() {
        return y1;
    }

    public float west() {
        return x1;
    }

    public float x1() {
        return x1;
    }

    public float y1() {
        return y1;
    }

    public float x2() {
        return x2;
    }

    public float y2() {
        return y2;
    }

    public float area() {
        return (x2 - x1) * (y2 - y1);
    }

    public Rectangle add(Rectangle r) {
        return new Rectangle(min(x1, r.x1), min(y1, r.y1), max(x2, r.x2), max(y2, r.y2));
    }

    public boolean contains(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    @Override
    public boolean intersects(Rectangle r) {
        return r.x2 >= x1 && r.x1 <= x2 && r.y2 >= y1 && r.y1 <= y2;
    }

    @Override
    public double distance(Rectangle r) {
        if (intersects(r))
            return 0;
        else {
            Rectangle mostLeft = x1 < r.x1 ? this : r;
            Rectangle mostRight = x1 > r.x1 ? this : r;
            double xDifference = max(0,
                    mostLeft.x1 == mostRight.x1 ? 0 : mostRight.x1 - mostLeft.x2);

            Rectangle upper = y1 < r.y1 ? this : r;
            Rectangle lower = y1 > r.y1 ? this : r;

            double yDifference = max(0, upper.y1 == lower.y1 ? 0 : lower.y1 - upper.y2);

            return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
        }
    }

    @Override
    public Rectangle mbr() {
        return this;
    }

    @Override
    public String toString() {
        return "Rectangle [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + "]";
    }

    public float intersectionArea(Rectangle r) {
        if (!intersects(r))
            return 0;
        else
            return new Rectangle(max(x1, r.x1), max(y1, r.y1), min(x2, r.x2), min(y2, r.y2)).area();
    }

    public float perimeter() {
        return 2 * (x2 - x1) + 2 * (y2 - y1);
    }

    @Override
    public Geometry geometry() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (Float.compare(rectangle.x1, x1) != 0) return false;
        if (Float.compare(rectangle.y1, y1) != 0) return false;
        if (Float.compare(rectangle.x2, x2) != 0) return false;
        return Float.compare(rectangle.y2, y2) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x1 != +0.0f ? Float.floatToIntBits(x1) : 0);
        result = 31 * result + (y1 != +0.0f ? Float.floatToIntBits(y1) : 0);
        result = 31 * result + (x2 != +0.0f ? Float.floatToIntBits(x2) : 0);
        result = 31 * result + (y2 != +0.0f ? Float.floatToIntBits(y2) : 0);
        return result;
    }
}