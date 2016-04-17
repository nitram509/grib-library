package org.meteogroup.griblibrary.grib;

import org.meteogroup.griblibrary.grib1.Grib1CollectionReader;
import org.meteogroup.griblibrary.grib2.Grib2CollectionReader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Optional;

import static org.meteogroup.griblibrary.grib1.Grib1RecordReader.checkIfGribFileIsValidGrib1;
import static org.meteogroup.griblibrary.grib2.Grib2RecordReader.checkIfGribFileIsValidGrib2;
import static org.meteogroup.griblibrary.util.IoUtils.closeSilent;

public class GribReaderFactory {

    public static final int GRIB_HEADER_LENGTH = 8;

    public Optional<Grib1CollectionReader> createVersion1(String fileName) throws IOException {
        return createVersion1(new File(fileName));
    }

    public Optional<Grib1CollectionReader> createVersion1(File file) throws IOException {
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "r");
            return createVersion1(r);
        } finally {
            closeSilent(r);
        }
    }

    public Optional<Grib1CollectionReader> createVersion1(RandomAccessFile randomAccessFile) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        FileChannel fileChannel = randomAccessFile.getChannel();
        long oldPosition = fileChannel.position();
        fileChannel.read(byteBuffer);
        fileChannel.position(oldPosition);
        if (checkIfGribFileIsValidGrib1(byteBuffer.array())) {
            return Optional.of(new Grib1CollectionReader());
        }
        return Optional.empty();
    }

    public Optional<Grib2CollectionReader> createVersion2(String fileName) throws IOException {
        return createVersion2(new File(fileName));
    }

    public Optional<Grib2CollectionReader> createVersion2(File file) throws IOException {
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "r");
            return createVersion2(r);
        } finally {
            closeSilent(r);
        }
    }

    public Optional<Grib2CollectionReader> createVersion2(RandomAccessFile randomAccessFile) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        FileChannel fileChannel = randomAccessFile.getChannel();
        long oldPosition = fileChannel.position();
        fileChannel.read(byteBuffer);
        fileChannel.position(oldPosition);
        if (checkIfGribFileIsValidGrib2(byteBuffer.array())) {
            return Optional.of(new Grib2CollectionReader());
        }
        return Optional.empty();
    }


}
