package org.meteogroup.griblibrary.example;

import org.meteogroup.griblibrary.gis.LatLon;
import org.meteogroup.griblibrary.grib.GribReaderFactory;
import org.meteogroup.griblibrary.grib1.Grib1CollectionReader;
import org.meteogroup.griblibrary.grib1.model.Grib1BinaryDataSection;
import org.meteogroup.griblibrary.grib1.model.Grib1GridDescriptionSection;
import org.meteogroup.griblibrary.grib1.model.Grib1ProductDefinitionSection;
import org.meteogroup.griblibrary.grib1.model.Grib1Record;
import org.meteogroup.griblibrary.grib1.spec.CenterNameResolver;
import org.meteogroup.griblibrary.grib1.spec.DataRepresentationTypeResolver;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * a.k.a. 'grib_ls'
 */
public class PrintGribFileContentExample {


    private final CenterNameResolver centerNameResolver = new CenterNameResolver();

    public static void main(String[] args) {
        try {
            new PrintGribFileContentExample().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        Optional<Grib1CollectionReader> version1 = new GribReaderFactory().createVersion1(getGribFilename());

        LatLon latLon = new LatLon(39.880218f, 116.367677f);


        long start = System.currentTimeMillis();
        if (version1.isPresent()) {
            Grib1CollectionReader grib1CollectionReader = version1.get();
            List<Grib1Record> gribRecords = grib1CollectionReader.readAllRecords(getGribFilename());
            for (Grib1Record gribRecord : gribRecords) {
//                printGridDescription(gribRecord.getGridDescription());
//                printProductDefinition(gribRecord.getProductDefinition());
//                printGrid(gribRecord.getBinaryData());
                new BinaryDataAccessor(gribRecord).getValues(latLon);
            }
        } else {
            throw new IllegalStateException("Sorry, this example just can process GRIB1 file types. But the given file wasn't v1.");
        }
        long end = System.currentTimeMillis();
        System.out.println("time (ms): " + (end - start));
    }

    private void printGridDescription(Grib1GridDescriptionSection gridDescription) {
        System.out.println("DataRepresentationType: " + new DataRepresentationTypeResolver().resolveShortName(gridDescription.getDataRepresentationType()));
    }

    private void printGrid(Grib1BinaryDataSection binaryData) {

    }

    private void printProductDefinition(Grib1ProductDefinitionSection productDefinition) {
        System.out.println("edition      centre       typeOfLevel  level        dataDate     stepRange    dataType     shortName    packingType  gridType");
        String msg = "";
        msg += 1;
        msg += "     ";
        msg += centerNameResolver.resolveShortName(productDefinition.getIdentificationOfCenter());
        System.out.println(msg);
    }

    private File getGribFilename() {
        String filename = System.getenv("GRIB_FILE");
        if (filename == null || filename.isEmpty()) {
            throw new IllegalStateException("Please provide an example grib file via ENVironment VARiable. E.g. GRIB_FILE=test.grb");
        }
        return new File(filename);
    }

}