package org.meteogroup.griblibrary.geometry;

import java.awt.geom.Line2D;

import static java.lang.Math.min;

/**
 * A line segment.
 */
public final class Line implements Geometry {

    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;

    private Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    static Line create(float x1, float y1, float x2, float y2) {
        return new Line(x1, y1, x2, y2);
    }

    static Line create(double x1, double y1, double x2, double y2) {
        return new Line((float) x1, (float) y1, (float) x2, (float) y2);
    }

    @Override
    public double distance(Rectangle r) {
        if (r.contains(x1, y1) || r.contains(x2, y2)) {
            return 0;
        } else {
            double d1 = distance(r.x1(), r.y1(), r.x1(), r.y2());
            if (d1 == 0)
                return 0;
            double d2 = distance(r.x1(), r.y2(), r.x2(), r.y2());
            if (d2 == 0)
                return 0;
            double d3 = distance(r.x2(), r.y2(), r.x2(), r.y1());
            double d4 = distance(r.x2(), r.y1(), r.x1(), r.y1());
            return min(d1, min(d2, min(d3, d4)));
        }
    }

    private double distance(float x1, float y1, float x2, float y2) {
        Line2D.Float line = new Line2D.Float(x1, y1, x2, y2);
        double d1 = line.ptSegDist(this.x1, this.y1);
        double d2 = line.ptSegDist(this.x2, this.y2);
        Line2D.Float line2 = new Line2D.Float(this.x1, this.y1, this.x2, this.y2);
        double d3 = line2.ptSegDist(x1, y1);
        if (d3 == 0)
            return 0;
        double d4 = line2.ptSegDist(x2, y2);
        if (d4 == 0)
            return 0;
        else
            return min(d1, min(d2, min(d3, d4)));

    }

    @Override
    public Rectangle mbr() {
        return Geometries.rectangle(min(x1, x2), min(y1, y2), Math.max(x1, x2),
                Math.max(y1, y2));
    }

    @Override
    public boolean intersects(Rectangle r) {
        throw new UnsupportedOperationException("Not yet implemented");
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

    public boolean intersects(Line b) {
        Line2D line1 = new Line2D.Float(x1, y1, x2, y2);
        Line2D line2 = new Line2D.Float(b.x1(), b.y1(), b.x2(), b.y2());
        return line2.intersectsLine(line1);
    }

    public boolean intersects(Point point) {
        return intersects(point.mbr());
    }

    public boolean intersects(Circle circle) {
        // using Vector Projection
        // https://en.wikipedia.org/wiki/Vector_projection
        Vector c = Vector.create(circle.x(), circle.y());
        Vector a = Vector.create(x1, y1);
        Vector cMinusA = c.minus(a);
        float radiusSquared = circle.radius() * circle.radius();
        if (x1 == x2 && y1 == y2) {
            return cMinusA.modulusSquared() <= radiusSquared;
        } else {
            Vector b = Vector.create(x2, y2);
            Vector bMinusA = b.minus(a);
            float bMinusAModulus = bMinusA.modulus();
            float lambda = cMinusA.dot(bMinusA) / bMinusAModulus;
            // if projection is on the segment
            if (lambda >= 0 && lambda <= bMinusAModulus) {
                Vector dMinusA = bMinusA.times(lambda / bMinusAModulus);
                // calculate distance to line from c using pythagoras' theorem
                return cMinusA.modulusSquared() - dMinusA.modulusSquared() <= radiusSquared;
            } else {
                // return true if and only if an endpoint is within radius of
                // centre
                return cMinusA.modulusSquared() <= radiusSquared
                        || c.minus(b).modulusSquared() <= radiusSquared;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (Float.compare(line.x1, x1) != 0) return false;
        if (Float.compare(line.y1, y1) != 0) return false;
        if (Float.compare(line.x2, x2) != 0) return false;
        return Float.compare(line.y2, y2) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x1 != +0.0f ? java.lang.Float.floatToIntBits(x1) : 0);
        result = 31 * result + (y1 != +0.0f ? java.lang.Float.floatToIntBits(y1) : 0);
        result = 31 * result + (x2 != +0.0f ? java.lang.Float.floatToIntBits(x2) : 0);
        result = 31 * result + (y2 != +0.0f ? java.lang.Float.floatToIntBits(y2) : 0);
        return result;
    }

    private static final class Vector {
        final float x;
        final float y;

        static Vector create(float x, float y) {
            return new Vector(x, y);
        }

        Vector(float x, float y) {
            this.x = x;
            this.y = y;
        }

        float dot(Vector v) {
            return x * v.x + y * v.y;
        }

        Vector times(float value) {
            return create(value * x, value * y);
        }

        Vector minus(Vector v) {
            return create(x - v.x, y - v.y);
        }

        float modulus() {
            return (float) Math.sqrt(x * x + y * y);
        }

        float modulusSquared() {
            return x * x + y * y;
        }

    }

}