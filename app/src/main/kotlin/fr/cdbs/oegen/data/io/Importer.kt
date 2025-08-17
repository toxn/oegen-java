package fr.cdbs.oegen.data.io

import java.io.InputStream

abstract class Importer(protected val inputStream: InputStream?) {
    abstract fun doImport()
}
