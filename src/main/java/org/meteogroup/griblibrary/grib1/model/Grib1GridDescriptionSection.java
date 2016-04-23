package org.meteogroup.griblibrary.grib1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.meteogroup.griblibrary.gis.LatLon;

/**
 * Created by roijen on 23-Oct-15.
 */
@Getter
@Setter
public class Grib1GridDescriptionSection {

    private int sectionLenght;
    private byte numberOfVerticalsCoordinateParams;
    private int locationOfVerticalCoordinateParams;
    private int locationListPer;
    private byte dataRepresentationType;
    private int numberOfPoints;

    Grib1GridDefinition gridDefinition;

    private LatLon[] latLons;
}
