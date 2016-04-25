package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.exception.GribReaderException;
import org.meteogroup.griblibrary.grib.GribReaderFactory;
import org.meteogroup.griblibrary.grib1.model.Grib1BinaryDataSection;
import org.meteogroup.griblibrary.grib1.model.Grib1GridDescriptionSection;
import org.meteogroup.griblibrary.grib1.model.Grib1ProductDefinitionSection;
import org.meteogroup.griblibrary.grib1.model.Grib1Record;
import org.meteogroup.griblibrary.util.BytesToPrimitiveHelper;

import java.nio.charset.Charset;

/**
 * Created by roijen on 19-Oct-15.
 */
public class Grib1RecordReader {

    private static final Charset ASCII = Charset.forName("ASCII");

    Grib1PDSReader pdsReader;
    Grib1GDSReader gdsReader;
    Grib1BDSReader bdsReader;

    private static final int POSITION_RECORDLENGTH1 = 4;
    private static final int POSITION_RECORDLENGTH2 = 5;
    private static final int POSITION_RECORDLENGTH3 = 6;
    private static final int POSITION_GRIBVERSION = 7;

    public Grib1RecordReader() {
        this.pdsReader = new Grib1PDSReader();
        this.gdsReader = new Grib1GDSReader();
        this.bdsReader = new Grib1BDSReader();
    }

    public static boolean checkIfGribFileIsValidGrib1(byte[] recordHeader) {
        if (recordHeader.length < GribReaderFactory.GRIB_HEADER_LENGTH) {
            throw new IllegalArgumentException("To check GRIB header, minimum 8 bytes need to be available. Actual length=" + recordHeader.length);
        }
        String headerAsString = new String(recordHeader, 0, 4, ASCII);
        short versionNumber = recordHeader[POSITION_GRIBVERSION];
        return (headerAsString.equals("GRIB") && versionNumber == 1);
    }

    public boolean checkIfGribFileEndsValid(byte[] bufferValues) {
        for (int i = 0; i < 4; i++) {
            if (bufferValues[bufferValues.length - 1 - i] != '7') {
                return false;
            }
        }
        return true;
    }

    public int readRecordLength(byte[] bufferValues) throws GribReaderException {
        int length = 0;
        length = BytesToPrimitiveHelper.asInt(bufferValues[POSITION_RECORDLENGTH1], bufferValues[POSITION_RECORDLENGTH2], bufferValues[POSITION_RECORDLENGTH3]);
        //length = BytesToPrimitiveHelper.signedBytesToInt(bufferValues[POSITION_RECORDLENGTH1], bufferValues[POSITION_RECORDLENGTH2], bufferValues[POSITION_RECORDLENGTH3]);
        byte[] tmp = new byte[3];
        tmp[0] = bufferValues[POSITION_RECORDLENGTH1];
        tmp[1] = bufferValues[POSITION_RECORDLENGTH2];
        tmp[2] = bufferValues[POSITION_RECORDLENGTH3];
        int i = (tmp[0] & 0xFF) << 16 | (tmp[1] & 0xFF) << 8 | (tmp[2] & 0xFF);
        if (length < 8) {
            throw new GribReaderException("The suggested length in the record header is invalid.");
        }
        return length;
    }

    public Grib1Record readCompleteRecord(Grib1Record grib1Record, byte[] bufferValues, int headerOffSet) throws GribReaderException, BinaryNumberConversionException {
        final Grib1ProductDefinitionSection pds = pdsReader.readPDSValues(bufferValues, headerOffSet);
        headerOffSet += pds.getSectionLenght();
        final Grib1GridDescriptionSection gds = gdsReader.readGDSValues(bufferValues, headerOffSet);
        headerOffSet += gds.getSectionLenght();
        final Grib1BinaryDataSection bds = bdsReader.readBDSValues(bufferValues, headerOffSet);
        grib1Record.setProductDefinition(pds);
        grib1Record.setGridDescription(gds);
        grib1Record.setBinaryData(bds);
        checkIfGribFileEndsValid(bufferValues);
        return grib1Record;
    }

}
