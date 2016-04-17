package org.meteogroup.griblibrary.grib;

/**
 * 
 * @author Pauw
 *
 */
public abstract class GribRecord {

	protected long length;
	protected final int version;

	protected GribRecord(int version) {
		this.version = version;
	}

	public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

	public int getVersion() {
		return version;
	}

}