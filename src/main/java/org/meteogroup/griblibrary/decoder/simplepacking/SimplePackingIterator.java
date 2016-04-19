package org.meteogroup.griblibrary.decoder.simplepacking;

import org.meteogroup.griblibrary.grib1.model.Grib1Record;
import org.meteogroup.griblibrary.grib2.model.Grib2Record;
import org.meteogroup.griblibrary.grib2.model.drstemplates.SimplePackingDRSTemplate;
import org.meteogroup.griblibrary.util.BitReader;

import java.util.PrimitiveIterator;

/**
 * Created by roijen on 23-Nov-15.
 */
public class SimplePackingIterator implements PrimitiveIterator.OfDouble {

    private int base10 = 10;
    private int currentIndex = 0;

    double binaryScalePowered;
    double division;
    int bitsForValue;
    int numberOfPoints;
    float referenceValue;
    BitReader bitReader;

    public SimplePackingIterator(Grib1Record record){
        initValues(record.getBinaryData().getPackedValues(), record.getGridDescription().getNumberOfPoints(), record.getBinaryData().getBytesForDatum(), record.getProductDefinition().getDecimalScaleFactor(),
                record.getBinaryData().getBinaryScaleFactor(), record.getBinaryData().getReferenceValue());
    }

    public SimplePackingIterator(Grib2Record record) {
        SimplePackingDRSTemplate simpleDRSTemplate = (SimplePackingDRSTemplate) record.getDrs().getDataTemplate();
        initValues(record.getDataSection().getPackedData(),
                record.getDrs().getNumberOfDataPoints(),
                simpleDRSTemplate.getBitsPerValue(),
                simpleDRSTemplate.getDecimalScaleFactor(),
                simpleDRSTemplate.getBinaryScaleFactor(),
                simpleDRSTemplate.getReferenceValue());
    }

    private void initValues(byte[] packedValues, int numberOfPoints, int bitsForValue, int decimalScale, int binaryScale, float referenceValue){

        this.binaryScalePowered = Math.pow(2, binaryScale);
        this.division = Math.pow(base10, decimalScale);
        this.bitsForValue = bitsForValue;
        this.bitReader = new BitReader(packedValues);
        this.numberOfPoints = numberOfPoints;
        this.referenceValue = referenceValue;
    }

    double decodeValue(long value, double division, double binaryScalePowered, float referenceValue) {
        return (referenceValue + (value * binaryScalePowered)) / division;
    }

    @Override
    public double nextDouble() {
        long packedValue = bitReader.readNext(bitsForValue);
        currentIndex ++;
        return decodeValue(packedValue, division, binaryScalePowered, referenceValue);
    }

    @Override
    public boolean hasNext() {
        return (currentIndex < numberOfPoints);
    }
}
