package org.meteogroup.griblibrary.grib2;

import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asInt;

/**
 * @author Pauw
 */
class Grib2SectionReader {

    private static final int POSITION_LENGTH_1 = 0;
    private static final int POSITION_LENGTH_2 = 1;
    private static final int POSITION_LENGTH_3 = 2;
    private static final int POSITION_LENGTH_4 = 3;
    private static final int POSITION_SECTIONNUMBER = 4;

    protected int readSectionLength(byte[] bytes, final int headerOffSet) {
        return asInt(
                bytes[POSITION_LENGTH_1 + headerOffSet],
                bytes[POSITION_LENGTH_2 + headerOffSet],
                bytes[POSITION_LENGTH_3 + headerOffSet],
                bytes[POSITION_LENGTH_4 + headerOffSet]
        );

    }

    protected int readSectionNumber(byte[] bytes, int headerOffset) {
        return asInt(bytes[POSITION_SECTIONNUMBER + headerOffset]);
    }

}
