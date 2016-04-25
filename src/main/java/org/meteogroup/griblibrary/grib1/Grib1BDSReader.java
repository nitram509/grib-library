package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.grib1.model.Grib1BinaryDataSection;
import org.meteogroup.griblibrary.util.BitChecker;

import java.util.Arrays;

import static java.util.Arrays.copyOfRange;
import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.bytesToFloatAsIBM;
import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asInt;

class Grib1BDSReader {

    private static final int POSITION_BDS_LENGTH_1 = 0;
    private static final int POSITION_BDS_LENGTH_2 = 1;
    private static final int POSITION_BDS_LENGTH_3 = 2;
    private static final int POSITION_BDS_FLAGS = 3;
    private static final int GRID_OR_SPHERICAL_BIT = 1;
    private static final int SIMPLE_OR_SECOND_ORDER_PACKING_BIT = 2;
    private static final int FLOAT_OR_INT_BIT = 3;
    private static final int POSITION_BDS_BINARY_SCALE_1 = 4;
    private static final int POSITION_BDS_BINARY_SCALE_2 = 5;
    private static final int BINARY_SCALE_SIGNING_BIT = 1;
    private static final int POSITION_BDS_REFERENCE_VALUE_1 = 6;
    private static final int POSITION_BDS_REFERENCE_VALUE_2 = 7;
    private static final int POSITION_BDS_REFERENCE_VALUE_3 = 8;
    private static final int POSITION_BDS_REFERENCE_VALUE_4 = 9;
    private static final int POSITION_BDS_DATUM = 10;
    private static final int POSITION_BDS_SLICE_POINT_FOR_STANDARD_PACKING = 11;

    public int readBDSLength(byte[] inputValues, int offSet) throws BinaryNumberConversionException {
        return asInt(inputValues[POSITION_BDS_LENGTH_1 + offSet], inputValues[POSITION_BDS_LENGTH_2 + offSet], inputValues[POSITION_BDS_LENGTH_3 + offSet]);
    }

    public Grib1BinaryDataSection readBDSValues(byte[] inputValues, int sectionOffset) throws BinaryNumberConversionException {
        Grib1BinaryDataSection binaryDataSection = new Grib1BinaryDataSection();
        binaryDataSection.setSectionLength(this.readBDSLength(inputValues, sectionOffset));
        binaryDataSection.setGridPointData(!BitChecker.testBit(inputValues[POSITION_BDS_FLAGS + sectionOffset], GRID_OR_SPHERICAL_BIT));
        binaryDataSection.setSphericalHarmonicCoefficient(BitChecker.testBit(inputValues[POSITION_BDS_FLAGS + sectionOffset], GRID_OR_SPHERICAL_BIT));
        binaryDataSection.setSimplePacking(!BitChecker.testBit(inputValues[POSITION_BDS_FLAGS + sectionOffset], SIMPLE_OR_SECOND_ORDER_PACKING_BIT));
        binaryDataSection.setSecondOrderPacking(BitChecker.testBit(inputValues[POSITION_BDS_FLAGS + sectionOffset], SIMPLE_OR_SECOND_ORDER_PACKING_BIT));
        binaryDataSection.setDataIsFloats(!BitChecker.testBit(inputValues[POSITION_BDS_FLAGS + sectionOffset], FLOAT_OR_INT_BIT));
        binaryDataSection.setDataIsInteger(BitChecker.testBit(inputValues[POSITION_BDS_FLAGS + sectionOffset], FLOAT_OR_INT_BIT));
        binaryDataSection.setBinaryScaleFactor(readBinaryScaleFactor(inputValues[POSITION_BDS_BINARY_SCALE_1 + sectionOffset], inputValues[POSITION_BDS_BINARY_SCALE_2 + sectionOffset]));
        binaryDataSection.setReferenceValue(bytesToFloatAsIBM(inputValues[POSITION_BDS_REFERENCE_VALUE_1 + sectionOffset], inputValues[POSITION_BDS_REFERENCE_VALUE_2 + sectionOffset], inputValues[POSITION_BDS_REFERENCE_VALUE_3 + sectionOffset], inputValues[POSITION_BDS_REFERENCE_VALUE_4 + sectionOffset]));
        binaryDataSection.setNumberOfBitsForDatumPoint(inputValues[POSITION_BDS_DATUM + sectionOffset]);
        final int fromOffset = sectionOffset + POSITION_BDS_SLICE_POINT_FOR_STANDARD_PACKING;
        final int toOffset = sectionOffset + binaryDataSection.getSectionLength();
        binaryDataSection.setPackedValues(copyOfRange(inputValues, fromOffset, toOffset));
        return binaryDataSection;
    }

    public short readBinaryScaleFactor(byte byte4, byte byte5) {
        boolean neg = BitChecker.testBit(byte4, BINARY_SCALE_SIGNING_BIT);
        int absoluteValue = byte5;
        return (short) (neg ? -1 * absoluteValue : absoluteValue);
    }

}
