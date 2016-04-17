package org.meteogroup.griblibrary.grib;

import org.meteogroup.griblibrary.exception.GribReaderException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import static org.meteogroup.griblibrary.util.IoUtils.closeSilent;

public abstract class AbstractGribCollectionReader {

    public List<? extends GribRecord> readAllRecords(String fileName) throws GribReaderException, FileNotFoundException {
        return readAllRecords(new File(fileName));
    }

    public List<? extends GribRecord> readAllRecords(File file) throws GribReaderException, FileNotFoundException {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            return readAllRecords(randomAccessFile);
        } finally {
            closeSilent(randomAccessFile);
        }
    }

    public List<? extends GribRecord> readAllRecords(RandomAccessFile randomAccessFile) throws GribReaderException {
        return readAllRecords(randomAccessFile.getChannel());
    }

    public abstract List<? extends GribRecord> readAllRecords(FileChannel fileChannel) throws GribReaderException;

    public abstract List<? extends GribRecord> readAllRecords(ReadableByteChannel readableByteChannel, long fileLength) throws GribReaderException;

    protected boolean allBytesZero(byte[] recordHeader) {
        for (byte b : recordHeader) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }
}
