package org.meteogroup.griblibrary.gis;

/**
 * Created by roijen on 26-Oct-15.
 */
public class LatLon {

    public final float lat;
    public final float lon;

    public LatLon(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatLon latLon = (LatLon) o;
        if (Float.compare(latLon.lat, lat) != 0) return false;
        return Float.compare(latLon.lon, lon) == 0;
    }

    @Override
    public int hashCode() {
        int result = (lat != +0.0f ? Float.floatToIntBits(lat) : 0);
        result = 31 * result + (lon != +0.0f ? Float.floatToIntBits(lon) : 0);
        return result;
    }
}
