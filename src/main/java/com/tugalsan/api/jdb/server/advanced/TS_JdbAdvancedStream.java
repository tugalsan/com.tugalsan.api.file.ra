package com.tugalsan.api.jdb.server.advanced;

import java.io.*;

/**
 * Extends ByteArrayOutputStream to provide a way of writing the buffer to a
 * DataOutput without re-allocating it.
 */
public class TS_JdbAdvancedStream extends ByteArrayOutputStream {

    public TS_JdbAdvancedStream() {
        super();
    }

    public TS_JdbAdvancedStream(int size) {
        super(size);
    }

    /**
     * Writes the full contents of the buffer a DataOutput stream.
     */
    public synchronized void writeTo(DataOutput dstr) throws IOException {
        var data = super.buf;
        var l = super.size();
        dstr.write(data, 0, l);
    }
}
