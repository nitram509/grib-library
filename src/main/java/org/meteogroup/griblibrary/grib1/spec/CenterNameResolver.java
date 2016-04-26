package org.meteogroup.griblibrary.grib1.spec;

public class CenterNameResolver {

    public String resolveShortName(byte centerId) {
        for (GRIB1_SPEC_TABLE0_ORIGINATING_CENTERS center : GRIB1_SPEC_TABLE0_ORIGINATING_CENTERS.values()) {
            if (center.centerId == centerId) return center.shortName.length() > 0 ? center.shortName : center.longName;
        }
        return "unknown(centerId=" + centerId + ")";
    }

    public String resolveLongtName(byte centerId) {
        for (GRIB1_SPEC_TABLE0_ORIGINATING_CENTERS center : GRIB1_SPEC_TABLE0_ORIGINATING_CENTERS.values()) {
            if (center.centerId == centerId) return center.longName;
        }
        return "unknown(centerId=" + centerId + ")";
    }

    public enum GRIB1_SPEC_TABLE0_ORIGINATING_CENTERS {
        USWSNMC(7, "", "US Weather Service - National Met. Center"),
        USWSTG(8, "", "US Weather Service - NWS Telecomms Gateway"),
        USWSFS(9, "", "US Weather Service - Field Stations"),
        JMAT(34, "", "Japanese Meteorological Agency - Tokyo"),
        NHCM(52, "", "National Hurricane Center, Miami"),
        CMSM(54, "", "Canadian Meteorological Service - Montreal"),
        USAFGWC(57, "", "U.S. Air Force - Global Weather Center"),
        USNFNOC(58, "", "US Navy - Fleet Numerical Oceanography Center"),
        NOAAFSLBCO(59, "", "NOAA Forecast Systems Lab, Boulder CO"),
        UKMO(74, "UKMO", "U.K. Met Office - Bracknell"),
        FWST(85, "", "French Weather Service - Toulouse"),
        ESAESA(97, "ESA", "European Space Agency (ESA)"),
        ECMWF(98, "ECMWF", "European Center for Medium-Range Weather Forecasts - Reading"),
        DE_BILT(99, "", "DeBilt, Netherlands");

        public final byte centerId;
        public final String shortName;
        public final String longName;

        GRIB1_SPEC_TABLE0_ORIGINATING_CENTERS(int ID, String shortName, String longName) {
            centerId = (byte) ID;
            this.shortName = shortName;
            this.longName = longName;
        }
    }

}
