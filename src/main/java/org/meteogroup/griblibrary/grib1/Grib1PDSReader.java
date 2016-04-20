package org.meteogroup.griblibrary.grib1;

import org.meteogroup.griblibrary.exception.BinaryNumberConversionException;
import org.meteogroup.griblibrary.grib1.model.Grib1ProductDefinitionSection;
import org.meteogroup.griblibrary.util.BitChecker;
import org.meteogroup.griblibrary.util.BytesToPrimitiveHelper;

import java.util.HashSet;
import java.util.Set;

import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asShort;
import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.bytesToInteger;

/**
 * Created by roijen on 21-Oct-15.
 */
class Grib1PDSReader {


    private static final int POSITION_PDS_LENGTH_1 = 0;
    private static final int POSITION_PDS_LENGTH_2 = 1;
    private static final int POSITION_PDS_LENGTH_3 = 2;
    private static final int POSITION_PDS_TABLE_VERSION_NUMBER = 3;
    private static final int POSITION_PDS_IDENTIFICATION_OF_CENTRE = 4;
    private static final int POSITION_PDS_GENERATING_PROCESS_NUMBER = 5;
    private static final int POSITION_PDS_GRIB_IDENTIFICATION = 6;
    private static final int POSITION_PDS_BMS_GDS_FLAGS = 7;
    private static final int FLAG_GDS = 1;
    private static final int FLAG_BMS = 2;
    private static final int POSITION_PDS_IDENTICATOR_OF_PARAMETER_AND_UNIT = 8;
    private static final int POSITION_PDS_IDENTICATOR_OF_TYPE_OF_LEVEL_OR_LAYER = 9;

    private static final Set<Byte> HEIGHT_LAYERS_WITH_DOUBLE_OCTET_VALUES = new HashSet<Byte>(11) {{
        add((byte) 100);
        add((byte) 103);
        add((byte) 105);
        add((byte) 107);
        add((byte) 109);
        add((byte) 111);
        add((byte) 113);
        add((byte) 125);
        add((byte) 160);
        add((byte) 200);
        add((byte) 201);
    }};

    private static final int POSITION_PDS_LEVEL_OR_LAYER_VALUE_1 = 10;
    private static final int POSITION_PDS_LEVEL_OR_LAYER_VALUE_2 = 11;
    private static final int POSITION_PDS_ISSUE_TIME_YEAR_OF_CENTURY = 12;
    private static final int POSITION_PDS_ISSUE_TIME_MONTH = 13;
    private static final int POSITION_PDS_ISSUE_TIME_DAY = 14;
    private static final int POSITION_PDS_ISSUE_TIME_HOUR = 15;
    private static final int POSITION_PDS_ISSUE_TIME_MINUTE = 16;
    private static final int POSITION_PDS_FORECAST_TIME_UNITE = 17;
    private static final int POSITION_PDS_FORECAST_PERIOD_OF_TIME_1 = 18;
    private static final int POSITION_PDS_FORECAST_PERIOD_OF_TIME_2 = 19;
    private static final int POSITION_PDS_TIME_RANGE_INDICATOR = 20;
    private static final int POSITION_PDS_NUMBER_INCLUDED_IN_AVERAGE_OR_ACCUMULATION_1 = 21;
    private static final int POSITION_PDS_NUMBER_INCLUDED_IN_AVERAGE_OR_ACCUMULATION_2 = 22;
    private static final int POSITION_PDS_NUMBER_OF_MISSING_FROM_AVERAGE_OR_ACCUMULATION = 23;
    private static final int POSITION_PDS_ISSUE_TIME_CENTURY = 24;
    private static final int POSITION_PDS_IDENTIFICATION_OF_SUBCENTRE = 25;
    private static final int POSITION_PDS_DECIMAL_SCALE_FACTOR_1 = 26;
    private static final int POSITION_PDS_DECIMAL_SCALE_FACTOR_2 = 27;


    public int readPDSLength(byte[] values, int headerOffSet) throws BinaryNumberConversionException {
        return bytesToInteger(values[POSITION_PDS_LENGTH_1 + headerOffSet], values[POSITION_PDS_LENGTH_2 + headerOffSet], values[POSITION_PDS_LENGTH_3 + headerOffSet]);
    }

