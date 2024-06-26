package com.tugalsan.api.file.ra.server.object;

import java.io.*;

public class TS_FileRaObjectReader {

    String key;
    byte[] data;
    ByteArrayInputStream in;
    ObjectInputStream objIn;

    public TS_FileRaObjectReader(String key, byte[] data) {
        this.key = key;
        this.data = data;
        in = new ByteArrayInputStream(data);
    }

    public String getKey() {
        return key;
    }

    public byte[] getData() {
        return data;
    }

    public InputStream getInputStream() throws IOException {
        return in;
    }

    public ObjectInputStream getObjectInputStream() throws IOException {
        if (objIn == null) {
            objIn = new ObjectInputStream(in);
        }
        return objIn;
    }

    public Object readObject() throws IOException, OptionalDataException, ClassNotFoundException {
        return getObjectInputStream().readObject();
    }
}
