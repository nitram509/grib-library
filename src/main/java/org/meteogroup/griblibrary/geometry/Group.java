package org.meteogroup.griblibrary.geometry;

import java.util.List;

public class Group<T extends HasGeometry> implements HasGeometry {

    private final List<T> list;
    private final Rectangle mbr;

    public Group(List<T> list) {
        this.list = list;
        this.mbr = GeometryCalculator.minimumBoundingRectangle(list);
    }

    public List<T> list() {
        return list;
    }

    @Override
    public Geometry geometry() {
        return mbr;
    }

}
