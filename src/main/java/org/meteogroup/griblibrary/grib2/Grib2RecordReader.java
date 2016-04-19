package org.meteogroup.griblibrary.grib2;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.exception.GribReaderException;
import org.meteogroup.griblibrary.grib.GribReaderFactory;
import org.meteogroup.griblibrary.grib2.model.*;
import org.meteogroup.griblibrary.util.BytesToPrimitiveHelper;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by roijen on 28-Oct-15.
 */
public class Grib2RecordReader {

    private static final Charset ASCII = Charset.forName("ASCII");

    public Grib2RecordReader() {
        idsReader = new Grib2IDSReader();
        lusReader = new Grib2LUSReader();
        gdsReader = new Grib2GDSReader();
        pdsReader = new Grib2PDSReader();
        drsReader = new Grib2DRSReader();
        bmsReader = new Grib2BMSReader();
        dsReader = new Grib2DSReader();
    }

    public static boolean checkIfGribFileIsValidGrib2(byte[] recordHeader) {
        if (recordHeader.length < GribReaderFactory.GRIB_HEADER_LENGTH) {
            throw new IllegalArgumentException("To check GRIB header, minimum 8 bytes need to be available. Actual length=" + recordHeader.length);
        }
        String headerAsString = new String(recordHeader, 0, GRIB_WORD_LENGTH, ASCII);
        short versionNumber = recordHeader[POSITION_VERSION_NUMBER];
        return (headerAsString.equals("GRIB") && versionNumber == 2);
    }

    public long readRecordLength(byte[] recordHeader) throws GribReaderException {
        long length = 0;
        try {
            length = BytesToPrimitiveHelper.bytesToLong(recordHeader[POSITION_LENGTH_1], recordHeader[POSITION_LENGTH_2], recordHeader[POSITION_LENGTH_3], recordHeader[POSITION_LENGTH_4], recordHeader[POSITION_LENGTH_5], recordHeader[POSITION_LENGTH_6], recordHeader[POSITION_LENGTH_7], recordHeader[POSITION_LENGTH_8]);
        } catch (BinaryNumberConversionException e) {
            throw new GribReaderException(e.getMessage(), e);
        }
        if (length < MINIMUM_REQUIRED_LENGTH_IN_BIT) {
            throw new GribReaderException("The suggested length in the record header is invalid.");
        }
        return length;
    }

    public Grib2Record readCompleteRecord(Grib2Record record, byte[] recordAsByteArray, int headerLength) throws GribReaderException {
        Grib2IDS identificationSection = null;
        Grib2LUS localUseSection = null;
        Grib2GDS gridDefinitionSection = null;
        Grib2PDS productDefinitionSection = null;
        Grib2DRS dataRepresentationSection = null;
        Grib2BMS bitmapSection = null;
        Grib2DS dataSection = null;

        int headerOffSet = headerLength;
        try {
            identificationSection = idsReader.readGIDValues(recordAsByteArray, headerOffSet);
            headerOffSet += identificationSection.getLength();
            localUseSection = lusReader.readLUSValues(recordAsByteArray, headerOffSet);
            headerOffSet += localUseSection.getLength();
            gridDefinitionSection = gdsReader.readGDSValues(recordAsByteArray, headerOffSet);
            headerOffSet += gridDefinitionSection.getLength();
            productDefinitionSection = pdsReader.readPDSValues(recordAsByteArray, headerOffSet);
            headerOffSet += productDefinitionSection.getLength();
            dataRepresentationSection = drsReader.readDRSValues(recordAsByteArray, headerOffSet);
            headerOffSet += dataRepresentationSection.getLength();
            bitmapSection = bmsReader.readBMSValues(recordAsByteArray, headerOffSet);
            headerOffSet += bitmapSection.getLength();
            dataSection = dsReader.readDSValues(recordAsByteArray, headerOffSet);
        } catch (BinaryNumberConversionException | IOException e) {
            throw new GribReaderException(e.getMessage(), e);
        }
        record.setIds(identificationSection);
        record.setLus(localUseSection);
        record.setGds(gridDefinitionSection);
        record.setPds(productDefinitionSection);
        record.setDrs(dataRepresentationSection);
        record.setBms(bitmapSection);
        record.setDataSection(dataSection);
        return record;
    }

    private final Grib2IDSReader idsReader;
    private final Grib2LUSReader lusReader;
    private final Grib2GDSReader gdsReader;
    private final Grib2PDSReader pdsReader;
    private final Grib2DRSReader drsReader;
    private final Grib2BMSReader bmsReader;
    private final Grib2DSReader dsReader;

    private static final int GRIB_WORD_LENGTH = 4;
    private static final int POSITION_VERSION_NUMBER = 7;
    private static final int POSITION_LENGTH_1 = 8;
    private static final int POSITION_LENGTH_2 = 9;
    private static final int POSITION_LENGTH_3 = 10;
    private static final int POSITION_LENGTH_4 = 11;
    private static final int POSITION_LENGTH_5 = 12;
    private static final int POSITION_LENGTH_6 = 13;
    private static final int POSITION_LENGTH_7 = 14;
    private static final int POSITION_LENGTH_8 = 15;
    private static final int MINIMUM_REQUIRED_LENGTH_IN_BIT = 15;
}
