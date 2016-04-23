package org.meteogroup.griblibrary.util;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.*;

/**
 * Created by roijen on 21-Oct-15.
 */
public class BytesToPrimitiveHelperTest {

    @Test
    public void testIBMFloatWithLengthOfFour() {
        float value = bytesToFloatAsIBM((byte) 0b0100_0010, (byte) (0b1101_0000 - 256), (byte) 0b0100_0001, (byte) 0b0011_0100);
        assertThat(value).isCloseTo(208.255f, within(0.001f));
    }

    @Test
    public void testFloatWithLengthOfFour() throws BinaryNumberConversionException {
        float value = BytesToPrimitiveHelper.bytesToFloat((byte) 67, (byte) 88, (byte) 0, (byte) -57);
        assertThat(value).isCloseTo(216.003f, within(0.001f));
    }

    @Test
    public void two_byte_converted_as_short() throws BinaryNumberConversionException {
        short aShort = asShort((byte) 0xca, (byte) 0xfe);
        assertThat(aShort).isEqualTo((short) 0xcafe);

        aShort = asShort((byte) 1, (byte) 0);
        assertThat(aShort).isEqualTo((short) 256);

        aShort = asShort((byte) 1);
        assertThat(aShort).isEqualTo((short) 1);
    }

    @Test
    public void one_byte_converted_as_short() {
        short aShort = asShort((byte) 0xff);
        assertThat(aShort).isEqualTo((short) 255);

        aShort = asShort((byte) 1);
        assertThat(aShort).isEqualTo((short) 1);
    }

    @Test
    public void one_byte_converted_as_int() {
        int anInt = asInt((byte) 0xff);
        assertThat(anInt).isEqualTo(255);

        anInt = asInt((byte) 1);
        assertThat(anInt).isEqualTo(1);
    }

    @Test
    public void two_bytes_converted_as_int() {
        int anInt = asInt((byte) 0xff, (byte) 0xff);
        assertThat(anInt).isEqualTo(0x0000ffff);

        anInt = asInt((byte) 0, (byte) 1);
        assertThat(anInt).isEqualTo(1);

        anInt = asInt((byte) 1, (byte) 0);
        assertThat(anInt).isEqualTo(256);
    }

    @Test
    public void thee_bytes_converted_as_int() {
        int anInt = asInt((byte) 0xff, (byte) 0xff, (byte) 0xff);
        assertThat(anInt).isEqualTo(0x00ffffff);

        anInt = asInt((byte) 0, (byte) 0, (byte) 1);
        assertThat(anInt).isEqualTo(1);

        anInt = asInt((byte) 0, (byte) 1, (byte) 0);
        assertThat(anInt).isEqualTo(256);

        anInt = asInt((byte) 1, (byte) 0, (byte) 0);
        assertThat(anInt).isEqualTo(65536);
    }

    @Test
    public void four_bytes_converted_as_int() {
        int anInt = asInt((byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff);
        assertThat(anInt).isEqualTo(0xffffffff);

        anInt = asInt((byte) 0, (byte) 0, (byte) 0, (byte) 1);
        assertThat(anInt).isEqualTo(1);

        anInt = asInt((byte) 0, (byte) 0, (byte) 1, (byte) 0);
        assertThat(anInt).isEqualTo(256);

        anInt = asInt((byte) 0, (byte) 1, (byte) 0, (byte) 0);
        assertThat(anInt).isEqualTo(65536);

        anInt = asInt((byte) 1, (byte) 0, (byte) 0, (byte) 0);
        assertThat(anInt).isEqualTo(16777216);
    }

    @Test
    public void one_bytes_converted_as_signed_int() {
        int value = asSignedInt((byte) 0b10000101);
        assertThat(value).isEqualTo(-5);
    }

    @Test
    public void two_bytes_converted_as_signed_int() {
        int value = asSignedInt((byte) 0b01011011, (byte) 0b10100110);
        assertThat(value).isEqualTo(0b00000000_00000000_01011011_10100110);

        value = asSignedInt((byte) 0b10000000, (byte) 0b00000101);
        assertThat(value).isEqualTo(-5);
    }

    @Test
    public void three_bytes_converted_as_signed_int() {
        int value = asSignedInt((byte) 0b00000101, (byte) 0b1011011, (byte) 0b10100110);
        assertThat(value).isEqualTo(0b00000000_00000101_01011011_10100110);

        value = asSignedInt((byte) 0b10000000, (byte) 0b00000000, (byte) 0b00000101);
        assertThat(value).isEqualTo(-5);
    }

    @Test
    public void four_bytes_converted_as_signed_int() {
        int value = asSignedInt((byte) 0b00000101, (byte) 0b1011011, (byte) 0b10100110, (byte) 0b101100);
        assertThat(value).isEqualTo(0b00000101_01011011_10100110_00101100);

        value = asSignedInt((byte) 0b10000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000101);
        assertThat(value).isEqualTo(-5);
    }

    @Test
    public void eight_bytes_converted_as_long() {
        long value = asLong((byte) 0b00000101, (byte) 0b1011011, (byte) 0b10100110, (byte) 0b101100, (byte) 0b00000101, (byte) 0b1011011, (byte) 0b10100110, (byte) 0b101100);
        assertThat(value).isEqualTo(0b00000101_01011011_10100110_00101100_00000101_01011011_10100110_00101100L);

        value = asLong((byte) 0b10000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000101);
        assertThat(value).isEqualTo(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_00000101L);
    }

}