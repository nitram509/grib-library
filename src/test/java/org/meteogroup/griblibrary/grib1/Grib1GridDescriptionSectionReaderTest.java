package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.geometry.Rectangle;
import org.meteogroup.griblibrary.grib1.model.Grib1GridDescriptionSection;
import org.meteogroup.griblibrary.grib1.model.Grib1LatLonGrib1GridDefinition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.assertj.core.api.Assertions.assertThat;


public class Grib1GridDescriptionSectionReaderTest {

    private static final byte[] BYTE_ARRAY_FOR_GAUUSION_COORDINATE_READOUT = new byte[]{0, 1, 0, 2, 0, 1};
    private static final int[] EXPECTED_ARRAY_FOR_GAUSSIAN_COORDINATE_READOUT = new int[]{1, 2, 1};
    private static final short EXPECTED_LENGTH_FOR_GAUSSIAN_COORDINATE_READOUT = 4;

    private static final byte[] GOOD_SHORT_GDS_ARRAY_FOR_LENGTH_ONLY = new byte[]{0, 0, 28};

    Grib1GDSReader gdsReader;

    @BeforeMethod
    public void prepare() throws Exception {
        gdsReader = new Grib1GDSReader();
    }

    @DataProvider(name = "goodGDSDataSetForLength")
    public static Object[][] goodGDSDataSetForLength() throws IOException, URISyntaxException {
        return new Object[][]{
                new Object[]{GOOD_GDS_ARRAY(), 0, 2592},
                new Object[]{GOOD_SHORT_GDS_ARRAY_FOR_LENGTH_ONLY, 0, 28}
        };
    }

    @DataProvider(name = "testSetForGausianCoordinateReadOut")
    public static Object[][] testSetForGausianCoordinateReadOut() throws IOException, URISyntaxException {
        return new Object[][]{
                new Object[]{GDS_FOR_GAUSSIAN_COORDINATE_READOUT(), BYTE_ARRAY_FOR_GAUUSION_COORDINATE_READOUT, 0, EXPECTED_ARRAY_FOR_GAUSSIAN_COORDINATE_READOUT, EXPECTED_LENGTH_FOR_GAUSSIAN_COORDINATE_READOUT}
        };
    }

    @Test(dataProvider = "goodGDSDataSetForLength")
    public void testReadGDSLength(byte[] testArray, int headerOffSet, int expectedValue) throws BinaryNumberConversionException {
        int length = gdsReader.readGDSLength(testArray, headerOffSet);
        assertThat(length).isEqualTo(expectedValue);
    }

    @Test()
    public void test_read_good_GDS_including_all_valid_values() throws BinaryNumberConversionException, IOException, URISyntaxException {
        byte[] good_gds_array = GOOD_GDS_ARRAY();
        int headerOffSet = 0;

        Grib1GridDescriptionSection gds = gdsReader.readGDSValues(good_gds_array, headerOffSet);

        Grib1GridDescriptionSection expected = GOOD_GDS_OBJECT();
        assertThat(gds.getSectionLenght()).isEqualTo(expected.getSectionLenght());
        assertThat(gds.getNumberOfVerticalsCoordinateParams()).isEqualTo(expected.getNumberOfVerticalsCoordinateParams());
        assertThat(gds.getLocationOfVerticalCoordinateParams()).isEqualTo(expected.getLocationOfVerticalCoordinateParams());
        assertThat(gds.getLocationListPer()).isEqualTo(expected.getLocationListPer());
        assertThat(gds.getDataRepresentationType()).isEqualTo(expected.getDataRepresentationType());
        assertThat(gds.getNumberOfPoints()).isEqualTo(expected.getNumberOfPoints());

        Grib1LatLonGrib1GridDefinition gd = (Grib1LatLonGrib1GridDefinition) gds.getGridDefinition();
        Grib1LatLonGrib1GridDefinition expectedGd = (Grib1LatLonGrib1GridDefinition) GOOD_GDS_OBJECT().getGridDefinition();
        assertThat(gd.getBoundingBox().north()).isEqualTo(expectedGd.getBoundingBox().north());
        assertThat(gd.getBoundingBox().east()).isEqualTo(expectedGd.getBoundingBox().east());
        assertThat(gd.getBoundingBox().south()).isEqualTo(expectedGd.getBoundingBox().south());
        assertThat(gd.getBoundingBox().west()).isEqualTo(expectedGd.getBoundingBox().west());
        assertThat(gd.getLat1InMilliDegree()).isEqualTo(expectedGd.getLat1InMilliDegree());
        assertThat(gd.getLat2InMilliDegree()).isEqualTo(expectedGd.getLat2InMilliDegree());
        assertThat(gd.getLon1InMilliDegree()).isEqualTo(expectedGd.getLon1InMilliDegree());
        assertThat(gd.getLon2InMilliDegree()).isEqualTo(expectedGd.getLon2InMilliDegree());
        assertThat(gd.getResolution()).isEqualTo(expectedGd.getResolution());
        assertThat(gd.getLongitudeIncrement()).isEqualTo(expectedGd.getLongitudeIncrement());
        assertThat(gd.getNumberOfCirclesBetweenPoleAndEquator()).isEqualTo(expectedGd.getNumberOfCirclesBetweenPoleAndEquator());
        assertThat(gd.getPointsAlongLatitudeCircle()).isEqualTo(expectedGd.getPointsAlongLatitudeCircle());
        assertThat(gd.getPointsAlongLongitudeMeridian()).isEqualTo(expectedGd.getPointsAlongLongitudeMeridian());
        assertThat(gd.isScanModeIIsPositive()).isEqualTo(expectedGd.isScanModeIIsPositive());
        assertThat(gd.isScanModeJIsPositve()).isEqualTo(expectedGd.isScanModeJIsPositve());
        assertThat(gd.isScanModeJIsConsectuve()).isEqualTo(expectedGd.isScanModeJIsConsectuve());
    }

