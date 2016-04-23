package org.meteogroup.griblibrary.grib2.drstemplates;

import org.meteogroup.griblibrary.grib2.model.drstemplates.DRSTemplate;

/**
 * @author Pauw
 */
public interface DataTemplateReader {

    public DRSTemplate readTemplate(byte[] bytes, int headerOffSet);
}