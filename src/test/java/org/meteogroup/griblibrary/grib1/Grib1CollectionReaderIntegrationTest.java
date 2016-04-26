package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.grib.GribReaderFactory;
import org.meteogroup.griblibrary.grib1.model.Grib1Record;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meteogroup.griblibrary.grib1.spec.CenterNameResolver.GRIB1_SPEC_TABLE0_ORIGINATING_CENTERS.ECMWF;
import static org.testng.FileAssert.fail;

public class Grib1CollectionReaderIntegrationTest {

    private static final String VAR_NAME = "GRIB_TEST_DATA_BASE_PATH";
    private static final String GRIB_TEST_DATA_BASE_PATH = System.getenv(VAR_NAME);

    private Grib1CollectionReader collectionReader;
    private File testFile;


    @BeforeMethod
    public void setUp() throws Exception {
        assertBasePathIsSet();
    }

    @Test
    public void all_mandatory_fields_can_be_read() throws Exception {
        testFile = createFullPath("regular_latlon_surface.grib1");
        collectionReader = new GribReaderFactory().createVersion1(testFile).get();
        List<Grib1Record> grib1Records = collectionReader.readAllRecords(testFile);

        assertThat(grib1Records).hasSize(1);
        Grib1Record record = grib1Records.get(0);
        assertThat(record.getProductDefinition().getIdentificationOfCenter()).isEqualTo(ECMWF.centerId);
        assertThat(record.getProductDefinition().getIdentificationOfSubCenter()).isEqualTo((byte) 0);
        assertThat(record.getProductDefinition().getParameterTableVersionNumber()).isEqualTo((byte) 128);
        assertThat(record.getProductDefinition().getGeneratingProcessIdNumber()).isEqualTo((byte) 130);
        assertThat(record.getProductDefinition().getIdenticatorOfParameterAndUnit()).isEqualTo((byte) 167);
        assertThat(record.getProductDefinition().getIdenticatorOfTypeOfLevelOrLayer()).isEqualTo((byte) 1);
        assertThat(record.getProductDefinition().getIssueTimeYearOfCentury()).isEqualTo((byte) 8);
        assertThat(record.getProductDefinition().getIssueTimeMonth()).isEqualTo((byte) 2);
        assertThat(record.getProductDefinition().getIssueTimeDay()).isEqualTo((byte) 6);
        assertThat(record.getProductDefinition().getIssueTimeHour()).isEqualTo((byte) 12);
        assertThat(record.getProductDefinition().getIssueTimeMinute()).isEqualTo((byte) 0);
        assertThat(record.getProductDefinition().getForecastTimeUnit()).isEqualTo((byte) 1);
    }

    private File createFullPath(String fileName) {
        return new File(GRIB_TEST_DATA_BASE_PATH, fileName);
    }

    private void assertBasePathIsSet() {
        if (GRIB_TEST_DATA_BASE_PATH == null) {
            fail("ENV variable '" + VAR_NAME + "' not set.\n" +
                    "1. Download http://download.ecmwf.org/test-data/grib_api/grib_api_test_data.tar.gz\n" +
                    "2. Extract to a folder\n" +
                    "3. set ENV variable '" + VAR_NAME + "' poiting to the base path of the test data.");
        }
    }
}