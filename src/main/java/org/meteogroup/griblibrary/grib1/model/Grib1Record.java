package org.meteogroup.griblibrary.grib1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.meteogroup.griblibrary.grib.GribRecord;

/**
 * Created by roijen on 20-Oct-15.
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Grib1Record extends GribRecord {

    private static final int GRIB_VERSION_1 = 1;

    Grib1ProductDefinitionSection productDefinition = new Grib1ProductDefinitionSection();
    Grib1GridDescriptionSection gridDescription = new Grib1GridDescriptionSection();
    Grib1BinaryDataSection binaryData = new Grib1BinaryDataSection();

    public Grib1Record() {
        super(GRIB_VERSION_1);
    }
}
