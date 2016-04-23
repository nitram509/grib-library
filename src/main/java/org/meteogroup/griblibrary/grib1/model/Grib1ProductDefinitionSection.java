package org.meteogroup.griblibrary.grib1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by roijen on 21-Oct-15.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Grib1ProductDefinitionSection {

    public static final int FORECASTPERIODTYPE_MINUTE = 0;
    public static final int FORECASTPERIODTYPE_HOUR = 1;
    public static final int FORECASTPERIODTYPE_DAY = 2;
    public static final int FORECASTPERIODTYPE_MONTH = 3;

    public static final int LEVELTYPE_SURFACE = 1;
    public static final int LEVELTYPE_ISOBARIC = 100;
    public static final int LEVELTYPE_MEAN_SEA_LEVEL = 102;
    public static final int LEVELTYPE_FIXED_HEIGHT_LEVEL = 103;
    public static final int LEVELTYPE_FIXED_HEIGHT_ABOVE_GROUND = 105;

    private int sectionLenght;

    private byte parameterTableVersionNumber;
    private byte identificationOfCenter;
    private byte identificationOfSubCenter;
    private byte generatingProcessIdNumber;
    private byte gridIdentification;
    private byte identicatorOfParameterAndUnit;
    private byte identicatorOfTypeOfLevelOrLayer;
    private short levelOrLayerValue1 = -1;
    private short levelOrLayerValue2 = -1;
    private byte issueTimeCentury;
    private byte issueTimeYearOfCentury;
    private byte issueTimeMonth;
    private byte issueTimeDay;
    private byte issueTimeHour;
    private byte issueTimeMinute;
    private byte forecastTimeUnit;
    private byte forecastPeriodOfTime1;
    private byte forecastPeriodOfTime2;
    private byte timeRangeIndicator = -1;
    private short numberIncludedInAverageOrAccumulation;
    private byte numberOfMissingFromAverageOrAcummulation;
    private short decimalScaleFactor;

    private boolean hasOnlyOneLevelOrLayerValue;

    private boolean hasGDS;
    private boolean hasBMS;

}