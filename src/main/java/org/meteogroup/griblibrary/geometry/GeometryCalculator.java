package org.meteogroup.griblibrary.geometry;

import java.util.Collection;

public class GeometryCalculator {

    /**
     * Returns the minimum bounding rectangle of a number of items. Benchmarks
     * below indicate that when the number of items is &gt;1 this method is more
     * performant than one using {@link Rectangle#add(Rectangle)}.
     * <p>
     * <pre>
     * Benchmark                             Mode  Samples         Score  Score error  Units
     * c.g.d.r.BenchmarksMbr.mbrList1       thrpt       10  48450492.301   436127.960  ops/s
     * c.g.d.r.BenchmarksMbr.mbrList2       thrpt       10  46658242.728   987901.581  ops/s
     * c.g.d.r.BenchmarksMbr.mbrList3       thrpt       10  40357809.306   937827.660  ops/s
     * c.g.d.r.BenchmarksMbr.mbrList4       thrpt       10  35930532.557   605535.237  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList1    thrpt       10  55848118.198  1342997.309  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList2    thrpt       10  25171873.903   395127.918  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList3    thrpt       10  19222116.139   246965.178  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList4    thrpt       10  14891862.638   198765.157  ops/s
     * </pre>
     *
     * @param items items to bound
     * @return the minimum bounding rectangle containings items
     */
    public static Rectangle minimumBoundingRectangle(Collection<? extends HasGeometry> items) {
        assert (!items.isEmpty());
        float minX1 = Float.MAX_VALUE;
        float minY1 = Float.MAX_VALUE;
        float maxX2 = -Float.MAX_VALUE;
        float maxY2 = -Float.MAX_VALUE;
        for (final HasGeometry item : items) {
            Rectangle r = item.geometry().mbr();
            if (r.x1() < minX1)
                minX1 = r.x1();
            if (r.y1() < minY1)
                minY1 = r.y1();
            if (r.x2() > maxX2)
                maxX2 = r.x2();
            if (r.y2() > maxY2)
                maxY2 = r.y2();
        }
        return Geometries.rectangle(minX1, minY1, maxX2, maxY2);
    }

}