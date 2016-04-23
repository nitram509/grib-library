package org.meteogroup.griblibrary.util;

/**
 * Created by roijen on 21-Oct-15.
 */
public class BytesToPrimitiveHelper {

    public static final int INT_BYTE_MASK = 0xff;
    public static final long LONG_BYTE_MASK = 0xffL;

    public static float bytesToFloatAsIBM(final byte highestValue, final byte highValue, final byte lowValue, final byte lowestValue) {
        //TODO Check signing...
        int sgn, mant, exp;
        mant = (highValue & 0xff) << 16 | (lowValue & 0xff) << 8 | lowestValue & 0xff;
        if (mant == 0) {
            return 0.0f;
        }
        sgn = -((((highestValue & 0xff) & 128) >> 6) - 1);
        exp = ((highestValue & 0xff) & 127) - 64;
        return (float) (sgn * Math.pow(16.0, exp - 6) * mant);
    }

    public static float bytesToFloat(byte... inputValue) {
        if (inputValue.length == 4) {
            int bits = asInt(inputValue[0], inputValue[1], inputValue[2], inputValue[3]);
            return Float.intBitsToFloat(bits);
        }
        throw new IllegalArgumentException("Invalid length of input value in an attempt to convert byte array to int");
    }

    public static long asLong(final byte value7, final byte value6, final byte value5, final byte value4, final byte value3, final byte value2, final byte value1, final byte value0) {
        return ((value7 & LONG_BYTE_MASK) << 56)
                | ((value6 & LONG_BYTE_MASK) << 48)
                | ((value5 & LONG_BYTE_MASK) << 40)
                | ((value4 & LONG_BYTE_MASK) << 32)
                | ((value3 & LONG_BYTE_MASK) << 24)
                | ((value2 & LONG_BYTE_MASK) << 16)
                | ((value1 & LONG_BYTE_MASK) << 8)
                | (value0 & LONG_BYTE_MASK);
    }

    public static final short asShort(final byte highValue, final byte lowValue) {
        return (short) (((highValue & INT_BYTE_MASK) << 8)
                | (lowValue & INT_BYTE_MASK));
    }

    public static final short asShort(final byte lowValue) {
        return (short) ((lowValue) & INT_BYTE_MASK);
    }

    public static final int asInt(byte highestValue, byte highValue, byte lowValue, byte lowestValue) {
        return ((highestValue & INT_BYTE_MASK) << 24)
                | ((highValue & INT_BYTE_MASK) << 16)
                | ((lowValue & INT_BYTE_MASK) << 8)
                | (lowestValue & INT_BYTE_MASK);
    }

    public static final int asInt(final byte highestValue, final byte highValue, final byte lowValue) {
        return ((highestValue & INT_BYTE_MASK) << 16)
                | ((highValue & INT_BYTE_MASK) << 8)
                | (lowValue & INT_BYTE_MASK);
    }

    public static final int asInt(final byte highValue, final byte lowValue) {
        return ((highValue & INT_BYTE_MASK) << 8)
                | (lowValue & INT_BYTE_MASK);
    }

    public static final int asInt(final byte lowValue) {
        return lowValue & INT_BYTE_MASK;
    }

    public static final int asSignedInt(final byte highestValue, final byte highValue, final byte lowValue, final byte lowestValue) {
        final int intVal = ((highestValue & 0b0111_1111) << 24)
                | ((highValue & INT_BYTE_MASK) << 16)
                | ((lowValue & INT_BYTE_MASK) << 8)
                | (lowestValue & INT_BYTE_MASK);
        return (highestValue & 0b1000_0000) == 0b1000_0000 ? -1 * intVal : intVal;
    }

    public static final int asSignedInt(final byte highestValue, final byte highValue, final byte lowValue) {
        final int intVal = ((highestValue & 0b0111_1111) << 16)
                | ((highValue & INT_BYTE_MASK) << 8)
                | (lowValue & INT_BYTE_MASK);
        return (highestValue & 0b1000_0000) == 0b1000_0000 ? -1 * intVal : intVal;
    }

    public static final int asSignedInt(final byte highValue, final byte lowValue) {
        final int intVal = ((highValue & 0b0111_1111) << 8)
                | (lowValue & INT_BYTE_MASK);
        return (highValue & 0b1000_0000) == 0b1000_0000 ? -1 * intVal : intVal;
    }

    public static final int asSignedInt(final byte value) {
        final int intVal = value & 0b0111_1111;
        return (value & 0b1000_0000) == 0b1000_0000 ? -1 * intVal : intVal;
    }
}