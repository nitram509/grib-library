package org.meteogroup.griblibrary.grib1;

import lombok.extern.slf4j.Slf4j;
import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.exception.GribReaderException;
import org.meteogroup.griblibrary.grib.AbstractGribCollectionReader;
import org.meteogroup.griblibrary.grib1.model.Grib1Record;
import org.meteogroup.griblibrary.util.FileChannelPartReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import static org.meteogroup.griblibrary.grib1.Grib1RecordReader.checkIfGribFileIsValidGrib1;

/**
 * Created by roijen on 20-Oct-15.
 */
@Slf4j
public class Grib1CollectionReader extends AbstractGribCollectionReader {

    private static final int HEADER_LENGTH = 8;
    private static final int NO_HEADER = 0;

    Grib1RecordReader recordReader;
    FileChannelPartReader partReader;

    public Grib1CollectionReader() {
        recordReader = new Grib1RecordReader();
        partReader = new FileChannelPartReader();
    }

    @Override
    public List<Grib1Record> readAllRecords(String fileName) throws GribReaderException, FileNotFoundException {
        return (List<Grib1Record>) super.readAllRecords(fileName);
    }

    @Override
    public List<Grib1Record> readAllRecords(File file) throws GribReaderException, FileNotFoundException {
        return (List<Grib1Record>) super.readAllRecords(file);
    }

    @Override
    public List<Grib1Record> readAllRecords(RandomAccessFile randomAccessFile) throws GribReaderException {
        return (List<Grib1Record>) super.readAllRecords(randomAccessFile);
    }

    @Override
    public List<Grib1Record> readAllRecords(FileChannel fileChannel) throws GribReaderException {
        long size;
        try {
            size = fileChannel.size();
        } catch (IOException e) {
            throw new GribReaderException(e.getMessage(), e);
        }
        return readAllRecords(fileChannel, size);
    }

    @Override
    public List<Grib1Record> readAllRecords(ReadableByteChannel readableByteChannel, long channelSize) throws GribReaderException {
        ArrayList<Grib1Record> response = new ArrayList<>();
        for (long channelOffset = 0; channelOffset < channelSize; ) {

            byte[] recordHeader = partReader.readPartOfFileChannel(readableByteChannel, HEADER_LENGTH);
            if (allBytesZero(recordHeader)) {
                channelOffset += HEADER_LENGTH;
                continue;
            }
            if (!checkIfGribFileIsValidGrib1(recordHeader)) {
                int attemptOffsetUpdate = 0;
                for (byte b : recordHeader) {
                    if (b == 0) {
                        attemptOffsetUpdate++;
                    } else {
                        break;
                    }
                }
                if (attemptOffsetUpdate != 0) {
                    log.info("Strange bit shifting detected.Attempting to recover.");
                    recordHeader = this.attemptRecovery(recordHeader, attemptOffsetUpdate, readableByteChannel);
                    channelOffset += attemptOffsetUpdate;
                }
            }
            Grib1Record record = new Grib1Record();
            try {
                record.setLength(recordReader.readRecordLength(recordHeader));
                byte[] recordAsByteArray = partReader.readPartOfFileChannel(readableByteChannel, record.getLength() - HEADER_LENGTH);
                record = recordReader.readCompleteRecord(record, recordAsByteArray, NO_HEADER);
            } catch (BinaryNumberConversionException e) {
                throw new GribReaderException(e.getMessage(), e);
            }
            response.add(record);
            channelOffset += recordReader.readRecordLength(recordHeader);
        }
        return response;
    }

    byte[] attemptRecovery(byte[] recordHeader, int attemptOffsetUpdate, ReadableByteChannel fileChannel) throws GribReaderException {
        byte[] recoveryBits = partReader.readPartOfFileChannel(fileChannel, attemptOffsetUpdate);
        byte[] potentialResult = new byte[HEADER_LENGTH];
        for (int x = 0; x < HEADER_LENGTH; x++) {
            if (x < HEADER_LENGTH - attemptOffsetUpdate) {
                potentialResult[x] = recordHeader[x + attemptOffsetUpdate];
            } else {
                potentialResult[x] = recoveryBits[(x - HEADER_LENGTH) + attemptOffsetUpdate];
            }
        }
        if (!checkIfGribFileIsValidGrib1(potentialResult)) {
            throw new GribReaderException("Unable to determine valid header");
        } else {
            return potentialResult;
        }

    }

}