package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.grib1.model.Grib1GridDefinition;
import org.meteogroup.griblibrary.grib1.spec.GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE;

interface SundryGridDefinitionReader {

    boolean accepts(GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE dataRepresentationType);
    boolean accepts(byte dataRepresentationType);

    Grib1GridDefinition read(byte[] buffer, int offset);

}
