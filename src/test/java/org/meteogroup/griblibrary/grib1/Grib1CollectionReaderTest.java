package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.exception.GribReaderException;
import org.meteogroup.griblibrary.grib1.model.Grib1Record;
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
import static org.mockito.Mockito.*;

/**
 * Created by roijen on 20-Oct-15.
 */
public class Grib1CollectionReaderTest {

    private static final String VERY_SIMPLE_TEXT_FILE_LOCATION = "VerySimpleSampleFile.txt";
    private static final String NOT_EXISTING_FILE_LOCATION = "/dev/null/doesnotexist.txt";
    private static final byte[] SIMULATED_BYTE_ARRAY = new byte[]{'G', 'R', 'I', 'B', 19, 84, -26, 1};
    private static final byte[] OFFSET_BYTE_ARRAY = new byte[]{0, 0, 'G', 'R', 'I', 'B', 19, 84};
    private static final byte[] RECOVERY_BIT = new byte[]{-26, 1};

    private Grib1CollectionReader collectionReader;


    @DataProvider(name = "simpleFileLocation")
    public static Object[][] simpleFileLocation() {
        return new Object[][]{
                new Object[]{VERY_SIMPLE_TEXT_FILE_LOCATION}
        };
    }

    @DataProvider(name = "notExistingFileLocation")
    public static Object[][] notExistingFileLocation() {
        return new Object[][]{
                new Object[]{NOT_EXISTING_FILE_LOCATION},
        };
    }


    @BeforeMethod
    public void setUp() {
        collectionReader = new Grib1CollectionReader();
    }

    @Test(dataProvider = "notExistingFileLocation", expectedExceptions = FileNotFoundException.class)
    public void getAFileChannelFromANotExistingFileName(String fileLocation) throws GribReaderException, FileNotFoundException {
        collectionReader.readAllRecords(fileLocation);
    }

    @Test
    public void testReadRecords() throws GribReaderException, IOException, BinaryNumberConversionException {

        collectionReader.partReader = mock(FileChannelPartReader.class);
        collectionReader.recordReader = mock(Grib1RecordReader.class);

        when(collectionReader.partReader.readPartOfFileChannel(any(ReadableByteChannel.class), anyLong())).thenReturn(SIMULATED_BYTE_ARRAY);
        when(collectionReader.recordReader.readRecordLength(any(byte[].class))).thenReturn(8);

        List<Grib1Record> records = collectionReader.readAllRecords(createReadableByteChannel(16), 16L);
        assertThat(records.size()).isEqualTo(2);
    }

    @Test
    public void testAttempRecovery() throws GribReaderException, IOException {
        collectionReader.partReader = mock(FileChannelPartReader.class);

        when(collectionReader.partReader.readPartOfFileChannel(any(FileChannel.class), anyInt())).thenReturn(RECOVERY_BIT);

        byte[] responseArray = collectionReader.attemptRecovery(OFFSET_BYTE_ARRAY, 2, createReadableByteChannel(32));

        assertThat(responseArray.length).isEqualTo(8);
        assertThat(responseArray[0]).isEqualTo((byte) 71);
        assertThat(responseArray[1]).isEqualTo((byte) 82);
        assertThat(responseArray[2]).isEqualTo((byte) 73);
        assertThat(responseArray[3]).isEqualTo((byte) 66);
        assertThat(responseArray[7]).isEqualTo((byte) 1);

    }

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