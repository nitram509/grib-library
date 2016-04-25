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

}