package com.tugalsan.api.file.ra.server.indexed;

import java.io.*;

public class TS_FileRaIndexedWriter {

    String key;
    TS_FileRaIndexedStream out;
    ObjectOutputStream objOut;

    public TS_FileRaIndexedWriter(String key) {
        this.key = key;
        out = new TS_FileRaIndexedStream();
    }

    public String getKey() {
        return key;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public ObjectOutputStream getObjectOutputStream() throws IOException {
        if (objOut == null) {
            objOut = new ObjectOutputStream(out);
        }
        return objOut;
    }

    public void writeObject(Object o) throws IOException {
        getObjectOutputStream().writeObject(o);
        getObjectOutputStream().flush();
    }

    /**
     * Returns the number of bytes in the data.
     */
    public int getDataLength() {
        return out.size();
    }

    /**
     * Writes the data out to the stream without re-allocating the buffer.
     */
    public void writeTo(DataOutput str) throws IOException {
        out.writeTo(str);
    }
}
