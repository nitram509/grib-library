package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.grib1.model.Grib1ProductDefinitionSection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by roijen on 21-Oct-15.
 */
public class Grib1ProductDefinitionSectionReaderTest {

    private Grib1PDSReader pdsReader;

    @DataProvider(name = "goodPDSDataSet")
    public static Object[][] goodPDSDataSet(){
        return new Object[][]{
                new Object[]{GOOD_PDS_ARRAY,0,GOOD_PDS_OBJECT()},
                new Object[]{GOOD_PDS_ARRAY_WITH_HEADER,8,GOOD_PDS_OBJECT()}
        };
    }

    @DataProvider(name = "goodPDSDataSetForLength")
    public static Object[][] goodPDSDataSetForLength() {
        return new Object[][]{
                new Object[]{GOOD_PDS_ARRAY, 0, 28},
                new Object[]{GOOD_PDS_ARRAY_WITH_HEADER, 8, 28}
        };
    }

    @BeforeMethod
    public void setUp(){
        pdsReader = new Grib1PDSReader();
    }

    @Test(dataProvider = "goodPDSDataSetForLength")
    public void testReadPDSLength(byte[] testArray, int headerOffSet, int expectedValue) throws BinaryNumberConversionException {
        int length = pdsReader.readPDSLength(testArray, headerOffSet);
        assertThat(length).isEqualTo(expectedValue);
    }


    @Test(dataProvider = "goodPDSDataSet")
    public void testReadPDS(byte[] testArray, int headerOffSet, Grib1ProductDefinitionSection expectedResponseObject) throws BinaryNumberConversionException {
        Grib1ProductDefinitionSection pds = pdsReader.readPDSValues(testArray,headerOffSet);
        assertThat(pds).isEqualTo(expectedResponseObject);
    }

    private static final byte[] GOOD_PDS_ARRAY = new byte[]{0,0,28,-128,98,-111,-1,-128,41,112,28,100,15,8,7,0,0,1,6,0,0,0,0,0,21,0,0,0};
    private static final byte[] GOOD_PDS_ARRAY_WITH_HEADER = new byte[]{'G','R','I','B',19,84,-26,1,0,0,28,-128,98,-111,-1,-128,41,112,28,100,15,8,7,0,0,1,6,0,0,0,0,0,21,0,0,0};

    private static final Grib1ProductDefinitionSection GOOD_PDS_OBJECT(){
        Grib1ProductDefinitionSection pds = new Grib1ProductDefinitionSection();
        pds.setSectionLenght(28);
        pds.setParameterTableVersionNumber((byte)128);
        pds.setIdentificationOfCentre((byte)98);
        pds.setGeneratingProcessIdNumber((byte)145);
        pds.setGridIdentification((byte)255);
        pds.setIdenticatorOfParameterAndUnit((byte)41);
        pds.setIdenticatorOfTypeOfLevelOrLayer((byte)112);
        pds.setLevelOrLayerValue1((byte)28);
        pds.setLevelOrLayerValue2((byte)100);
        pds.setIssueTimeYearOfCentury((byte)15);
        pds.setIssueTimeMonth((byte)8);
        pds.setIssueTimeDay((byte)7);
        pds.setIssueTimeHour((byte)0);
        pds.setIssueTimeMinute((byte)0);
        pds.setIssueTimeCentury((byte)21);
        pds.setForecastTimeUnit((byte)1);
        pds.setForecastPeriodOfTime1((byte)6);
        pds.setForecastPeriodOfTime2((byte)0);
        pds.setTimeRangeIndicator((byte)0);
        pds.setHasGDS(true);
        pds.setHasBMS(false);
        pds.setNumberIncludedInAverageOrAccumulation((byte)0);
        pds.setIdentificationOfSubCentre((byte)0);
        pds.setDecimalScaleFactor((byte)0);
        return pds;
    }

}
