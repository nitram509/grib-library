package org.meteogroup.griblibrary.grib1;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class LatLonGridDefinitionReaderTest {

    private static final int VALUE_FOR_NORTH_SOUTH_TEST1 = 89892;
    private static final int VALUE_FOR_NORTH_SOUTH_TEST2 = -89892;

    private LatLonGridDefinitionReader reader;

    @BeforeMethod
    public void setUp() throws Exception {
        reader = new LatLonGridDefinitionReader();
    }

    @DataProvider(name = "findNorthDataSet")
    public static Object[][] findNorthDataSet() throws IOException, URISyntaxException {
        return new Object[][]{
                new Object[]{VALUE_FOR_NORTH_SOUTH_TEST1, VALUE_FOR_NORTH_SOUTH_TEST2, -89.892f}
        };
    }

    @Test(dataProvider = "findNorthDataSet")
    public void testFindNorth(int inputValue1, int inputValue2, float expectedResult) {
        float actualValue = reader.getNorth(inputValue1, inputValue2);
        assertThat(actualValue).isEqualTo(expectedResult);
    }

    @DataProvider(name = "findSouthDataSet")
    public static Object[][] findSouthDataSet() throws IOException, URISyntaxException {
        return new Object[][]{
                new Object[]{VALUE_FOR_NORTH_SOUTH_TEST1, VALUE_FOR_NORTH_SOUTH_TEST2, 89.892f}
        };
    }

    @Test(dataProvider = "findSouthDataSet")
    public void testFindSouth(int inputValue1, int inputValue2, float expectedResult) {
        float actualValue = reader.getSouth(inputValue1, inputValue2);
        assertThat(actualValue).isEqualTo(expectedResult);
    }

}