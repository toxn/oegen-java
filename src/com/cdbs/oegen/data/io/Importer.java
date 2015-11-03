package com.cdbs.oegen.data.io;

import java.io.InputStream;

public abstract class Importer {
    protected final InputStream inputStream;

    public Importer(InputStream is) {
	inputStream = is;
    }

    public abstract void doImport();
}
