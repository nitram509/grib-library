package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.geometry.Rectangle;
import org.meteogroup.griblibrary.grib1.model.Grib1LatLonGrib1GridDefinition;
import org.meteogroup.griblibrary.grib1.spec.GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE;
import org.meteogroup.griblibrary.util.BitChecker;

import static org.meteogroup.griblibrary.grib1.spec.GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE.EQUIDISTANT_CYLINDRICAL;
import static org.meteogroup.griblibrary.grib1.spec.GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE.GAUSSIAN;
import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asInt;
import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asSignedInt;

class LatLonGridDefinitionReader implements SundryGridDefinitionReader {

    private static final int POSITION_GDS_POINTS_ALONG_LATITUDE_CIRCLE_1 = 6;
    private static final int POSITION_GDS_POINTS_ALONG_LATITUDE_CIRCLE_2 = 7;
    private static final int POSITION_GDS_POINTS_ALONG_LONGITUDE_MERIDIAN_1 = 8;
    private static final int POSITION_GDS_POINTS_ALONG_LONGITUDE_MERIDIAN_2 = 9;
    private static final int POSITION_GDS_LAT_1_1 = 10;
    private static final int POSITION_GDS_LAT_1_2 = 11;
    private static final int POSITION_GDS_LAT_1_3 = 12;
    private static final int POSITION_GDS_LON_1_1 = 13;
    private static final int POSITION_GDS_LON_1_2 = 14;
    private static final int POSITION_GDS_LON_1_3 = 15;
    private static final int POSITION_GDS_RESOLUTION = 16;
    private static final int POSITION_GDS_LAT_2_1 = 17;
    private static final int POSITION_GDS_LAT_2_2 = 18;
    private static final int POSITION_GDS_LAT_2_3 = 19;
    private static final int POSITION_GDS_LON_2_1 = 20;
    private static final int POSITION_GDS_LON_2_2 = 21;
    private static final int POSITION_GDS_LON_2_3 = 22;
    private static final int POSITION_GDS_LONGITUDE_INCREMENT_1 = 23;
    private static final int POSITION_GDS_LONGITUDE_INCREMENT_2 = 24;
    private static final int POSITION_GDS_NUMBER_OF_CIRCLES_BETWEEN_POLE_AND_EQUATOR_1 = 25;
    private static final int POSITION_GDS_NUMBER_OF_CIRCLES_BETWEEN_POLE_AND_EQUATOR_2 = 26;
    private static final int POSITION_GDS_SCANNING_MODE_FLAGS = 27;

    private static final int SCANNING_MODE_I_BIT = 1;
    private static final int SCANNING_MODE_J_BIT = 2;
    private static final int SCANNING_MODE_DIRECTION_BIT = 3;

    private static final float MILLI_DEGREE_FACTOR = 1000f;

    @Override
    public boolean accepts(GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE dataRepresentationType) {
        return dataRepresentationType == EQUIDISTANT_CYLINDRICAL || dataRepresentationType == GAUSSIAN;
    }

    @Override
    public boolean accepts(byte dataRepresentationType) {
        return dataRepresentationType == EQUIDISTANT_CYLINDRICAL.id || dataRepresentationType == GAUSSIAN.id;
    }

    public Grib1LatLonGrib1GridDefinition read(byte[] buffer, int offset) {
        Grib1LatLonGrib1GridDefinition gridDefinition = new Grib1LatLonGrib1GridDefinition();
        gridDefinition.setPointsAlongLatitudeCircle(asInt(buffer[POSITION_GDS_POINTS_ALONG_LATITUDE_CIRCLE_1 + offset], buffer[POSITION_GDS_POINTS_ALONG_LATITUDE_CIRCLE_2 + offset]));
        gridDefinition.setPointsAlongLongitudeMeridian(asInt(buffer[POSITION_GDS_POINTS_ALONG_LONGITUDE_MERIDIAN_1 + offset], buffer[POSITION_GDS_POINTS_ALONG_LONGITUDE_MERIDIAN_2 + offset]));
        gridDefinition.setLat1InMilliDegree(asSignedInt(buffer[POSITION_GDS_LAT_1_1 + offset], buffer[POSITION_GDS_LAT_1_2 + offset], buffer[POSITION_GDS_LAT_1_3 + offset]));
        gridDefinition.setLon1InMilliDegree(asSignedInt(buffer[POSITION_GDS_LON_1_1 + offset], buffer[POSITION_GDS_LON_1_2 + offset], buffer[POSITION_GDS_LON_1_3 + offset]));
        gridDefinition.setResolution(asInt((buffer[POSITION_GDS_RESOLUTION + offset])));
        gridDefinition.setLat2InMilliDegree(asSignedInt(buffer[POSITION_GDS_LAT_2_1 + offset], buffer[POSITION_GDS_LAT_2_2 + offset], buffer[POSITION_GDS_LAT_2_3 + offset]));
        gridDefinition.setLon2InMilliDegree(asSignedInt(buffer[POSITION_GDS_LON_2_1 + offset], buffer[POSITION_GDS_LON_2_2 + offset], buffer[POSITION_GDS_LON_2_3 + offset]));
        gridDefinition.setLongitudeIncrement(asInt(buffer[POSITION_GDS_LONGITUDE_INCREMENT_1 + offset], buffer[POSITION_GDS_LONGITUDE_INCREMENT_2 + offset]) / MILLI_DEGREE_FACTOR);
        gridDefinition.setNumberOfCirclesBetweenPoleAndEquator(asInt(buffer[POSITION_GDS_NUMBER_OF_CIRCLES_BETWEEN_POLE_AND_EQUATOR_1 + offset], buffer[POSITION_GDS_NUMBER_OF_CIRCLES_BETWEEN_POLE_AND_EQUATOR_2 + offset]));

        //A positive I would come from a FALSE bit...
        gridDefinition.setScanModeIIsPositive(!BitChecker.testBit(buffer[POSITION_GDS_SCANNING_MODE_FLAGS + offset], SCANNING_MODE_I_BIT));
        //A positive J would come from a TRUE bit....
        gridDefinition.setScanModeJIsPositve(BitChecker.testBit(buffer[POSITION_GDS_SCANNING_MODE_FLAGS + offset], SCANNING_MODE_J_BIT));
        gridDefinition.setScanModeJIsConsectuve(BitChecker.testBit(buffer[POSITION_GDS_SCANNING_MODE_FLAGS + offset], SCANNING_MODE_DIRECTION_BIT));

        gridDefinition.setBoundingBox(new Rectangle(
                gridDefinition.getLon1InMilliDegree() / MILLI_DEGREE_FACTOR,
                gridDefinition.getLon2InMilliDegree() / MILLI_DEGREE_FACTOR,
                gridDefinition.getLat1InMilliDegree() / MILLI_DEGREE_FACTOR,
                gridDefinition.getLat2InMilliDegree() / MILLI_DEGREE_FACTOR
        ));

        return gridDefinition;
    }

}
