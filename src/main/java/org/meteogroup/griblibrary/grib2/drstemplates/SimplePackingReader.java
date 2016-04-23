package org.meteogroup.griblibrary.grib2.drstemplates;

import org.meteogroup.griblibrary.grib2.model.drstemplates.DRSTemplate;
import org.meteogroup.griblibrary.grib2.model.drstemplates.SimplePackingDRSTemplate;
import org.meteogroup.griblibrary.util.BytesToPrimitiveHelper;

import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asInt;
import static org.meteogroup.griblibrary.util.BytesToPrimitiveHelper.asSignedInt;

/**
 * @author Pauw
 */
public class SimplePackingReader implements DataTemplateReader {

    //see http://grepcode.com/file/repo1.maven.org/maven2/edu.ucar/grib/4.5.4/ucar/nc2/grib/grib2/Grib2Drs.java#Grib2Drs.Type50002
    //and // according to https://github.com/erdc-cm/grib_api/blob/master/definitions/grib2/template.5.50002.def
    static final int POSITION_REFERENCE_VALUE1 = 11;
    static final int POSITION_REFERENCE_VALUE2 = 12;
    static final int POSITION_REFERENCE_VALUE3 = 13;
    static final int POSITION_REFERENCE_VALUE4 = 14;

    static final int POSITION_BINARYSCALEFACTOR1 = 15;
    static final int POSITION_BINARYSCALEFACTOR2 = 16;

    static final int POSITION_DECIMALSCALEFACTOR1 = 17;
    static final int POSITION_DECIMALSCALEFACTOR2 = 18;

    static final int POSITION_BITSPERVALUE = 19;


    public SimplePackingReader() {
        //this.bytes = bytes;
    }

    @Override
    public DRSTemplate readTemplate(byte[] bytes, int headerOffSet) {
        if (bytes == null) throw new IllegalArgumentException("argument bytes are not allowed to be null.");
        SimplePackingDRSTemplate simpleTemplate = new SimplePackingDRSTemplate();
        simpleTemplate.setReferenceValue(BytesToPrimitiveHelper.bytesToFloat(//.bytesToFloatAsIBM(
                bytes[POSITION_REFERENCE_VALUE1 + headerOffSet],
                bytes[POSITION_REFERENCE_VALUE2 + headerOffSet],
                bytes[POSITION_REFERENCE_VALUE3 + headerOffSet],
                bytes[POSITION_REFERENCE_VALUE4 + headerOffSet]));

        simpleTemplate.setBinaryScaleFactor(asSignedInt(bytes[POSITION_BINARYSCALEFACTOR1 + headerOffSet], bytes[POSITION_BINARYSCALEFACTOR2 + headerOffSet]));
        simpleTemplate.setDecimalScaleFactor(asSignedInt(bytes[POSITION_DECIMALSCALEFACTOR1 + headerOffSet], bytes[POSITION_DECIMALSCALEFACTOR2 + headerOffSet]));
        simpleTemplate.setBitsPerValue(asInt(bytes[POSITION_BITSPERVALUE + headerOffSet]));
        return simpleTemplate;
    }
}