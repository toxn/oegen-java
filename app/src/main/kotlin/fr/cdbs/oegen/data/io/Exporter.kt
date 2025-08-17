package fr.cdbs.oegen.data.io

import java.io.OutputStream

abstract class Exporter(protected val outputStream: OutputStream?) {
    /**
     * If set to true, every relation entered in the database is exported. If
     * false (default case), redundant information will not be exported.
     *
     * In general, redundancy increase human readability, but could lead to
     * errors, when manually editing the resulting export file.
     *
     * @return state of completeness of the export
     */
    /**
     * Set the completeness of the data export
     *
     * @see isComplete
     *
     * @param this.isComplete
     * le complete à définir
     */
    protected var isComplete: Boolean = false

    abstract fun doExport()
}