    @Test(dataProvider = "testSetForGausianCoordinateReadOut")
    public void testGaussianReadout(Grib1GridDescriptionSection gridDescriptionSection, byte[] buffer, int offSet, int[] expectedResult, int expectedLength) throws BinaryNumberConversionException {

        gdsReader.enrichNisAndNumberOfPoints(gridDescriptionSection, buffer, offSet);

        assertThat(gridDescriptionSection.getNumberOfPoints()).isEqualTo(expectedLength);
        Grib1LatLonGrib1GridDefinition gridDefinition = (Grib1LatLonGrib1GridDefinition) gridDescriptionSection.getGridDefinition();
        assertThat(gridDefinition.getPointsAlongLatitudeCircleForGaussian()).isEqualTo(expectedResult);
    }

    private static final Grib1GridDescriptionSection GOOD_GDS_OBJECT() {
        Grib1GridDescriptionSection gds = new Grib1GridDescriptionSection();
        gds.setSectionLenght(2592);
        gds.setNumberOfVerticalsCoordinateParams((byte) 0);
        gds.setLocationOfVerticalCoordinateParams(33);
        gds.setLocationListPer(0);
        gds.setDataRepresentationType((byte) 4);
        gds.setNumberOfPoints(2140702);

        Grib1LatLonGrib1GridDefinition gridDefinition = new Grib1LatLonGrib1GridDefinition();
        gridDefinition.setBoundingBox(new Rectangle(0f, 359.9f, 89.892f, -89.892f));
        gridDefinition.setLat1InMilliDegree(89892);
        gridDefinition.setLat2InMilliDegree(-89892);
        gridDefinition.setLon1InMilliDegree(0);
        gridDefinition.setLon2InMilliDegree(359900);
        gridDefinition.setResolution(0);
        gridDefinition.setLongitudeIncrement(65.535f);
        gridDefinition.setNumberOfCirclesBetweenPoleAndEquator((short) 640);
        gridDefinition.setPointsAlongLatitudeCircle(65535);
        gridDefinition.setPointsAlongLongitudeMeridian(1280);
        gridDefinition.setScanModeIIsPositive(true);
        gridDefinition.setScanModeJIsPositve(false);
        gridDefinition.setScanModeJIsConsectuve(false);
        gds.setGridDefinition(gridDefinition);
        return gds;
    }

    private static final byte[] GOOD_GDS_ARRAY() throws URISyntaxException, IOException {
        String filename = "ecmwf-grib1-example-grid-definition-section.grb";

        String name = Grib1GDSReader.class.getResource(filename).toString();
        File f = new File(Grib1GDSReader.class.getResource(filename).toURI());
        if (!f.exists()) {
            throw new IOException("file does not exist at " + name);
        }
        RandomAccessFile raFile = new RandomAccessFile(f, "r");
        FileChannel fc = raFile.getChannel();
        fc.position(0);
        ByteBuffer buffer = ByteBuffer.allocate((int) raFile.length());
        fc.read(buffer);
        buffer.rewind();
        byte[] response = buffer.array();
        raFile.close();
        return response;
    }

    private static final Grib1GridDescriptionSection GDS_FOR_GAUSSIAN_COORDINATE_READOUT() {
        Grib1GridDescriptionSection gds = new Grib1GridDescriptionSection();
        Grib1LatLonGrib1GridDefinition gridDefinition = new Grib1LatLonGrib1GridDefinition();
        gridDefinition.setPointsAlongLongitudeMeridian(3);
        gds.setGridDefinition(gridDefinition);
        gds.setLocationOfVerticalCoordinateParams(1);
        return gds;
    }


}
