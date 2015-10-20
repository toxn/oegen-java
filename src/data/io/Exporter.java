package data.io;

import java.io.OutputStream;

public abstract class Exporter {
    protected final OutputStream outputStream;

    public Exporter(OutputStream is) {
	outputStream = is;
    }

    public abstract void doExport();

}
