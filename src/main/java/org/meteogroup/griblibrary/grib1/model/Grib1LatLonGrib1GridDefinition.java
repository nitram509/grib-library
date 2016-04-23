package org.meteogroup.griblibrary.grib1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Grib1LatLonGrib1GridDefinition implements Grib1GridDefinition {
    private int pointsAlongLatitudeCircle;
    private int lat1;
    private int lat2;
    private int lon1;
    private int lon2;
    private int resolution;
    private float longitudeIncrement;
    private int numberOfCirclesBetweenPoleAndEquator;
    private int[] pointsAlongLatitudeCircleForGaussian;
    private int pointsAlongLongitudeMeridian;
    private float north;
    private float south;
    private boolean scanModeIIsPositive;
    private boolean scanModeJIsPositve;
    private boolean scanModeJIsConsectuve;
}