    public Grib1ProductDefinitionSection readPDSValues(byte[] values, int headerOffSet) throws BinaryNumberConversionException {

        Grib1ProductDefinitionSection objectToReadInto = new Grib1ProductDefinitionSection();
        objectToReadInto.setSectionLenght(readPDSLength(values, headerOffSet));
        objectToReadInto.setParameterTableVersionNumber(values[POSITION_PDS_TABLE_VERSION_NUMBER + headerOffSet]);
        objectToReadInto.setIdentificationOfCentre(values[POSITION_PDS_IDENTIFICATION_OF_CENTRE + headerOffSet]);
        objectToReadInto.setGeneratingProcessIdNumber(values[POSITION_PDS_GENERATING_PROCESS_NUMBER + headerOffSet]);
        objectToReadInto.setGridIdentification(values[POSITION_PDS_GRIB_IDENTIFICATION + headerOffSet]);

        objectToReadInto.setHasGDS(BitChecker.testBit(values[POSITION_PDS_BMS_GDS_FLAGS + headerOffSet], FLAG_GDS));
        objectToReadInto.setHasBMS(BitChecker.testBit(values[POSITION_PDS_BMS_GDS_FLAGS + headerOffSet], FLAG_BMS));

        objectToReadInto.setIdenticatorOfParameterAndUnit(values[POSITION_PDS_IDENTICATOR_OF_PARAMETER_AND_UNIT + headerOffSet]);
        objectToReadInto.setIdenticatorOfTypeOfLevelOrLayer(values[POSITION_PDS_IDENTICATOR_OF_TYPE_OF_LEVEL_OR_LAYER + headerOffSet]);

        if (HEIGHT_LAYERS_WITH_DOUBLE_OCTET_VALUES.contains(objectToReadInto.getIdenticatorOfTypeOfLevelOrLayer())) {
            objectToReadInto.setHasOnlyOneLevelOrLayerValue(true);
            objectToReadInto.setLevelOrLayerValue1(asShort(values[POSITION_PDS_LEVEL_OR_LAYER_VALUE_1 + headerOffSet], values[POSITION_PDS_LEVEL_OR_LAYER_VALUE_2 + headerOffSet]));
        } else {
            objectToReadInto.setHasOnlyOneLevelOrLayerValue(false);
            objectToReadInto.setLevelOrLayerValue1(asShort(values[POSITION_PDS_LEVEL_OR_LAYER_VALUE_1 + headerOffSet]));
            objectToReadInto.setLevelOrLayerValue2(asShort(values[POSITION_PDS_LEVEL_OR_LAYER_VALUE_2 + headerOffSet]));
        }

        objectToReadInto.setIssueTimeYearOfCentury(values[POSITION_PDS_ISSUE_TIME_YEAR_OF_CENTURY + headerOffSet]);
        objectToReadInto.setIssueTimeMonth(values[POSITION_PDS_ISSUE_TIME_MONTH + headerOffSet]);
        objectToReadInto.setIssueTimeDay(values[POSITION_PDS_ISSUE_TIME_DAY + headerOffSet]);
        objectToReadInto.setIssueTimeHour(values[POSITION_PDS_ISSUE_TIME_HOUR + headerOffSet]);
        objectToReadInto.setIssueTimeMinute(values[POSITION_PDS_ISSUE_TIME_MINUTE + headerOffSet]);
        objectToReadInto.setForecastTimeUnit(values[POSITION_PDS_FORECAST_TIME_UNITE + headerOffSet]);
        objectToReadInto.setForecastPeriodOfTime1(values[POSITION_PDS_FORECAST_PERIOD_OF_TIME_1 + headerOffSet]);
        objectToReadInto.setForecastPeriodOfTime2(values[POSITION_PDS_FORECAST_PERIOD_OF_TIME_2 + headerOffSet]);
        objectToReadInto.setTimeRangeIndicator(values[POSITION_PDS_TIME_RANGE_INDICATOR + headerOffSet]);

        objectToReadInto.setNumberIncludedInAverageOrAccumulation(asShort(values[POSITION_PDS_NUMBER_INCLUDED_IN_AVERAGE_OR_ACCUMULATION_1 + headerOffSet], values[POSITION_PDS_NUMBER_INCLUDED_IN_AVERAGE_OR_ACCUMULATION_2 + headerOffSet]));

        objectToReadInto.setNumberOfMissingFromAverageOrAcummulation(values[POSITION_PDS_NUMBER_OF_MISSING_FROM_AVERAGE_OR_ACCUMULATION + headerOffSet]);
        objectToReadInto.setIssueTimeCentury(values[POSITION_PDS_ISSUE_TIME_CENTURY + headerOffSet]);
        objectToReadInto.setIdentificationOfSubCentre(values[POSITION_PDS_IDENTIFICATION_OF_SUBCENTRE + headerOffSet]);

        objectToReadInto.setDecimalScaleFactor(asShort(values[POSITION_PDS_DECIMAL_SCALE_FACTOR_1 + headerOffSet], values[POSITION_PDS_DECIMAL_SCALE_FACTOR_2 + headerOffSet]));

        return objectToReadInto;
    }
}
