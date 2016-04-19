package org.meteogroup.griblibrary.grib2;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.exception.GribReaderException;
import org.meteogroup.griblibrary.grib2.model.Grib2Record;
import org.meteogroup.griblibrary.util.FileChannelPartReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by roijen on 28-Oct-15.
 */
public class Grib2CollectionReaderTest {

    Grib2CollectionReader collectionReader;

    @DataProvider(name = "notExistingFileLocation")
    public static Object[][] notExistingFileLocation() {
        return new Object[][]{
                new Object[]{NOT_EXISTING_FILE_LOCATION},
        };
    }

    @BeforeMethod
    public void setUp() {
        collectionReader = new Grib2CollectionReader();
    }

    @Test(dataProvider = "notExistingFileLocation", expectedExceptions = FileNotFoundException.class)
    public void getAFileChannelFromANotExistingFileName(String fileLocation) throws GribReaderException, FileNotFoundException {
        collectionReader.readAllRecords(fileLocation);
    }

    @Test
    public void testReadRecords() throws GribReaderException, IOException, BinaryNumberConversionException {

        collectionReader.partReader = mock(FileChannelPartReader.class);
        collectionReader.recordReader = mock(Grib2RecordReader.class);

        when(collectionReader.partReader.readPartOfFileChannel(any(ReadableByteChannel.class), anyLong())).thenReturn(SIMULATED_BYTE_ARRAY);
        when(collectionReader.recordReader.readRecordLength(any(byte[].class))).thenReturn(16L);

        List<Grib2Record> records;

        records = collectionReader.readAllRecords(createReadableByteChannel(16), 16L);
        assertThat(records.size()).isEqualTo(1);

        records = collectionReader.readAllRecords(createReadableByteChannel(32), 32L);
        assertThat(records.size()).isEqualTo(2);
    }

    private static final String NOT_EXISTING_FILE_LOCATION = "/dev/null/doesnotexist.txt";

    private static final byte[] SIMULATED_BYTE_ARRAY = new byte[]{'G', 'R', 'I', 'B', 19, 84, -26, 2};

    private static ReadableByteChannel createReadableByteChannel(int size) throws IOException {
        return new ReadableByteChannel() {
            @Override
            public boolean isOpen() {
                return true;
            }

            @Override
            public void close() throws IOException { /* nothing to do */ }

            @Override
            public int read(ByteBuffer dst) throws IOException {
                dst.put(SIMULATED_BYTE_ARRAY, 0, SIMULATED_BYTE_ARRAY.length);
                return size;
            }
        };
    }
}
