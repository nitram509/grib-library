package org.meteogroup.griblibrary.grib1.spec;

public class DataRepresentationTypeResolver {

    public GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE resolve(byte dataRepresentationType) {
        for (GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE representationType : GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE.values()) {
            if (representationType.id == dataRepresentationType)
                return representationType;
        }
        throw new IllegalArgumentException("Unknown dataRepresentationType=" + dataRepresentationType);
    }

    public String resolveShortName(byte dataRepresentationType) {
        for (GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE representationType : GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE.values()) {
            if (representationType.id == dataRepresentationType)
                return representationType.shortName.length() > 0 ? representationType.shortName : representationType.longName;
        }
        return "unknown(id=" + dataRepresentationType + ")";
    }

}
