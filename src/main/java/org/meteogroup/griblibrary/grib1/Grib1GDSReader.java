package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.grib1.model.Grib1GridDescriptionSection;
import org.meteogroup.griblibrary.grib1.model.Grib1LatLonGrib1GridDefinition;

import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asInt;

/**
 * Created by roijen on 23-Oct-15.
 */
class Grib1GDSReader {

    private static final int POSITION_GDS_LENGTH_1 = 0;
    private static final int POSITION_GDS_LENGTH_2 = 1;
    private static final int POSITION_GDS_LENGTH_3 = 2;
    private static final int POSITION_GDS_NUMBER_OF_VERTICAL_COORDINATE_PARAMS = 3;
    private static final int POSITION_GDS_LOCATION_OF_VERTICAL_PARAMS = 4;
    private static final int POSITION_GDS_REPRESENTATION_TYPE = 5;

    private final LatLonGridDefinitionReader latLonGridDefinitionReader = new LatLonGridDefinitionReader();

    public int readGDSLength(byte[] inputValues, int offSet) {
        return asInt(
                inputValues[POSITION_GDS_LENGTH_1 + offSet],
                inputValues[POSITION_GDS_LENGTH_2 + offSet],
                inputValues[POSITION_GDS_LENGTH_3 + offSet]
        );
    }

    public Grib1GridDescriptionSection readGDSValues(byte[] buffer, int offset) {
        Grib1GridDescriptionSection gridDescriptionSection = new Grib1GridDescriptionSection();
        gridDescriptionSection.setSectionLenght(readGDSLength(buffer, offset));
        gridDescriptionSection.setNumberOfVerticalsCoordinateParams(buffer[POSITION_GDS_NUMBER_OF_VERTICAL_COORDINATE_PARAMS + offset]);
        gridDescriptionSection.setLocationOfVerticalCoordinateParams(buffer[POSITION_GDS_LOCATION_OF_VERTICAL_PARAMS + offset]);
        gridDescriptionSection.setDataRepresentationType(buffer[POSITION_GDS_REPRESENTATION_TYPE + offset]);
        assert latLonGridDefinitionReader.accepts(gridDescriptionSection.getDataRepresentationType());
        gridDescriptionSection.setGridDefinition(latLonGridDefinitionReader.read(buffer, offset));
        gridDescriptionSection = generateNisAndNumberOfPoints(gridDescriptionSection, buffer, offset);
        return gridDescriptionSection;
    }

    Grib1GridDescriptionSection generateNisAndNumberOfPoints(Grib1GridDescriptionSection gds, byte[] buffer, int offSet) {
        final Grib1LatLonGrib1GridDefinition gridDefinition = (Grib1LatLonGrib1GridDefinition) gds.getGridDefinition();
        int[] nis = new int[gridDefinition.getPointsAlongLongitudeMeridian()];
        int numberOfPoints = 0;
        for (int x = 0; x < gridDefinition.getPointsAlongLongitudeMeridian(); x++) {
            //Position -1 (for array) +x*2 (for pos)
            int position = offSet + (gds.getLocationOfVerticalCoordinateParams() + (x * 2) - 1);
            nis[x] = asInt(buffer[position], buffer[position + 1]);
            numberOfPoints += nis[x];
        }
        gds.setNumberOfPoints(numberOfPoints);
        gridDefinition.setPointsAlongLatitudeCircleForGaussian(nis);
        return gds;
    }
}
