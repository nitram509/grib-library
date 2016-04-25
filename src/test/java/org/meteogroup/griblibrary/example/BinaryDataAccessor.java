package org.meteogroup.griblibrary.example;

import org.meteogroup.griblibrary.gis.LatLon;
import org.meteogroup.griblibrary.grib1.model.*;

public class BinaryDataAccessor {

    private final Grib1GridDescriptionSection gridDescription;
    private final Grib1BinaryDataSection binaryData;
    private final Grib1ProductDefinitionSection productDefinition;

    public BinaryDataAccessor(Grib1Record gribRecord) {
        gridDescription = gribRecord.getGridDescription();
        binaryData = gribRecord.getBinaryData();
        productDefinition = gribRecord.getProductDefinition();
    }

    public void getValues(LatLon latLon) {
        final Grib1LatLonGrib1GridDefinition gridDefinition = (Grib1LatLonGrib1GridDefinition) gridDescription.getGridDefinition();
//        gridDefinition.

        if (productDefinition.getIdenticatorOfParameterAndUnit() != (byte)0xD3) return;

        byte value = binaryData.getPackedValues()[123];
    }
}
