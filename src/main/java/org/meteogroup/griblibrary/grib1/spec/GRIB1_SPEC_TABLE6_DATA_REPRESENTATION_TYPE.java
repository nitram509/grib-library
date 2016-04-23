package org.meteogroup.griblibrary.grib1.spec;

public enum GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE {
    EQUIDISTANT_CYLINDRICAL(0, "Equidistant Cylindrical", "Latitude/Longitude Grid also called Equidistant Cylindrical or Plate Carree projection grid"),
    MERCATOR(1, "Mercator", "Mercator Projection Grid"),
    GNOMIC(2, "Gnomonic", "Gnomonic Projection Grid"),
    LAMBERT_CONFORMAL(3, "Lambert Conformal", "Lambert Conformal, secant or tangent, conical or bipolar (normal or oblique) Projection Grid"),
    GAUSSIAN(4, "GAUSSIAN", "Gaussian Latitude/Longitude Grid"),
    POLAR_STEREOGRAPHIC(5, "Polar Stereographic", "Polar Stereographic Projection Grid"),
    //GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE(6 - 12	(reserved - see Manual on Codes)
    OBLIQUE_LAMBERT_CONFORMAL(13, "Oblique Lambert conformal", "Oblique Lambert conformal, secant or tangent, conical or bipolar, projection"),
    //GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE(14 - 49	(reserved - see Manual on Codes)
    SPHERICAL_HARMONIC(50, "Spherical Harmonic", "Spherical Harmonic Coefficients"),
    //GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE(51 - 89	(reserved - see Manual on Codes)
    ORTHOGRAPHIC(90, "Orthographic", "Space view perspective or orthographic grid");
    //GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE(91 - 254	(reserved - see Manual on Codes);


    public final byte id;
    public final String shortName;
    public final String longName;

    GRIB1_SPEC_TABLE6_DATA_REPRESENTATION_TYPE(int ID, String shortName, String longName) {
        id = (byte) ID;
        this.shortName = shortName;
        this.longName = longName;
    }
}
