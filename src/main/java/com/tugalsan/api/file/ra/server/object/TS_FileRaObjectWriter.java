package com.tugalsan.api.file.ra.server.object;

import java.io.*;

public class TS_FileRaObjectWriter {

    String key;
    TS_FileRaObjectStream out;
    ObjectOutputStream objOut;

    public TS_FileRaObjectWriter(String key) {
        this.key = key;
        out = new TS_FileRaObjectStream();
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
