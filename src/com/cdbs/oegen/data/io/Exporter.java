package com.cdbs.oegen.data.io;

import java.io.OutputStream;

public abstract class Exporter {
    protected final OutputStream outputStream;
    private boolean complete = false;

    public Exporter(OutputStream is) {
	outputStream = is;
    }

    public abstract void doExport();

    /**
     * If set to true, every relation entered in the database is exported. If
     * false (default case), redundant informations will not be exported.
     *
     * In general, redundancy increase human readability, but could lead to
     * errors, when manually editing the resulting export file.
     * 
     * @return state of completeness of the export
     */
    public boolean isComplete() {
	return complete;
    }

    /**
     * Set the completeness of the data export
     * 
     * @see isComplete
     * @param complete
     *            le complete à définir
     */
    public void setComplete(boolean complete) {
	this.complete = complete;
    }

}
