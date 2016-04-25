package org.meteogroup.griblibrary.grib1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.meteogroup.griblibrary.geometry.Rectangle;

@Getter
@Setter
@EqualsAndHashCode
public class Grib1LatLonGrib1GridDefinition implements Grib1GridDefinition {
    private int pointsAlongLatitudeCircle;
    private int lat1InMilliDegree;
    private int lat2InMilliDegree;
    private int lon1InMilliDegree;
    private int lon2InMilliDegree;
    private int resolution;
    private float longitudeIncrement;
    private int numberOfCirclesBetweenPoleAndEquator;
    private int[] pointsAlongLatitudeCircleForGaussian;
    private int pointsAlongLongitudeMeridian;
    private Rectangle boundingBox;
    private boolean scanModeIIsPositive;
    private boolean scanModeJIsPositve;
    private boolean scanModeJIsConsectuve;
}
